<?php
	include 'engine.php';

    $id = $_GET['id']; 
    $query = mysqli_query($link, "select * from `cook_country` where `ID` = '$id'"); 
	$row = mysqli_fetch_assoc($query);
	$newjson = $row;  
    echo json_encode($newjson); 