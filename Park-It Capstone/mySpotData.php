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



$sql = "SELECT street, city, state, zip, description, hourlyCost FROM Spot WHERE userID = '3'";


$result = $con->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        echo "<br> Street: " . $row["street"] . "<br> City: " . $row["city"] . "<br> State: " . $row["state"] . "<br> Zip: " . $row["zip"] . "<br> Description: " .$row["description"] . "<br> Cost: ". $row["hourlyCost"] . "<br>";
    }
} else {
    echo "0 results";
}
$con->close();
?>