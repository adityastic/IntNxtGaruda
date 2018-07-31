

<?php

$username=$_GET['uname'];
$password=$_GET['pass'];


		$pass='pass@123.*';
        $db="nxtgarud_db";
        $user='nxtgarud_user';
		$con=new mysqli("localhost",$user,$pass,$db)or die("unable to connect");
		//$q="select * from register where name='$username' or phonenumber='$username'";
		$q="select * from registration where number='$username'";
		$result=$con->query($q);
			if(mysqli_num_rows($result)>=1)
				{

   					while ($row = mysqli_fetch_assoc($result))
   					 		 {
   
    				                      $Info = new stdClass();
    									  $Info->name= $row['name'];
                               		 	  $Info->phone=$row['number'];
 							 		 	  $Info->id=$row['email'];
                               			  $Info->gender=$row['gender'];
                               			  $Info->dob=$row['dob'];
                               			  $Info->password=$row['password'];
                               			  $data=json_encode($Info);
 							 			  echo $data;
 							 			

 							 }

 							
				}
			else
				{
										   $Info = new stdClass();
                                           $Info->code="503";
 							 			   echo json_encode($Info);
				}

?>




