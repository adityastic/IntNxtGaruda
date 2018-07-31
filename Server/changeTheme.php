<!DOCTYPE html>
<html>
<head>
	<title>nxt visoion firstapi</title>
</head>
<body>

	<form action='changeTheme.php' method='GET'>

    <h1> CHOOSE COLORS </h1>

  <input type="radio" name="gender" value="red" checked> RED<br>
  <input type="radio" name="gender" value="blue"> BLUE<br>
  <input type="radio" name="gender" value="green"> GREEN<br>  
  <input type="radio" name="gender" value="grey"> GREY<br>
  <input type="radio" name="gender" value="salmon">SALMON<br>  
  <input type="radio" name="gender" value="seagreen"> SEAGREEN<br>
  <input type="radio" name="gender" value="rosybrown">ROSYBROWN<br>
  <input type="radio" name="gender" value="mulberry">MULBERRY<br> 
  <input type="radio" name="gender" value="wine">WINE<br>     






<h1> CHOOSE LAYOUTS </h1>

  <input type="radio" name="layout" value="linear">LINEAR<br>  
  <input type="radio" name="layout" value="grid"> GRID<br><br>
 

   <input type="submit" name="bu1" value="send"><br>
	</form>
<?php
if ($_SERVER['REQUEST_METHOD'] == "GET") {


 $color = $_GET['gender'];
 $lay=$_GET['layout'];


    echo $color;

    $Color = new stdClass();
    $Color->color = $color;

    $Color->layout =$lay;




    $result = json_encode($Color);


    $myfile = fopen("Theme.json", "w") or die("Unable to open file!");;
    fwrite($myfile, $result);
    fclose($myfile);





}

?>

</body>
</html>