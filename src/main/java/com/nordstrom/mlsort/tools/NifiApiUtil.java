package com.nordstrom.mlsort.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

/**
 * Class to make all nifi api calls to stop/disable elements before deployment. Also contains
 * methods to monitor the ongoing processing of messages in process groups.
 * 
 * @author a8n1
 *
 */
public final class NifiApiUtil {

  private static RestTemplate restTemplate = new RestTemplate();

  private static final List<String> curlList = new ArrayList<>();

  private static String nifiApi;

  /**
   * Method to stop elements in a given process group or groups.
   * 
   * @param path String
   * @param processorsToStop String
   * @param processGroupsToDeploy String
   * @param nifiMachine String
   * @throws IOException IOException
   */
  public static void stopOrDisablePGElements(final String path, final String processorsToStop,
      final String processGroupsToDeploy, final String nifiMachine) throws IOException {
    // Split to find the list of process groups
    String[] processGroups = processGroupsToDeploy.split(",");
    String[] processors = processorsToStop.split(",");
    Map<String, String> processorNameToIdMap = new HashMap<>();

    nifiApi = "http://" + nifiMachine + "/nifi-api/";
    // Get root process group id
    String rootPGId = getRootPGId();

    // Loop through and disable elements
    for (String processGroupName : processGroups) {
      System.out.println("Disabling elements in " + processGroupName);

      // Find process group id
      List<String> parentProcessGroups = new ArrayList<>();
      parentProcessGroups.add(rootPGId);
      String processGroupId = getProcessGroupId(parentProcessGroups, processGroupName);

      if (StringUtils.isNotBlank(processGroupId)) {
        // Stop the process group
        modifyProcessGroup(processGroupId, "STOPPED", null);

        // Find all child process groups
        List<String> childProcessGroupIds = getChildProcessGroupIds(processGroupId);

        // Add parent to child
        childProcessGroupIds.add(processGroupId);

        List<String> inputPortIds = new ArrayList<>();
        List<String> outputPortIds = new ArrayList<>();
        // Loop through and stop all ports and controller services
        for (String pgId : childProcessGroupIds) {
          inputPortIds.addAll(getPortIds(pgId, "I"));
          outputPortIds.addAll(getPortIds(pgId, "O"));

          // Find all controller services
          List<String> controllerServices = getControllerServices(pgId);

          for (String controllerService : controllerServices) {
            modifyControllerService(controllerService, "DISABLED");
          }
        }
        // Stop ports
        stopPorts(inputPortIds, outputPortIds, "STOPPED");

      }
    }

    // Get all child process groups
    List<String> childProcessGroupIdsFirstLevel = getChildProcessGroupIds(rootPGId);
    for (String pgId : childProcessGroupIdsFirstLevel) {
      // Find all processors for the process group and populate
      // the map
      populateProcessorNameToIdMap(processors, processorNameToIdMap, pgId);
    }

    modifyProcessor(processorNameToIdMap, "STOPPED");

    if (!CollectionUtils.isEmpty(curlList)) {
      // Write commands to a file
      // String path = NifiApiUtil.class.getResource("/").getFile();
      // File curlFile = new File(path + "../curl_commands.txt");

      StringBuilder sbStopProcessors = new StringBuilder("#!/bin/bash").append("\n");
      StringBuilder sbStopOthers = new StringBuilder("#!/bin/bash").append("\n");
      StringBuilder sbStart = new StringBuilder("#!/bin/bash").append("\n");

      StringBuilder sb = new StringBuilder("***** Stopping commands for a Process Group *****\n");
      for (String curl : curlList) {
        sb.append(curl).append("\n");

        if (curl.contains("\"STOPPED\"") && curl.contains("processors")) {
          sbStopProcessors.append(curl).append("\n");
        } else {
          sbStopOthers.append(curl).append("\n");
        }

      }

      sb.append("\n\n***** Commands to start all Process Groups *****\n");
      List<String> startPGCommands = startAllProcessGroups(rootPGId);
      for (String curl : startPGCommands) {
        sb.append(curl).append("\n");
        sbStart.append(curl).append("\n");
      }
      File curlFileStopProcessors = new File(path + File.separator + "curl_stop_processors.sh");
      File curlFileStopOthers = new File(path + File.separator + "curl_stop.sh");
      File curlFileStart = new File(path + File.separator + "curl_start.sh");
      // FileUtils.writeStringToFile(curlFile, sb.toString());
      FileUtils.writeStringToFile(curlFileStopProcessors, sbStopProcessors.toString());
      FileUtils.writeStringToFile(curlFileStopOthers, sbStopOthers.toString());
      FileUtils.writeStringToFile(curlFileStart, sbStart.toString());

      System.out.println("Created commands to Disable/Stop all elements of " + processGroupsToDeploy
          + " and start commands for the Parent");
    } else {
      System.out.println("No commands generated for " + processGroupsToDeploy);
    }

  }

  /**
   * Method to populate processorNameToIdMap with key as processor name and value as processor id.
   * 
   * @param processors String[]
   * @param processorNameToIdMap Map with key as Processor Name and value as is Id
   * @param pgId String
   */
  private static void populateProcessorNameToIdMap(final String[] processors,
      final Map<String, String> processorNameToIdMap, final String pgId) {
    String uri = "process-groups/" + pgId + "/processors";
    Set<String> VALUES = new HashSet<String>(Arrays.asList(processors));
    String response = restTemplate.getForObject(nifiApi + uri, String.class);
    JSONObject json = new JSONObject(response);
    JSONArray array = json.getJSONArray("processors");
    for (int i = 0; i < array.length(); i++) {
      JSONObject jsonChild = ((JSONObject) array.get(i)).getJSONObject("status");
      String id = (String) jsonChild.get("id");
      String name = (String) jsonChild.get("name");
      if (VALUES.contains(name)) {
        processorNameToIdMap.put(name, id);
      }
    }
  }

  /**
   * Method to stop/start Processor.
   * 
   * @param processorNameToIdMap Map with key as ProcessorName and value as its Id
   * @param action String
   */
  private static void modifyProcessor(final Map<String, String> processorNameToIdMap,
      final String action) {

    for (Entry<String, String> entry : processorNameToIdMap.entrySet()) {

      String url = nifiApi + "processors/" + entry.getValue();

      String response = restTemplate.getForObject(url, String.class);
      int version = (int) ((JSONObject) new JSONObject(response).get("revision")).get("version");

      JSONObject componentObj = new JSONObject();
      componentObj.put("id", entry.getValue());
      componentObj.put("state", action);

      JSONObject revisionObj = new JSONObject();
      revisionObj.put("version", version);

      JSONObject payloadObj = new JSONObject();
      payloadObj.put("component", componentObj);
      payloadObj.put("revision", revisionObj);

      curlList.add(generateCurlCommand(url, payloadObj.toString(), "put"));

      System.out
          .println("Controller Service " + action + " command generated for " + entry.getValue());

    }
  }

  /**
   * Start all process groups in the root level.
   * 
   * @param rootPGId String
   * @return List- curl commands to start as Strings
   */
  private static List<String> startAllProcessGroups(final String rootPGId) {

    List<String> childProcessGroupIds = new ArrayList<>();

    String uri = "process-groups/" + rootPGId + "/process-groups";
    String response = restTemplate.getForObject(nifiApi + uri, String.class);
    JSONObject json = new JSONObject(response);

    JSONArray array = json.getJSONArray("processGroups");

    for (int i = 0; i < array.length(); i++) {
      JSONObject jsonObj = (JSONObject) ((JSONObject) array.get(i)).get("status");
      String name = (String) jsonObj.get("name");
      String pgId = (String) jsonObj.get("id");
      childProcessGroupIds.add(pgId);

    }
    // Commands to start all process groups
    List<String> curlHolder = new ArrayList<>();
    for (String childProcessGroupId : childProcessGroupIds) {
      modifyProcessGroup(childProcessGroupId, "RUNNING", curlHolder);
    }

    return curlHolder;

  }

  /**
   * Method to find controller services.
   * 
   * @param processGroupId String
   * @return List of controller service Ids as String
   */
  private static List<String> getControllerServices(final String processGroupId) {
    List<String> controllerServices = new ArrayList<>();

    String uri = "/flow/process-groups/" + processGroupId + "/controller-services";

    String response = restTemplate.getForObject(nifiApi + uri, String.class);
    JSONObject jsonP = new JSONObject(response);

    JSONArray json = jsonP.getJSONArray("controllerServices");

    for (int i = 0; i < json.length(); i++) {
      String id = (String) ((JSONObject) json.get(i)).get("id");
      controllerServices.add(id);
    }

    return controllerServices;
  }

  /**
   * Method to stop ports.
   * 
   * @param inputPortIds List of inputPortIds as String
   * @param outputPortIds List of outputPortIds as String
   * @param action String
   */
  private static void stopPorts(final List<String> inputPortIds, final List<String> outputPortIds,
      final String action) {
    for (String portId : inputPortIds) {
      String uri = "input-ports/" + portId;
      modifyPort(portId, uri, action);
    }
    for (String portId : outputPortIds) {
      String uri = "output-ports/" + portId;
      modifyPort(portId, uri, action);
    }
  }

  /**
   * Method to stop/start Process Group.
   * 
   * @param processGroup String
   * @param action String
   * @param List containing curl commands
   */
  private static void modifyProcessGroup(final String processGroup, final String action,
      final List<String> curlHolder) {

    String url = nifiApi + "flow/process-groups/" + processGroup;

    JSONObject payloadObj = new JSONObject();
    payloadObj.put("id", processGroup);
    payloadObj.put("state", action);

    if (null == curlHolder) {
      curlList.add(generateCurlCommand(url, payloadObj.toString(), "put"));
    } else {
      curlHolder.add(generateCurlCommand(url, payloadObj.toString(), "put"));
    }

    System.out.println("Process Group " + action + " command generated for " + processGroup);
  }

  /**
   * Method to stop/start Controller Service.
   * 
   * @param controllerService String
   * @param action String
   */
  private static void modifyControllerService(final String controllerService, final String action) {

    String url = nifiApi + "controller-services/" + controllerService;

    String response = restTemplate.getForObject(url, String.class);
    int version = (int) ((JSONObject) new JSONObject(response).get("revision")).get("version");

    JSONObject componentObj = new JSONObject();
    componentObj.put("id", controllerService);
    componentObj.put("state", action);

    JSONObject revisionObj = new JSONObject();
    revisionObj.put("version", version);

    JSONObject payloadObj = new JSONObject();
    payloadObj.put("component", componentObj);
    payloadObj.put("revision", revisionObj);

    curlList.add(generateCurlCommand(url, payloadObj.toString(), "put"));

    System.out
        .println("Controller Service " + action + " command generated for " + controllerService);
  }

  /**
   * Method to stop/start port.
   * 
   * @param portId String
   * @param uri String
   * @param action String
   */
  private static void modifyPort(final String portId, final String uri, final String action) {

    String response = restTemplate.getForObject(nifiApi + uri, String.class);
    int version = (int) ((JSONObject) new JSONObject(response).get("revision")).get("version");

    JSONObject componentObj = new JSONObject();
    componentObj.put("id", portId);
    componentObj.put("state", action);

    JSONObject revisionObj = new JSONObject();
    revisionObj.put("version", version);

    JSONObject payloadObj = new JSONObject();
    payloadObj.put("component", componentObj);
    payloadObj.put("revision", revisionObj);

    curlList.add(generateCurlCommand(nifiApi + uri, payloadObj.toString(), "put"));

    System.out.println("Port " + action + " command generated for " + portId);

  }

  /**
   * Method to generate curl commands.
   * 
   * @param url String
   * @param payload String
   * @param requestType String
   * @return String of curl command
   */
  private static String generateCurlCommand(final String url, final String payload,
      final String requestType) {
    String curl = "";
    if (requestType.equalsIgnoreCase("put")) {
      curl = "curl -X PUT -H \"Content-Type: application/json\" -d '" + payload + "' " + url;
    }
    return curl;
  }

  /**
   * Method to get port ids.
   * 
   * @param processGroupId String
   * @param type String
   * @return List of port Ids as String
   */
  private static List<String> getPortIds(final String processGroupId, final String type) {
    List<String> ports = new ArrayList<>();
    String uri = null;
    String element = null;
    if ("I".equals(type)) {
      uri = "process-groups/" + processGroupId + "/input-ports";
      element = "inputPorts";
    } else {
      uri = "process-groups/" + processGroupId + "/output-ports";
      element = "outputPorts";
    }
    String response = restTemplate.getForObject(nifiApi + uri, String.class);
    JSONObject jsonP = new JSONObject(response);

    JSONArray json = jsonP.getJSONArray(element);

    for (int i = 0; i < json.length(); i++) {
      String id = (String) ((JSONObject) json.get(i)).get("id");
      ports.add(id);
    }

    return ports;
  }

  /**
   * Method to find all child process group ids.
   * 
   * @param processGroupId String
   * @return List of child process group ids as String
   */
  private static List<String> getChildProcessGroupIds(final String processGroupId) {
    List<String> childProcessGroupIds = new ArrayList<>();

    String uri = "process-groups/" + processGroupId + "/process-groups";
    String response = restTemplate.getForObject(nifiApi + uri, String.class);
    JSONObject json = new JSONObject(response);

    JSONArray array = json.getJSONArray("processGroups");

    for (int i = 0; i < array.length(); i++) {
      JSONObject jsonObj = (JSONObject) ((JSONObject) array.get(i)).get("status");
      String name = (String) jsonObj.get("name");
      String pgId = (String) jsonObj.get("id");
      childProcessGroupIds.add(pgId);
      List<String> childrenOfChild = getChildProcessGroupIds(pgId);
      if (!CollectionUtils.isEmpty(childrenOfChild)) {
        childProcessGroupIds.addAll(childrenOfChild);
      }
    }
    return childProcessGroupIds;
  }

  /**
   * Method to find ProcessGroupId for a given name.
   * 
   * @param parentProcessGroups List of parent Process Group Ids as String
   * @param processGroupName String
   * @return String ProcessGroupId
   */
  private static String getProcessGroupId(final List<String> parentProcessGroups,
      final String processGroupName) {
    List<String> newParentProcessGroups = new ArrayList<>();

    for (String parentProcessGroupId : parentProcessGroups) {
      String uri = "process-groups/" + parentProcessGroupId + "/process-groups";
      String response = restTemplate.getForObject(nifiApi + uri, String.class);
      // System.out.println(response);
      JSONObject json = new JSONObject(response);

      JSONArray array = json.getJSONArray("processGroups");

      for (int i = 0; i < array.length(); i++) {
        JSONObject jsonObj = (JSONObject) ((JSONObject) array.get(i)).get("status");
        String name = (String) jsonObj.get("name");
        String pgId = (String) jsonObj.get("id");
        if (processGroupName.equals(name)) {
          return pgId;
        } else {
          newParentProcessGroups.add(pgId);
        }
      }

    }

    if (newParentProcessGroups.size() > 0) {
      getProcessGroupId(newParentProcessGroups, processGroupName);
    }
    return null;
  }

  /**
   * Method to get root process group id.
   * 
   * @return String RootPGId
   */
  private static String getRootPGId() {
    String uri = "flow/process-groups/root";
    String response = restTemplate.getForObject(nifiApi + uri, String.class);
    JSONObject json = new JSONObject(response);
    return json.getJSONObject("processGroupFlow").get("id").toString();

  }

  /**
   * Method to display the count of in process flow files in each connections, grouped by Process
   * group.
   * 
   * @param processGroupsToMonitor String
   * @param nifiMachine String
   */
  public static void monitorRunningFlowCount(final String processGroupsToMonitor,
      final String nifiMachine) {
    String[] processGroups = processGroupsToMonitor.split(",");

    nifiApi = "http://" + nifiMachine + "/nifi-api/";
    // Get root process group id
    String rootPGId = getRootPGId();

    // Loop through and disable elements
    for (String processGroupName : processGroups) {
      System.out.println("Monitoring flow files in " + processGroupName);
      List<String> parentProcessGroups = new ArrayList<>();
      parentProcessGroups.add(rootPGId);
      String processGroupId = getProcessGroupId(parentProcessGroups, processGroupName);
      monitorConnectionQueues(processGroupId);

    }
  }

  /**
   * Method to monitor connection Queues.
   * 
   * @param processGroupId String
   */
  private static void monitorConnectionQueues(final String processGroupId) {
    String uri = "process-groups/" + processGroupId + "/connections";
    String response = restTemplate.getForObject(nifiApi + uri, String.class);
    // System.out.println(response);
    JSONObject json = new JSONObject(response);

    JSONArray array = json.getJSONArray("connections");

    for (int i = 0; i < array.length(); i++) {
      JSONObject jsonObjStatus = (JSONObject) ((JSONObject) array.get(i)).get("status");
      String sourceName = (String) jsonObjStatus.get("sourceName");
      String destinationName = (String) jsonObjStatus.get("destinationName");
      String queuedCount =
          ((JSONObject) jsonObjStatus.get("aggregateSnapshot")).getString("queuedCount");

      System.out
          .println(" " + queuedCount + " queued between " + sourceName + " and " + destinationName);

    }
    System.out
        .println("\n=========================================================================\n");

  }

}
