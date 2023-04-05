function addNextDetailSection(){
	
	allDivDetails = $("[id^='divDetail']");
	divDetailsCount = allDivDetails.length;
	
	htmlDetailSection = `
	<div class="form-inline" id="divDetail${nextDivDetailsCount}">
		<label class="m-3">Name:</label>
		<input type="text" class="form-control w-25" name="detailNames" maxLength="255">
	    <label class="m-3">Value:</label>
	    <input type="text" class="form-control w-25" name="detailValues" maxLength="255">
	</div>
	`;
	
	$("#divProductDetails").append(htmlDetailSection);
	
	 previousDivDetailSection = allDivDetails.last();
	 previousDivDetailID =  previousDivDetailSection.attr("id");
	 
    htmlLinkRemove =`
			<a class="btn fas fa-times-circle fa-2x icon-dark" 
			href="javascript:removeDetailSectionById('${previousDivDetailID}')"
			title="Remove this detail"></a>;
			`;
			
    previousDivDetailSection = allDivDetails.last();
    previousDivDetailSection.append(htmlLinkRemove);
}

function removeDetailSectionById(id){
	$("#divDetail" + id).remove();
}