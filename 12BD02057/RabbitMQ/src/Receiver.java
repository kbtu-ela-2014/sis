import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;

public class Receiver {
  static String ans="";
  static String fileName="";
  static String html="";
  private static final String TASK_QUEUE_NAME = "answr";

  public  static void main(String[] args) throws Exception {
	 
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    
    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
    
    String message="";
    
 
        
    	while(true){
    		 QueueingConsumer consumer = new QueueingConsumer(channel);
    	        System.out.println("QQ!!!b");  
    	        channel.basicConsume("answr", false, consumer);  
    	       //error is here
    	        
    	        	QueueingConsumer.Delivery delivery = consumer.nextDelivery();
    	        	ans = new String(delivery.getBody());
    	        	channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    	       
    	               
    	        fileName=ans.substring(0, ans.indexOf(" ")-1);
    	        System.out.println("SAD AA "+fileName);
    	        html=ans.substring(fileName.length());
    	        File file=new File(fileName+".html");
    	        FileWriter fw=new FileWriter(file);
    	        BufferedWriter bw=new BufferedWriter(fw);
    	        bw.write(html);
    	        bw.close();
    	} 
  
  }
    
}