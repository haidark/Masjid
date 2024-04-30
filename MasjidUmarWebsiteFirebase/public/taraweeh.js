


function taraweeh(){
    var first=0;
    var date = new Date();
    var day = date.getDate(); // 1 - 31
    var month=date.getMonth()+1;//0-11
    var months="d";
    year=date.getFullYear();
    currentdate=month+"/"+day+"/"+year;
 
    var reciters = [
        { date: '3/23/2024',reciternum: 'First' , pagestart: 242, pageend: 245,Surah:'Yusuf',ayat:'53-87',reciter:'Zarar' },
        { date: '3/23/2024',reciternum: 'Second' , pagestart: 246, pageend: 249,Surah:'Yusuf',ayat:'88-5',reciter:'Mudasir' },
        { date: '3/23/2024',reciternum: 'Third' , pagestart: 250, pageend: 253,Surah:"Ra'd",ayat:'6-34',reciter:'Fawad' },
        { date: '3/23/2024',reciternum: 'Fourth' , pagestart: 254, pageend: 257,Surah:"Ra'd/Ibrahim",ayat:'35-18',reciter:'Badran' },
        { date: '3/23/2024',reciternum: 'Fifth' , pagestart: 258, pageend: 261,Surah:'Ibrahim',ayat:'19-52',reciter:'Badran' },

        { date: '3/24/2024',reciternum: 'First' , pagestart: 262, pageend: 265,Surah:'Hijr',ayat:'1-66',reciter:'Tahsin' },
        { date: '3/24/2024',reciternum: 'Second' , pagestart: 266, pageend: 269,Surah:'Hijr/Nahl',ayat:'67-29',reciter:'Fahad' },
        { date: '3/24/2024',reciternum: 'Third' , pagestart: 270, pageend: 273,Surah:'Nahl',ayat:'30-64',reciter:'Badran' },
        { date: '3/24/2024',reciternum: 'Fourth' , pagestart: 274, pageend: 277,Surah:'Nahl',ayat:'65-93',reciter:'Badran' },
        { date: '3/24/2024',reciternum: 'Fifth' , pagestart: 278, pageend: 281,Surah:'Nahl',ayat:'94-128',reciter:'Zarar' },

        { date: '3/25/2024',reciternum: 'First' , pagestart: 282, pageend: 285,Surah:'Isra',ayat:'1-49',reciter:'Zarar' },
        { date: '3/25/2024',reciternum: 'Second' , pagestart: 286, pageend: 289,Surah:'Isra',ayat:'50-98',reciter:'Mudasir' },
        { date: '3/25/2024',reciternum: 'Third' , pagestart: 290, pageend: 293,Surah:'Isra-Kahf',ayat:'99-31',reciter:'Badran' },
        { date: '3/25/2024',reciternum: 'Fourth' , pagestart: 294, pageend: 297,Surah:'Kahf',ayat:'32-74',reciter:'Badran' },
        { date: '3/25/2024',reciternum: 'Fifth' , pagestart: 298, pageend: 301,Surah:'Kahf-Maryam',ayat:'75-15',reciter:'Salah' },

        { date: '3/26/2024',reciternum: 'First' , pagestart: 302, pageend: 305,Surah:'Maryam',ayat:'16-98',reciter:'Mudasir' },
        { date: '3/26/2024',reciternum: 'Second' , pagestart: 306, pageend: 309,Surah:'Ta Ha',ayat:'1-82',reciter:'Salah' },
        { date: '3/26/2024',reciternum: 'Third' , pagestart: 310, pageend: 313,Surah:'Ta Ha',ayat:'83-135',reciter:'Zarar' },
        { date: '3/26/2024',reciternum: 'Fourth' , pagestart: 314, pageend: 317,Surah:'Anbiya',ayat:'1-50',reciter:'Badran' },
        { date: '3/26/2024',reciternum: 'Fifth' , pagestart: 318, pageend: 321,Surah:'Anbiya',ayat:'51-121',reciter:'Badran' },

        { date: '3/27/2024',reciternum: 'First' , pagestart: 322, pageend: 325,Surah:'Hajj',ayat:'1-37',reciter:'Salah' },
        { date: '3/27/2024',reciternum: 'Second' , pagestart: 326, pageend: 329,Surah:'Hajj',ayat:'38-78',reciter:'Zarar' },
        { date: '3/27/2024',reciternum: 'Third' , pagestart: 330, pageend: 333,Surah:"Mu'minum",ayat:'1-74',reciter:'Badran' },
        { date: '3/27/2024',reciternum: 'Fourth' , pagestart: 334, pageend: 337,Surah:"Mu'minum/Nur",ayat:'75-20',reciter:'Badran' },
        { date: '3/27/2024',reciternum: 'Fifth' , pagestart: 338, pageend: 341,Surah:"Nur",ayat:'21-52',reciter:'Mudasir' },

        { date: '3/28/2024',reciternum: 'First' , pagestart: 342, pageend: 345,Surah:"Nur/Furqan",ayat:'53-20',reciter:'Mudasir' },
        { date: '3/28/2024',reciternum: 'Second' , pagestart: 346, pageend: 349,Surah:'Furqan',ayat:'21-77',reciter:'Mudasir' },
        { date: '3/28/2024',reciternum: 'Third' , pagestart: 350, pageend: 353,Surah:"Ash Shuara",ayat:'1-122',reciter:'Badran' },
        { date: '3/28/2024',reciternum: 'Fourth' , pagestart: 354, pageend: 357,Surah:"Ash Shuara",ayat:'123-227',reciter:'Badran' },
        { date: '3/28/2024',reciternum: 'Fifth' , pagestart: 358, pageend: 361,Surah:"Naml",ayat:'1-58',reciter:'Salah' },

        { date: '3/29/2024',reciternum: 'First' , pagestart: 362, pageend: 365,Surah:"Naml",ayat:'59-93',reciter:'Tahsin' },
        { date: '3/29/2024',reciternum: 'Second' , pagestart: 366, pageend: 369,Surah:'Qasas',ayat:'1-35',reciter:'Badran' },
        { date: '3/29/2024',reciternum: 'Third' , pagestart: 370, pageend: 373,Surah:"Qasas",ayat:'36-70',reciter:'Fawad' },
        { date: '3/29/2024',reciternum: 'Fourth' , pagestart: 374, pageend: 377,Surah:"Qasas",ayat:'71-15',reciter:'Mudasir' },
        { date: '3/29/2024',reciternum: 'Fifth' , pagestart: 378, pageend: 381,Surah:"Ankabut",ayat:'16-45',reciter:'Zarar' },

        { date: '3/30/2024',reciternum: 'First' , pagestart: 382, pageend: 385,Surah:"Ankabut",ayat:'46-16',reciter:'Mudasir' },
        { date: '3/30/2024',reciternum: 'Second' , pagestart: 386, pageend: 389,Surah:'Rum',ayat:'17-50',reciter:'Fahad' },
        { date: '3/30/2024',reciternum: 'Third' , pagestart: 390, pageend: 393,Surah:"Rum/Luqman",ayat:'51-28',reciter:'Badran' },
        { date: '3/30/2024',reciternum: 'Fourth' , pagestart: 394, pageend: 397,Surah:"Luqman/Sajdah",ayat:'29-30',reciter:'Haidar' },
        { date: '3/30/2024',reciternum: 'Fifth' , pagestart: 398, pageend: 401,Surah:"Ahzab",ayat:'1-27',reciter:'Zarar' },

        { date: '3/31/2024',reciternum: 'First' , pagestart: 402, pageend: 405,Surah:"Ahzab",ayat:'28-54',reciter:'Fahad' },
        { date: '3/31/2024',reciternum: 'Second' , pagestart: 406, pageend: 409,Surah:'Ahzab/Saba',ayat:'55-14',reciter:'Haidar' },
        { date: '3/31/2024',reciternum: 'Third' , pagestart: 410, pageend: 413,Surah:"Saba",ayat:'15-54',reciter:'Mudasir' },
        { date: '3/31/2024',reciternum: 'Fourth' , pagestart: 414, pageend: 417,Surah:"Fatir",ayat:'1-30',reciter:'Fawad' },
        { date: '3/31/2024',reciternum: 'Fifth' , pagestart: 418, pageend: 421,Surah:"Fatir/Yaseen",ayat:'31-27',reciter:'Zarar' },

        { date: '4/1/2024',reciternum: 'First' , pagestart: 422, pageend: 425,Surah:"Yaseen",ayat:'28-83',reciter:'Badran' },
        { date: '4/1/2024',reciternum: 'Second' , pagestart: 426, pageend: 429,Surah:'As-Saffat',ayat:'1-98',reciter:'Zarar' },
        { date: '4/1/2024',reciternum: 'Third' , pagestart: 430, pageend: 433,Surah:"As-Saffat",ayat:'99-16',reciter:'Tahsin' },
        { date: '4/1/2024',reciternum: 'Fourth' , pagestart: 434, pageend: 437,Surah:"Saad",ayat:'17-88',reciter:'Haidar' },
        { date: '4/1/2024',reciternum: 'Fifth' , pagestart: 438, pageend: 441,Surah:"Zumar",ayat:'1-31',reciter:'Salah' },

        { date: '4/2/2024',reciternum: 'First' , pagestart: 442, pageend: 445,Surah:"Zumar",ayat:'32-66',reciter:'Salah' },
        { date: '4/2/2024',reciternum: 'Second' , pagestart: 446, pageend: 449,Surah:'Zumar/Ghafir',ayat:'67-22',reciter:'Zarar' },
        { date: '4/2/2024',reciternum: 'Third' , pagestart: 450, pageend: 453,Surah:"Ghafir",ayat:'23-58',reciter:'Haidar' },
        { date: '4/2/2024',reciternum: 'Fourth' , pagestart: 454, pageend: 457,Surah:"Ghafir",ayat:'59-8',reciter:'Badran' },
        { date: '4/2/2024',reciternum: 'Fifth' , pagestart: 458, pageend: 461,Surah:"Fussilat",ayat:'9-46',reciter:'Mudasir' },

        { date: '4/3/2024',reciternum: 'First' , pagestart: 462, pageend: 465,Surah:"Fussilat/Ash-Shura",ayat:'47-22',reciter:'Salah' },
        { date: '4/3/2024',reciternum: 'Second' , pagestart: 466, pageend: 469,Surah:'Ash-Shura',ayat:'23-53',reciter:'Mudasir' },
        { date: '4/3/2024',reciternum: 'Third' , pagestart: 470, pageend: 473,Surah:"Zukhruf",ayat:'1-56',reciter:'Tahsin' },
        { date: '4/3/2024',reciternum: 'Fourth' , pagestart: 474, pageend: 477,Surah:"Zukhruf/Dukhaan",ayat:'57-39',reciter:'Badran' },
        { date: '4/3/2024',reciternum: 'Fifth' , pagestart: 478, pageend: 481,Surah:"Dukhaan/Jathiya",ayat:'40-37',reciter:'Zarar' },

        { date: '4/4/2024',reciternum: 'First' , pagestart: 482, pageend: 485,Surah:"Ahqaf",ayat:'1-28',reciter:'Salah' },
        { date: '4/4/2024',reciternum: 'Second' , pagestart: 486, pageend: 489,Surah:'Ahqaf/Muhammad',ayat:'29-30',reciter:'Irshad' },
        { date: '4/4/2024',reciternum: 'Third' , pagestart: 490, pageend: 493,Surah:"Muhammad/Fath",ayat:'31-23',reciter:'Badran' },
        { date: '4/4/2024',reciternum: 'Fourth' , pagestart: 494, pageend: 497,Surah:"Fath/Hujrat",ayat:'24-18',reciter:'Zarar' },
        { date: '4/4/2024',reciternum: 'Fifth' , pagestart: 498, pageend: 501,Surah:"Qaf/Dhariyat",ayat:'1-23',reciter:'Mudasir' },

        
        { date: '4/5/2024',reciternum: 'First' , pagestart: 502, pageend: 505,Surah:"Dhariyat/Toor",ayat:'24-49',reciter:'Tahsin' },
        { date: '4/5/2024',reciternum: 'Second' , pagestart: 506, pageend: 509,Surah:'Najm/Qamar',ayat:'1-22',reciter:'Badran' },
        { date: '4/5/2024',reciternum: 'Third' , pagestart: 510, pageend: 513,Surah:"Qamar/Rahman",ayat:'23-78',reciter:'Azer' },
        { date: '4/5/2024',reciternum: 'Fourth' , pagestart: 514, pageend: 517,Surah:"Waqiah",ayat:'waqiah',reciter:'Mudasir' },
        { date: '4/5/2024',reciternum: 'Fifth' , pagestart: 518, pageend: 521,Surah:"Hadid",ayat:'hadid',reciter:'Zarar' },

        { date: '4/6/2024',reciternum: 'First' , pagestart: 522, pageend: 525,Surah:"Mujadilah",ayat:'Mujadilah',reciter:'Rayan' },
        { date: '4/6/2024',reciternum: 'Second' , pagestart: 526, pageend: 529,Surah:'Hashr',ayat:'Hashr-Mu 5',reciter:'Mudasir' },
        { date: '4/6/2024',reciternum: 'Third' , pagestart: 530, pageend: 533,Surah:"Mumtahinah/Saff",ayat:'6-Jumuah',reciter:'Azer' },
        { date: '4/6/2024',reciternum: 'Fourth' , pagestart: 534, pageend: 537,Surah:"Munafiqun/Taghabun",ayat:'Munafiqun-tag',reciter:'Zarar' },
        { date: '4/6/2024',reciternum: 'Fifth' , pagestart: 538, pageend: 541,Surah:"Talaq/Tahrim",ayat:'Talaq/Tahrim',reciter:'Badran' },

        { date: '4/7/2024',reciternum: 'First' , pagestart: 542, pageend: 545,Surah:"Mulk/Qalam",ayat:'Mulk/Qalam',reciter:'Zarar' },
        { date: '4/7/2024',reciternum: 'Second' , pagestart: 546, pageend: 549,Surah:"Haqqah/Mai'rij",ayat:"Haqqah/Mai'rij",reciter:'Mudasir' },
        { date: '4/7/2024',reciternum: 'Third' , pagestart: 550, pageend: 553,Surah:"Nuh/Jinn",ayat:'6-Nuh/Jinn',reciter:'Rayan' },
        { date: '4/7/2024',reciternum: 'Fourth' , pagestart: 554, pageend: 557,Surah:"Muzammil/Mudasir",ayat:'Muzammil/Mudasir',reciter:'Irshad' },
        { date: '4/7/2024',reciternum: 'Fifth' , pagestart: 558, pageend: 561,Surah:"Qiyamah-Mursalat",ayat:'Qiyamah-Mursalat',reciter:'Azer' },

        { date: '4/8/2024',reciternum: 'First' , pagestart: 562, pageend: 565,Surah:"Naba-Abasa",ayat:'Naba-Abasa',reciter:'Farhan' },
        { date: '4/8/2024',reciternum: 'Second' , pagestart: 566, pageend: 569,Surah:"Takwir -- Inshiqaq",ayat:"Takwir - Inshiqaq",reciter:'Zarar' },
        { date: '4/8/2024',reciternum: 'Third' , pagestart: 570, pageend: 573,Surah:"Buruj -- Fajr",ayat:'Buruj- Balad',reciter:'Azer' },
        { date: '4/8/2024',reciternum: 'Fourth' , pagestart: 574, pageend: 577,Surah:"Balad -- Alaq",ayat:'Shams-Bayanah',reciter:'Mudasir' },
        { date: '4/8/2024',reciternum: 'Fifth' , pagestart: 578, pageend: 604,Surah:"Qadr-Nas",ayat:'Zalzalah-Naas',reciter:'Badran' }

    ];

    for (i = 0; i < reciters.length; i++) {
        if (currentdate==reciters[i].date && reciters[i].reciternum=='First') {
            var First =  reciters[i].reciternum+":"  + reciters[i].reciter +", Surah:"+reciters[i].Surah+", Ayat:"+reciters[i].ayat;
            document.getElementById("one").innerText = First;
            document.getElementById("one").textContent = First;
        }


        if (currentdate==reciters[i].date && reciters[i].reciternum=='Second') {
            var Second =   reciters[i].reciternum+":"  + reciters[i].reciter +", Surah:"+reciters[i].Surah+", Ayat:"+reciters[i].ayat;
            document.getElementById("two").innerText = Second;
            document.getElementById("two").textContent = Second;
        }

        if (currentdate==reciters[i].date && reciters[i].reciternum=='Third') {
            var Third =   reciters[i].reciternum+":"  + reciters[i].reciter +", Surah:"+reciters[i].Surah+", Ayat:"+reciters[i].ayat;
            document.getElementById("three").innerText = Third;
            document.getElementById("three").textContent = Third;
        }

        if (currentdate==reciters[i].date && reciters[i].reciternum=='Fourth') {
            var Fourth =   reciters[i].reciternum+":"  + reciters[i].reciter +", Surah:"+reciters[i].Surah+", Ayat:"+reciters[i].ayat;
            document.getElementById("four").innerText = Fourth;
            document.getElementById("four").textContent = Fourth;
        }

        if (currentdate==reciters[i].date && reciters[i].reciternum=='Fifth') {
            var Fifth =  reciters[i].reciternum+":"  + reciters[i].reciter +", Surah:"+reciters[i].Surah+", Ayat:"+reciters[i].ayat;
            document.getElementById("five").innerText = Fifth;
            document.getElementById("five").textContent = Fifth;
        }
    }
    

    /*var second = reciters[1].date + ", " + reciters[1].reciternum + " " + reciters[1].reciter ;
    document.getElementById("second").innerText = second;
    document.getElementById("second").textContent = second;

  */
    setTimeout(taraweeh, 1000);
    
}

taraweeh();