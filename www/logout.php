<?php 
	$past = time() - 100;
	//this makes the time in the past to destroy the cookie
	setcookie('ID', gone, $past);
	setcookie('Key', gone, $past);
	header("Location: index.php");
?>


