<?php

$myfile = fopen("kycdata.json", "r") or die("Unable to open file!");
$data= fread($myfile,filesize("kycdata.json"));
fclose($myfile);
$arr=array();
 $data;

 	$arr=json_decode($data);

$info=new Stdclass();
$name=array();

$info=new Stdclass();
 	foreach ($arr as $key => $value) {
 		
 		if($value->type=="file")
 		{
        $f= $_FILES[$value->name]['name'];
        echo "<br>";
        $na=$value->name;
        $info->$na=$f;
      
 	    }


 		else
 		{
         $f= $_POST[$value->name];
          $na=$value->name;
          $info->$na=$f;
       
         echo "<br>";
 	    }
 	}

 	 array_push($name,$info);

 $kyc=json_encode($name);
 
 
$pass='pass@123.*';
        $db="nxtgarud_db";
        $user='nxtgarud_user';
        $n=$_GET['number'];
        

	$mycon=new mysqli("localhost",$user,$pass,$db)or die("unable to connect");
	
	$sql="update registration SET kyc='$kyc' where number='$n'";

	$result=$mycon->query($sql);
	if($result>0)
		{
		    echo "request received";
		}
	else
		{
                         
		    echo "request not received";	   
		}


$target_dir = 'upload/';



$target_file = $target_dir . basename($_FILES["fname"]["name"]);
$imageFileType = pathinfo($target_file,PATHINFO_EXTENSION);

move_uploaded_file($_FILES["fname"]["tmp_name"], $target_file);





    
?>