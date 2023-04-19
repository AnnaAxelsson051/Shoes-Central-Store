var fieldProductCost;
var fieldSubtotal;
var fieldShippingCost;
var fieldTax;
var fieldTotal;

$(document).ready(function() {
	
	fieldProductCost = $("#productCost");
	fieldSubtotal = $("#subtotal");
	fieldShippingCost = $("#shippingCost");
	fieldTax = $("#tax");
	fieldTotal = $("#total");
	
	formatOrderAmounts();
	formatProductAmounts();
	
	$("#productList").on("change",".quantity-input", function(e){
		updateSubtotalWhenQuantityChanged($(this));
		
	});
	
		$("#productList").on("change",".price-input", function(e){
		updateSubtotalWhenPriceChanged($(this));
		
	});
	});
	
	function updateSubtotalWhenPriceChanged(input){
	    priceValue = input.val().replace(",", "");
		rowNumber = input.attr("rowNumber");
		
		quantityField = $("#quantity" + rowNumber);
		quantityValue = quantityField.val();
		newSubtotal = parseFloat(quantityValue) * priceValue;
		
		subtotalField = $("#subtotal" + rowNumber);
		subtotalField.val($.number(newSubtotal, 2));
	}
	
	function updateSubtotalWhenQuantityChanged(input){
		quantityValue = input.val();
		rowNumber = input.attr("rowNumber");
		priceField = $("#price" + rowNumber);
		priceValue = parseFloat(priceField.val().replace(",", ""));
		newSubtotal = parseFloat(quantityValue) * priceValue;
		
		subtotalField = $("#subtotal" + rowNumber);
		subtotalField.val($.number(newSubtotal, 2));
	}
	
	function formatProductAmounts() {
	$(".cost-input").each(function(e) {
		formatNumberForField($(this));
	});

	$(".price-input").each(function(e) {
		formatNumberForField($(this));
	});	
	
	$(".subtotal-output").each(function(e) {
		formatNumberForField($(this));
	});	
	
	$(".ship-input").each(function(e) {
		formatNumberForField($(this));
	});	
}
	
	function formatOrderAmounts() {
	formatNumberForField(fieldProductCost);
	formatNumberForField(fieldSubtotal);
	formatNumberForField(fieldShippingCost);
	formatNumberForField(fieldTax);
	formatNumberForField(fieldTotal);	
}

function formatNumberForField(fieldRef) {
	fieldRef.val($.number(fieldRef.val(), 2));
}
