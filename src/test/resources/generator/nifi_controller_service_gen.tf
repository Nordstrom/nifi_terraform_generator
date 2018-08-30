resource "nifi_controller_service" "ControllerServices_CTRL_SER" {

	component	{
		parent_group_id = "aaaa-bbbb-cccc-dddd"
		name = "TEST_CTRL_SER"
		type = "com.nordstrom.mlsort.jaxb.ControllerServiceType"
		properties	{
			"PROP_NAME" = "PROP_VALUE"
		}
	
	}

}
