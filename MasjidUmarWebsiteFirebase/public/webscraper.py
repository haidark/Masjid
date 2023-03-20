from flask import Flask, render_template
app=Flask(__name__)
from bs4 import BeautifulSoup
import requests
import lxml
from lxml import etree

app=Flask(__name__,template_folder='templates')

response = requests.get("https://www.scrapingbee.com/blog/")
soup = BeautifulSoup(response.content, 'html.parser')
body = soup.find("body")

dom = etree.HTML(str(body)) # Parse the HTML content of the page
xpath_str = '//*[@id="content"]/section/div/div[1]/h1' # The XPath expression for the blog's title
xpath_strtxt=dom.xpath(xpath_str)[0].text

print(xpath_strtxt)


@app.route('/')
def home():
    return render_template('display_month.html',xpath_strtxt=xpath_strtxt)



