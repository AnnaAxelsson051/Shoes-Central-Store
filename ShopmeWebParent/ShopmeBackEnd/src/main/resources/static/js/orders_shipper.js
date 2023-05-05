	$(document).ready(function() {
	$(".linkUpdateStatus").on("click", function(e) {
		e.preventDefault();
		link = $(this);
		alert(link.attr("href"));
	});
		});
	
		