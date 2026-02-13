// Countdown timer to next iqamah time
// Colors: Blue = >10 min to iqamah, Green = <=10 min to iqamah, Red = past iqamah (up to 10 min)
// After 10 min past iqamah, moves to next prayer

function countdown(fajr, dhuhr, asr, maghrib, isha, ndfajar) {

	// Parse "h:mm" string into a Date object for today (or tomorrow for next-day fajr)
	function parseTime(timeStr, forceAM) {
		var parts = timeStr.split(":");
		var hr = parseInt(parts[0]);
		var mn = parseInt(parts[1]);
		return { hr: hr, mn: mn };
	}

	var now = new Date();

	// Build iqamah Date objects with correct 24-hour conversion
	// Fajr is AM (keep as-is since it's early morning, hours < 12)
	var fajrParsed = parseTime(fajr);
	var fajrTime = new Date(now);
	fajrTime.setHours(fajrParsed.hr, fajrParsed.mn, 0, 0);

	// Dhuhr is PM: 12:xx stays 12, 1:xx becomes 13
	var dhuhrParsed = parseTime(dhuhr);
	var dhuhrHr = dhuhrParsed.hr;
	if (dhuhrHr !== 12) { dhuhrHr += 12; }
	var dhuhrTime = new Date(now);
	dhuhrTime.setHours(dhuhrHr, dhuhrParsed.mn, 0, 0);

	// Asr is PM
	var asrParsed = parseTime(asr);
	var asrTime = new Date(now);
	asrTime.setHours(asrParsed.hr + 12, asrParsed.mn, 0, 0);

	// Maghrib is PM
	var maghribParsed = parseTime(maghrib);
	var maghribTime = new Date(now);
	maghribTime.setHours(maghribParsed.hr + 12, maghribParsed.mn, 0, 0);

	// Isha is PM
	var ishaParsed = parseTime(isha);
	var ishaTime = new Date(now);
	ishaTime.setHours(ishaParsed.hr + 12, ishaParsed.mn, 0, 0);

	// Next day's fajr
	var ndfajarParsed = parseTime(ndfajar);
	var ndfajarTime = new Date(now);
	ndfajarTime.setDate(ndfajarTime.getDate() + 1);
	ndfajarTime.setHours(ndfajarParsed.hr, ndfajarParsed.mn, 0, 0);

	// Ordered list of iqamah times
	var prayers = [
		{ name: 'Fajr', time: fajrTime },
		{ name: 'Dhuhr', time: dhuhrTime },
		{ name: 'Asr', time: asrTime },
		{ name: 'Maghrib', time: maghribTime },
		{ name: 'Isha', time: ishaTime },
		{ name: 'Fajr', time: ndfajarTime }
	];

	var TEN_MIN = 10 * 60 * 1000;
	var distance = null;
	var color = 'blue';

	// Find the current relevant prayer:
	// - If a prayer is upcoming (distance > 0), count down to it
	// - If a prayer just passed (within 10 min), show negative time in red
	// - If a prayer passed more than 10 min ago, skip to the next one
	for (var i = 0; i < prayers.length; i++) {
		var diff = prayers[i].time - now;

		if (diff > 0) {
			// Upcoming prayer
			distance = diff;
			if (diff <= TEN_MIN) {
				color = 'green';
			} else {
				color = 'blue';
			}
			break;
		} else if (diff >= -TEN_MIN) {
			// Prayer passed within last 10 minutes - show negative countdown
			distance = diff;
			color = 'red';
			break;
		}
		// else: more than 10 min past, skip to next prayer
	}

	// Fallback: if somehow nothing matched, count to next day fajr
	if (distance === null) {
		distance = ndfajarTime - now;
		color = 'blue';
	}

	// Calculate display values
	var absDistance = Math.abs(distance);
	var isNegative = distance < 0;

	var hours = Math.floor(absDistance / (1000 * 60 * 60));
	var minutes = Math.floor((absDistance / (1000 * 60)) % 60);
	var seconds = Math.floor((absDistance / 1000) % 60);

	// Format with leading zeros
	var hoursStr = (hours < 10) ? "" + hours : "" + hours;
	var minutesStr = (minutes < 10) ? "0" + minutes : "" + minutes;
	var secondsStr = (seconds < 10) ? "0" + seconds : "" + seconds;

	var timedown = (isNegative ? "-" : "") + hoursStr + ":" + minutesStr + ":" + secondsStr;

	var el = document.getElementById("myLink");
	el.textContent = timedown;
	el.style.color = color;

	// Update every second
	setTimeout(function () {
		countdown(fajr, dhuhr, asr, maghrib, isha, ndfajar);
	}, 1000);
}
