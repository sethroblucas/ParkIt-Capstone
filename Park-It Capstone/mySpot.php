<?php

require "dbconfig.php";

$userID = 3;
$ID = $_POST["mainID"];
$street = $_POST["street"];  
$city = $_POST["city"];
$state = $_POST["state"];
$zip = $_POST["zip"];
$description = $_POST["description"];
$lat = $_POST["latitude"];
$lng = $_POST["longitude"];
$cost = $_POST["hourlyCost"];

$sql = "INSERT INTO Spot (userID, mainID, street, city, state, zip, description, latitude, longitude, hourlyCost) VALUES('$userID', '$ID', '$street', '$city', '$state', '$zip', '$description', '$lat', '$lng', '$cost')";

if ($con->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $con->error;
}

$con->close();
?>