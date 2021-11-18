<?php
	include 'engine.php';
	
	if(!isset($_GET['phone']) || !isset($_GET['pass'])) {
		echo "Ошибка!\nНе указан один из параметров формы.";
		return;
	}
	$phone = $_GET['phone'];
	$password = $_GET['pass'];

	$query = mysqli_query($link, "select * from `cook_user` where `Phone` = '$phone' AND `Password` = '$password' limit 1");
	$rows = mysqli_num_rows($query);

	if($rows == 1) {
		$row = mysqli_fetch_assoc($query);
		$array = array(
			'ID' => $row['ID'],
			'LastOnline' => $row['LastOnline']
		);
		echo json_encode($array);
		return;
	}
	echo "Ошибка!\nАккаунт в базе данных не найден. Проверьте введённые Вами значения.";
	