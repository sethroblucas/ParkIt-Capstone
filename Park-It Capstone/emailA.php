<?php

define('HOST','db.soic.indiana.edu');
define('USER','i494f19_team56');
define('PASS','my+sql=i494f19_team56');
define('DB','i494f19_team56');
 
$con = mysqli_connect(HOST,USER,PASS,DB);

$Name=$_POST['userName'];
$Email=$_POST['email'];
$ID=$_POST['userID'];

$sql = "select * from Users where email = '$Email'";

$emess = <<<'EOD'
User,

Your account information has just been updated through the application.

If this is not you, please contact support ASAP.

Thank you!

Park-it - 'Where commuting meets convenience.'
EOD;

if ($con) {
	// the message
	$msg = $emess;

	// use wordwrap() if lines are longer than 70 characters
	$msg = wordwrap($msg,90);

	// send email
	mail($Email,"Park-It - Account information",$msg);

    echo "Email Notification Sent!";
} else {
    echo "Error";
}

$con->close();
?>

