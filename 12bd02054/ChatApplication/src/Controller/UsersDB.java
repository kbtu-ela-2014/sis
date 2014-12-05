package Controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import DAO.Queries;
import Model.IMessage;
import Model.User;

public class UsersDB {
	
		public static TreeSet<User> users = Queries.findAll();
	
	
	    public static  boolean isUserExist(User u){
				    	if(users.contains(u)) return true;
	    	return false;
	    } 	
	    
	    public static int findPassword( String login){
			
			User user = new User();
			
			for(User p:UsersDB.users){
				if(p.getLogin().equals(login))
					user = (User)p;
			}
			
			return user.getPassword();
	    }
	    
	    public static byte[] serialize(IMessage im) throws IOException{
	    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    	ObjectOutputStream oos = new ObjectOutputStream(bos);
	    	oos.writeObject(im);
	    	return bos.toByteArray();
	    }
	    public static byte[] serialize(Vector<String> data) throws IOException{
	    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    	ObjectOutputStream oos = new ObjectOutputStream(bos);
	    	oos.writeObject(data);
	    	return bos.toByteArray();
	    }
	    
	    
	    public static IMessage deserializeIM(byte[] serialized) throws IOException, ClassNotFoundException{
	    	ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
	    	ObjectInputStream ois = new ObjectInputStream(bis);
	    	
	    		IMessage im = (IMessage)ois.readObject();
	    	return im;
	    }
	    public static Vector<String> deserialize(byte[] serialized) throws IOException, ClassNotFoundException{
	    	ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
	    	ObjectInputStream ois = new ObjectInputStream(bis);
	    	
	    		Vector<String> data = (Vector<String>)ois.readObject();
	    	return data;
	    }
}
