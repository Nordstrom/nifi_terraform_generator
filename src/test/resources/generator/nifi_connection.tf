resource "nifi_connection" "CONNECTION" {

	component	{
		parent_group_id = "aaaa-bbbb-cccc-dddd"
		back_pressure_data_size_threshold = "100"
		back_pressure_object_threshold = 10
		
		source	{
			id = "SRC_ID"
			type = "SRC_TYP"
			group_id = "SRC_GRP_ID"
		}
	
		
		destination	{
			id = "DEST_ID"
			type = "DEST_TYP"
			group_id = "DEST_GRP_ID"
		}
	
		selected_relationships	=	[
			"A"
		]
	
		bends = [{x=100.0, y=100.0}]
	}

}
