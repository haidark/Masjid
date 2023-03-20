const puppeteer = require('puppeteer');

(async () => {
  // Launch the browser
  const browser = await puppeteer.launch();

  // Open a new page
  const page = await browser.newPage();

  // Navigate to the website
  await page.goto('https://chicagohilal.org/');

  // Wait for the hijri date element to load
  await page.waitForSelector('#hijri-date');

  // Get the hijri date text
  const hijriDate = await page.$eval('#hijri-date', element => element.textContent);

  // Create an HTML file with the hijri date
  const html = `
    <html>
      <head>
        <title>Chicago Hilal Hijri Date</title>
      </head>
      <body>
        <h1>Chicago Hilal Hijri Date</h1>
        <p>The hijri date is: ${hijriDate}</p>
      </body>
    </html>
  `;

  // Save the HTML file
  const fs = require('fs');
  fs.writeFileSync('hijri-date.html', html);

  // Close the browser
  await browser.close();
})();