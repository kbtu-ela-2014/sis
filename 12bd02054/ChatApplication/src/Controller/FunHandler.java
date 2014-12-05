package Controller;


import ionic.Msmq.Message;
import ionic.Msmq.MessageQueueException;

import java.io.IOException;
import java.util.Vector;

import View.TemplateView;
import View.ViewForRecord;

import com.sun.net.httpserver.HttpExchange;


public class FunHandler {
	static Channel requests = new Channel("queue3");
	public static boolean status = false;  
	
	public static String regPage(HttpExchange t) throws IOException, MessageQueueException, InterruptedException{
		String response = "" ;
		Vector<String> data = ReadInputs.readUserInput(t);
		System.out.println("it was here///");
		String correlationId = makeCorrelationId();
		//Feedback.updateMesList();
		
		String label = "registration"+correlationId;
		byte[] body = UsersDB.serialize(data);
		Message msg = new Message(body, label, correlationId);
		Message msg2 = null;
		requests.open();
		requests.send(msg);
		System.out.println("it was...");
		requests.close(); 
		//
		
		while(true){
			Thread.sleep(1000);
		msg2 = Feedback.findTheMes(label);
		System.out.println("response = "+msg2.getBodyAsString());
		
			if(msg2!=null){
			System.out.println("have result...");
				response= msg2.getBodyAsString();	
		}
		
            return response;
		}
	}
	
	public static String authorization(HttpExchange t) throws IOException, InterruptedException, MessageQueueException{
		String response = "";
		Vector<String> data = ReadInputs.readUserInput(t);
		System.out.println("it was here///");
		String correlationId = makeCorrelationId();
		String label = "authorization"+correlationId;
		byte[] body = UsersDB.serialize(data);
		Message msg = new Message(body, label, correlationId);
		Message msg2 = null;
		requests.open();
		requests.send(msg);
		System.out.println("it was...");
		requests.close(); 
		//
		
		while(true){
			Thread.sleep(500);
		
		msg2 = Feedback.findTheMes(label);
		System.out.println("response = "+msg2.getBodyAsString());
		
			if(msg2!=null){
			System.out.println("have result...");
				response= msg2.getBodyAsString();	
		}
            return response;
		}
	}
	
	
	public static String forBlog(HttpExchange t, int roomID) throws IOException, InterruptedException, MessageQueueException{
		String response = null;
		Vector<String> data = ReadInputs.readUserInput(t);
		System.out.println("it was here///");
		String correlationId = makeCorrelationId();
		String label = "blog"+roomID+correlationId;
		byte[] body = UsersDB.serialize(data);
		Message msg = new Message(body, label, correlationId);
		Message msg2 = null;
		requests.open();
		requests.send(msg);
		System.out.println("it was...");
		requests.close(); 
		//
		
		while(true){
			Thread.sleep(1000);
		
		msg2 = Feedback.findTheMes(label);
		System.out.println("response = "+msg2.getBodyAsString());
		
			if(msg2!=null){
			System.out.println("have result...");
				response= msg2.getBodyAsString();	
		}
            return response;
		}
	}
	
	public static String forBlog(int roomID) throws IOException, InterruptedException, MessageQueueException{
		String response = null;
		System.out.println("it was here///");
		String correlationId = makeCorrelationId();
		String label = "pblog"+roomID+correlationId;
		String body="";
		Message msg = new Message(body, label, correlationId);
		Message msg2 = null;
		requests.open();
		requests.send(msg);
		System.out.println("it was...");
		requests.close(); 
		while(true){
			Thread.sleep(1000);
		
		msg2 = Feedback.findTheMes(label);
		System.out.println("response = "+msg2.getBodyAsString());
		
			if(msg2!=null){
			System.out.println("have result...");
				response= msg2.getBodyAsString();	
		}
            return response;
		}
	}
	
	
	
	public static String noSlash(String templ,String path) throws IOException{
		String response;
		path=path.substring(1);
		String sub=ReadFunction.readFromFile(path);
		response=templ.replace("%content%", sub);
		 response = TemplateView.patternForRegUser(response);
         
		 return response;
     }
	
	public static String withSlash(String templ) throws IOException{
		String response;		
		String newContent = ReadFunction.readFromFile("1Page.html");
    	response = templ.replace("%content%", newContent);
    	response = TemplateView.patternForRegUser(response);
     
    	 return response;
     }
	public static String makeCorrelationId() throws IOException{
		String correlationId = "";
		//Feedback.updateMesList();
		correlationId = ReadFunction.readFromFile("id.txt");
	
		int id = Integer.parseInt(correlationId)+1;
		correlationId = id+"";              
		System.out.println(correlationId);
		ReadFunction.writeTXT("id.txt", correlationId);	
		return correlationId;
	}
}
