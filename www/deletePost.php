<?php
	//Authenticate login
	require "checkCookies.php";
	if($loggedin){
		unlink($_GET['filename']);
		header("Location: news.php");
	} else{
		header("Location: login.php");
	}
?>