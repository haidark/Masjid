<nav class="navbar navbar-inverse navbar-static-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="index.php">Masjid Umar</a>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <!-- <li><a href="NewsPage.php">News</a></li> -->
                <li><a href="About.php">About</a></li>
                <li><a href="PrayerTimes.php">Prayer Times</a></li>
                <!-- <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Prayer Times<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="MonthPrayer.php">Month</a></li>
                        <li><a href="YearPrayer.php">Year</a></li>
                    </ul>
                </li> -->
                <li><a href="Contact.php">Contact</a></li>
            </ul>
        </div>
    </div>
    <div align="center">
    <?php							
        //get prayer times data for today and display it
        $spath = "files/timings/";
        $month = date("n");
        $day = date("j");
        $daynum = intval($day)-1;
        //open the j{$month}.xml file
        $treej = simplexml_load_file($spath.'j'.$month.".xml") or die("Error: Cannot create object");						
    ?>
    <a href= "PrayerTimes.php" >Iqamah times for today... 
        Fajr: <?php echo $treej->date[$daynum]->fajr; ?>
        Dhuhr: <?php echo $treej->date[$daynum]->dhuhr; ?>
        Asr: <?php echo $treej->date[$daynum]->asr; ?> 
        Maghrib: <?php echo $treej->date[$daynum]->maghrib; ?>
        Isha: <?php echo $treej->date[$daynum]->isha; ?>
    </a>
    </div>
    <!-- <div class="onoffswitch3">
        <input type="checkbox" name="onoffswitch3" class="onoffswitch3-checkbox" id="myonoffswitch3" checked>
        <label class="onoffswitch3-label" for="myonoffswitch3">
                <span class="onoffswitch3-active">
                    <marquee class="scroll-text" >We hold classes year-round between Maghrib and Isha for all age groups, come with your family and friends to share in our learning, activities and celebrations!</marquee>
                        </span>
        </label>
    </div> -->
</nav>