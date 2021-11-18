<?php
  	include 'engine.php';
	$name = $_GET['name'];
	$photo = rand(100000)+10000 . "_" . time() . ".png";
	$target_file_name = "./images/img_$photo";

	if (isset($_FILES["photo"])) {
		if (move_uploaded_file($_FILES["photo"]["tmp_name"], $target_file_name)) {
			$query = "insert into `cook_category` (`Name`, `Photo`, `Valid`) values ('$name', 'img_$photo', '0')";
			file_put_contents("error.txt", $query);
			if(mysqli_query($link, $query)) {
				echo "Успешная регистрация! Теперь Вы можете войти в свой Личный Кабинет.";
				return;
			}
		}
		else {
			file_put_contents("error.txt", "Неизвестная ошибка при загрузке."); 
		}
	}
	else {
		file_put_contents("error.txt", "Вы не выбрали фото на предыдущем этапе."); 
	}
?>