package Controller;

import ionic.Msmq.Message;
import ionic.Msmq.MessageQueueException;

import java.io.IOException;
import java.util.Vector;

public class Worker2 {

	public static void main(String[] args) throws IOException, MessageQueueException, ClassNotFoundException, InterruptedException {
		// TODO Auto-generated method stub

		String response = "";
		while(true){
			Message msg = WorkerFunction.getMes();
			if(msg!=null){
				if(msg.getLabel().startsWith("authorization")){
					Vector<String> data = UsersDB.deserialize(msg.getBody());
					response = WorkerFunction.forAuthorization(data);
				}
				
				else if(msg.getLabel().startsWith("registration")){
					Vector<String> data = UsersDB.deserialize(msg.getBody());
					response = WorkerFunction.forRegistration(data);
				}
				else if(msg.getLabel().startsWith("blog")){
					Vector<String> data = UsersDB.deserialize(msg.getBody());
					if(msg.getLabel().charAt(4)=='1'){
						response = WorkerFunction.forBlog(data, 1);
					}
					if(msg.getLabel().charAt(4)=='2'){
						response = WorkerFunction.forBlog(data, 2);
					}
				}
				else if(msg.getLabel().startsWith("pblog")){
					if(msg.getLabel().charAt(5)=='1')
						response = WorkerFunction.forBlog(1);
					if(msg.getLabel().charAt(5)=='2')
						response = WorkerFunction.forBlog(2);
				}
				
				Message request = new Message(response, msg.getLabel(), msg.getCorrelationId());
				WorkerFunction.sendMes(request);
				FunHandler.status=true;
				
			}
			
			Thread.sleep(700);
			
		}
	}

}
