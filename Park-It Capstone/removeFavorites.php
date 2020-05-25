<?php
define('HOST','db.soic.indiana.edu');
define('USER','i494f19_team56');
define('PASS','my+sql=i494f19_team56');
define('DB','i494f19_team56');
 
$con = mysqli_connect(HOST,USER,PASS,DB);
$ID = $_POST["mainID"];
$Address = $_POST["address"];
// sql to delete a record
$sql = "DELETE FROM FavoriteSpots WHERE mainID = '$ID' AND address = '$Address'";
if ($con->query($sql) === TRUE) {
    echo "Record deleted successfully";
} else {
    echo "Error deleting record: " . $con->error;
}
 
mysqli_close($con);
 
?>