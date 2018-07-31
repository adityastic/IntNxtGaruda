

<?php

$payid=$_GET['payid'];
$name=$_GET['name'];
$number=$_GET['number'];
$amount=$_GET['amount'];
$sname=$_GET['sname'];
$sterm=$_GET['sterm'];
$date= date('m/d/Y');



    $pass='pass@123.*';
    $db="nxtgarud_db";
    $user='nxtgarud_user';

$mycon=new mysqli("localhost",$user,$pass,$db)or die("unable to connect");
$sql="insert into payment_info (number,name,amount,pay_id,service_name,service_term,date) values('$number','$name','$amount','$payid','$sname','$sterm','$date')";

$result=$mycon->query($sql);
$ans = new stdClass();
if($result>0)
{
    $ans->code = "100";
}
else
{
    $ans->code = "404";
}
echo json_encode($ans);


?>







