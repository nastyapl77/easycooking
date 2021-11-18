<?php
    include 'engine.php';
    
    $id = $_GET['id'];
    $q = "select * from `cook_recipe` where `Category` = '$id' AND `Valid` = '1'";
    if(isset($_GET['sort'])) { 
    	$sort = $_GET['sort'];
    	if($sort == 1) { $q = "select * from `cook_recipe` where `Category` = '$id' AND `Valid` = '1' ORDER BY `Calories` DESC"; }
    	if($sort == 2) { $q = "select * from `cook_recipe` where `Category` = '$id' AND `Valid` = '1' ORDER BY `Calories` ASC"; }
    }

    $query = mysqli_query($link, $q);
    $array = array();
	while($row = mysqli_fetch_assoc($query)) { 
		$newjson = $row;
		array_push($array, $newjson);
    }
    echo json_encode($array); 