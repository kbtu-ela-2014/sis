import java.io.IOException;
import java.util.Iterator;



import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
  
public class Worker {

  private static final String TASK_QUEUE_NAME = "readURL";

  public static void main(String[] argv) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    
    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
    
    
    
  //  channel.queueDelete("readURL");
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
   
    channel.basicQos(1);
    
    QueueingConsumer consumer = new QueueingConsumer(channel);
    channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
    while (true) {
      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
      String message = new String(delivery.getBody());
      
      System.out.println(" [x] Received '" + message + "'");
    /*
      channel.queueDelete("readURL"); 
      channel.queueDelete("router");
       channel.queueDelete("answr");
      */
      URLReader reader=new URLReader();
      reader.read(message);
     
      String answer="ans "+reader.fileName+" "+reader.curDoc;
      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    channel.basicPublish( "", "router", 
              MessageProperties.PERSISTENT_TEXT_PLAIN,
              answer.getBytes());

    /*
    channel.basicPublish( "", "readURL", 
            MessageProperties.PERSISTENT_TEXT_PLAIN,
            (reader.newHrefs).getBytes()); 
            */
    		
    }         
  }
  
 
	 static boolean isPrime(Integer num){
		  boolean prime=true;
		  for(int i=2; i*i<=num; i++){
			  System.out.println("dadsa" + num+"a s"+i);
			  if(num%i==0){
				  prime=false;
				  break;
			  }
		  }
		  return prime;
	  
  }
	 void clean(Channel channel){
		 try {
			channel.queueDelete("answr");
			channel.queueDelete("isprime");
		    channel.queueDelete("router");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    
	 }
}