dropdownBrands = $("#brand");
dropdownCategories = $("#category");

	$(document).ready(function(){
		
//selects shortdesc/fulldesc element and calls richText method		
		$("#shortDescription").richText();
		$("#fullDescription").richText();
		
		
		dropdownBrands.change(function(){
			dropdownCategories.empty();
			getCategories();
		});
		getCategories();
		
	//shows image thumbnail when uploading user photo
	//first checks so photo is not larger than 100kb
	$("input[name='extraImage']").each(function(index) {
		$(this).change(function() {
			showExtraImageThumbnail(this, index);
		});	
	});
	});
	
	function showExtraImageThumbnail(fileInput, index){
		var file = fileInput.files[0];
		var reader = new FileReader();
		reader.onload = function(e){
			$("#extraThumbnail" + index).attr("src", e.target.result);
		};
		reader.readAsDataURL(file);
		
		addNextExtraImageSection(index +1);
	}
	
	function addNextExtraImageSection(index){
		html = `<div class"col border m-3 p-2">
			<div><label>Extra Image #${index}:</label></div>
			<div class="m-2">
				<img id="extraThumbnail${index}" alt="Extra image #${index} preview" class="img-fluid"
				th:src="${defaultImageThumbnailSrc}">
			</div>
			<div>
				<input type="file" name="extraImage" 
				onchange="showExtraImageThumbnail1(this, ${index})"
				accept="image/png, image/jpeg"/>
			</div>`;
			
			$("#divProductImages").append(html);
	}
	
	
	function getCategories(){
		brandId = dropdownBrands.val();
		url = brandModuleURL + "/" + brandId + "/categories";
	
	$.get(url, function(responseJson){
		//alert(responseJson);
		$.each(responseJson), function(index, category){
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
		});
	});
	
	}

	
	function checkUnique(form){
		productId = $("#id").val();
		productName = $("#name").val();
		
		csrfValue = $("input[name'_csrf']").val();
		
		url = "[[@{/products/check_unique}]]";
		
		params = {id: productId, name: productName, _csrf: csrfValue};
		
		$.post(url, params, function(response){
			if (response == "OK"){
				form.submit();
			} else if (response == "Duplicate"){
				showWarningModal("There is another product having the name " + productName);
			} else {
				showErrorModal("Unknown reposnse from server");
			}
		}).fail(function(){
			showErrorModal("Could not connect to the server")
		});
		return false;
	}
	