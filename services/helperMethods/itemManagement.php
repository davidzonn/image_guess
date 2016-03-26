<?php
	
	$root = $_SERVER['DOCUMENT_ROOT'];
	$conn = include("../db/dbConnect.php");

	function getItems($categoryName) {
		global $conn;		
		$data = array();
		$sql = "SELECT item.name, item.route FROM item JOIN category ON item.id_category = category.id_category WHERE category.name = '$categoryName'";
		$result = $conn -> query($sql);
		while ($row = $result -> fetch_assoc()) {
			$itemName = $row["name"];
			$route = $row ["route"];

			$itemArray = array();
			$itemArray["type"] = "item";
			$itemArray["image"] = $route;
			$data [$itemName] = $itemArray;
		}
		//var_dump($data);
		return $data;
	}

	function getCategories($nameCategory) {
		global $conn;
		$itemsCategory = getItems($nameCategory);
		if ($itemsCategory) {
			return $nameCategory;
		} else {
			$data = array();
			$sql = "SELECT *
				FROM category AS category JOIN category AS subcategory ON category.id_category = subcategory.super_category
				WHERE category.name = '$nameCategory'";
			$result = $conn -> query($sql);
			while ($row = ($result -> fetch_assoc())) {
				$name = $row["name"];
				$subCategories = getCategories($name);
				if (!empty($subCategories)) {
					array_push($data, $subCategories); //recursion for all subcategories.
				} 	
			}
			return $data;
		}
	}

	function getTopLevelNames() {
		global $conn;
		$answer = array();
		$sql = "SELECT * FROM category WHERE category.super_category IS NULL";
		$result = $conn -> query($sql);
		while ($row = ($result -> fetch_assoc())) {
			$name = $row["name"];
			array_push($answer, $name); 	
		}
		return $answer;
	}

	function getCategoriesAndItemsTree($nameCategory) {
		
		$data =array();

		$itemsCategory = getItems($nameCategory);
		if ($itemsCategory) {
			$data["type"] = "itemsCategory";
			$data["name"] = $nameCategory;
			$data["items"] = $itemsCategory;
		} else {
			$itemArray["type"] = "item";
			global $conn;
			$sql = "SELECT *
				FROM category AS category JOIN category AS subcategory ON category.id_category = subcategory.super_category
				WHERE category.name = '$nameCategory'";
			$result = $conn -> query($sql);
			while ($row = ($result -> fetch_assoc())) {
				$name = $row["name"];
				//echo $name;
				$subCategoriesAndItems = getCategoriesAndItemsTree($name); //recursion for all subcategories.
				if (!empty($subCategoriesAndItems)) { 	
					$data[$name] = $subCategoriesAndItems;
				}
			}
		}
		return $data;
	}

	function getItemList($nameCategory) {

		$data =array();

		$itemsCategory = getItems($nameCategory);
		//var_dump($nameCategory);
		if ($itemsCategory) {
			$data = $itemsCategory;
		} else {
			global $conn;
			$sql = "SELECT *
				FROM category AS category JOIN category AS subcategory ON category.id_category = subcategory.super_category
				WHERE category.name = '$nameCategory'";
			$result = $conn -> query($sql);
			while ($row = ($result -> fetch_assoc())) {
				$name = $row["name"];
				//echo $name;
				$data = array_merge($data, getItemList($name)); //recursion for all subcategories. 	
				//var_dump($data);
			}
		}
		return $data;	
	}
?>