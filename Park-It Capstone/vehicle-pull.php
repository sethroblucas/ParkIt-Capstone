<?php

define('HOST','db.soic.indiana.edu');
define('USER','i494f19_team56');
define('PASS','my+sql=i494f19_team56');
define('DB','i494f19_team56');
 
$con = mysqli_connect(HOST,USER,PASS,DB);

$personID  = $_GET['mainID'];
 
$sql = "select * from Vehicle where mainID = '$personID'";
 
$res = mysqli_query($con,$sql);
 
$result = array();
 
while($row = mysqli_fetch_array($res)){
array_push($result,array(
'make'=>$row[1], 
'model'=>$row[2],
'year'=>$row[3],
'color'=>$row[4],
));
}
 
echo json_encode(array("result"=>$result));
 
mysqli_close($con);
 
?>