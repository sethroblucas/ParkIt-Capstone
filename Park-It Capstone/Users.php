<?php

require "dbconfig.php";


$Name=$_POST['userName'];
$Email=$_POST['email'];
$ID=$_POST['userID'];


$sql = "select * from Users where email = '$Email'";

$result = mysqli_query($con,$sql);

$emess = <<<'EOD'
Welcome to Park-it!

Park-it is a revolutionary application that strives to dramatically
improve your parking experience!

For more information on how to use Park-it, please visit the 'About' page in the application.

Thanks for joining our community!

Park-it - 'Where commuting meets convenience.'
EOD;

if(mysqli_num_rows($result)>0)
{
$row = mysqli_fetch_assoc($result);
$name = $row["userName"];
$status = "Welcome Back ".$name."!";
}

else
{
$sql = "INSERT INTO Users(userID,userName,email) VALUES('$ID','$Name','$Email');";

	if(mysqli_query($con,$sql))
	{
		// the message
		$msg = $emess;

		// use wordwrap() if lines are longer than 70 characters
		$msg = wordwrap($msg,70);

		// send email
		mail($Email,"Welcome to Park-it!",$msg);
		$status = "Account Created!" ; 
	}
	else
	{
		$status = "Error";
	}
}

echo $status;

mysqli_close($con);

?>