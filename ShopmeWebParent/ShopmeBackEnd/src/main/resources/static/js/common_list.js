function clearFilter(){
	window.location = moduleURL;
}

//Confirm message when deleting category
function showDeleteConfirmModal(link, entityName){
	entityId = link.attr("entityId");
	
	$("#yesButton").attr("href", link.attr("href"));
	$("#confirmText").text("Are you sure you want to delete this " 
	+ entityName + " Id " + entityId + "?"); 
	
	$("#confirmModal").modal();
}