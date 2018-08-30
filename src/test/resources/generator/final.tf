resource "nifi_process_group" "PGP_VY1R5P_TEST_PROCS_GRP" {

	component	{
		parent_group_id = "aaaa-bbbb-cccc-dddd"
		name = "PGP_VY1R5P_TEST_PROCS_GRP"
		
		position	{
			x = 100.0
			y = 100.0
		}
	
	}

}

resource "nifi_connection" "CON_NAME" {

	component	{
		parent_group_id = "bbbb-cccc-dddd-eeee"
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

resource "nifi_port" "PRT_TYP_NAME" {

	component	{
		parent_group_id = "bbbb-cccc-dddd-eeee"
		name = "PRT_TYP_NAME"
		type = "OUTPUT_PORT"
		
		position	{
			x = 100.0
			y = 100.0
		}
	
	}

}

resource "nifi_port" "PRT_TYP_NAME" {

	component	{
		parent_group_id = "bbbb-cccc-dddd-eeee"
		name = "PRT_TYP_NAME"
		type = "INPUT_PORT"
		
		position	{
			x = 100.0
			y = 100.0
		}
	
	}

}

resource "nifi_processor" "PCR_NAME" {

	component	{
		parent_group_id = "bbbb-cccc-dddd-eeee"
		name = "PCR_NAME"
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
				"PROP_NAME" = "PROP_VALUE"
			}
		
			auto_terminated_relationships	=	[
				"RLSP"
			]
		
		}
	
	}

}

resource "nifi_controller_service" "CSR_NAME" {

	component	{
		parent_group_id = "bbbb-cccc-dddd-eeee"
		name = "CSR_NAME"
		type = "com.nordstrom.mlsort.jaxb.ControllerServiceType"
		properties	{
			"PROP_NAME" = "PROP_VALUE"
		}
	
	}

}

resource "nifi_funnel" "FNL_GAY1KH" {

	component	{
		parent_group_id = "bbbb-cccc-dddd-eeee"
		
		position	{
			x = 100.0
			y = 100.0
		}
	
	}

}
