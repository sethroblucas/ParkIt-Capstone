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


$spotID = $_POST["spotID"];
$ID = $_POST["mainID"];
$street = $_POST["street"];  
$city = $_POST["city"];
$state = $_POST["state"];
$zip = $_POST["zip"];
$description = $_POST["description"];
$lat = $_POST["latitude"];
$lng = $_POST["longitude"];
$hourlyCost = $_POST["hourlyCost"];
$timeStart = $_POST["timeStart"];
$timeEnd = $_POST["timeEnd"];
$totalCost = $_POST["totalCost"];



$sql = "INSERT INTO ParkingHistory (spotID, mainID, street, city, state, zip, description, latitude, longitude, hourlyCost, timeStart, timeEnd, totalCost) VALUES('$spotID', '$ID', '$street', '$city', '$state', '$zip', '$description', '$lat', '$lng', '$hourlyCost', '$timeStart', '$timeEnd', '$totalCost')";


if ($con->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $con->error;
}
$con->close();
?>