package View;

import java.io.IOException;



import Controller.Authorization;
import Controller.ReadFunction;

public class TemplateView {
	
	static public String patternForRegUser(String templ) throws IOException{
         	
         	if(!Authorization.user.getLogin().equals("???"))
     		return templ.replace("%user_name%", "Hello, "+Authorization.user.getFirstName()+" "+Authorization.user.getLastName());
        
         		return templ.replace("%user_name%", "");
         }
	
	static public String patternForRegPage(String templ) throws IOException{
		String response = "";
		response = templ.replace("%content%", ReadFunction.readFromFile("regPage.html"));
		return response;
	}
	
	static public String patternForBadReg(String templ) throws IOException{
		String response = "";
		response = templ.replace("%content%", ReadFunction.readFromFile("badReg.html"));
		
		return response;
	}
}

