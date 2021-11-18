<?php
	include 'engine.php';

    $id = $_GET['id'];
    $query = mysqli_query($link, "select * from `cook_recipe` where `Country` = '$id'"); 

    $array = array();
	while($row = mysqli_fetch_assoc($query)) { 
		$newjson = $row;
		array_push($array, $newjson);
    }
    echo json_encode($array); 