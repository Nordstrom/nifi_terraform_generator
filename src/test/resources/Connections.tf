resource "nifi_connection" "Connections_null" {

	component	{
		parent_group_id = null
		back_pressure_data_size_threshold = "100"
		back_pressure_object_threshold = null
		
		source	{
			id = null
			type = null
			group_id = null
		}
	
		
		destination	{
			id = null
			type = null
			group_id = null
		}
	
		selected_relationships	=	[
		
		]
	
		bends = []
	}

}

