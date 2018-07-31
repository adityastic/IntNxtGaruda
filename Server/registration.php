

<?php

$name=$_GET['name'];
$email=$_GET['email'];
$number=$_GET['number'];
$gender=$_GET['gender'];
$dob=$_GET['dob'];
$password=$_GET['pass'];

$pass='pass@123.*';
        $db="nxtgarud_db";
        $user='nxtgarud_user';

	$mycon=new mysqli("localhost",$user,$pass,$db)or die("unable to connect");
	$sql="insert into registration (name,email,number,gender,dob,password) values('$name','$email','$number','$gender','$dob','$password')";

	$result=$mycon->query($sql);
	if($result>0)
		{
										   $Info = new stdClass();
                                           $Info->code="200";
 							 			   echo json_encode($Info);
		}
	else
		{
                                 		   $Info = new stdClass();
                                           $Info->code="503";
 							 			   echo json_encode($Info);
		}


?>







