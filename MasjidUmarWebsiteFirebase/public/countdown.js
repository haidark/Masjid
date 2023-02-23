//<script type="text/javascript" src="../PrayTimes.js"></script>

//import PrayTimes.js


function countdown(fajr,dhuhr,asr,maghrib,isha){
  
    //fajr='5:00'




    


    //var fajr=countdownf();

    //fajr,dhuhr,asr,maghrib,isha
    //fajr='15:34';
    var fajr_HR=parseInt(fajr.split(":")[0]);
    
   
    var fajr_MN=parseInt(fajr.split(":")[1]);

    var dhuhr_HR=parseInt(dhuhr.split(":")[0]);
    if(dhuhr_HR>1)
    {
        dhuhr_HR=dhuhr_HR+12;
    }
    var dhuhr_MN=parseInt(dhuhr.split(":")[1]);


    var asr_HR=parseInt(asr.split(":")[0]);
    if(asr_HR>=1)
    {
        asr_HR=asr_HR+12;
    }
    var asr_MN=parseInt(asr.split(":")[1]);

    var maghrib_HR=parseInt(maghrib.split(":")[0]);
    if(maghrib_HR>=1)
    {
        maghrib_HR=maghrib_HR+12;
    }
    var maghrib_MN=parseInt(maghrib.split(":")[1]);

    var isha_HR=parseInt(isha.split(":")[0]);
    if(isha_HR>=1)
    {
        isha_HR=isha_HR+12;
       
    }
    var isha_MN=parseInt(isha.split(":")[1]);


    

   // const timer = document.getElementById("timer");

    fajartime=new Date('2023-02-23T05:00:00')
    fajartime.getDate()
    fajartime.setHours(fajr_HR);
    fajartime.setMinutes(fajr_MN);
    fajartime.setSeconds(0);

   

     const dhurtime=new Date();
    dhurtime.setHours(dhuhr_HR);
    dhurtime.setMinutes(dhuhr_MN);
    dhurtime.setSeconds(0);

   const asrtime=new Date();
   asrtime.setHours(asr_HR);
   asrtime.setMinutes(asr_MN);
   asrtime.setSeconds(0);

    const maghribtime=new Date();
    maghribtime.setHours(maghrib_HR);
    maghribtime.setMinutes(maghrib_MN);
    maghribtime.setSeconds(0);

    const ishatime=new Date();
    ishatime.setHours(isha_HR);
    ishatime.setMinutes(isha_MN);
    ishatime.setSeconds(0);

    
   /* function calcDifference(hours1,minutes1,hours2,minutes2) {
  
   
        var first= (hours1 * 60 + minutes1) / 60;
        var second=(hours2 * 60 + minutes2) / 60;
     
      return second - first;
    }*/
   
    //calcDifference(hours1,minutes1,hours2,minutes2);*/

        
       const now = new Date();
       //now.setHours(21);
      // now.setMinutes(36);
       //now.setSeconds(00);

       const distance1 = fajartime - now; //prayertime-now
       const distance2 = dhurtime - now;
       const distance3 = asrtime - now;
       const distance4 = maghribtime - now;
       const distance5 = ishatime - now;
       var distance;

       
       if(distance1)
       {
           //var sub=24-now.getHours();
          //  fajr_HR=fajr_HR+sub;
          // fajartime.setHours(fajr_HR);
          // distance=(fajartime-now)*1;
       }
       else{
        fajartime.setHours(fajr_HR);
       }


       fajarhoursRemaining = Math.floor(distance1 / (1000 * 60 * 60));
       fajarminutesRemaining = Math.floor((distance1 / (1000 * 60)) % 60);
       fajarsecondsRemaining = Math.floor((distance1 / 1000) % 60);

      dhuhrRemaining = Math.floor(distance2 / (1000 * 60 * 60));
      dhuhrminutesRemaining = Math.floor((distance2 / (1000 * 60)) % 60);
      dhuhrsecondsRemaining = Math.floor((distance2 / 1000) % 60);

      
      asrhoursRemaining = Math.floor(distance3 / (1000 * 60 * 60));
      asrminutesRemaining = Math.floor((distance3 / (1000 * 60)) % 60);
      asrsecondsRemaining = Math.floor((distance3 / 1000) % 60);

      maghribhoursRemaining = Math.floor(distance4 / (1000 * 60 * 60));
      maghribminutesRemaining = Math.floor((distance4 / (1000 * 60)) % 60);
      maghribsecondsRemaining = Math.floor((distance4 / 1000) % 60);

      ishahoursRemaining = Math.floor(distance5 / (1000 * 60 * 60));
      ishaminutesRemaining = Math.floor((distance5 / (1000 * 60)) % 60);
      ishasecondsRemaining = Math.floor((distance5 / 1000) % 60);

      if((fajarhoursRemaining>0) && dhuhrsecondsRemaining<0 && asrsecondsRemaining<0 && maghribsecondsRemaining<0 && ishasecondsRemaining<0   ) 
      {
        distance=distance1;
        if(fajarhoursRemaining>14)
        {
          
            fajartime.setDate(  dhurtime.getDate());
            fajartime.getDate();
            fajartime.setHours(fajr_HR);
            fajartime.setMinutes(fajr_MN);
            fajartime.setSeconds(0);

            distance=(fajartime-now);
            
        }
        
      }
      else if(dhuhrsecondsRemaining>0 )
      {
        distance=distance2;
        
      }
      else if(asrsecondsRemaining>0)
      {
        distance=distance3;
      }

      else if(maghribsecondsRemaining>0)
      {
        distance=distance4;
      }
      else if((ishasecondsRemaining>0))
      {
        distance=distance5;
      }
     else if(ishasecondsRemaining<0)
      {
        fajartime=new Date('2023-02-23T05:00:00')
        fajrtime.setDate(dhurtime.getDate);
        fajartime.getDate();
        fajartime.setHours(fajr_HR);
        fajartime.setMinutes(fajr_MN);
        fajartime.setSeconds(0);
        distance=distance5;
      }

      else{
       
        var sub=24-now.getHours();
        fajr_HR=fajr_HR+sub;
        fajartime.setHours(fajr_HR);
        distance=(fajartime-now)*1;
      }

    
     

      //distance=distance4;

     hoursRemaining = Math.floor(distance / (1000 * 60 * 60));
      minutesRemaining = Math.floor((distance / (1000 * 60)) % 60);
      secondsRemaining = Math.floor((distance / 1000) % 60);



      hoursRemaining = (hoursRemaining < 10) ? "" +hoursRemaining : hoursRemaining;
      minutesRemaining = (minutesRemaining < 10) ? "0" + minutesRemaining : minutesRemaining;
      secondsRemaining = (secondsRemaining < 10) ? "0" + secondsRemaining : secondsRemaining;

      var timedown = hoursRemaining + ":" + minutesRemaining + ":" + secondsRemaining;
      document.getElementById("myLink").innerText = timedown;
      document.getElementById("myLink").textContent = timedown;
    
    
  
      //Timeout( countdown(), 1000); // Update the timer every second
    setTimeout(function(){
        countdown(fajr,dhuhr,asr,maghrib,isha);
    },1000)    // Just figuring out this part drove me crazy 
    
    //secondsRemaining; // Return the updateTimer function
    
    

}

countdown(fajr,dhuhr,asr,maghrib,isha);