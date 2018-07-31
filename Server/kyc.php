<?php


$pass='pass@123.*';
        $db="nxtgarud_db";
        $user='nxtgarud_user';
        $n=$_GET['number'];

  $mycon=new mysqli("localhost",$user,$pass,$db)or die("unable to connect");
  $sql="select * from registration where number='$n'";

  $result=$mycon->query($sql);
if(mysqli_num_rows($result)>=1)
        {

            if ($row = mysqli_fetch_assoc($result))
                 {
   
                                      $kyc=$row['kyc'];
                                      if($kyc)
                                      {
                                          echo "already present!!";
                                      }
                                      else
                                      {
?>
<!DOCTYPE html>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title></title>
</head>
<body>

	
	<form action="kycdone.php?number=<?php echo $_GET['number']; ?>" method="post" enctype="multipart/form-data">
		<?php
function check($value)
{

    switch ($value->type) {
    	case 'text':
    		return '<input type="text" name="' . $value->name . '" placeholder="' . $value->hint . '">';
    		break;
    	
    	case 'button':
    		return '<input type="button"  value="' . $value->value . '">';
    		break;


    	case 'select':

    	       $arr=$value->value;

    			$ans='<select name="'.$value->name.'">';
    		
    	       foreach ($arr as $key => $value) {
    	       $out="<option value='".$value."''>".$value."</option>";
 				$ans = $ans . $out;
    	       }
    	      $ans = $ans . "</select>";
    	      
    		return $ans;
    
    		break;

        
    	case 'radio':
    	        return '<input type="radio" name="'.$value->name.'" value="'.$value->value.'" checked>'.$value->value;
    	    		break;

        case 'checkbox':

         return '<input type="checkbox" name="'.$value->name.'" value="'.$value->value.'">'.$value->value;
 

        break;
        case 'file':

         return  '<input type="'.$value->type.'" name="'.$value->name.'" id="'.$value->id.'">';
 

        break;
    	default:
    		# code...
    		break;
    }



         } 

                               			      

$myfile = fopen("kjson.json", "r") or die("Unable to open file!");
$data= fread($myfile,filesize("kjson.json"));
fclose($myfile);

                     $info=new Stdclass();
                     $name=array();
		      		$arr=json_decode($data);
                     $count=0;
					    foreach ($arr as $key => $value) {
					   echo "<br>";
						echo "<table  bordercolor=blue  cellspacing=2 cellpadding=2>";
						echo "<tr>";
						  echo "<td>".$value->hint."</td>";
						  echo "<td>".check($value)."</td>";
                        echo"</tr>";
                        echo"</table>"; 


						
						

						array_push($name,$info);

					    $result= json_encode($arr);

    
 						$myfile1 = fopen("kycdata.json", "w") or die("Unable to open file!");;
    					fwrite($myfile1, $result);
    					fclose($myfile1);

                               			  }
                               			 

 						





                       
						
				

					


	

					
 ?>


<br><br>
<input type="submit" value="Submit">
	</form>



</body>
</html>
<?php
}
   }

              
        }
      else
        {
                      echo "No user Found!!";
        }?>