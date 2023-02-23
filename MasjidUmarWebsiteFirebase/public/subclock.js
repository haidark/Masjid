var time = "2200-1845",
    difference = calcDifference(22,00,5,35);

console.log(difference);

function calcDifference(hours1,minutes1,hours2,minutes2) {
  
   
    var first= (hours1 * 60 + minutes1) / 60;
    var second=(hours2 * 60 + minutes2) / 60;
 
  return second - first;
}