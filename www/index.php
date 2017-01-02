<?php
	//Authenticate login
	require "checkCookies.php";

	//set timeszone for date() calls
	date_default_timezone_set('America/New_York');
	$rpath = "/home/users/web/b1822/ipw.masjidumarcom/public_html/";
	$spath = $rpath."files/timings/";
?>

<!DOCTYPE HTML>
<html>
<head>
	<meta charset="UTF-8"><title>Homepage - Masjid Umar</title>
	<link rel="stylesheet" href="css/style.css" type="text/css">
</head>

<body>
	<?php include "header.html"; ?>
	<div id="wrapper">
	<div id="jummah">
		<h2>Jummah, congregational prayers every Friday, begins at <?php include "files/jummah.txt"; ?>.</h2>
	</div>
	<div id="timingstables">
		<div id="prayertimes">
			<table id="prayertimestable">
				<caption>Prayer Start Times</caption>
					<tr>
						<th>Day</th>
						<th>Fajr</th>
						<th>Sunrise</th>
						<th>Dhuhr</th>
						<th>Asr</th>
						<th>Maghrib</th>
						<th>Isha</th>
					</tr>
					<tr>						
						<td>Today</td>
						<?php							
							//get prayer times data for today and display it
							$month = date("n");
							$day = date("j");
							$daynum = intval($day)-1;
							//open the p{$month}.xml file
							$treep = simplexml_load_file($spath.'p'.$month.".xml") or die("Error: Cannot create object");
							echo "<td>".$treep->date[$daynum]->fajr."</td>";
							echo "<td>".$treep->date[$daynum]->sunrise."</td>";
							echo "<td>".$treep->date[$daynum]->dhuhr."</td>";
							echo "<td>".$treep->date[$daynum]->asr."</td>";
							echo "<td>".$treep->date[$daynum]->maghrib."</td>";
							echo "<td>".$treep->date[$daynum]->isha."</td>";							
						?>
					</tr>
					<tr>
						<td>
							<?php $tomm = new DateTime('tomorrow'); ?>
							Tommorow
						</td>
						<?php	
							$month = $tomm->format("n");
							$day = $tomm->format("j");
							$daynum = intval($day)-1;
							//open the p{$month}.xml file
							$treep = simplexml_load_file($spath.'p'.$month.".xml") or die("Error: Cannot create object");
							echo "<td>".$treep->date[$daynum]->fajr."</td>";
							echo "<td>".$treep->date[$daynum]->sunrise."</td>";
							echo "<td>".$treep->date[$daynum]->dhuhr."</td>";
							echo "<td>".$treep->date[$daynum]->asr."</td>";
							echo "<td>".$treep->date[$daynum]->maghrib."</td>";
							echo "<td>".$treep->date[$daynum]->isha."</td>";	
						?>						
					</tr>
				</table>
		</div>

		<div id="jamaattimes">
			<table id="jamaattimestable">
				<caption>Iqamah Times</caption>
					<tr>
						<th>Day</th>
						<th>Fajr</th>
						<th>Sunrise</th>
						<th>Dhuhr</th>
						<th>Asr</th>
						<th>Maghrib</th>
						<th>Isha</th>
					</tr>
					<tr>
						<td>Today</td>
						<?php							
							//get jamaat times data and display it
							$month = date("n");
							$day = date("j");
							$daynum = intval($day)-1;
							//open the j{$month}.xml file
							$treej = simplexml_load_file($spath.'j'.$month.".xml") or die("Error: Cannot create object");
							echo "<td>".$treej->date[$daynum]->fajr."</td>";
							echo "<td>".$treej->date[$daynum]->sunrise."</td>";
							echo "<td>".$treej->date[$daynum]->dhuhr."</td>";
							echo "<td>".$treej->date[$daynum]->asr."</td>";
							echo "<td>".$treej->date[$daynum]->maghrib."</td>";
							echo "<td>".$treej->date[$daynum]->isha."</td>";								
						?>
					</tr>
					<tr>
						<td>
							<?php $tomm = new DateTime('tomorrow'); ?>
							Tommorow
						</td>
						<?php	
							$month = $tomm->format("n");
							$day = $tomm->format("j");
							$daynum = intval($day)-1;
							//open the j{$month}.xml file
							$treej = simplexml_load_file($spath.'j'.$month.".xml") or die("Error: Cannot create object");
							echo "<td>".$treej->date[$daynum]->fajr."</td>";
							echo "<td>".$treej->date[$daynum]->sunrise."</td>";
							echo "<td>".$treej->date[$daynum]->dhuhr."</td>";
							echo "<td>".$treej->date[$daynum]->asr."</td>";
							echo "<td>".$treej->date[$daynum]->maghrib."</td>";
							echo "<td>".$treej->date[$daynum]->isha."</td>";	
						?>						
					</tr>
				</table>
		</div>
	</div>
	<div id="news">
		<table id="newstable">
			<caption><a href="news.php">Latest News</a></caption>
				<?php
				//select latest post and display it
				$files = glob("files/posts/*.htm");
				$files = array_combine($files, array_map("filemtime", $files));
				arsort($files);
				$latestPost = key($files);
				//get date from the post						
				$handle = fopen($latestPost, 'r');
				if($handle){
					$date = fgets($handle);
					$date = explode("-", $date);
					$month = $date[0];
					$day = $date[1];
					$year = $date[2];
					$title = fgets($handle);
					$content = fgets($handle);
					fclose($handle);
					echo "<tr><td><h3 id=\"title\">$title</h3></td></tr>";
					echo "<tr><td><h4 id=\"time\">$month$day$year</h4></td></tr>";					
					echo "<tr><td><p id=\"text\">$content</p></td></tr>";
				}
				?>
		</table>				
	</div>
	</div>
	<?php include "footer.html"; ?>
</body>
</html>