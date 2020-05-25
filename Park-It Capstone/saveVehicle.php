<?php
define('HOST','db.soic.indiana.edu');
define('USER','i494f19_team56');
define('PASS','my+sql=i494f19_team56');
define('DB','i494f19_team56');
 
$con = mysqli_connect(HOST,USER,PASS,DB);

$userID = 3;



$personID = $_POST["mainID"];
$str_make = $_POST["make"];  
$str_model = $_POST["model"];
$str_year = $_POST["year"];
$str_color = $_POST["color"];


$sql = "REPLACE INTO Vehicle (make, model, year, color, userID, mainID) VALUES('$str_make', '$str_model', '$str_year', '$str_color', '$userID', '$personID')";


$result = mysqli_query($con,$sql);

echo $result;
$con->close();
?>