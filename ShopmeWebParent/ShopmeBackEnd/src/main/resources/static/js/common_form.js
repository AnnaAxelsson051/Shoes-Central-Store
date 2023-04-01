$(document).ready(function(){
			$("#buttonCancel").on("click", function(){
	window.location = moduleURL;
	});
	
	//shows image thumbnail when uploading user photo
	//first checks so photo is not larger than 100kb
	$("#fileImage").change(function(){
	fileSize = this.files[0].size;
	if (fileSize > 102400){
		this.setCustomValidity("You must chose an image less than 100KB")
		this.reportValidity();
	}else{
		this.setCustomValidity("");
		showImageThumbnail(this);
	}	
	});
	});
	
	function showImageThumbnail(fileInput){
		var file = fileInput.files[0];
		var reader = new FileReader();
		reader.onload = function(e){
			$("#thumbnail").attr("src", e.target.result);
		};
		reader.readAsDataURL(file);
	}
		
	
		function showModalDialog(title, message){
		$("#modalTitle").text(title);
		$("#modalBody").text(message);
		$("#modalDialog").modal();
	}
	
	function showErrorModal(message){
		showModalDialog("Error",message);
	}
	
	function showWarningModal(message){
		showModalDialog("Warning",message);
	}