<?php
	include_once ("helperMethods/itemManagement.php");
	include_once ("helperMethods/parameterManagement.php");

	$parameters = getParameters();
	$data = getData($parameters);

	function getData($parameters){
		global $conn;
		$data = array();
		if ($parameters) {
			$data = getItemList($parameters[1]);
			//var_dump($data);
		} else {
			/*by default return everything */
			$topLevelNames = getTopLevelNames();
			foreach ($topLevelNames as $name) {
				$data = array_merge (getItemList($name), $data); 
			}
		}
		return $data;
	}

	$parameters = getParameters();
	$data = getData($parameters);
	
	$json_data = json_encode($data);
	echo $json_data;
	
?>

