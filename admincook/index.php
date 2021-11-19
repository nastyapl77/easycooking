<?php
    include 'engine.php';

    $query = mysqli_query($link, "select * from `cook_user` where 1");
    $users = mysqli_num_rows($query);
    $query = mysqli_query($link, "select * from `cook_country` where 1");
    $countrys = mysqli_num_rows($query);
    $query = mysqli_query($link, "select * from `cook_category` where 1");
    $categories = mysqli_num_rows($query);
    $query = mysqli_query($link, "select * from `cook_recipe` where 1");
    $recipes = mysqli_num_rows($query);

?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>EasyCooking | AdminPanel</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <meta name="author" content="Muhammad Usman">
    <link id="bs-css" href="css/bootstrap-cybrog.min.css" rel="stylesheet">
    <link href="css/charisma-app.css" rel="stylesheet">
    <link href='bower_components/fullcalendar/dist/fullcalendar.css' rel='stylesheet'>
    <link href='bower_components/fullcalendar/dist/fullcalendar.print.css' rel='stylesheet' media='print'>
    <link href='bower_components/chosen/chosen.min.css' rel='stylesheet'>
    <link href='bower_components/colorbox/example3/colorbox.css' rel='stylesheet'>
    <link href='bower_components/responsive-tables/responsive-tables.css' rel='stylesheet'>
    <link href='bower_components/bootstrap-tour/build/css/bootstrap-tour.min.css' rel='stylesheet'>
    <link href='css/jquery.noty.css' rel='stylesheet'>
    <link href='css/noty_theme_default.css' rel='stylesheet'>
    <link href='css/elfinder.min.css' rel='stylesheet'>
    <link href='css/elfinder.theme.css' rel='stylesheet'>
    <link href='css/jquery.iphone.toggle.css' rel='stylesheet'>
    <link href='css/uploadify.css' rel='stylesheet'>
    <link href='css/animate.min.css' rel='stylesheet'>
    <script src="bower_components/jquery/jquery.min.js"></script>
    <link rel="shortcut icon" href="img/favicon.ico">
</head>
<body>
    <?php include 'topbar.php'; ?>
    <div class="ch-container">
        <div class="row">
            <div class="btn-group pull-right theme-container animated tada">
                <button class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                    <i class="glyphicon glyphicon-tint"></i><span
                        class="hidden-sm hidden-xs"> Change Theme / Skin</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" id="themes">
                    <li><a data-value="classic" href="#"><i class="whitespace"></i> Classic</a></li>
                    <li><a data-value="cerulean" href="#"><i class="whitespace"></i> Cerulean</a></li>
                    <li><a data-value="cyborg" href="#"><i class="whitespace"></i> Cyborg</a></li>
                    <li><a data-value="simplex" href="#"><i class="whitespace"></i> Simplex</a></li>
                    <li><a data-value="darkly" href="#"><i class="whitespace"></i> Darkly</a></li>
                    <li><a data-value="lumen" href="#"><i class="whitespace"></i> Lumen</a></li>
                    <li><a data-value="slate" href="#"><i class="whitespace"></i> Slate</a></li>
                    <li><a data-value="spacelab" href="#"><i class="whitespace"></i> Spacelab</a></li>
                    <li><a data-value="united" href="#"><i class="whitespace"></i> United</a></li>
                </ul>
            </div>
                <?php include 'menu.php'; ?>
                <div id="content" class="col-lg-10 col-sm-10">
                    <div>
                        <ul class="breadcrumb">
                            <li>
                                <a href="#">Главная</a>
                            </li>
                        </ul>
                    </div>
                    <div class=" row">
                        <div class="col-md-3 col-sm-3 col-xs-6">
                            <a data-toggle="tooltip" title="<? echo $users; ?> new members." class="well top-block" href="#">
                                <i class="glyphicon glyphicon-user blue"></i>
                                <div>Всего пользователей</div>
                                <div><? echo $users; ?></div>
                            </a>
                        </div>
                        <div class="col-md-3 col-sm-3 col-xs-6">
                            <a data-toggle="tooltip" title="<? echo $categories; ?> new categories." class="well top-block" href="#">
                                <i class="glyphicon glyphicon-star green"></i>
                                <div>Групп рецептов</div>
                                <div><? echo $categories; ?></div>
                            </a>
                        </div>
                        <div class="col-md-3 col-sm-3 col-xs-6">
                            <a data-toggle="tooltip" title="<? echo $recipes; ?> new recipes." class="well top-block" href="#">
                                <i class="glyphicon glyphicon-shopping-cart yellow"></i>
                                <div>Рецептов</div>
                                <div><? echo $recipes; ?></div>
                            </a>
                        </div>
                        <div class="col-md-3 col-sm-3 col-xs-6">
                            <a data-toggle="tooltip" title="<? echo $countrys; ?> new countries." class="well top-block" href="#">
                                <i class="glyphicon glyphicon-envelope red"></i>
                                <div>Национальных кухонь</div>
                                <div><? echo $countrys; ?></div>
                            </a>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="box col-md-4">
                        <div class="box-inner">
                            <div class="box-header well" data-original-title="">
                                <h2><i class="glyphicon glyphicon-user"></i> Последние 5 рецептов</h2>
                                <div class="box-icon">
                                    <a href="#" class="btn btn-minimize btn-round btn-default"><i
                                            class="glyphicon glyphicon-chevron-up"></i></a>
                                    <a href="#" class="btn btn-close btn-round btn-default"><i
                                            class="glyphicon glyphicon-remove"></i></a>
                                </div>
                            </div>
                            <div class="box-content">
                                <div class="box-content">
                                    <ul class="dashboard-list">
                                        <?php

                                            $query = mysqli_query($link, "select * from `cook_recipe` where `Valid` = '1' order by `ID` desc limit 5");
                                            echo mysqli_error($link);
                                            while($row = mysqli_fetch_assoc($query)) {
                                                $id = $row['ID'];
                                                $name = $row['Name'];
                                                $photo = $site . "/images/" . $row['Photo'];
                                                $time = $row['Time'];
                                                $cal = $row['Calories'];
                                                $msg = "<li><a href=\"recipe.php?id=$id\"><img class=\"dashboard-avatar\" alt=\"$name\"
                                                             src=\"$photo\"></a>
                                                    <strong>Название:</strong> <a href=\"recipe.php?id=$id\">$name
                                                    </a><br>
                                                    <strong>Время:</strong> $time<br>
                                                    <strong>Каллории:</strong> $cal
                                                </li>";
                                                echo $msg;

                                            }
                                        ?>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <hr>
    <? include "footer.php"; ?>
    <script src="bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
    <script src="js/jquery.cookie.js"></script>
    <script src='bower_components/moment/min/moment.min.js'></script>
    <script src='bower_components/fullcalendar/dist/fullcalendar.min.js'></script>
    <script src='js/jquery.dataTables.min.js'></script>
    <script src="bower_components/chosen/chosen.jquery.min.js"></script>
    <script src="bower_components/colorbox/jquery.colorbox-min.js"></script>
    <script src="js/jquery.noty.js"></script>
    <script src="bower_components/responsive-tables/responsive-tables.js"></script>
    <script src="bower_components/bootstrap-tour/build/js/bootstrap-tour.min.js"></script>
    <script src="js/jquery.raty.min.js"></script>
    <script src="js/jquery.iphone.toggle.js"></script>
    <script src="js/jquery.autogrow-textarea.js"></script>
    <script src="js/jquery.uploadify-3.1.min.js"></script>
    <script src="js/jquery.history.js"></script>
    <script src="js/charisma.js"></script>
    </body>
</html>
