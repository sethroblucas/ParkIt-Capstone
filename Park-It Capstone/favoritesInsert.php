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


$userID = 3;
$ID = $_POST["mainID"];
$address = $_POST["address"];
$description = $_POST["description"];
$date = $_POST["date"];
$cost = $_POST["totalCost"];


$sql = "INSERT INTO FavoriteSpots (userID, mainID, address, description, date, totalCost) VALUES('$userID', '$ID', '$address', '$description', '$date', '$cost')";


if ($con->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $con->error;
}


$con->close();
?>