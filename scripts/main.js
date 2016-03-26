//(function() { //for making all variables local.
	var contentScreen = document.getElementsByTagName("main")[0];
	var items = [];
	var TRIALS = 10;
	
	var startGameButton = document.getElementById("startGame");
	document.getElementById("instructions").onclick = function(){alert("Really?")};
	startGameButton.onclick = loadGame;

	var correct = 0;
	var wrong = 0;
	/**
		Makes an XMLHttpRequest.
		Second parameter, callback, is a function that receives one parameter, the answer of the server.
		Third parameter is optional, by default it's "get".
		Fourth parameter is optional, by default it's true (we make assync request).
	*/
	function makeRequest (url, callback, method, assync) {
		method = setDefault(method, "GET");
		assync = setDefault(assync, true);
		
		var xmlhttp=new XMLHttpRequest(); 

		xmlhttp.onreadystatechange=function(){
			if (xmlhttp.readyState==4 && xmlhttp.status==200){
				try {
					var answer = JSON.parse(xmlhttp.responseText);
				} catch (error) {
					alert("Error processing the answer of the server. \n Answer was: \n" + xmlhttp.responseText);
				}
				callback(answer);
			} 	
		};

		xmlhttp.open(
			method,
			url,
			true
		);

		xmlhttp.send();
	}

	function setDefault(variable, defaultValue) {
		return typeof variable === "undefined" ? defaultValue : variable;
	}

	function loadGame() {
		var serverUrl = "services/categoriesAndItems.php";
		makeRequest (serverUrl, createScreen);
		
		correct = 0;
		wrong = 0;
	}

	function createScreen(items) {
		if (items["type"] == "itemsCategory") {//Base case
			loadGameScreen(items["items"]);
		} else { //Recursive Case
			clear(contentScreen);
			var buttonList = appendMain("UL");
			
			for (var nameCategory in items) {
				var listElement = appendElement(buttonList, "LI");
				var button = appendElement(listElement, "BUTTON");
				button.innerHTML = nameCategory;
				
				button.onclick = function(){createScreen(items[this.innerHTML]);}; /*next level in recursive array*/
		
			}
		}
	}

	function appendElement(elementReference, tagName) {
		var childElement = document.createElement(tagName);
		elementReference.appendChild(childElement);
		return childElement;
	}

	function appendText (elementReference, text) {
		var childElement = document.createTextNode(text);
		elementReference.appendChild(childElement);
		return childElement;
	} 

	function appendMain(tagName) {
		var element = document.createElement(tagName);
		var mainScreen = document.getElementsByTagName("main")[0];
		mainScreen.appendChild(element);
		return element; //Return a reference to the newly created element, in case we want to add it attributes, events, etc.
	}

	function loadGameScreen(itemList) {
		clear(contentScreen);
		if (correct + wrong == TRIALS) {
			showStatistics();
		} else {
			setScoreScreen();
			
			items = itemList;
			// 1) Select a random item
			var itemName = selectRandom(itemList);
			// 2) Get its image
			var itemParameters = itemList[itemName];
			var image = getImage (itemParameters["image"]);
			// 3) Get three random possibilities, including the one selected.
			var possibilities = getPossibilities (itemList, itemName, 3);
			// 4) update the main to show image with possibilities
			showImage(image, possibilities, itemName);
		}
	}

	function showStatistics() {
		var results = appendMain("div");
		results.setAttribute("id", "results");
		appendText(results, "Your score was " + correct + " out of " + TRIALS + ".");
		var qualification = appendElement(results, "div");
		var feedbackText = correct > 8 ? "Excelent": correct>6 ? "Good" : correct > 4 ? "Not Bad" : correct > 2?"Bad":"Very Bad";
		appendText(qualification, feedbackText);
		setShareWithFacebook();
		var tryAgainButton = appendMain("button");
		tryAgainButton.setAttribute("id", "tryAgain");
		tryAgainButton.innerHTML = "Play Again";
		tryAgainButton.onclick = loadGame;
	}
	
	function getPossibilities (arrayObject, mandatoryItem, n) {
		var keysArray = Object.keys(arrayObject);
		keysArray.sort(function(a,b) { return a == mandatoryItem? -1: 
			b == mandatoryItem? 1:
				0.5 - Math.random(); });
		return keysArray.slice(0, n);
	}

	function showImage(image, possibilities, realName) {
		contentScreen.appendChild(image);
		var header = document.createElement("h2");
		header.innerHTML = "What's its name?";
		contentScreen.appendChild(header);
		addPossibilities(possibilities, realName);
		
	}

	function setScoreScreen() {
		var scoreScreen = document.createElement("div");
		appendText(scoreScreen, "Correct Answers: " + correct);
		appendText(scoreScreen, " Wrong Answers: " + wrong);
		contentScreen.appendChild(scoreScreen);
		scoreScreen.id = "score";
	}
	
	function setShareWithFacebook() {
		var fbShare = document.createElement("button");
		appendText(fbShare, "Share your Accomplishment!");
		fbShare.id = "FBShare";
		fbShare.onclick = function() {FB.ui({
			method: 'feed',
			link: 'https://developers.facebook.com/docs/',
			caption: 'I got ' + correct + " out of " + TRIALS + ". Think you can beat me?"
		}, function(response){});};
		
		contentScreen.appendChild(fbShare); 
		
	}
	function addPossibilities(array, realName) {
		shuffle(array);
		for (var key in array) {
			var button = document.createElement("button");
			button.innerHTML = array[key];
			button.onclick = function() {giveFeedback(this.innerHTML, realName);};
			contentScreen.appendChild(button);
		}
	}

	function giveFeedback(givenName, realName) {
		var feedback = realName == givenName;
		//score += feedback? 2:score > 0?-1:0; //score never goes bellow 0.
		popup (feedback, realName);
	}

	function appendTick(element, feedback) {
		var tick = getImage("/res/img/game/tick.png");
		var wrong = getImage("/res/img/game/error.png");
		element.appendChild(feedback?tick:wrong);		
	}
	
	function popup (feedback, realName) {
		div = document.getElementById("feedback");
		if (!div) { //If we didn't give displayed the popup already
			if(feedback) {correct++;} else {wrong++;}
			var div = appendMain("DIV");
			div.setAttribute("id", "feedback");
			var tick = appendTick(div, feedback);
			var button = appendElement(div, "BUTTON");
			button.setAttribute("class", "continue");
			button.innerHTML = "Continue";
			button.onclick = function () {loadGameScreen(items);};
			div.appendChild(button);
			div.setAttribute("class", feedback?"success":"failure");
		}
	}

	function shuffle(array) {
		array.sort(function() {
			  return 0.5 - Math.random();
		});
	}

	function selectRandom (arrayObject) {
		var keysArray = Object.keys(arrayObject);
		var length = keysArray.length;
		var randomIndex = Math.floor(Math.random() * keysArray.length);
		var randomElement = keysArray[randomIndex];
		return randomElement;
	}
	function log(obj) {
		console.log(JSON.stringify(obj));
	}
	function clear(node) {
		node.innerHTML = "";
	}

	function getImage(imgPath) {
		var image = document.createElement("img");
		
		image.setAttribute("src", location.pathname + imgPath);
		return image;
	}
//})();
