<?php
	//Authenticate login
	require "checkCookies.php";
?>

<html>
<head>
	<meta charset="UTF-8"><title>Monthly Timesheet - Masjid Umar</title>
	<link rel="stylesheet" href="css/style.css" type="text/css">
</head>
<body>
	<?php include "header.html"; ?>
	<div id="getMonth">
		<form action="monthTimes.php" method="post">
			<p>Select a month:</p>
			<select name="month">
				<option value="1">January</option>
				<option value="2">February</option>
				<option value="3">March</option>
				<option value="4">April</option>
				<option value="5">May</option>
				<option value="6">June</option>
				<option value="7">July</option>
				<option value="8">August</option>
				<option value="9">September</option>
				<option value="10">October</option>
				<option value="11">November</option>
				<option value="12">December</option>
			</select>
			<p><input type="submit" /></p>
		</form>
	</div>
	<?php include "footer.html"; ?>
</body>
</html>