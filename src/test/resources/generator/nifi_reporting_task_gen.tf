resource "nifi_reporting_task" "RPT_TSK_NM" {

	component	{
		parent_group_id = ""${var.nifi_root_process_group_id}""
		name = "RPT_TSK_NM"
		type = "com.nordstrom.mlsort.jaxb.ReportingTaskType"
		properties	{
			"PROP_NAME" = "PROP_VALUE"
		}
	
		scheduling_strategy = "PRIMARY_NODE_ONLY"
		scheduling_period = "1 min"
	}

}
