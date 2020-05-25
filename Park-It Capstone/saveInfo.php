<?php

define('HOST','db.soic.indiana.edu');
define('USER','i494f19_team56');
define('PASS','my+sql=i494f19_team56');
define('DB','i494f19_team56');
 
$con = mysqli_connect(HOST,USER,PASS,DB);

$personID = $_POST["userID"];
$str_username = $_POST["userName"];  
$str_first = $_POST["firstName"];
$str_last = $_POST["lastName"];
$str_phone = $_POST["phone"];

$sql = "UPDATE Users SET userName='$str_username', firstName='$str_first', lastName='$str_last', phone='$str_phone' WHERE userID = '$personID'";

if ($con->query($sql) === TRUE) {
    echo "Account info updated!";
} else {
    echo "Error: " . $sql . "<br>" . $con->error;
}

$con->close();
?>