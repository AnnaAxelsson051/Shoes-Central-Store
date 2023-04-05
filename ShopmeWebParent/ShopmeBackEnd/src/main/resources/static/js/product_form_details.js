function addNextDetailSection(){
	htmlDetailSection = `
	<div class="form-inline">
		<label class="m-3">Name:</label>
		<input type="text" class="form-control w-25" name="detailNames" maxLength="255">
	    <label class="m-3">Value:</label>
	    <input type="text" class="form-control w-25" name="detailValues" maxLength="255">
	</div>
	`;
	
	$("#divProductDetails").append(htmlDetailSection);
}