<!DOCTYPE html>
<html>
<head>
	<title></title>
</head>
<body>

  <h1>Insertion part</h1>

<form action="Aboutus.php" method="post">

	Enter Link Of the Logo <input type="text" name="li"><br>
   
   Enter Name of the Company <input type="text" name="name"><br>

  Enter Description about Company <input type="text" name="description"><br>

  
 Our Vision:  <input type="text" name="OurVision"><br>
 
 Our Value:<input type="text" name="OurValue"><br>

 Our Misiion:<input type="text" name="OurMission"><br>


 
  <input type="submit" value="insert"><br>


</form>




<?php

if($_SERVER['REQUEST_METHOD'] == 'POST')
{


$news=new Stdclass();

$value=$_POST['OurValue'];
$vison=$_POST['OurVision'];
$mission=$_POST['OurMission'];
$name=$_POST['name'];
$li=$_POST['li'];
$des=$_POST['description'];
  

$news->des=$des;

$news->logo=$li;
$news->name=$name;
$news->Ourmission=$mission;
$news->Ourvision=$vison;
$news->Ourvalue=$value;



       
$result1=json_encode($news);

 $myfile1 = fopen("aboutus.json", "w") or die("Unable to open file!");;
    fwrite($myfile1, $result1);
    fclose($myfile1);



}

?>