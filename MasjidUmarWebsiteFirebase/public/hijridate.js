/*const axios = require('axios');
const cheerio = require('cheerio');
const fs = require('fs');

async function scrapeWeb() {
  axios.get('https://chicagohilal.org/')
    .then(response => {
      const $ = cheerio.load(response.data);
      const bodyText = $('body').text().trim();

      // Find the specific word "Ramadan" in the body text
      if (bodyText.includes('144')) {
        const ramadanDate = bodyText.split('14')[0] + '144'; // Get the text before and including the word "Ramadan"
        const htmlData = `<html><body><h1>${ramadanDate}</h1></body></html>`;

        // Write the new date to the output.html file
        fs.writeFile('output.html', htmlData, (err) => {
          if (err) throw err;
          console.log('The Ramadan date has been written to output.html');
        });
      } else {
        console.log('The word "Ramadan" has not been found yet.');
      }
    })
    .catch(error => console.error(error));



    var fulldate = "1444";
    document.getElementById("hijridateval").innerText = fulldate;
    document.getElementById("hijridateval").textContent = fulldate;
    
}

setInterval(scrapeWeb, 1000); //time is in ms
*/

/*const axios = require('axios');
const cheerio = require('cheerio');
const fs = require('fs');
const { JSDOM } = require('jsdom');
*/
async function scrapeWeb() {


 /* axios.get('https://chicagohilal.org/')
    .then(response => {
      const $ = cheerio.load(response.data);
      const bodyText = $('body').text().trim();

      // Find the specific word "1444" in the body text
      if (bodyText.includes('1444')) {
        const hijriDate = bodyText.split('1444')[0] + '1444'; // Get the text before and including the word "Ramadan"
        const htmlData = `<html><body><h1>${hijriDate}</h1></body></html>`;

        // Write the new date to the output.html file
        fs.writeFile('output.html', htmlData, (err) => {
          if (err) throw err;
          console.log('The Ramadan date has been written to output.html');
        });
      } else {
        console.log('The word "Ramadan" has not been found yet.');
      }
    })
    .catch(error => console.error(error));

 */
  //setTimeout(scrapeWeb, 1000);

  var date = new Date();

  date.setDate(date.getDate()-1);// Added a fix for when saudi time doesnt match 
  //when it doesnt match subtract 1 and if it does match remove the -1 TANVIR 8/17/23 
  hijridate=new Date(date).toLocaleDateString('en-US-u-ca-islamic', //removed -umalqura at the end becuase it didnt allow for 30 days
    { timeZone: 'UTC', month: 'long', day: 'numeric', year: 'numeric'}
  );


  
    //var fulldate="Dhuʻl-Qiʻdah 30, 1445 AH" // added forto make sure it doesnt change when changing months TK
   var fulldate = hijridate;
    document.getElementById("hijridateval").innerText = fulldate;
    document.getElementById("hijridateval").textContent = fulldate;
}
//setInterval(scrapeWeb, 1000); //time is in ms

setInterval(scrapeWeb, 1000); //time is in ms
scrapeWeb();