<?php
	
	function getParameters() {
		$pathParametersArray = null;
		if (isset($_SERVER["PATH_INFO"])) {
			$pathParametersString = $_SERVER["PATH_INFO"];
			$pathParametersArray = explode("/", $pathParametersString);
		}
		return $pathParametersArray;
	}

?>