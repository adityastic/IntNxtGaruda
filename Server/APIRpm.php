<?php
    $_filename = "rpmquestions.json";

    $pass='pass@123.*';
    $db="nxtgarud_db";
    $user='nxtgarud_user';
    
    $_phone = $_GET['phone'];
    $_apply = $_GET['apply'];
    $_value = $_GET['value'];

    $con=new mysqli("localhost", $user, $pass, $db)or die('{"code":404,"Message":"Not able to connect to the server"}');
    $query="SELECT * FROM registration WHERE number='" . $_phone . "'";
    $result=$con->query($query);

    if (mysqli_num_rows($result)>=1) {
        if ($_apply == 'true') {
            $query="UPDATE registration SET rpm='" . $_value . "' WHERE number='" . $_phone . "'";
            if ($con->query($query) === true) {
                echo '[{"code":202,"Message":"Done"}]';
            } else {
                echo '[{"code":404,"Error":"' . $sql . "<br>" . $conn->error . '"}]';
            }
        } else {
            if ($row = mysqli_fetch_assoc($result)) {
                if ($row['rpm'] == '') {
                    echo file_get_contents($_filename);
                } else {
                    echo '[{"code":200,"Message":"RPM Done"}]';
                }
            }
        }
    } else {
        echo '[{"code":404,"Message":"No Users Found"}]';
    }
?>