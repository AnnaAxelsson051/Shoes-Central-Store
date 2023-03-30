import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.User;

import ch.qos.logback.core.model.Model;

@Controller
public class AccountController {
	
	@Autowired
	private UserService service;
	
	//Retrieve data from currently loggen in user
	//set the user obj onto model
	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal ShopmeUserDetails loggedUser, 
			Model model) {
		String email = loggedUser.getUsername();
		User user = service.getByEmail(email);
		model.addAttribute("user",user);
		
		return "account_form";
	}
	
	
	

}
