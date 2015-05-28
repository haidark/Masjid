<?php
	//THIS CODE AUTHENTICATES LOGIN!
if(isset($_COOKIE['ID']) && isset($_COOKIE['Key'])){
	//Get database information from config file
	$conf = fopen("files/sqlconf.cfg", 'r');
	if ($conf){
		$host=trim(fgets($conf)); // Host name 
		$username= trim(fgets($conf)); // Mysql username 
		$password=trim(fgets($conf)); // Mysql password 
		$db_name=trim(fgets($conf)); // Database name 
		$tbl_name=trim(fgets($conf)); // Table name
	} else{
		echo("cannot read sql configuration file!");
	}
	// Connect to server and select database.
	mysql_connect("$host", "$username", "$password")or die("cannot connect"); 
	mysql_select_db("$db_name")or die("cannot select DB");
	
	// get username and password from cookies
	$myusername=$_COOKIE['ID']; 
	$mypassword=md5($_COOKIE['Key']);
	//check username and pass
	$sql="SELECT * FROM $tbl_name WHERE name='$myusername' and password='$mypassword'";
	$result=mysql_query($sql);
	// Mysql_num_row is counting table row
	$count=mysql_num_rows($result);
	
	if($count != 1){
		header("Location: logout.php");
	} else{
		$loggedin = 1;
	}
	
} else {
	$loggedin = 0;
}
?>