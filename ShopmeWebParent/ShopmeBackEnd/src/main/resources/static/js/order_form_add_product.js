//Showing the add product modal dialog
$(document).ready(function(){
	$("#products").on("click", "#linkAddProduct", function(e){
		e.preventDefault();
		link = $(this);
		url = link.attr("href");
		
		$("#addProductModal").on("shown.bs.modal", function(){
			$(this).find("iframe").attr("src", url);
		});
		
		$("#addProductModal").modal();
		
	})
});

//Will be called in the search product page after product 
//is selected
function addProduct(productId, productName){
    getShippingCost(productId);
}

//Getting shippingcost based on what location user selected
function getShippingCost(productId){
	salectedCountry = $("#country option:selected");
	countryId = selectedCountry.val();
	
	state = $("#state").val();
	if(state.length == 0){
		state = $("#city").val();
	}
	requestUtl = contextPath + "/get_shipping_cost";
	params = {productId: productId, countryId: countryId, state:state};

	$.ajax({
		type: 'POST',
		url: requestUrl,
	    beforeSend:function(xhr){
		xhr.setRequestHeader(csrfHeaderName, csrfValue);
	},
	data: params
	}).done(function(shippingCost){
	getProductInfo(productId);
	}).fail(function(err){
     showWarningModal(err.responseJSON.message);
     getProductInfo(productId);
	}).always(function(){
		$("#addProductModal").modal("hide");
	});
}

//accessing the productDTO info
function getProductInfo(productId){
	requestURL = contextPath + "products/get/" + productId;
$.get(requestURL, function(productJson){
	console.log(productJson);
}).fail(function(err){
	showWarningModal(err.responseJSON.message);
});
}

//Checking if product exists before its added to order
//Reading all the product ids on the order form
function idProductAlreadyAdded(productId){
	productExists = false;
	
	$(".hiddenProductId").each(function(e){
		aProductId = $(this).val();
		
		if (aProductId == productId){
			productExists = true;
			return;
		}
	});
	return productExists;
}