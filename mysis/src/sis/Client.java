package sis;

import java.io.IOException;
import java.util.Scanner;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

public class Client {
  
  private static final String TASK_QUEUE_NAME = "image";

  public void run(String message) {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection;
    
	try {		
		connection = factory.newConnection();
		Channel channel = connection.createChannel();
	    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
	    
	    channel.basicPublish( "", TASK_QUEUE_NAME, 
	                MessageProperties.PERSISTENT_TEXT_PLAIN,
	                message.getBytes());
	    channel.close();
	    connection.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    System.out.println(" [x] Sent '" + "image");
    
   
  }
    

}