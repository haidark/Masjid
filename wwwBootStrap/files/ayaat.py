# Shuaib Peters
# 1-28-16
# Project Haidar

from bs4 import BeautifulSoup
import requests
import webbrowser

verse = raw_input("Please enter the verses in this format: Surah1:Verse1, Surah2:Verse2, ...:\n")

ayah_1 = verse.replace(', ' , ',')

ayah = ayah_1.split(',')


f = open('output_ayahs.htm', 'w')

dictionary_1 = [ayah][0]
header = """
		<html>
			<head>
				<title>Quran Translation</title>
			</head>
			<body> """
f.write(header)
for item in dictionary_1:
	x = item.split(':')[0] 
	y = item.split(':')[1]
	
	print(" ")
	
	url = ("http://legacy.quran.com/" + str(x) + "/" + str(y))
	a = "Surah "+str(x)+": Verse "+str(y)
	r = requests.get(url)
	r.content

	soup = BeautifulSoup(r.content, "html.parser")

	arabic = soup.find("div", {"class" : "ayah language_1 images"})
	b = arabic.contents[0]

	english = soup.find("div", {"class" : "ayah language_6 text"})
	c = english.contents[1].string

	message = """
				<p>%s</p>
				%s
				<p>%s</p>
				"""

	whole = message %(a, b, c)
	f.write(whole)
closer = """</body>
		   </html>"""
f.write(closer)
f.close()		
webbrowser.open('output_ayahs.htm')

