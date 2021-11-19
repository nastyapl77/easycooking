<?php
    include 'engine.php';

?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>EasyCooking | AdminPanel | Все рецепты</title>
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
            <?php include 'menu.php'; ?>
            <div id="content" class="col-lg-10 col-sm-10"></div>
            <ul class="breadcrumb">
                <li>
                    <a href="#">Главная</a>
                </li>
                <li>
                    <a href="#">Все рецепты</a>
                </li>
            </ul>
        </div>
        <div class="row">
            <div class="box col-md-12">
                <div class="box-inner">
                    <div class="box-header well" data-original-title="">
                        <h2><i class="glyphicon glyphicon-user"></i> Значения взяты из базы данных</h2>
                        <div class="box-icon">
                            <a href="#" class="btn btn-setting btn-round btn-default"><i class="glyphicon glyphicon-cog"></i></a>
                            <a href="#" class="btn btn-minimize btn-round btn-default"><i
                                    class="glyphicon glyphicon-chevron-up"></i></a>
                            <a href="#" class="btn btn-close btn-round btn-default"><i class="glyphicon glyphicon-remove"></i></a>
                        </div>
                    </div>
                    <div class="box-content">
                        <table class="table table-striped table-bordered bootstrap-datatable datatable responsive">
                            <thead>
                                <tr>
                                    <th>Название рецепта</th>
                                    <th>Время приготовления</th>
                                    <th>Каллории</th>
                                </tr>
                            </thead>
                            <tbody>
                                <?php
                                    $query = mysqli_query($link, "select * from `cook_recipe` where 1");
                                    echo mysqli_error($query);
                                    while($row = mysqli_fetch_assoc($query)) {
                                        $id = $row['ID'];
                                        $name = $row['Name'];
                                        $name = "<a href=\"recipe.php?id=$id\">$name</a>";
                                        $time = $row['Time'];
                                        $cal = $row['Calories'];
                                        $msg = "<tr>
                                                <td>$name</td>
                                                <td class=\"center\">$time</td>
                                                <td class=\"center\">$cal</td><td class=\"center\">";
                                        if($row['Valid'] == 1) $msg .= "<span class=\"label-success label label-default\">Одобрено</span>";
                                        else $msg .= "<span class=\"label-warning label label-default\">Рассматривается</span>";
                                        $msg .= "</td></tr>";
                                        echo $msg;
                                    }
                                ?>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
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
