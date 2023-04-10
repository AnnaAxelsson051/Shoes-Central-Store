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
	
	//If value on the btn is add calling addCountry function
	//Otherwise changing the form state to new
	buttonAddCountry.click(function(){
		if(buttonAddCountry.val() == "Add"){
			addCountry();
		}else{
			changeFormStateToNew();
		}
	});
	
	buttonUpdateCountry.click(function(){
		updateCountry();
	});
	
	buttonDeleteCountry.click(function(){
		deleteCountry()
	});
});

function deleteCountry(){
	optionValue = dropDownCountry.val();
	countryId = optionValue.spint("-")[0];
	url = contextPath + "countries/delete/" + countryId;

	$.get(url, function(responseJSON){
		$("#dropDownCountries option[value='" + optionValue + "']").remove();
		changeFormStateToNew();
	}).done(function(){
		showToastMessage("The country has been deleted")
	}).fail(function(){
		showToastMessage("ERROR: Could not connect to server or server encountered an error")
	});
}

function updateCountry(){
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	
	countryID = dropDownCountry.val().spit("-")[0];
	
	jsonData = {id: countryId, name:countryName, code};
	
	$.ajax({
		type: 'POST',
		url: url,
	    beforeSend:function(xhr){
		xhr.setRequestHeader(csrfHeaderName, csrfValue);
	},
	data: JSON.stringify(jsonData),
	contentType: 'application/json'
	}).done(function(countryId){
		 $("#dropDownCountries option:selected").text(countryId + "-" + countruCode);
		 $("#dropDownCountries option:selected").text(countryName);
		showToastMessage("The country has been updated");
	
	changeFormStateToNew();
	
	}).fail(function(){
		showToastMessage("ERROR: Could not connect to server or server encountered an error")
	});
}

//Making an ajax call to server to the countries/save urö
//Getting country name and code from the textfields
//creating a json obj that will be sent with the request with key n val
//Tru security check, vonverting json obj to string
function addCountry(){
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	jsonData = {name:countryName, code};
	
	$.ajax({
		type: 'POST',
		url: url,
	    beforeSend:function(xhr){
		xhr.setRequestHeader(csrfHeaderName, csrfValue);
	},
	data: JSON.stringify(jsonData),
	contentType: 'application/json'
	}).done(function(countryId){
		selectNewlyAddedCountry(countryId, countryCode, countryName);
		showToastMessage("The new country has been added");
	}).fail(function(){
		showToastMessage("ERROR: Could not connect to server or server encountered an error")
	});
}


function selectNewlyAddedCountry(countryId, countryCode, countryName){
	optionValue = countryId + "-" + countryCode;
	$("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);
$("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);
fieldCountryCode.val("");
fieldCountryName.val("").focus();
}
//Altering text on buttons from new to add when clicking
//a country in the list and clicking new btn
//New is for enterig values, Add is for saving values to db
//Toasmessage appering after adding, country appears in list
//and fields are cleared
function changeFormStateToNew(){
	buttonAddCountry.val("Add");
	labelCountryName.text("Country Name:");
	
    buttonEditCountry.prop("disabled", true);
	buttonDeleteCountry.prop("disabled", true);

fieldCountryCode.val("");
fieldCountryName.val("").focus();
}

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
	});
}

function showToastMessage(message) {
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}