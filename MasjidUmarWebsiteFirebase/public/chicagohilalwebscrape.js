//const puppeteer=require('puppeteer')//--- if you uncomment you will see what i mean.  //Prints hijri
//const fs=require("fs");  //Prints hijri

async function showscrapeWeb(){
    

  //Prints hijri 
  //////////////////////////////////////////////
   /* const browser = await puppeteer.launch();
    const page =  await browser.newPage();
    url='https://chicagohilal.org/';
    await page.goto(url);

    const[el1]= await page.$x();
    const txt= await el1.getProperty('textContent');
    const title= await txt.jsonValue();
 
    await browser.close();
    console.log({title})*/
////////////////////////////////////////////////////





   // const html=await page.content();
   
   
    /*var showtitle = title;
    //fs.writeFileSync("webscrape.html",html);
    console.log(showtitle);
    document.getElementById("MytitleDisplay").innerText = showtitle;
    document.getElementById("MytitleDisplay").textContent = showtitle;
    document.write(showtitle);
    setTimeout(scrapeWeb, 1000);*/



//prints hello.Before i had title. run "node chicagohilalwebscrape.js" in directory and uncomment all the Prints hijri comments. 
////////////////////////////////////////////////////////
   // var fulldate = "Hello";// using to test to see if library does affect it and it does
    //document.getElementById("MytitleDisplay").innerText = fulldate;
    //document.getElementById("MytitleDisplay").textContent = fulldate;
    //setTimeout(showscrapeWeb, 1000);
    /////////////////////////////////////////////////
   
}   

showscrapeWeb();