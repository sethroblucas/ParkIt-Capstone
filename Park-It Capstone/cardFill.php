<?php
define('HOST','db.soic.indiana.edu');
define('USER','i494f19_team56');
define('PASS','my+sql=i494f19_team56');
define('DB','i494f19_team56');
 
$con = mysqli_connect(HOST,USER,PASS,DB);
 
$sql = "select spotID, street, city, state, zip, description, hourlyCost from Spot";
 
$res = mysqli_query($con,$sql);
 
$result = array();
 
while($row = mysqli_fetch_array($res)){
array_push($result,array(
'spotID'=>$row[0],
'street'=>$row[1],
'city'=>$row[2], 
'state'=>$row[3],
'zip'=>$row[4],
'description'=>$row[5],
'cost'=>$row[6],
));
}
 
echo json_encode(array("result"=>$result));
 
mysqli_close($con);
 
?>