package Controller;

import ionic.Msmq.MessageQueueException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;


import java.net.URI;
import java.util.Vector;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		  HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
		     
		     server.createContext("/", new Handler());
		        
		     server.setExecutor(null); // creates a default executor
		     server.start(); 
	}

	 static class Handler implements HttpHandler {
		   public void handle(HttpExchange t) throws IOException {
		   	
		   	
		   	
		   	URI uri = t.getRequestURI();
		   	String path = uri.toString();
		       String response = new String();
		       
		       String templ = ReadFunction.readFromFile("templ.html");
		       String view = new String();
		      
		   
		       if(path.equals("/regPage")){
	                 try {
						response = FunHandler.regPage(t);
					} catch (MessageQueueException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}      	
	            
	            	System.out.println(Authorization.user.getLogin()); 
	            }
	            
	            else if(path.equals("/authorization")){
	            	try {
						response = FunHandler.authorization(t);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MessageQueueException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	            else if(path.equals("/preparation")){
	            	try {
						response = FunHandler.forBlog(t, 1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MessageQueueException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	            else if(path.equals("/leisure")){
						try {
							response = FunHandler.forBlog(t, 2);
						} catch (InterruptedException | MessageQueueException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
	            }
	            else if(path.equals("/preparation.html")){
					try {
						response = FunHandler.forBlog(1);
					} catch (InterruptedException | MessageQueueException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	            }
	            else if(path.equals("/leisure.html")){
					try {
						response = FunHandler.forBlog(2);
					} catch (InterruptedException | MessageQueueException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	            }
	            else if(!path.equals("/")) {

	            	response = FunHandler.noSlash(templ, path);
	            }
	            else if(path.equals("/")){
	            	response = FunHandler.withSlash(templ);
	            }
	          
		  
				Headers header = t.getResponseHeaders();	//adding header
				header.add("Content-Type", "text/html; charset=UTF-8");
				
				path = "";
		       t.sendResponseHeaders(200, response.length());
		       OutputStream os = t.getResponseBody();
		       os.write(response.getBytes());
		       os.close();
		   
		}


		}


}
