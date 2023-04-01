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
		
	
	/*$(document).ready(function() {
			$("#logoutLink").on("click", function(e) {
				e.preventDefault();
				document.logoutForm.submit();
			});
			
				customizeDropDownMenu();
		});
		
		//Function for log out menu sliding down when mouse hover over drop down link 
	function customizeDropDownMenu(){
		$(".navbar .dropdown").hover(
			function(){
				$(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();
			},
			function(){
				$(this).find('.dropdown-menu').first().stop(true, true).delay(100).slideUp();
			//when mouse move away fro droplink - slide up, closse drop down menu
			}
		);
		$(".dropdown > a").click(function(){
			location.href = this.href;
		});
	}
		*/