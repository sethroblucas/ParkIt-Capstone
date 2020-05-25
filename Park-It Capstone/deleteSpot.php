<?php
define('HOST','db.soic.indiana.edu');
define('USER','i494f19_team56');
define('PASS','my+sql=i494f19_team56');
define('DB','i494f19_team56');
 
$con = mysqli_connect(HOST,USER,PASS,DB);
$spotID = $_POST["spotID"];
$sql = "DELETE FROM Spot WHERE spotID = '$spotID'";
if ($con->query($sql) === TRUE) {
    echo "Spot deleted";
} else {
    echo "Error: " . $sql . "<br>" . $con->error;
}
$con->close();
?>