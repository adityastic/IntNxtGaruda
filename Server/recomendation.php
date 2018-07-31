<!DOCTYPE html>
<html>
  <head>
    <title>
    </title>
  </head>
  <body>
    <h1>Insertion part
    </h1>
    <form action="recomendation.php" method="post">
      Enter news Description
      <input type="text" name="description">
      <br>
      <input type="submit" value="insert">
      <br>
    </form>
    
<?php
require 'GarudaFCMAPI.php';

$data = file_get_contents ("recom.json");
$arr = json_decode($data, true);
if (empty($arr))
$arr = array();

$des=$_POST['description'];
date_default_timezone_set("Asia/Kolkata");
$dt = new DateTime();
$date=$dt->format('Y-m-d H:i:s');
$news=new Stdclass();
$news->des=$des;
$news->date=$date;
array_push($arr,$news);

if($_SERVER['REQUEST_METHOD'] == "POST")
{
    $result1=json_encode($arr);
    $myfile1 = fopen("recom.json", "w") or die("Unable to open file!");;
    fwrite($myfile1, $result1);
    fclose($myfile1);

    send_notification($des,"Recommendation Updates","recommendation");
}
?>

