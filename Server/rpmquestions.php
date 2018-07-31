<?php
    $_filename = "rpmquestions.json";

    if ($_SERVER["REQUEST_METHOD"]=="POST") {

    	if($_POST["operation"] == "create")
    	{
    		$question = $_POST['question'];
	        $answers = $_POST['ans'];

	        if (!file_exists($_filename)) {
	            file_put_contents($_filename, "[]");
	        }
	        $_jsonArr = json_decode(file_get_contents($_filename));

	        $_jsonObject = new Stdclass();
	        $_jsonObject->question = $question;
	        $_jsonObject->answers = $answers;

	        array_push($_jsonArr,$_jsonObject);

	        file_put_contents($_filename, json_encode($_jsonArr));
    	}else if($_POST["operation"] == "delete")
    	{
	        file_put_contents($_filename, "[]");
    	}
    }
?>
<!DOCTYPE html>
<html>
	<head>
	    <title>RPM Questions</title>
	</head>
	<body>
	    <h1>Create Questions</h1>
	    <form action="rpmquestions.php" method="post">
	        Question :<input type="text" name="question"><br>
	        Enter number of answers:<input type="number" id="noofin"><button type="button" onclick="addInputs()">Add Answer Fields</button>
	        <div id="inputfields">
	        </div>
	        <input type="submit" value="Submit Question">
	        <input type="text" name="operation" value="create" hidden>
	    </form>
	    <h1>Delete Questions</h1>
	    <form action="rpmquestions.php" method="post">
	        <input type="submit" value="Delete All">
	        <input type="text" name="operation" value="delete" hidden>
	    </form>
	</body>
	<script type="text/javascript">
		function addInputs()
		{
			var no = document.getElementById('noofin').value;
			console.log(no);
			var dummy = '<span>Answer {0} : <input type="text" name="ans[]"></span><br>';
			var str = "";
			for(var i = 1; i <= no ; i++)
			{
				str += dummy.replace(new RegExp("\\{0\\}", 'g'), i);
			}
			document.getElementById('inputfields').innerHTML = str; 
		}
	</script>
</html>