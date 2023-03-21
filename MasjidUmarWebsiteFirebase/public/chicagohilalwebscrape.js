
const axios = require('axios');
const cheerio = require('cheerio');
const fs = require('fs');

//const url = 'https://chicagohilal.org/';
async function showscrapeWeb(){
axios.get('https://chicagohilal.org/')
  .then(response => {




    const $ = cheerio.load(response.data);
    const hijriDate = $('body').text().trim();
    const htmlData = `<html><body><h1>adaw${hijriDate}</h1></body></html>`;
    fs.writeFile('output.html', htmlData, (err) => {
      if (err) throw err;
      console.log('The hijri date has been written to output.html');
    //  setTimeout(showscrapeWeb, 1000);
    });
    

    var fulldate = htmlData;// using to test to see if library does affect it and it does
   // document.getElementById("MytitleDisplay").innerText = fulldate;
   // document.getElementById("MytitleDisplay").textContent = fulldate;

    


    //setTimeout(showscrapeWeb, 1000);
  

     




  })

  .catch(error => console.error(error));

}

//showscrapeWeb();
setInterval(showscrapeWeb, 1000); //time is in ms