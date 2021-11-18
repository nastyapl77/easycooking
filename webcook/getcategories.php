<?php
	include 'engine.php';
    $query = mysqli_query($link, "select * from `cook_category` where `Valid` = '1'"); 

    $array = array();
	while($row = mysqli_fetch_assoc($query)) { 
		$newjson = $row;
		array_push($array, $newjson);
    }
    echo json_encode($array); 