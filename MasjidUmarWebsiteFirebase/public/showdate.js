
//const puppeteer=require('puppeteer');
function showDate(){
    var date = new Date();
    var day = date.getDate(); // 1 - 31
    var month=date.getMonth();//0-11
    var months="d";
    year=date.getFullYear();

    var weekday = date.getDay(); // 0 - 6
    var weekdaysnow="d";

    var s = date.getSeconds(); // 0 - 59
   // const months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
   // const days=["Monday","Tuesday", "Wednesday","Thursday","Friday","Saturday", "Sunday"]
    var session = "AM";
    
    switch (weekday) {
        case 0:
            weekdaysnow = "Sunday";
          break;
        case 1:
            weekdaysnow = "Monday";
          break;
        case 2:
            weekdaysnow="Tuesday";
        case 3:
            weekdaysnow = "Wednesday";
          break;
        case 4:
            weekdaysnow="Thursday";
            break;
        case 5:
            weekdaysnow = "Friday";
          break;
        case 6:
            weekdaysnow = "Saturday";
      }

      switch (month) {
        case 0:
          months = "January";
          break;
        case 1:
            months = "February";
          break;
        case 2:
            months = "March";
          break;
        case 3:
            months = "April";
          break;
        case 4:
            months = "May";
          break;
        case 5:
            months = "June";
          break;
        case 6:
            months = "July";
        case 7:
            months = "August";
          break;
        case 8:
            months = "September";
          break;
        case 9:
            months = "October";
          break;
        case 11:
            months = "November";
          break; 
        case 12:
            months = "December";
          break; 
    
      }

      //const browser =await puppeteer.launch();
      //const page = await browser.newPage();
      //url='https://chicagohilal.org/';
      //await page.goto(url);
  
      //const[el1]=await page.$x('//*[@id="post-3108"]/div/p[1]');
      //const txt=await el1.getProperty('textContent');
      //const title= await txt.jsonValue();
    
  
    
    var fulldate = weekdaysnow + ", " + months + " " + day + ", " + year;
    document.getElementById("MyDateDisplay").innerText = fulldate;
    document.getElementById("MyDateDisplay").textContent = fulldate;
    
    setTimeout(showDate, 1000);
    
}

showDate();

