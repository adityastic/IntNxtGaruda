<!DOCTYPE html>
<html>
<head>
	<title></title>
</head>
<body>

  <h1>Insertion part</h1>

<form action="news.php" method="post">
	Enter news title <input type="text" name="sname"><br>

  Enter news Description<input type="text" name="description"><br>


  Enter Link<input type="text" name="li"><br>
  <input type="submit" value="insert"><br>


</form>

<?php

if($_SERVER['REQUEST_METHOD'] == "POST")
{


$data = file_get_contents ("news.json");
        $arr = json_decode($data, true);
         if (empty($arr))
            $arr = array();

$name=$_POST['sname'];
$des=$_POST['description'];
$link=$_POST['li'];
date_default_timezone_set("Asia/Kolkata");
$dt = new DateTime();
$date=$dt->format('Y-m-d H:i:s');


$news=new Stdclass();
$news->info=$name;
$news->des=$des;
$news->li=$link;
$news->date=$date;


array_push($arr,$news);



       
$result1=json_encode($arr);

 $myfile1 = fopen("news.json", "w") or die("Unable to open file!");;
    fwrite($myfile1, $result1);
    fclose($myfile1);



}

?>