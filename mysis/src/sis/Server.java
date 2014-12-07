package sis;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.*;
import java.io.IOException;
import java.io.InputStream;


import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
  




public class Server {
	  
  private static final String TASK_QUEUE_NAME = "image";

  public static void main(String[] argv) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    
    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    
    channel.basicQos(1);
    
    QueueingConsumer consumer = new QueueingConsumer(channel);
    channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
    
    while (true) {
      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
      String message = new String(delivery.getBody());
      
      System.out.println(" [x] Received '" + "image");
      doWork(message);
      //System.out.println(" [x] Answer is: " + doWork(message));

      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    }         
  }
  
  private static void doWork(String message) {
	  System.out.println("1");
	  byte[] imageB = decodeImage(message);
	  
	  InputStream in = new ByteArrayInputStream(imageB);
	  try {
			BufferedImage image = ImageIO.read(in);
			int width = image.getWidth();
	        int height = image.getHeight();
	         for(int i=0; i<height; i++){
	            for(int j=0; j<width; j++){
	               Color c = new Color(image.getRGB(j, i));
	               int red = (int)(c.getRed() * 0.299);
	               int green = (int)(c.getGreen() * 0.587);
	               int blue = (int)(c.getBlue() *0.114);
	               Color newColor = new Color(red+green+blue,
	               red+green+blue,red+green+blue);
	               image.setRGB(j,i,newColor.getRGB());
	               }
	         }
	         File ouptut = new File("C:/Users/Mehrdod/Desktop/grayscale.jpg");
	         System.out.println("wr");
	         ImageIO.write(image, "jpg", ouptut);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
	    	
  }
  public static byte[] decodeImage(String imageDataString) {
      return Base64.decodeBase64(imageDataString);
    }
}