import java.util.StringTokenizer;
import java.util.Vector;
import java.net.*;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
  
public class Router {

  private static final String TASK_QUEUE_NAME = "router";
  public static String mainURL="";
  public static boolean OK=false;
  public static void main(String[] argv) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    
    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    
    channel.basicQos(1);
    String queue="";
    QueueingConsumer consumer = new QueueingConsumer(channel);
    channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
   
    while (true) {
      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
      String message = new String(delivery.getBody());
      
      System.out.println("Received '" + message + "'");
      //
      StringTokenizer st=new StringTokenizer(message, " ");
      String task="";
      String body="";
      String urlName="";
      int cnt=0;
      while(st.hasMoreTokens()){
    	  if(cnt==0){
    		  task=st.nextToken();
    		 
    		  cnt++;
    	  }else{
    		 
    		  if(cnt == 1 && task.equals("ans")) {
    			  body=message.substring(4);
    			  break;
    		  }
    		  body=st.nextToken();
    	  }
      }
      if(task.equals("read")){
    	 queue="readURL";
    	 channel.queueDeclare("readURL", true, false, false, null);
    	 StringTokenizer st2=new StringTokenizer(message," ");
    	 while(st2.hasMoreTokens()){
    		 String temp=st2.nextToken();
    		 if(temp.contains("http")){
    			 System.out.println("Sdfghjk");
    			 if(OK==false){
    				 mainURL=temp;
    				 System.out.println("QWETYU "+temp);
    				 OK=true;
    			 }
    		 }	 
    	 }
      }else if(task.equals("exp")){
    	 queue="exp";
      }else if(task.equals("ans")){
    	  queue="answr";
    	  System.out.println("body "+body);
      }
      //asd
      
      channel.queueDeclare("answr", true, false, false, null);
    
      System.out.println(" [x] Sent '" + body + "'"+"q "+queue);
      
      //
      channel.basicPublish( "", queue, 
              MessageProperties.PERSISTENT_TEXT_PLAIN,
              body.getBytes());
      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
      
    }         
 }
  
  
}