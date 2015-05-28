<?php
	//Authenticate login
	require "checkCookies.php";
	if(!$loggedin){
		header("Location: login.php");
	}else{
		$file = fopen("files/jummah.txt", 'w');
		fwrite($file, $_POST['time']);
		fclose($file);
		
		header("Location: index.php");
	}
?>