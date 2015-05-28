<?php
	//Authenticate login
	require "checkCookies.php";
	//THIS CODE AUTHENTICATES LOGIN!
	if(!$loggedin){
		header("Location: login.php");
	}
?>

<html>
<head>
	<meta charset="UTF-8"><title>New Post - Masjid Umar</title>
	<link rel="stylesheet" href="css/style.css" type="text/css">
</head>
<body>
	<?php include "header.html"; ?>
	<div id="newpost">
		<form action="updatePost.php" method="post">
			<p>Title of Post</p>
			<textarea rows="1" cols="40" name="title"></textarea>
			<p>Content of Post (HTML preferred)</p>
			<textarea rows="20" cols="40" name="content"></textarea>
			<p><input type="submit" /></p>
		</form>
	</div>
	<?php include "footer.html"; ?>
</body>
</html>