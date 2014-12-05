package Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;





import DAO.Queries;
import Model.IMessage;
import Model.User;


public class DbActions {
	public static boolean addUser(Vector<String> data){
		User u1 = new User();
    	
    	u1.setFirstName(data.get(1));
    	u1.setLastName(data.get(3));
    	u1.setLogin(data.get(5)); 
    	u1.setPassword(data.get(7).hashCode());
 	
    	Queries.insertUser(u1.getFirstName(), u1.getLastName(), u1.getLogin(), u1.getPassword());
		return UsersDB.users.add(u1);
	}
	public static ArrayList<IMessage> getIMessagesHistory(int roomID){
		return Queries.getHistory(roomID);
	}
	
	public static void addRecord(Vector<String> data, int roomID){
		IMessage im = new IMessage();
    	
    	im.setBody(data.get(1).replace("+", " "));
    	im.setDate(new Date()); 
    	im.setSender(Authorization.user);
    	im.setRoomID(roomID);
 	
    	
    	if(!im.getSender().getLogin().equals("???"))
    	Queries.insertMes(im);
	}
	
} 
