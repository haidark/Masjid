<?php
	//Authenticate login
	require "checkCookies.php";

	if(!$loggedin){
		header("Location: login.php");
	}
?>

<html>
<head>
	<meta charset="UTF-8"><title>Set Jummah - Masjid Umar</title>
	<link rel="stylesheet" href="css/style.css" type="text/css">
</head>
<body>
	<?php include "header.html"; ?>
	<div id="setjummah">
		<form action="jummahTime.php" method="post">
			<p>Enter new Jummah time. (Format: h:mm)</p>
			<textarea rows="1" cols="40" name="time"></textarea>
			<p><input type="submit" /></p>
		</form>
	</div>
	<?php include "footer.html"; ?>
</body>
</html>