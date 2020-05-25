<?php
define('HOST','db.soic.indiana.edu');
define('USER','i494f19_team56');
define('PASS','my+sql=i494f19_team56');
define('DB','i494f19_team56');
 
$con = mysqli_connect(HOST,USER,PASS,DB);
$ID = $_GET["mainID"];
 
$sql = "select * from FavoriteSpots where mainID = '$ID'";
 
$res = mysqli_query($con,$sql);
 
$result = array();
 
while($row = mysqli_fetch_array($res)){
array_push($result,array(
'address' =>$row[4],
'description'=>$row[5],
'date'=>$row[6],
'totalCost'=>$row[7]
));
}
 
echo json_encode(array("result"=>$result));
 
mysqli_close($con);
 
?>