resource "nifi_processor" "PROCESSOR" {

	component	{
		parent_group_id = "aaaa-bbbb-cccc-dddd"
		name = "TEST_PROCR"
		type = "com.nordstrom.mlsort.jaxb.ProcessorType"
		
		position	{
			x = 100.0
			y = 100.0
		}
	
		
		config	{
			concurrently_schedulable_task_count = 100
			scheduling_strategy = "PRIMARY_NODE_ONLY"
			scheduling_period = "1 min"
			execution_node = "ALL"
			properties	{
			}
		
			auto_terminated_relationships	=	[
			
			]
		
		}
	
	}

}
