<?php
//Define your host here.
$HostName = "db.soic.indiana.edu";
//Define your database username here.
$HostUser = "i494f19_team56";
//Define your database password here.
$HostPass = "my+sql=i494f19_team56";
//Define your database name here.
$DatabaseName = "i494f19_team56";
$con = mysqli_connect($HostName,$HostUser,$HostPass,$DatabaseName)or die('Unable to connect');


$ID = $_POST["mainID"];
$paypalID = $_POST["paypalID"];
$date = $_POST["date"];
$cost = $_POST["totalCost"];
$stat = $_POST["status"];
$sql = "INSERT INTO Transaction (mainID, paypalID, date, totalCost, status) VALUES('$ID', '$paypalID', '$date', '$cost', '$stat')";
if ($con->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $con->error;
}
$con->close();
?>