package sis;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;
  
public class RPCServer {
  
  private static final String RPC_QUEUE_NAME = "rpc_image2";
  
  
    
  public static void main(String[] argv) {
    Connection connection = null;
    Channel channel = null;
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("localhost");
  
      connection = factory.newConnection();
      channel = connection.createChannel();
      
      channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
  
      channel.basicQos(1);
  
      QueueingConsumer consumer = new QueueingConsumer(channel);
      channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
  
      System.out.println(" [x] Awaiting RPC requests");
  
      while (true) {
        String response = null;
        
        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
        
        BasicProperties props = delivery.getProperties();
        BasicProperties replyProps = new BasicProperties
                                         .Builder()
                                         .correlationId(props.getCorrelationId())
                                         .build();
        
        try {
          String message = new String(delivery.getBody(),"UTF-8");
          if (message.charAt(0) == '1'){
        	  message = message.substring(1);
              response = doBlack(message);
          }
          if (message.charAt(0) == '2'){
        	  message = message.substring(1);
              response = doBright(message);
              System.out.println(response);
          }
        }
        catch (Exception e){
          System.out.println(" [.] " + e.toString());
          response = "";
        }
        finally {  
          channel.basicPublish( "", props.getReplyTo(), replyProps, response.getBytes("UTF-8"));
  
          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
      }
    }
    catch  (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (connection != null) {
        try {
          connection.close();
        }
        catch (Exception ignore) {}
      }
    }      		      
  }
  private static String doBlack(String message) {
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
	         ByteArrayOutputStream baos = new ByteArrayOutputStream();
	         ImageIO.write( image, "jpg", baos );
	     	 baos.flush();
	     	 byte[] imageInByte = baos.toByteArray();
	     	 baos.close();
	     	String imageDataString = encodeImage(imageInByte);
	     	//System.out.println(imageDataString);
	     	return imageDataString;
	         /*File ouptut = new File("C:/Users/Mehrdod/Desktop/grayscale2.jpg");
	         System.out.println("wr");
	         ImageIO.write(image, "jpg", ouptut);
			*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return "1";
	  
	    	
  }
  private static String doBright(String message) {
	  System.out.println("2");
	  byte[] imageB = decodeImage(message);	  
	  InputStream in = new ByteArrayInputStream(imageB);
	  
	  try {
			BufferedImage image = ImageIO.read(in);
			int width = image.getWidth();
	        int height = image.getHeight();
	         for(int i=0; i<height; i++){
	            for(int j=0; j<width; j++){
	               Color c = new Color(image.getRGB(j, i));
	               int red = (int)((255-c.getRed())*0.25 +c.getRed());
	               int green = (int)((255- c.getGreen()) * 0.5 +c.getGreen());
	               int blue = (int)((255-c.getBlue())*0.75 + c.getBlue());
	               Color newColor = new Color(red,
	               green,blue);
	               image.setRGB(j,i,newColor.getRGB());
	               }
	         }
	         ByteArrayOutputStream baos = new ByteArrayOutputStream();
	         ImageIO.write( image, "jpg", baos );
	     	 baos.flush();
	     	 byte[] imageInByte = baos.toByteArray();
	     	 baos.close();
	     	String imageDataString = encodeImage(imageInByte);
	     	//System.out.println(imageDataString);
	     	return imageDataString;
	         /*File ouptut = new File("C:/Users/Mehrdod/Desktop/grayscale2.jpg");
	         System.out.println("wr");
	         ImageIO.write(image, "jpg", ouptut);
			*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return "2";
	  
	    	
  }
  public static String encodeImage(byte[] imageByteArray) {
      return Base64.encodeBase64URLSafeString(imageByteArray);
  }
  public static byte[] decodeImage(String imageDataString) {
      return Base64.decodeBase64(imageDataString);
    }
}

