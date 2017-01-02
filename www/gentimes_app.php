<?php 
	//Authenticate login
	require "checkCookies.php";
	
//if user is logged in
if($loggedin){
	//GENTIMES_APP.PHP - PHP version to download prayer timings and generate jamaat timings

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

	/******** Create an xml tree for year's the jamaat times */
	$year_year_treej = new DOMDocument('1.0', 'UTF-8');
	// create root element and append it to the tree
	$year_rootj = $year_treej->createElement("jamaat");		
	$year_rootj = $year_treej->appendChild($year_rootj);
	// append some info to the root node
	$year_rootj->appendChild($year_treej->createElement("city", "Kingston"));
	$year_rootj->appendChild($year_treej->createElement("country", "Usa"));
	$year_rootj->appendChild($year_treej->createElement("website", "www.masjidumar.com"));
	// create year element
	$yearElemj = $year_treej->createElement("year");
	$yearElemj = $year_rootj->appendChild($yearElemj);
	$yearElemj->setAttribute("value", $year);

	/********* Create an xml tree for year's the prayer times */
	$year_treep = new DOMDocument('1.0', 'UTF-8');
	// create root element and append it to the tree
	$year_rootp = $year_treep->createElement("prayer");		
	$year_rootp = $year_treep->appendChild($year_rootp);
	// append some info to the root node
	$year_rootp->appendChild($year_treep->createElement("city", "Kingston"));
	$year_rootp->appendChild($year_treep->createElement("country", "Usa"));
	$year_rootp->appendChild($year_treep->createElement("website", "www.masjidumar.com"));
	// create year element
	$yearElemp = $year_treep->createElement("year");
	$yearElemp = $year_rootp->appendChild($yearElemp);
	$yearElemp->setAttribute("value", $year);


	//for each month
	//parse the downloaded xml files and compute jamaat times
	//write this data to "prayer.htm", "jx.csv" and "px.csv" where x is month number
	for($i = 0; $i<12; $i++){
		
		//create an element for each month (jamaat)
		$monthElemj = $year_treej->createElement("month");
		$monthElemj->setAttribute("value", strval($i+1));
		$yearElemj->appendChild($monthElemj);

		//create an element for each month (prayer)
		$monthElemp = $year_treep->createElement("month");
		$monthElemp->setAttribute("value", strval($i+1));
		$yearElemp->appendChild($monthElemp);
		
		//parse the month's prayer time xml file 
		$year_treepm = simplexml_load_file($url.strval($i+1));// or die("Error: Cannot create object")		
		
		//for each day
		foreach($year_treepm->date as $day){
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
			
			/* Iqamah */	
			// add a date element to jammaat xml file for each one in the prayer xml file
			$year_year_datej = $year_treej->createElement("date");
			$year_year_datej = $monthElemj->appendChild($year_year_datej);
			$year_datej->setAttribute("day", $day['day']);
			$year_datej->setAttribute("month", $day['month']);
			$year_datej->setAttribute("year", $day['year']);
			$year_datej->setAttribute("week_day", $day['week_day']);

			// append each salah to date element
			$year_datej->appendChild($year_treej->createElement("fajr", $jFajr));
			$year_datej->appendChild($year_treej->createElement("sunrise", $day->sunrise));
			$year_datej->appendChild($year_treej->createElement("dhuhr", $jZuhr));
			$year_datej->appendChild($year_treej->createElement("asr", $jAsr));
			$year_datej->appendChild($year_treej->createElement("maghrib", $jMaghrib));
			$year_datej->appendChild($year_treej->createElement("isha", $jIsha));

			/* Prayer */
			// add a date element to jammaat xml file for each one in the prayer xml file
			$year_datep = $year_treep->createElement("date");
			$year_datep = $monthElemp->appendChild($year_datep);
			$year_datep->setAttribute("day", $day['day']);
			$year_datep->setAttribute("month", $day['month']);
			$year_datep->setAttribute("year", $day['year']);
			$year_datep->setAttribute("week_day", $day['week_day']);

			// append each salah to date element
			$year_datep->appendChild($year_treep->createElement("fajr", $pFajr));
			$year_datep->appendChild($year_treep->createElement("sunrise", $day->sunrise));
			$year_datep->appendChild($year_treep->createElement("dhuhr", $pZuhr));
			$year_datep->appendChild($year_treep->createElement("asr", $pAsr));
			$year_datep->appendChild($year_treep->createElement("maghrib", $pMaghrib));
			$year_datep->appendChild($year_treep->createElement("isha", $pIsha));

		}
	}	

	//save jamaat xml file
	$year_treej->save($spath.'iqamah_timings.xml');
	//save prayer xml file
	$year_treep->save($spath.'prayer_timings.xml');
	
	header("Location: index.php");
} 
else {
	header("Location: login.php");
	exit;
}
?>
