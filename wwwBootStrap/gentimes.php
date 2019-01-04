<?php 
	//GENTIMES.PHP - PHP version to download prayer timings and generate iqamah timings

	// import the PrayTime class
	include('PrayTime.php');

	//set timezone for date:
	date_default_timezone_set('America/New_York');

	//assign path variables: $rpath, $spath - website root path and path to temp storage
	$rpath = "./"; //"/home/users/web/b1822/ipw.masjidumarcom/public_html/";
	$spath = $rpath."files/timings/";

	//paths for windows testing - comment out when deploying
	//$rpath = 'C:\Users\Haidar\Documents\Google Drive\Masjid Website\\';
	//$spath = $rpath.'files\timings\\';

	//create array of month names
	$months = array('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December');

	// set location and time zone
	$latitude = 41.9270;
	$longitude = -73.9974;
	$timeZone = -5.0;
	//create Prayer time calculator object with ISNA method
	$prayTime = new PrayTime();
	$prayTime->setCalcMethod($prayTime->ISNA);
	//set the output time format
	$prayTime->setTimeFormat($prayTime->Time12NS);
	
	// get year as argument or use current year
	if($_GET["year"] != null){
		$year = $_GET["year"];
	} else {
		$year = date("Y");
	}

	/******** Create an xml tree for the year's iqamah times */
	$year_treej = new DOMDocument('1.0', 'UTF-8');
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

	/********* Create an xml tree for the year's prayer times */
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

	// open html document "prayer.htm" for writing
	// write header and title to html document
	$htmf = fopen($rpath.'prayer.htm', 'w');
	fwrite($htmf, "<!DOCTYPE html>\n<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n<link rel=\"shortcut icon\" href=\"favicon.ico\" />\n<title>Iqamah Timings - Masjid Al-Umar</title>\n<link href=\"css/prayerstyle.css\" rel=\"stylesheet\" type=\"text/css\">\n</head>\n<body>\n");
	fwrite($htmf, "<p id=\"title\">Masjid Umar Iqamah Timings - ".$year."</p>\n");

	//for each month
	//compute prayer and iqamah times
	//write this data to "jx.xml" and "px.xml" where x is month number
	// also make an xml file for each year
	for($i = 0; $i<12; $i++){
		
		//write the table headers for each month
		fwrite($htmf, "<table id=\"".$months[$i]."\">\n");
		fwrite($htmf, "<caption>".$months[$i]."</caption>\n");
		fwrite($htmf, "<tr>\n<th>".$months[$i]."</th>\n<th>Fajr</th>\n<th>Dhuhr</th>\n<th>Asr</th>\n<th>Maghrib</th>\n<th>Isha</th>\n</tr>\n");
		
		//create an element for each month (jamaat)
		$monthElemj = $year_treej->createElement("month");
		$monthElemj->setAttribute("value", strval($i+1));
		$yearElemj->appendChild($monthElemj);

		//create an element for each month (prayer)
		$monthElemp = $year_treep->createElement("month");
		$monthElemp->setAttribute("value", strval($i+1));
		$yearElemp->appendChild($monthElemp);

		/* Create an xml file for the iqamah times */
		$treej = new DOMDocument('1.0', 'UTF-8');
		// create root element and append it to the tree
		$rootj = $treej->createElement("iqamah");		
		$rootj = $treej->appendChild($rootj);
		$rootj->setAttribute("month", $months[$i]);
		// append some info to the root node
		$rootj->appendChild($treej->createElement("city", "Kingston"));
		$rootj->appendChild($treej->createElement("country", "Usa"));
		$rootj->appendChild($treej->createElement("website", "www.masjidumar.com"));

		/* Create an xml file for the prayer times */
		$treep = new DOMDocument('1.0', 'UTF-8');
		// create root element and append it to the tree
		$rootp = $treep->createElement("prayer");		
		$rootp = $treep->appendChild($rootp);
		$rootp->setAttribute("month", $months[$i]);
		// append some info to the root node
		$rootp->appendChild($treep->createElement("city", "Kingston"));
		$rootp->appendChild($treep->createElement("country", "Usa"));
		$rootp->appendChild($treep->createElement("website", "www.masjidumar.com"));

		//initialize $rows to keep track of days written so far 
		//(need to fill in balance until 31 rows are made on html doc)
		$rows = 1;

		//for each day in the month
		$month=$i+1;
		$offset_time=' 03:00:00';
		$day = strtotime($year.'-'.$month.'-1'.$offset_time);
		if($month==12)
			$endDay = strtotime(($year+1).'-1-1'.$offset_time);
		else
			$endDay = strtotime($year.'-'.($month+1).'-1'.$offset_time);

		while($day < $endDay){
			//get start time of each prayer in a day
			$dst = date('I', $day);
			$times = $prayTime->getPrayerTimes($day, $latitude, $longitude, $timeZone, $dst);
			$pFajr = $times[0];
			$pSunrise = $times[1];
			$pZuhr = $times[2];			
			$pAsr = $times[3];
			$pSunset = $times[4];
			$pMaghrib = $times[5];
			$pIsha = $times[6];
			
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
			elseif (strtotime($pAsr) < strtotime('4:50'))
				$jAsr = '5:00';
			elseif (strtotime($pAsr) < strtotime('5:10'))
				$jAsr = '5:15';
			else
				$jAsr = '5:30';		
			//make sure to process ASR before ZUHR
			//set zuhr based on iqamah time for asr
			if($jAsr == '3:00')
				$jZuhr = '12:45';
			elseif($jAsr == '3:30')
				$jZuhr = '1:00';
			else
				$jZuhr = '1:30';
				
			//after all prayers are processed, write to prayer.htm
			//write to prayer.htm
			fwrite($htmf, "<tr>\n");
			fwrite($htmf, "\t<td>".strval(date('j', $day))."</td>\n");
			fwrite($htmf, "\t<td>".$jFajr."</td>\n");
			fwrite($htmf, "\t<td>".$jZuhr."</td>\n");
			fwrite($htmf, "\t<td>".$jAsr."</td>\n");
			fwrite($htmf, "\t<td>".$jMaghrib."</td>\n");
			fwrite($htmf, "\t<td>".$jIsha."</td>\n");
			fwrite($htmf, "</tr>\n");
			
			//write to xml files

			/* Year Iqamah */	
			// add a date element to jammaat xml file for each one in the prayer xml file
			$year_datej = $year_treej->createElement("date");
			$year_datej = $monthElemj->appendChild($year_datej);
			$year_datej->setAttribute("day",date('j', $day));
			$year_datej->setAttribute("month", date('F', $day));
			$year_datej->setAttribute("year", date('Y', $day));
			$year_datej->setAttribute("week_day", date('l', $day));
			// append each salah to date element
			$year_datej->appendChild($year_treej->createElement("fajr", $jFajr));
			$year_datej->appendChild($year_treej->createElement("sunrise", $pSunrise));
			$year_datej->appendChild($year_treej->createElement("dhuhr", $jZuhr));
			$year_datej->appendChild($year_treej->createElement("asr", $jAsr));
			$year_datej->appendChild($year_treej->createElement("sunset", $pSunset));
			$year_datej->appendChild($year_treej->createElement("maghrib", $jMaghrib));
			$year_datej->appendChild($year_treej->createElement("isha", $jIsha));

			/* Year Prayer */
			// add a date element to jammaat xml file for each one in the prayer xml file
			$year_datep = $year_treep->createElement("date");
			$year_datep = $monthElemp->appendChild($year_datep);
			$year_datep->setAttribute("day",date('j', $day));
			$year_datep->setAttribute("month", date('F', $day));
			$year_datep->setAttribute("year", date('Y', $day));
			$year_datep->setAttribute("week_day", date('l', $day));
			// append each salah to date element
			$year_datep->appendChild($year_treep->createElement("fajr", $pFajr));
			$year_datep->appendChild($year_treep->createElement("sunrise", $pSunrise));
			$year_datep->appendChild($year_treep->createElement("dhuhr", $pZuhr));
			$year_datep->appendChild($year_treep->createElement("asr", $pAsr));
			$year_datep->appendChild($year_treep->createElement("sunset", $pSunset));
			$year_datep->appendChild($year_treep->createElement("maghrib", $pMaghrib));
			$year_datep->appendChild($year_treep->createElement("isha", $pIsha));

			/* Month Iqamah */	
			// add a date element to iqamah xml file
			$datej = $treej->createElement("date");
			$datej = $rootj->appendChild($datej);
			$datej->setAttribute("day", date('j', $day));
			$datej->setAttribute("month", date('F', $day));
			$datej->setAttribute("year", date('Y', $day));
			$datej->setAttribute("week_day", date('l', $day));
			// append each salah to date element
			$datej->appendChild($treej->createElement("fajr", $jFajr));
			$datej->appendChild($treej->createElement("sunrise", $pSunrise));
			$datej->appendChild($treej->createElement("dhuhr", $jZuhr));
			$datej->appendChild($treej->createElement("asr", $jAsr));
			$datej->appendChild($treej->createElement("sunset", $pSunset));
			$datej->appendChild($treej->createElement("maghrib", $jMaghrib));
			$datej->appendChild($treej->createElement("isha", $jIsha));

			/* Month Prayer */	
			// add a date element to prayer xml file
			$datep = $treep->createElement("date");
			$datep = $rootp->appendChild($datep);
			$datep->setAttribute("day", date('j', $day));
			$datep->setAttribute("month", date('F', $day));
			$datep->setAttribute("year", date('Y', $day));
			$datep->setAttribute("week_day", date('l', $day));
			// append each salah to date element
			$datep->appendChild($treep->createElement("fajr", $pFajr));
			$datep->appendChild($treep->createElement("sunrise", $pSunrise));
			$datep->appendChild($treep->createElement("dhuhr", $pZuhr));
			$datep->appendChild($treep->createElement("asr", $pAsr));
			$datep->appendChild($treep->createElement("sunset", $pSunset));
			$datep->appendChild($treep->createElement("maghrib", $pMaghrib));
			$datep->appendChild($treep->createElement("isha", $pIsha));

			$day += 24* 60* 60;  // next day
			$rows = $rows + 1;
		}
		// add rows until there are 31 rows in each table
		while($rows < 32){
			fwrite($htmf, "<tr>\n<td></td><td></td><td></td><td></td><td></td><td></td>\n</tr>\n");
			$rows = $rows + 1;
		}
		//end table for this month
		fwrite($htmf, "</table>\n");		
		//save iqamah xml file
		$treej->save($spath.'j'.strval($i+1).".xml");
		//save prayer xml file
		$treep->save($spath.'p'.strval($i+1).".xml");
	}
	fwrite($htmf, "</body>\n</html>");
	fclose($htmf);

	//save jamaat xml file
	$year_treej->save($spath.'iqamah_timings.xml');
	//save prayer xml file
	$year_treep->save($spath.'prayer_timings.xml');

	//end tags and close html file
	header("Location: prayer.htm");
?>
