<?php
define('HOST','db.soic.indiana.edu');
define('USER','i494f19_team56');
define('PASS','my+sql=i494f19_team56');
define('DB','i494f19_team56');
 
$con = mysqli_connect(HOST,USER,PASS,DB);
$ID = $_GET["mainID"];
 
$sql = "select * from Spot where mainID = '$ID'";
 
$res = mysqli_query($con,$sql);
 
$result = array();
 
while($row = mysqli_fetch_array($res)){
array_push($result,array(
'spotID' =>$row[0],
'street'=>$row[3],
'city'=>$row[4],
'state'=>$row[5],
'zip'=>$row[6],
'description'=>$row[7],
'hourlyCost'=>$row[12]
));
}
 
echo json_encode(array("result"=>$result));
 
mysqli_close($con);
 
?>