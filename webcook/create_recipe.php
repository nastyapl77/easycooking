<?php
  include 'engine.php';

  $name = $_GET['name'];
  $text = $_GET['text'];
  $time = $_GET['time']; 
  $cat = $_GET['cat'];   
  $coun = $_GET['coun'];  
  
  $photo = rand(100000)+10000 . "_" . time() . ".png";
  $target_file_name = "./images/recipe_$photo";

  if (isset($_FILES["photo"])) {
    if (move_uploaded_file($_FILES["photo"]["tmp_name"], $target_file_name)) {
      $query = "insert into `cook_recipe` (`Name`, `Photo`, `Time`, `Category`, `Valid`, `Text`, `Country`) values";
      $query .= " ('$name', 'recipe_$photo', '$time', '$cat', '0', '$text', '$coun')"; 
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