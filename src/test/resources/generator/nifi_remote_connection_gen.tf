resource "nifi_connection" "CON_NECTION" {

	component	{
		parent_group_id = "aaaa-bbbb-cccc-dddd"
		back_pressure_data_size_threshold = "100"
		back_pressure_object_threshold = 10
		
		source	{
			id = "SRC_ID"
			type = "REMOTE_OUTPUT_PORT"
			group_id = "SRC_GRP_ID"
		}
	
		
		destination	{
			id = "DEST_ID"
			type = "REMOTE_INPUT_PORT"
			group_id = "DEST_GRP_ID"
		}
	
		selected_relationships	=	[
			"A"
		]
	
		bends = [{x=100.0, y=100.0}]
	}

}

resource "nifi_funnel" "FUNNEL" {

	component	{
		parent_group_id = "aaaa-bbbb-cccc-dddd"
		
		position	{
			x = 100.0
			y = 100.0
		}
	
	}

}
