<!DOCTYPE html>
<html>
<head>
	<title></title>
</head>
<body>

<form action="tabs.php" method="post">

enter file name<input type="text" name="fname">
<input type="submit" value="createtab">


</form>

<?php

if($_SERVER['REQUEST_METHOD']=="POST")
{
$info=new Stdclass();
	$arr=array();
$name=$_POST['fname'];

		$pass='pass@123.*';
        $db="nxtgarud_db";
        $user='nxtgarud_user';
    $sql1 = "
CREATE TABLE ".$name . "services(
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `des` text NOT NULL,
  `monthly` double NOT NULL,
  `quaterly` double NOT NULL,
  `hyearly` double NOT NULL,
  `yearly` double NOT NULL
)";

$con=new mysqli("localhost",$user,$pass,$db)or die("unable to connect");
$result=$con->query($sql1);
if($result>0)
{
  echo "succesfully created";
}
else
{
  echo "not created";
}
$myfile = fopen("servicedatabase.php", "r") or die("Unable to open file!");
$data= fread($myfile,filesize("servicedatabase.php"));

$data=str_replace("#12345",$name."services",$data);
fclose($myfile);

$myfile1 = fopen("ADD".$name.".php", "w") or die("Unable to open file!");;
    fwrite($myfile1, $data);
    fclose($myfile1);


$myfile = fopen("servicedatabase1.php", "r") or die("Unable to open file!");
$data1= fread($myfile,filesize("servicedatabase.php"));

$data1=str_replace("#12345",$name."services",$data1);
fclose($myfile);

    $myfile2 =fopen($name."APISERVICE.php","w")or("Unable to open file!");;
    fwrite($myfile2, $data1);
    fclose($myfile2);


$data = file_get_contents ("tabs.json");
        $arr = json_decode($data, true);
         if (empty($arr))
            $arr = array();



$second="http://nxtgaruda.com/";

$first= $name."APISERVICE.php";

$link=$second.$first;

$info->name=$name;

$info->jsonlink=$link;



array_push($arr,$info);



       
$result1=json_encode($arr);

 $myfile3 = fopen("tabs.json", "w") or die("Unable to open file!");;
    fwrite($myfile3, $result1);
    fclose($myfile3);







}
?>
</body>
</html>