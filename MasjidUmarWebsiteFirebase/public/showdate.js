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
   // const days=["Monday","Tuesday", "Wednesday","Thursday","Firday","Saturday", "Sunday"]
    var session = "AM";
    
    switch (weekday) {
        case 0:
            weekdaysnow = "Sunday";
          break;
        case 1:
            weekdaysnow = "Monday";
          break;
        case 2:
            weekdaysnow;
        case 3:
            weekdaysnow = "Wednesday";
          break;
        case 4:
            weekdaysnow;
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


    
  
    
    var fulldate = weekdaysnow + ", " + months + " " + day + ", " + year;
    document.getElementById("MyDateDisplay").innerText = fulldate;
    document.getElementById("MyDateDisplay").textContent = fulldate;
    
    setTimeout(showDate, 1000);
    
}

showDate();