<?php
	$db_host = "localhost";
	$db_username = "root";
	$db_password = "";
	$db_name = "items";
	$conn = new mysqli($db_host, $db_username, $db_password, $db_name);
	if ($conn->connect_errno) { //all but 0 is true.
	    return null;
	}
	return $conn;
?>