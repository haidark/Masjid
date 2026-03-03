// Firebase configuration for Masjid Umar
// Initialize Firebase app and Realtime Database
var firebaseConfig = {
	apiKey: "AIzaSyC66sRpScF977k-YbVIXq5I9-j8b82CV34",
	authDomain: "masjid-umar.firebaseapp.com",
	databaseURL: "https://masjid-umar-default-rtdb.firebaseio.com",
	projectId: "masjid-umar",
	storageBucket: "masjid-umar.appspot.com",
	messagingSenderId: "345938836566",
	appId: "1:345938836566:web:c9ec04b4cf84b7ed104ff7"
};

firebase.initializeApp(firebaseConfig);
var db = firebase.database();

// Validate admin phrase against value stored in Firebase
// Returns a Promise that resolves to true/false
function validatePhrase(input) {
	return db.ref('config/adminPhrase').once('value').then(function(snapshot) {
		var storedPhrase = snapshot.val();
		return storedPhrase !== null && input === storedPhrase;
	});
}
