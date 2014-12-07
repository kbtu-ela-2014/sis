package sis;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.ShutdownSignalException;

import java.io.IOException;
import java.util.UUID;
    
public class RPCClient {
    
  private Connection connection;
  private Channel channel;
  private String requestQueueName = "rpc_image2";
  private String replyQueueName;
  private QueueingConsumer consumer;
    
  public RPCClient(){
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try {
		connection = factory.newConnection();
		channel = connection.createChannel();

	    replyQueueName = channel.queueDeclare().getQueue(); 
	    consumer = new QueueingConsumer(channel);
	    channel.basicConsume(replyQueueName, true, consumer);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
  }
  
  public String run(String message){     
    String response = null;
    String corrId = UUID.randomUUID().toString();
    
    BasicProperties props = new BasicProperties
                                .Builder()
                                .correlationId(corrId)
                                .replyTo(replyQueueName)
                                .build();
    
    try {
		channel.basicPublish("", requestQueueName, props, message.getBytes());
		while (true) {
		      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		      if (delivery.getProperties().getCorrelationId().equals(corrId)) {
		        response = new String(delivery.getBody(),"UTF-8");
		        break;
		      }
		    }
	} catch (IOException | ShutdownSignalException | ConsumerCancelledException | InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}    

    return response; 
  }
    
  public void close() throws Exception {
    connection.close();
  }
  
  
}

