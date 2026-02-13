function scrapeWeb() {
  var date = new Date();
  date.setDate(date.getDate() - 1);
  var hijridate = date.toLocaleDateString('en-US-u-ca-islamic',
    { timeZone: 'UTC', month: 'long', day: 'numeric', year: 'numeric' });
  var fulldate = hijridate;
  var el = document.getElementById("hijridateval");
  if (el) {
    el.innerText = fulldate;
    el.textContent = fulldate;
  }
}

setInterval(scrapeWeb, 1000);
scrapeWeb();
