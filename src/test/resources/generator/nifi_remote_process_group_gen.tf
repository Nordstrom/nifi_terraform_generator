resource "nifi_remote_process_group" "RPG_NAME" {

	component	{
		parent_group_id = "aaaa-bbbb-cccc-dddd"
		name = "RPG_NAME"
		
		position	{
			x = 100.0
			y = 100.0
		}
	
		targetUris = "http://urls"
		yieldPeriod = "1 min"
		timeout = "1 min"
		transportProtocol = "TLS"
		proxyHost = "PRXT HST"
		proxyUser = "PRXY USR"
		
		contents	{
			input_ports     = [
			]
		
			output_ports     = [
			]
		}
	
	}

}
