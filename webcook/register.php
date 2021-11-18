<?php
	include 'engine.php';
	
	if(!isset($_GET['name']) || !isset($_GET['family']) || !isset($_GET['phone']) || !isset($_GET['pass'])) {
		echo "Ошибка!\nНе указан один из параметров формы.";
		return;
	}

	$firstname = $_GET['name'];
	$lastname = $_GET['family'];
	$phone = $_GET['phone'];
	$password = $_GET['pass'];

	$query = mysqli_query($link, "select * from `cook_user` where `Phone` = '$phone' limit 1");
	$rows = mysqli_num_rows($query);

	if($rows == 1) {
		echo "Ошибка!\nЭтот номер уже используется!";
		return;
	}

	$query = "insert into `cook_user` (`Name`, `Family`, `Phone`, `Password`, `LastOnline`) values ('$firstname', '$lastname', '$phone', '$password', '" . time() . "')";
	if(mysqli_query($link, $query)) {
		echo "Успешная регистрация! Теперь Вы можете войти в свой Личный Кабинет.";
		return;
	}
	echo mysqli_error($link);