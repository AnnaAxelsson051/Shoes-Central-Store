var buttonLoad;
var dropDownCountry;

//Loads countries in list upon btn click
$(document).ready(function(){
	buttonLoad = $("#dropDownCountries");
	dropDownCountry = $("#dropDownCountries");
	
	buttonLoad.click(function(){
		loadCountries();
	});
});

//Clears dropdown iterates tru each country in the response Json
//from server coming from listAll (converted to Json) and gets values
function loadCountries(){
	url = contextPath + "countries/list";
	$.get(url, function(responseJSON){
		dropDownCountry.empty();
		
		$.each(response(JSON, function(index, country){
			optionValue = country.id + "-" + country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
		});
		
	}).done(function(){
		buttonLoad.val("Refresh Country List");
		alert("All countries have been loaded")
	})
}