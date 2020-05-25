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

if(mysqli_connect_error($con))
{
    echo "Failed to Connect to Database ".mysqli_connect_error();
}

/* check if server is alive */
if (mysqli_ping($con)) {
    printf ("Our connection is good!\n");
} else {
    printf ("Error: %s\n", mysqli_error($con));
}
mysqli_close($con);
?>