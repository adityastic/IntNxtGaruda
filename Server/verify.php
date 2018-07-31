

<?php

$username=$_GET['uname'];


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
                                           $Info->code="200";
 							 			   echo json_encode($Info);

 							 }

 							
				}
			else
				{
										   $Info = new stdClass();
                                           $Info->code="503";
 							 			   echo json_encode($Info);
				}

?>




