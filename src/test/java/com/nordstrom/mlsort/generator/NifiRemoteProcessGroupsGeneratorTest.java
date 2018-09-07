package com.nordstrom.mlsort.generator;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import com.nordstrom.mlsort.jaxb.PositionType;
import com.nordstrom.mlsort.jaxb.RemoteGroupPortType;
import com.nordstrom.mlsort.jaxb.RemoteProcessGroupType;

/**
 * Test class for NifiRemoteProcessGroupsGenerator
 */
public class NifiRemoteProcessGroupsGeneratorTest {

  @Test
  public void testGenerateTFElement() throws IOException, URISyntaxException {

    String parentGroupId = "aaaa-bbbb-cccc-dddd";
    RemoteProcessGroupType remoteProcessGroupType = new RemoteProcessGroupType();
    remoteProcessGroupType.setName("TEST_REP_TSK");
    PositionType positionType = new PositionType();
    positionType.setX(100);
    positionType.setY(100);
    remoteProcessGroupType.setPosition(positionType);
    remoteProcessGroupType.setUrls("http://url");
    remoteProcessGroupType.setYieldPeriod("1 min");
    remoteProcessGroupType.setTimeout("1 min");
    remoteProcessGroupType.setProxyHost("localhost");
    remoteProcessGroupType.setProxyUser("user");
    RemoteGroupPortType remoteGroupPortTypeI = new RemoteGroupPortType();
    remoteGroupPortTypeI.setName("IN_PRT_NM");
    remoteGroupPortTypeI.setMaxConcurrentTasks(BigInteger.valueOf(100));
    remoteGroupPortTypeI.setId("IN_PRT_ID");
    remoteProcessGroupType.getInputPort().add(remoteGroupPortTypeI);

    RemoteGroupPortType remoteGroupPortTypeO = new RemoteGroupPortType();
    remoteGroupPortTypeO.setName("OUT_PRT_NM");
    remoteGroupPortTypeO.setMaxConcurrentTasks(BigInteger.valueOf(100));
    remoteGroupPortTypeO.setId("OUT_PRT_ID");
    remoteProcessGroupType.getOutputPort().add(remoteGroupPortTypeO);

    NifiRemoteProcessGroupsGenerator nifiRemoteProcessGroupsGenerator =
        new NifiRemoteProcessGroupsGenerator(remoteProcessGroupType);
    String actual =
        nifiRemoteProcessGroupsGenerator.generateTFElement("REMOTE_PROCESS_GROUPS", parentGroupId);

    String[] actualarray = actual.split("\n");

    ClassLoader classLoader = getClass().getClassLoader();
    File file =
        new File(classLoader.getResource("generator/nifi_remote_process_group.tf").getFile());

    String expected = FileUtils.readFileToString(file);

    String[] expectedarray = expected.split("\n");
    boolean comparison = expectedarray.length == actualarray.length;
    for (int i = 0; i < expectedarray.length; i++) {
      if (!comparison) {
        break;
      }
      String expectedrow = expectedarray[i];
      String actualrow = actualarray[i];
      comparison = expectedrow.trim().equals(actualrow.trim());
    }

    assertTrue("Response id not matching the Expected string", comparison);
  }

}
