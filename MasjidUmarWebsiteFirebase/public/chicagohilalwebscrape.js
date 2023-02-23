const puppeteer=require('puppeteer');

async function scrapeWeb(){
   const browser =await puppeteer.launch();
    const page = await browser.newPage();
    url='https://chicagohilal.org/';
    await page.goto(url);

    const[el1]=await page.$x('//*[@id="post-3108"]/div/p[1]');
    const txt=await el1.getProperty('textContent');
    const title= await txt.jsonValue();
    browser.close();
    //console.log({title})

   

    var showtitle = title;
    console.log(showtitle);
    document.getElementById("MytitleDisplay").innerText = showtitle;
    document.getElementById("MytitleDisplay").textContent = showtitle;
    document.write(showtitle);
    //setTimeout(scrapeWeb, 1000);
   
}   

scrapeWeb();