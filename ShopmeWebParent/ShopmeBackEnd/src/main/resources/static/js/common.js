	$(document).ready(function() {
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
		