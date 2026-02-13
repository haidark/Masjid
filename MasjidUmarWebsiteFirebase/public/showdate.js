function showDate() {
    var date = new Date();
    var day = date.getDate();
    var month = date.getMonth();
    var months = "d";
    var year = date.getFullYear();
    var weekday = date.getDay();
    var weekdaysnow = "d";

    switch (weekday) {
        case 0:
            weekdaysnow = "Sunday";
          break;
        case 1:
            weekdaysnow = "Monday";
          break;
        case 2:
            weekdaysnow="Tuesday";
            break;
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
            break;
        case 7:
            months = "August";
          break;
        case 8:
            months = "September";
          break;
        case 9:
            months = "October";
          break;
        case 10:
            months = "November";
          break; 
        case 11:
            months = "December";
          break;
      }

    var fulldate = weekdaysnow + ", " + months + " " + day + ", " + year;
    var el = document.getElementById("MyDateDisplay");
    if (el) {
      el.innerText = fulldate;
      el.textContent = fulldate;
    }
    setTimeout(showDate, 1000);
}

showDate();

