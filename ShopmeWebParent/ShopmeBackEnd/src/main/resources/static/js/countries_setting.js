var buttonLoad;
var dropDownCountry;
var nuttonAddCountry;
var buttonEditCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

//Loads countries in list upon btn click
$(document).ready(function(){
	buttonLoad = $("#dropDownCountries");
	dropDownCountry = $("#dropDownCountries");
	buttonAddCountry = $("#buttonAddCountry");
	buttonUpdateCountry = $("#buttonUpdateCountry");
	buttonDeleteCountry = $("#buttonDeleteCountry");
	labelCountryName = $("#labelCountryName");
	fieldCountryName = $("#fieldCountryName");
    fieldCountryCode = $("#fieldCountryCode");
	
	buttonLoad.click(function(){
		loadCountries();
	});
	
	dropDownCountry.on("change",function(){
		changeFormStateToSelectedCountry();
	});
});

//Handeling what buttons are clickeable
//Setting values for text field, country code etc
function changeFormStateToSelectedCountry(){
	buttonAddCountry.prop("value", "New");
	buttonEditCountry.prop("disabled", false);
	buttonDeleteCountry.prop("disabled", false);
	
	labelCountryName.text("Selected Country:");
	selectedCountryName = $("#dropDownCountries option:selected").text();
    fieldCountryName.val(selectedCountryName);
    
    contryCode = dropDownCountry.val().split("-")[1];
    fieldCountryCode.val(countryCode);
}

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
		showToastMessage("All countries have been loaded")
	}).fail(function(){
		showToastMessage("ERROR: Could not connect to server or server encountered an error")
	})
}

function showToastMessage(message) {
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}