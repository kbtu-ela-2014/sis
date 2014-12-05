package View;

import java.util.ArrayList;


import Model.IMessage;



public class ViewForRecord {
	public static String viewForRec(ArrayList<IMessage> message){
		String response = "";
		for(int i=0; i<message.size(); i++ ){
			response += "<p class='mes'><strong style='color:#191943;'>" +message.get(i).getBody()+"</strong>"+",  from:"+
		                 message.get(i).getSender()+", "+message.get(i).getDate()+"</p><br><br>";
		    	
		}
		
		return response;
	}
}
