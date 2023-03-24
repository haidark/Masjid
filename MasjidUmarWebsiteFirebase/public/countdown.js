sunset=0;


function countdown(fajr,dhuhr,asr,maghrib,isha,ndfajar){
  



    

    var ndfajar_HR=parseInt(ndfajar.split(":")[0]);
    
   
    var ndfajar_MN=parseInt(ndfajar.split(":")[1]);

    
    


    
    var fajr_HR=parseInt(fajr.split(":")[0]);
    
   
    var fajr_MN=parseInt(fajr.split(":")[1]);

    var dhuhr_HR=parseInt(dhuhr.split(":")[0]);
    if(dhuhr_HR>=1)
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


    

    fajartime=new Date();//('2023-02-23T05:00:00')
    var current_fajartime=fajartime.getDate();
   


   

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

    


        
       const now = new Date();
      if((dhurtime - now)>0 )
      {
        fajartime.setDate(dhurtime.getDate());
        fajartime.setHours(fajr_HR);
        fajartime.setMinutes(fajr_MN);
        fajartime.setSeconds(0);
      }
      else{
        fajartime.setDate(current_fajartime+1);
        fajartime.setHours(ndfajar_HR);
        fajartime.setMinutes(ndfajar_MN);
        fajartime.setSeconds(0);
      }

       

       var distance1 = fajartime - now;
       var distance2 = dhurtime - now;
       var distance3 = asrtime - now;
       var distance4 = maghribtime - now;
       var distance5 = ishatime - now;
       var distance;

     



       
        if(now.getHours()<13)
        {
          var distfd1=12-now.getHours();
        }
        


       fajarhoursRemaining = Math.floor(distance1 / (1000 * 60 * 60));
       fajarminutesRemaining = Math.floor((distance1 / (1000 * 60)) % 60);
       fajarsecondsRemaining = Math.floor((distance1 / 1000) % 60);

      dhuhrhoursRemaining = Math.floor(distance2 / (1000 * 60 * 60));
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

         
 
 
      if(fajarhoursRemaining<dhuhrhoursRemaining || distance1>0)
      {
        distance=distance1;
      }
      if(fajarhoursRemaining<-1 || fajarminutesRemaining<-14 )
      {
        fajarhoursRemaining=500;
        fajarminutesRemaining=500;
        fajarsecondsRemaining=500;
      }
      
      


      if(dhuhrhoursRemaining<fajarhoursRemaining ) 
      {
       distance=distance2;
        
      }
      if(dhuhrhoursRemaining<-1 || dhuhrminutesRemaining<-14 )// || dhuhrsecondsRemaining<-1
      {
        dhuhrsecondsRemaining=500;
        dhuhrhoursRemaining=500;
        dhuhrsecondsRemaining=500;
      }

      if(asrhoursRemaining<dhuhrhoursRemaining && (dhuhrhoursRemaining>=-1 || dhuhrminutesRemaining>=-14 || dhuhrsecondsRemaining>=-1) ) 
      {

        distance=distance3;
       
      }
      if(asrhoursRemaining<-1 || asrminutesRemaining<-14)
      {
        asrhoursRemaining=500;
        asrminutesRemaining=500;
        asrsecondsRemaining=500;
      }
      if(maghribhoursRemaining<asrhoursRemaining && (asrhoursRemaining>=-1 || asrminutesRemaining>=-14 || asrsecondsRemaining>=-1) ) 
      {
        distance=distance4;
       
      }
      if(maghribhoursRemaining<-1 || maghribminutesRemaining<-14)
      {
        maghribhoursRemaining=500;
        maghribminutesRemaining=500;
        maghribsecondsRemaining=500;
      }
      if(ishahoursRemaining<maghribhoursRemaining && (maghribhoursRemaining>=-1 || maghribminutesRemaining>=-14 || maghribsecondsRemaining>=-1) ) 
      {
        distance=distance5;
       
      }
      if(ishahoursRemaining<-1 || ishaminutesRemaining<-14)
      {
        distance=distance1;
        ishahoursRemaining=500;
        ishaminutesRemaining=500;
        ishasecondsRemaining=500;
      }

     
     
   
      
     

      hoursRemaining = Math.floor(distance / (1000 * 60 * 60));
      minutesRemaining = Math.abs(Math.floor((distance / (1000 * 60)) % 60));
      secondsRemaining = Math.abs(Math.floor((distance / 1000) % 60));



      hoursRemaining = (hoursRemaining < 10) ? "" +hoursRemaining : hoursRemaining;
      minutesRemaining = (minutesRemaining < 10) ? "0" + minutesRemaining : minutesRemaining;
      secondsRemaining = (secondsRemaining < 10) ? "0" + secondsRemaining : secondsRemaining;


     // var timedown = hoursRemaining + ":" + minutesRemaining + ":" + secondsRemaining;

     var timedown = hoursRemaining + ":" + minutesRemaining + ":" + secondsRemaining;

      document.getElementById("myLink").innerText = timedown;
      document.getElementById("myLink").textContent = timedown;
      
    
  
      //Timeout( countdown(), 1000); // Update the timer every second
    setTimeout(function(){
        countdown(fajr,dhuhr,asr,maghrib,isha,ndfajar);
    },1000)    // Just figuring out this part drove me crazy 
    
    //secondsRemaining; // Return the updateTimer function
    
    

}

countdown(fajr,dhuhr,asr,maghrib,isha,ndfajar);

//countUpTo15Minutes();