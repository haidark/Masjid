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

	//create array of month names
	$months = array('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December');

	// get current year
	$year = date("Y");
	// store url to xml files in variable $url
	$url = 'http://www.islamicfinder.org/prayer_service.php?country=usa&city=kingston&state=NY&zipcode=12401&latitude=41.9320&longitude=-74.0577&timezone=-5.0&HanfiShafi=1&pmethod=5&fajrTwilight1=&fajrTwilight2=&ishaTwilight=0&ishaInterval=0&dhuhrInterval=1&maghribInterval=1&dayLight=1&simpleFormat=xml&monthly=1&month=';

	/******** Create an xml tree for the jamaat times */
	$treej = new DOMDocument('1.0', 'UTF-8');
	// create root element and append it to the tree
	$rootj = $treej->createElement("jamaat");		
	$rootj = $treej->appendChild($rootj);
	// append some info to the root node
	$rootj->appendChild($treej->createElement("city", "Kingston"));
	$rootj->appendChild($treej->createElement("country", "Usa"));
	$rootj->appendChild($treej->createElement("website", "www.masjidumar.com"));
	// create year element
	$yearElemj = $treej->createElement("year");
	$yearElemj = $rootj->appendChild($yearElemj);
	$yearElemj->setAttribute("value", $year);

	/********* Create an xml tree for the prayer times */
	$treep = new DOMDocument('1.0', 'UTF-8');
	// create root element and append it to the tree
	$rootp = $treep->createElement("prayer");		
	$rootp = $treep->appendChild($rootp);
	// append some info to the root node
	$rootp->appendChild($treep->createElement("city", "Kingston"));
	$rootp->appendChild($treep->createElement("country", "Usa"));
	$rootp->appendChild($treep->createElement("website", "www.masjidumar.com"));
	// create year element
	$yearElemp = $treep->createElement("year");
	$yearElemp = $rootp->appendChild($yearElemp);
	$yearElemp->setAttribute("value", $year);


	//for each month
	//parse the downloaded xml files and compute jamaat times
	//write this data to "prayer.htm", "jx.csv" and "px.csv" where x is month number
	for($i = 0; $i<12; $i++){
		
		//create an element for each month (jamaat)
		$monthElemj = $treej->createElement("month");
		$monthElemj->setAttribute("value", strval($i+1));
		$yearElemj->appendChild($monthElemj);

		//create an element for each month (prayer)
		$monthElemp = $treep->createElement("month");
		$monthElemp->setAttribute("value", strval($i+1));
		$yearElemp->appendChild($monthElemp);
		
		//parse the month's prayer time xml file 
		$treepm = simplexml_load_file($url.strval($i+1));// or die("Error: Cannot create object")		
		
		//for each day
		foreach($treepm->date as $day){
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
			
			/* JAMAAT */	
			// add a date element to jammaat xml file for each one in the prayer xml file
			$datej = $treej->createElement("date");
			$datej = $monthElemj->appendChild($datej);
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

			/* Prayer */
			// add a date element to jammaat xml file for each one in the prayer xml file
			$datep = $treep->createElement("date");
			$datep = $monthElemp->appendChild($datep);
			$datep->setAttribute("day", $day['day']);
			$datep->setAttribute("month", $day['month']);
			$datep->setAttribute("year", $day['year']);
			$datep->setAttribute("week_day", $day['week_day']);

			// append each salah to date element
			$datep->appendChild($treep->createElement("fajr", $pFajr));
			$datep->appendChild($treep->createElement("sunrise", $day->sunrise));
			$datep->appendChild($treep->createElement("dhuhr", $pZuhr));
			$datep->appendChild($treep->createElement("asr", $pAsr));
			$datep->appendChild($treep->createElement("maghrib", $pMaghrib));
			$datep->appendChild($treep->createElement("isha", $pIsha));

		}
	}	

	//save jamaat xml file
	$treej->save($spath.'jamaat_timings.xml');
	//save prayer xml file
	$treep->save($spath.'prayer_timings.xml');
	
	header("Location: index.php");
} 
else {
	header("Location: login.php");
	exit;
}
?>
