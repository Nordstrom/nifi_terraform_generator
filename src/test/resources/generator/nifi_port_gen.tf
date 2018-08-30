resource "nifi_port" "PRT_ROOT" {

	component	{
		parent_group_id = "aaaa-bbbb-cccc-dddd"
		name = "PRT_ROOT"
		type = "INPUT_PORT"
		
		position	{
			x = 100.0
			y = 100.0
		}
	
	}

}
