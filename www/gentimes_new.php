<?php 
	//Authenticate login
	require "checkCookies.php";
	
//if user is logged in
if($loggedin){
	//GENTIMES.PHP - PHP version to download prayer timings and generate jamaat timings

	//set timezone for date:
	date_default_timezone_set('America/New_York');

	//assign path variables: $rpath, $spath - website root path and path to temp storage
	$rpath = "/home/users/web/b1822/ipw.masjidumarcom/public_html/";
	$spath = $rpath."files/timings/";

	//paths for windows testing - comment out when deploying
	//$rpath = 'C:\Users\Haidar\Documents\Google Drive\Masjid Website\\';
	//$spath = $rpath.'files\timings\\';

	//create array of month names
	$months = array('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December');

	// get current year
	$year = date("Y");
	// store url to xml files in variable $url
	$url = 'http://www.islamicfinder.org/prayer_service.php?country=usa&city=kingston&state=NY&zipcode=12401&latitude=41.9320&longitude=-74.0577&timezone=-5.0&HanfiShafi=1&pmethod=5&fajrTwilight1=&fajrTwilight2=&ishaTwilight=0&ishaInterval=0&dhuhrInterval=1&maghribInterval=1&dayLight=1&simpleFormat=xml&monthly=1&month=';

	// open html document "prayer_new.htm" for writing
	// write header and title to html document
	$htmf = fopen($rpath.'prayer_new.htm', 'w');
	fwrite($htmf, "<!DOCTYPE html>\n<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n<link rel=\"shortcut icon\" href=\"favicon.ico\" />\n<title>Jamaat Timings - Masjid Al-Umar</title>\n<link href=\"css/prayerstyle.css\" rel=\"stylesheet\" type=\"text/css\">\n</head>\n<body>\n");
	fwrite($htmf, "<p id=\"title\">Masjid Umar Jamaat Timings - ".$year."</p>\n");

	/* Create an xml tree for the jamaat times */
	$treej = new DOMDocument('1.0', 'UTF-8');
	// create root element and append it to the tree
	$rootj = $treej->createElement("jamaat");		
	$rootj = $treej->appendChild($rootj);
	// append some info to the root node
	$rootj->appendChild($treej->createElement("city", "Kingston"));
	$rootj->appendChild($treej->createElement("country", "Usa"));
	$rootj->appendChild($treej->createElement("website", "www.masjidumar.com"));
	// create year element
	$yearElem = $treej->createElement("year");
	$yearElem = $rootj->appendChild($yearElem);
	$yearElem->setAttribute("value", $year);
	//for each month
	//parse the downloaded xml files and compute jamaat times
	//write this data to "prayer.htm", "jx.csv" and "px.csv" where x is month number
	for($i = 0; $i<12; $i++){
		//write the table headers for each month
		fwrite($htmf, "<table id=\"".$months[$i]."\">\n");
		fwrite($htmf, "<caption>".$months[$i]."</caption>\n");
		fwrite($htmf, "<tr>\n<th>".$months[$i]."</th>\n<th>Fajr</th>\n<th>Dhuhr</th>\n<th>Asr</th>\n<th>Maghrib</th>\n<th>Isha</th>\n</tr>\n");
		
		//create an element for each month
		$monthElem = $treej->createElement("month");
		$monthElem->setAttribute("value", strval($i+1));
		$yearElem->appendChild($monthElem);
		
		//parse the xml 
		$treep = simplexml_load_file($url.strval($i+1));// or die("Error: Cannot create object")		
		
		//initialize $rows to keep track of days written so far 
		//(need to fill in balance until 31 rows are made on html doc)
		$rows = 1;
		//for each day
		foreach($treep->date as $day){
			//get start time of each prayer in a day (as strings)
			$pFajr = $day->fajr;
			$pZuhr = $day->dhuhr;
			$pAsr = $day->asr;
			$pMaghrib = $day->maghrib;
			$pIsha = $day->isha;
			
			//process it (careful here) (results should all be strings)
			//add 10 minutes to fajr and maghrib
			$jFajr = date("g:i", strtotime('+10 minutes', strtotime($pFajr)));
			$jMaghrib = date("g:i", strtotime('+10 minutes', strtotime($pMaghrib)));
			//add 5 minutes to isha
			$jIsha = date("g:i", strtotime('+5 minutes', strtotime($pIsha)));		
			//set Asr times based on the ranges
			if (strtotime($pAsr) < strtotime('2:30'))
				$jAsr = '3:00';
			elseif (strtotime($pAsr) < strtotime('3:25'))
				$jAsr = '3:30';
			elseif (strtotime($pAsr) < strtotime('4:20'))
				$jAsr = '4:30';
			elseif (strtotime($pAsr) < strtotime('4:55'))
				$jAsr = '5:00';
			elseif (strtotime($pAsr) < strtotime('5:10'))
				$jAsr = '5:15';
			else
				$jAsr = '5:30';		
			//make sure to process ASR before ZUHR
			//set zuhr based on jamaat time for asr
			if($jAsr == '3:00')
				$jZuhr = '1:15';
			else
				$jZuhr = '1:30';
				
			//after all prayers are processed, write to prayer.htm
			//write to prayer.htm
			fwrite($htmf, "<tr>\n");
			fwrite($htmf, "\t<td>".strval($day['day'])."</td>\n");
			fwrite($htmf, "\t<td>".$jFajr."</td>\n");
			fwrite($htmf, "\t<td>".$jZuhr."</td>\n");
			fwrite($htmf, "\t<td>".$jAsr."</td>\n");
			fwrite($htmf, "\t<td>".$jMaghrib."</td>\n");
			fwrite($htmf, "\t<td>".$jIsha."</td>\n");
			fwrite($htmf, "</tr>\n");
			
			//write to xml files
			// add a date element to jammaat xml file for each one in the prayer xml file
			$datej = $treej->createElement("date");
			$datej = $monthElem->appendChild($datej);
			$datej->setAttribute("day", $day['day']);
			$datej->setAttribute("month", $day['month']);
			$datej->setAttribute("year", $day['year']);
			$datej->setAttribute("week_day", $day['week_day']);

			// append each salah to date element
			$datej->appendChild($treej->createElement("fajr", $jFajr));
			$datej->appendChild($treej->createElement("sunrise", $day->sunrise));
			$datej->appendChild($treej->createElement("dhuhr", $jZuhr));
			$datej->appendChild($treej->createElement("asr", $jAsr));
			$datej->appendChild($treej->createElement("maghrib", $jMaghrib));
			$datej->appendChild($treej->createElement("isha", $jIsha));

			$rows = $rows + 1;
		}
		// add rows until there are 31 rows in each table
		while($rows < 32){
			fwrite($htmf, "<tr>\n<td></td><td></td><td></td><td></td><td></td><td></td>\n</tr>\n");
			$rows = $rows + 1;
		}
		//end table for this month
		fwrite($htmf, "</table>\n");
		//save prayer xml file
		$treep->saveXML($spath.'p'.strval($i+1).".xml");
		
	}	

	fwrite($htmf, "</body>\n</html>");
	fclose($htmf);

	//save jamaat xml file
	$treej->save($spath.'jamaat_timings.xml');
	//end tags and close html file
	header("Location: prayer.htm");
} 
else {
	header("Location: login.php");
	exit;
}
?>
