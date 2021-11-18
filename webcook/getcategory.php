<?php
	include 'engine.php'; 
    
    $id = $_GET['id'];
    $query = mysqli_query($link, "select * from `cook_category` where `ID` = '$id'");
    $row = mysqli_fetch_assoc($query);
    echo json_encode($row); 