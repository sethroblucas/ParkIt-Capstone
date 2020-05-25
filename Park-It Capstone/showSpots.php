<?php
#require "conn.php";
//Define your host here.
$HostName = "db.soic.indiana.edu";
//Define your database username here.
$HostUser = "i494f19_team56";
//Define your database password here.
$HostPass = "my+sql=i494f19_team56";
//Define your database name here.
$DatabaseName = "i494f19_team56";
$con = mysqli_connect($HostName,$HostUser,$HostPass,$DatabaseName)or die('Unable to connect');


$sql = "SELECT latitude, longitude FROM Spot";

$result = $con->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        echo $row["latitude"] . " " . $row["longitude"] . " ";
    }
} else {
    echo "0 results";
}
$con->close();
?>