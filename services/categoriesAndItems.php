<?php
	include_once ("helperMethods/parameterManagement.php");
	include_once ("helperMethods/itemManagement.php");

	function getData($parameters){
		$data = array();
		global $conn;
		if ($parameters) {
			$data = getCategoriesAndItemsTree($parameters[1]);
		} else {
			/*by default return everything */
			$topLevelNames = getTopLevelNames();
			foreach ($topLevelNames as $name) {
				$data[$name] = getCategoriesAndItemsTree($name); 
			}
		}
		return $data;
	}

	$parameters = getParameters();
	$data = getData($parameters);
	
	$json_data = json_encode($data);
	echo $json_data;
