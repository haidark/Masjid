// Get Jamaat Time	
function jamaatify(jamaatTimes) {
    var list = ['Fajr', 'Sunrise', 'Dhuhr', 'Asr', 'Maghrib', 'Isha'];
    var format = 'hh:mm';

    for(var i in list){
        var jamaat = moment(jamaatTimes[list[i].toLowerCase()], "hh:mm");
        switch(i){
            case "0": //Set Fajr Time
                jamaat.add(10, 'm');
                jamaatTimes[list[i].toLowerCase()] = jamaat.format("h:mm");
                break;

            case "1": //Sunrise stays the same
                jamaatTimes[list[i].toLowerCase()] = jamaat.format("h:mm");
                break;
                
            case "3": //Asr time is set with Zuhr time. Skipped Zuhr time.
                // If Asr start time is at or before 2:30 PM        
                if(moment(jamaat.format(format),format).isSameOrBefore(moment('2:30', format))){    
                    jamaatTimes[list[3].toLowerCase()] = '3:00';
                    jamaatTimes[list[2].toLowerCase()] = '12:45';
                    // console.log(jamaatTimes[list[i-1].toLowerCase()]);
                }
                else 
                // If Asr start time is between 2:30 and 3:25 pm. Asr: 3:30, Zuhr 1:00 pm
                if(moment(jamaat.format(format),format).isBetween(moment('2:30', format),moment('3:25', format))){
                    jamaatTimes[list[3].toLowerCase()] = '3:30';
                    jamaatTimes[list[2].toLowerCase()] = '1:00';
                }
                else 
                // If Asr start time is between 3:25 and 4:20 pm. Asr: 4:30, Zuhr 1:30 pm
                if(moment(jamaat.format(format),format).isBetween(moment('3:25', format),moment('4:20', format))){
                    jamaatTimes[list[3].toLowerCase()] = '4:30';
                    jamaatTimes[list[2].toLowerCase()] = '1:30';
                }
                else 
                // If Asr start time is between 4:20 and 4:50 pm. Asr: 5:00, Zuhr 1:30 pm
                if(moment(jamaat.format(format),format).isBetween(moment('4:20', format),moment('4:50', format))){
                    jamaatTimes[list[3].toLowerCase()] = '5:00';
                    jamaatTimes[list[2].toLowerCase()] = '1:30';
                }
                else 
                // If Asr start time is 4:50 pm. Asr: 5:00, Zuhr 1:30 pm
                //Tanvir 5/17/23
                if(moment(jamaat.format(format),format).isBetween(moment('4:49', format),moment('4:51', format))){
                    jamaatTimes[list[3].toLowerCase()] = '5:15';
                    jamaatTimes[list[2].toLowerCase()] = '1:30';
                }
                else
               
                // If Asr start time is between 4:50 and 5:10 pm. Asr: 5:15, Zuhr 1:30 pm
                if(moment(jamaat.format(format),format).isBetween(moment('4:50', format),moment('5:10', format))){
                    jamaatTimes[list[3].toLowerCase()] = '5:15';
                    jamaatTimes[list[2].toLowerCase()] = '1:30';
                }
                
                break;

            // case '3': //Asr and Zuhr logic are combined

            case "4": //Maghrib is 10 minutes after the sunset
                // console.log("Maghrib");
                jamaat.add(10, 'm');
                jamaatTimes[list[i].toLowerCase()] = jamaat.format("h:mm");
                break;

            case "5": //Isha is 5 minutes after the timing enters
                // console.log("Isha");
                jamaat.add(5, 'm');
                jamaatTimes[list[i].toLowerCase()] = jamaat.format("h:mm");
                break;

            case "6": //Tahajjud time, optional
                // console.log("Midnight");
                jamaatTimes[list[i].toLowerCase()] = jamaat.format("h:mm a");
                break;

            default:
                break;
        }
    }
    }