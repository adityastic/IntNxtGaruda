 <?php

        $pass='pass@123.*';
        $db="nxtgarud_db";
        $user='nxtgarud_user';

$con=new mysqli("localhost",$user,$pass,$db)or die("unable to connect");
$q="select * from customservices";
$result=$con->query($q);


 $arr=array();
    if(mysqli_num_rows($result)>=1)
      {
while ($row = mysqli_fetch_assoc($result)) {

            $Service = new stdClass();
            $Service->id = $row['id'];
            $Service->name = $row['name'];
            $Service->des= $row['des'];
            $Service->monthly=$row['monthly'];
            $Service->quaterly=$row['quaterly'];
            $Service->hyearly=$row['hyearly'];
            $Service->yearly=$row['yearly'];
     
           
            array_push($arr,$Service);
}

$ans=json_encode($arr);
print_r($ans);
}

?>