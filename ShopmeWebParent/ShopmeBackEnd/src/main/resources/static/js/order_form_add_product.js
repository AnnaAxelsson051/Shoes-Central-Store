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
	showWarningModal("Product is not added.");
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