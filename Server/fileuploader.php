
<!DOCTYPE html>
<html>
<head>
	<title></title>
</head>

<body>
<form action="fileuploader.php" method="post" enctype="multipart/form-data">
<input type="file"  name="filepath" id="filepath"/></td><td><input type="submit" name="SubmitButton"/>

</td>
</form>

<?php    

if(isset($_POST['SubmitButton'])){ //check if form was submitted

$target_dir = 'sheetfiles/';
$target_file = $target_dir . basename($_FILES["filepath"]["name"]);
$imageFileType = pathinfo($target_file,PATHINFO_EXTENSION);

move_uploaded_file($_FILES["filepath"]["tmp_name"], $target_file);
echo "File Uploaded Sucessfully";
}
?>
</body>
</html>