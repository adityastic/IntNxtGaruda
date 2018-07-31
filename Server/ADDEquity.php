<!DOCTYPE html>
<html>
<head>
  <title></title>
</head>
<body>

  <h1>ADD  SERVICE</h1>
<form action="<?php $_SERVER['PHP_SELF']; ?>" method="post">
  Enter Services name <input type="text" name="sname"><br>

  Enter Description<input type="text" name="description"><br>
  

  Enter Montly Price<input type="number" name="monthlyprice"><br>
  

  Enter Quaterly Price<input type="number" name="quaterlyprice"><br>
  

  Enter Halfyearly Price<input type="number" name="hyearlyprice"><br>
  

  Enter Yearly Price<input type="number" name="yearlyprice"><br>
  
  <input type="submit" value="insert"><br>
</form>

<?php

if($_SERVER['REQUEST_METHOD']=="POST")
{
$name=$_POST['sname'];
$des=$_POST['description'];
$monthly=$_POST['monthlyprice'];
$quaterly=$_POST['quaterlyprice'];
$hyearly=$_POST['hyearlyprice'];
$yearly=$_POST['yearlyprice'];


    $pass='pass@123.*';
        $db="nxtgarud_db";
        $user='nxtgarud_user';
    


$mycon=new mysqli("localhost",$user,$pass,$db)or die("unable to connect");
$sql="insert into Equityservices (name,des,monthly,quaterly,hyearly,yearly) values('$name','$des',$monthly,$quaterly,$hyearly,$yearly)";

$result=$mycon->query($sql);
if($result>0)
{
  echo "succesfully inserted";
}
else
{
  echo "not inserted";
}

     
}
?>




</body>
</html>
