resource "nifi_reporting_task" "REPORTING_TASKS_NAME" {

	component	{
		parent_group_id = "aaaa-bbbb-cccc-dddd"
		name = "TEST_REP_TSK"
		type = "com.nordstrom.mlsort.jaxb.ReportingTaskType"
		properties	{
			"PROP_NAME" = "PROP_VALUE"
		}
	
		scheduling_strategy = "PRIMARY_NODE_ONLY"
		scheduling_period = "1 min"
	}

}