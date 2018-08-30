resource "nifi_remote_process_group" "REMOTE_PROCESS_GROUPS" {

	component	{
		parent_group_id = "aaaa-bbbb-cccc-dddd"
		name = "TEST_REP_TSK"
		
		position	{
			x = 100.0
			y = 100.0
		}
	
		targetUris = "http://url"
		yieldPeriod = "1 min"
		timeout = "1 min"
		transportProtocol = null
		proxyHost = "localhost"
		proxyUser = "user"
		
		contents	{
			input_ports     = [
		
			
				{
					id = "IN_PRT_ID"
					name = "IN_PRT_NM"
					concurrentlySchedulableTaskCount = 100
				}
			]
		
			output_ports     = [
		
			
				{
					id = "OUT_PRT_ID"
					name = "OUT_PRT_NM"
					concurrentlySchedulableTaskCount = 100
				}
			]
		}
	
	}

}
