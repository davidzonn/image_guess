
try {
	makeRequest("services/categories.php", loadNav);
} catch (error) {
	alert(error);
}
function loadNav(items) {
	var nav = document.getElementsByTagName("NAV")[0];
	var h2 = appendElement(nav, "H2");
	h2.innerHTML = "Category";
	recursiveFill(nav, items);
}


function recursiveFill (externalElement, items) {
	var ul = appendElement (externalElement, "UL");
	for (var subItem in items) {
		var li = appendElement(ul, "li");
		var button = appendElement(li, "BUTTON");
		var categoryName = "";
		if (items[subItem] instanceof Array) {
			button.innerHTML = subItem;
			recursiveFill(li, items[subItem]);
		} else {
			button.innerHTML = items[subItem];
		}
		button.onclick = function() {jumpTo(this.innerHTML);};
	}
}
function jumpTo (categoryName) {
	var countryURIName = encodeURIComponent(categoryName.trim());
	var requestPage = "services/items.php/" + countryURIName;
	makeRequest(requestPage, loadGameScreen); 
}



