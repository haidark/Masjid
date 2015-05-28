<?php
	//Authenticate login
	require "checkCookies.php";

	//set timeszone for date() calls
	date_default_timezone_set('America/New_York');
	if(isset($_POST['month'])){
		$month=$_POST['month'];
	} else {
		$month = date("n");
	}
	$MONTHS = array('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December');
	$monthName = $MONTHS[intval($month)-1];
	$rpath = "/home/users/web/b1822/ipw.masjidumarcom/public_html/";
	$spath = $rpath."files/timings/";
?>

<!DOCTYPE HTML>
<html>
<head>
	<meta charset="UTF-8"><title>Timings - <?php echo $monthName;?></title>
	<link rel="stylesheet" href="css/style.css" type="text/css">
</head>
<body>
	<?php include "header.html"; ?>
	<div>
		<table id="prayertimestable">
			<caption><?php echo $monthName;?> Timesheet</caption>
			<tr>
				<th></th>
				<th colspan="2">Fajr</th>
				<th>Sunrise</th>
				<th colspan="2">Zuhr</th>
				<th colspan="2">Asr</th>
				<th colspan="2">Maghrib</th>
				<th colspan="2">Isha</th>
			</tr>
			<tr>
				<th>Date</th>
				<td>Adhaan</td>
				<td>Iqaamah</td>
				<td></td>
				<td>Adhaan</td>
				<td>Iqaamah</td>
				<td>Adhaan</td>
				<td>Iqaamah</td>
				<td>Adhaan</td>
				<td>Iqaamah</td>
				<td>Adhaan</td>
				<td>Iqaamah</td>
			</tr>
			<?php							
				//open the p{$month}.xml file
				$treep = simplexml_load_file($spath.'p'.$month.".xml") or die("Error: Cannot create object");
				//open the j{$month}.xml file
				$treej = simplexml_load_file($spath.'j'.$month.".xml") or die("Error: Cannot create object");
				$day = 0;
				while(!empty($treep->date[$day]) and !empty($treej->date[$day])){
					$prayer = $treep->date[$day];
					$jamaat = $treej->date[$day];
					echo "<tr>";
					echo "<td>".$prayer['day']."</td>";
					echo "<td>".$prayer->fajr."</td>";
					echo "<td>".$jamaat->fajr."</td>";
					echo "<td>".$prayer->sunrise."</td>";
					echo "<td>".$prayer->dhuhr."</td>";
					echo "<td>".$jamaat->dhuhr."</td>";
					echo "<td>".$prayer->asr."</td>";
					echo "<td>".$jamaat->asr."</td>";
					echo "<td>".$prayer->maghrib."</td>";
					echo "<td>".$jamaat->maghrib."</td>";
					echo "<td>".$prayer->isha."</td>";
					echo "<td>".$jamaat->isha."</td>";
					echo "</tr>";
					$day = $day + 1;
				}
			?>
		</table>
	</div>
	<?php include "footer.html"; ?>
</body>
</html>