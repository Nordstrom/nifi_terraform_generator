[![Build Status](https://travis-ci.org/KailasJanardhanan/nifi_terraform_generator.svg?branch=master)](https://travis-ci.org/KailasJanardhanan/nifi_terraform_generator)
[![Code Coverage](https://codecov.io/github/KailasJanardhanan/nifi_terraform_generator/coverage.svg)](https://codecov.io/gh/KailasJanardhanan/nifi_terraform_generator)

## What
A component which takes nifi flow xml file as input and converts it into terraform script for creating/updating a flow on nifi

## Prerequisites
* Java 1.8
* Maven 3+

## Reference
https://github.com/Glympse/terraform-provider-nifi/


## Features
* Dynamic generation of terraform script(`.tf`) from flow.xml for creating nifi flows. Each processor group will have a corresponding tf file which makes it easily manageable(vs one monolithic tf file)
* Leverage terraform capability to identify delta and apply those changes alone
* Single variables.tf file to support remote nifi flows and multiple process groups

## Limitations
* Does not support remote process group(experimental)

## How to generate terraform scripts from existing nifi cluster?
* From an existing nifi cluster, extract the flow.xml.gz from master node located in the folder - `{Nifi HOME}/conf`.
* copy the extracted flow.xml to `terraform-nifi-flow-generator\src\main\resources\flowfiles` folder of the project.
* Enter the following details in the file `terraform-nifi-flow-generator\src\main\resources\config.properties`<br />nifi_machine - the hostname and port of the nifi machine on which the terraform file should be deployed. Eg - `nifi_machine=172.16.71.125:8080`<br />
root_element=The id of the root element of Nifi cluster on which the terraform file should create the nifi flow. This can be located on the `flow.xml` that we fetched from the cluster in the above step. 
Eg - ```  <rootGroup>
    <id>34900dd5-0161-1000-b7a0-ed93f5fa490d</id>
    ...```
    Set as `root_element=34900dd5-0161-1000-b7a0-ed93f5fa490d`

* Run the main class ```com.nordstrom.mlsort.TFGenerator```
* terraform files will be generated at the `terraform-nifi-flow-generator\target` folder. 

`Note!!`
* All the files except remoteconnections.tf can be used to generate nifi flow in destination nifi cluster. Remote process group is currently not supported(experimental).

## How to use terraform scripts to setup a flow in nifi cluster?

### Create a brand new flow
* Get your tf files as mentioned above from `flow.xml`
* Install terraform and terraform-nifi-provider plugin on the machine. 
* Copy all the terraform files the machine 
* Run the following commands to create a cluster in nifi```
sudo ./terraform init
sudo ./terraform plan -out=plan.out
sudo ./terraform apply plan.out```
* This will create the whole flow in nifi. To start each process group REST calls can be used. This project provides a utility `NifiApiUtil.java` to generate curl commands that can be used to start/stop each processor, ports, controller service. 

### Update/Modify an existing flow

* Get your tf files as mentioned above from `flow.xml`
* Install terraform and terraform-nifi-provider plugin on the a machine. 
* Copy all the terraform files the machine 
* It is recommended to use REST calls to nifi cluster to stop the sources of existing flow and let messages be drained before deploying a modification or update to existing flow. This can be best demonstrated with an example. Imagine the following flow:
```A(source)->B(some transformation)->C(destination)```
In this flow `A` needs to be stopped and we need to let all messages to make to the destination `C`. If this step is omitted, the messages in flight can be lost which is not desirable in some scenarios. 
This project provides a utility `NifiApiUtil.java` to generate curl commands that can be used to start/stop each processor, ports, controller service. 
* Once all the messages are drained or reached to the destination, the following commands can be used to deploy an update:
```
sudo ./terraform init
sudo ./terraform plan -out=plan.out
sudo ./terraform apply plan.out
```
* Terraform will identify the previous state and will apply the delta/change of the flow to the existing flow.

