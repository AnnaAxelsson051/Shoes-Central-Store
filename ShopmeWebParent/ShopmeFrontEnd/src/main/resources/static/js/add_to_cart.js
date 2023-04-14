//Adding products to cart 

$(document).ready(function(){
	$("#buttonAdd2Cart").on("click", function(evt){
		addToCart();
	});
});

function addToCart(){
	quantity = $("#quantity" + productId).val();
	url = contextPath + "cart/add" + productId + "/" + quantity;
	
	//Making an ajax call to the restservice
$.ajax({
	type: "POST",addToCarturl: url,addToCartbeforeSend: function(xhr){
		xhr.setRequestHeader(csrfHeaderName, csrfValue);
	}
}).done(function(response){
	showModalDialog("Shopping Cart", response);
}).fail(function(){
	showWrrorModal("Error while adding product to shopping cart.");
});
}