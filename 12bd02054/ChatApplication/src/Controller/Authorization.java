package Controller;

import java.util.Vector;

import Model.User;


public class Authorization {
	
	
	public static User user= new User("","","???","");
	{
		user.setLogin("???");
	}
	
	public static boolean canUserLog(Vector<String> data){
		System.out.println(data);
		String login = data.get(1);
		String password = data.get(3);
		if(isSuchUserExist(new User("","",login ,password)) && isCorrectPassword(login, password)){
			
			for(User p:UsersDB.users){
				if(p.getLogin().equals(login))
					user = (User)p;
			}
			
			return true;
		}
		return false;
	}
	
	public static boolean isSuchUserExist(User user){
		if(UsersDB.isUserExist(user)) return true;
		return false;
		
	}
	
	public static boolean isCorrectPassword(String login, String password){
		if(UsersDB.findPassword(login)==password.hashCode()) return true;
		return false;
		
	}
}
