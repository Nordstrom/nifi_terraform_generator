resource "nifi_port" "PROCESSOR" {

	component	{
		parent_group_id = "aaaa-bbbb-cccc-dddd"
		name = "PRT_TYP_NAME"
		type = "INPUT"
		
		position	{
			x = 100.0
			y = 100.0
		}
	
	}

}
