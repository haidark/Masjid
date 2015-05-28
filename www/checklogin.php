<?php

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

// username and password sent from form 
$myusername=$_POST['myusername']; 
$mypassword=$_POST['mypassword']; 

// To protect MySQL injection (more detail about MySQL injection)
$myusername = stripslashes($myusername);
$mypassword = stripslashes($mypassword);

$myusername = mysql_real_escape_string($myusername);
$mypassword = mysql_real_escape_string($mypassword);
$unencpass = $mypassword;
$mypassword= md5($mypassword);

$sql="SELECT * FROM $tbl_name WHERE name='$myusername' and password='$mypassword'";
$result=mysql_query($sql);
// Mysql_num_row is counting table row
$count=mysql_num_rows($result);

// If result matched $myusername and $mypassword, table row must be 1 row
if($count==1){
	// Register $myusername, $mypassword and redirect to file "index.php"
	setcookie('ID', $myusername);
	setcookie('Key', $unencpass);
	header("Location: index.php");
}
else {
	echo "Wrong Username or Password";
}
?>
