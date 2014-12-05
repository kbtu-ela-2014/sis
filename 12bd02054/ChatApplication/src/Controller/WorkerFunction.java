package Controller;

import ionic.Msmq.Message;
import ionic.Msmq.MessageQueueException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import View.TemplateView;
import View.ViewForRecord;


public class WorkerFunction {
	static String templ;
	public static Message getMes() throws UnsupportedEncodingException, MessageQueueException{
		Channel ch = new Channel("queue3"); 
		ch.open();
		Message msg = ch.receive();
		ch.close();
		
		return msg;
	}
	
	public static void sendMes(Message msg) throws UnsupportedEncodingException{
		Channel ch = new Channel("queue4"); 
		ch.open();
		ch.send(msg);
		ch.close();
	}
	
	public static String forAuthorization(Vector<String> data) throws IOException{
		String response = "";
		templ = ReadFunction.readFromFile("templ.html");
		if(Authorization.canUserLog(data)){
    		templ = TemplateView.patternForRegUser(templ);
    		response = templ.replace("%content%", "");
    	}
    	else response = templ.replace("%content%", "bad password");
    	 response = TemplateView.patternForRegUser(response);
         
    	 return response;
	}  
	public static String forRegistration(Vector<String> data) throws IOException{
		String response = "" ;
		templ = ReadFunction.readFromFile("templ.html");
		if(DbActions.addUser(data)) {               
        	response = TemplateView.patternForRegPage(templ);
   
        	}
        	else 
        		response = TemplateView.patternForBadReg(templ);
        	
        	 response = TemplateView.patternForRegUser(response);
        	 return response;
	}
	
	public static String forBlog(Vector<String> data, int roomID) throws IOException{
		String response = "";
		String messages = "";
		templ = ReadFunction.readFromFile("templ.html");
		DbActions.addRecord(data, roomID);
		String newContent = ReadFunction.readFromFile("blog.html");
		templ = templ.replace("%content%", newContent);
		
		messages = ViewForRecord.viewForRec(DbActions.getIMessagesHistory(roomID));
		templ = TemplateView.patternForRegUser(templ);
		response = templ.replace("%blog%", messages);
		
    	 return response;
	}
	
	public static String forBlog(int roomID) throws IOException{
		String response = "";
		String messages = "";
		String newContent ="";
		templ = ReadFunction.readFromFile("templ.html");
		if(roomID==1){
			newContent = ReadFunction.readFromFile("preparation.html");
		}
		else 
			newContent = ReadFunction.readFromFile("leisure.html");
		
		templ = templ.replace("%content%", newContent);
		
		messages = ViewForRecord.viewForRec(DbActions.getIMessagesHistory(roomID));
		templ = TemplateView.patternForRegUser(templ);
		response = templ.replace("%blog%", messages);
		
    	 return response;
	}
}
