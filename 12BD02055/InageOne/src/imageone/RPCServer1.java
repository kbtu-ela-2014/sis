package imageone;
import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
  
public class RPCServer1 {
  
  private static final String RPC_QUEUE_NAME = "rpc_queue";
 
  
 public static BufferedImage removeRed(BufferedImage modifiedBuffer) {
    WritableRaster localWritableRaster = modifiedBuffer.getRaster();
    int[] arrayOfInt = new int[3];
    for (int i = 0; i < modifiedBuffer.getWidth(); i++) {
      for (int j = 0; j < modifiedBuffer.getHeight(); j++) {
        localWritableRaster.getPixel(i, j, arrayOfInt);
        arrayOfInt[0] = 0;
        localWritableRaster.setPixel(i, j, arrayOfInt);
      }
    }
   return modifiedBuffer;
  }
 
 public static BufferedImage grey(BufferedImage modifiedBuffer)
  {
    WritableRaster localWritableRaster = modifiedBuffer.getRaster();
    int[] arrayOfInt = new int[3];

    for (int j = 0; j < modifiedBuffer.getWidth(); j++) {
      for (int k = 0; k < modifiedBuffer.getHeight(); k++) {
        localWritableRaster.getPixel(j, k, arrayOfInt);
        int i = (arrayOfInt[0] + arrayOfInt[1] + arrayOfInt[2]) / 3;
        arrayOfInt[0] = i;
        arrayOfInt[1] = i;
        arrayOfInt[2] = i;
        localWritableRaster.setPixel(j, k, arrayOfInt);
      }
    }
    return modifiedBuffer;
  }
 
 public static BufferedImage blur(BufferedImage modifiedBuffer)
  {
      int width = modifiedBuffer.getWidth(), height = modifiedBuffer.getHeight();
    BufferedImage localBufferedImage = new BufferedImage(width, height, 1);
   
    WritableRaster localWritableRaster1 = modifiedBuffer.getRaster();
    WritableRaster localWritableRaster2 = localBufferedImage.getRaster();
    int[] arrayOfInt1 = new int[3];
    int[] arrayOfInt2 = new int[3];

    int i = 0;
    for (int j = 0; j < width; j++) {
      for (int k = 0; k < height; k++) {
        for (int m = j - 1; m <= j + 1; m++)
          for (int n = k - 1; n <= k + 1; n++)
            try {
              localWritableRaster1.getPixel(m, n, arrayOfInt1);
              i++;
              for (int i1 = 0; i1 < 3; i1++)
                arrayOfInt2[i1] += arrayOfInt1[i1];
            }
            catch (Exception localException)
            {
            }
        for (int m = 0; m < 3; m++) {
          arrayOfInt2[m] /= i;
        }
        i = 0;
        localWritableRaster2.setPixel(j, k, arrayOfInt2);
        for (int m = 0; m < 3; m++) {
          arrayOfInt2[m] = 0;
        }
      }
    }
    return localBufferedImage;
  }
 
 public static BufferedImage  darken(BufferedImage modifiedBuffer)
  {
      Double paramDouble = 0.9D;
    WritableRaster localWritableRaster = modifiedBuffer.getRaster();
    int[] arrayOfInt = new int[3];
    for (int i = 0; i < modifiedBuffer.getWidth(); i++) {
      for (int j = 0; j < modifiedBuffer.getHeight(); j++) {
        localWritableRaster.getPixel(i, j, arrayOfInt);
        for (int k = 0; k < 3; k++) {
          arrayOfInt[k] = ((int)(arrayOfInt[k] * paramDouble));
          if (arrayOfInt[k] < 0) {
            arrayOfInt[k] = 0;
          }
        }
        localWritableRaster.setPixel(i, j, arrayOfInt);
      }
    }
    return modifiedBuffer;
  }
 
 public static BufferedImage  lighten(BufferedImage modifiedBuffer) {
     Double paramDouble = 0.9D;
    WritableRaster localWritableRaster = modifiedBuffer.getRaster();
    int[] arrayOfInt = new int[3];
    for (int i = 0; i < modifiedBuffer.getWidth(); i++) {
      for (int j = 0; j < modifiedBuffer.getHeight(); j++) {
        localWritableRaster.getPixel(i, j, arrayOfInt);
        for (int k = 0; k < 3; k++) {
          arrayOfInt[k] = (255 - (int)((255 - arrayOfInt[k]) * paramDouble));
          if (arrayOfInt[k] < 0) {
            arrayOfInt[k] = 0;
          }
        }
        localWritableRaster.setPixel(i, j, arrayOfInt);
      }
    }
    return modifiedBuffer;
 }
 public static BufferedImage  noise(BufferedImage modifiedBuffer)
  {
      int paramInt = 20;
    Random localRandom = new Random();
    int width = modifiedBuffer.getWidth(), height = modifiedBuffer.getHeight();
    WritableRaster localWritableRaster = modifiedBuffer.getRaster();
    int[] arrayOfInt = new int[3];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        localWritableRaster.getPixel(i, j, arrayOfInt);
        for (int k = 0; k < 3; k++) {
          int m = localRandom.nextInt(paramInt * 2 + 1) - paramInt;
          arrayOfInt[k] += m;
          if (arrayOfInt[k] < 0) arrayOfInt[k] = 0;
          else if (arrayOfInt[k] > 255) arrayOfInt[k] = 255;
        }
        localWritableRaster.setPixel(i, j, arrayOfInt);
      }
    }
    return modifiedBuffer;
  }
 
 public static BufferedImage reduceColor(BufferedImage modifiedBuffer)
  {
    int i = 32;
    WritableRaster localWritableRaster = modifiedBuffer.getRaster();
    int[] arrayOfInt = new int[3];
    for (int j = 0; j < modifiedBuffer.getWidth(); j++) {
      for (int k = 0; k < modifiedBuffer.getHeight(); k++) {
        localWritableRaster.getPixel(j, k, arrayOfInt);
        arrayOfInt[0] = (arrayOfInt[0] / i * i + i / 2);
        arrayOfInt[1] = (arrayOfInt[1] / i * i + i / 2);
        arrayOfInt[2] = (arrayOfInt[2] / i * i + i / 2);
        localWritableRaster.setPixel(j, k, arrayOfInt);
      }
    }
    return modifiedBuffer;
  }
   public static BufferedImage  rotateCW(BufferedImage modifiedBuffer)
  {
      int width = modifiedBuffer.getWidth(), height = modifiedBuffer.getHeight();
    BufferedImage localBufferedImage = new BufferedImage(height, width, 1);

    WritableRaster localWritableRaster1 = localBufferedImage.getRaster();
    WritableRaster localWritableRaster2 = modifiedBuffer.getRaster();
    int[] arrayOfInt = new int[3];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        localWritableRaster2.getPixel(i, j, arrayOfInt);
        localWritableRaster1.setPixel(height - 1 - j, i, arrayOfInt);
      }
    }
    return localBufferedImage;
  }
   
    public static BufferedImage negative(BufferedImage modifiedBuffer) {
    int i = 32;
    WritableRaster localWritableRaster = modifiedBuffer.getRaster();
    int[] arrayOfInt = new int[3];
    for (int j = 0; j < modifiedBuffer.getWidth(); j++) {
      for (int k = 0; k <  modifiedBuffer.getHeight(); k++) {
        localWritableRaster.getPixel(j, k, arrayOfInt);
        arrayOfInt[0] = (255 - arrayOfInt[0]);
        arrayOfInt[1] = (255 - arrayOfInt[1]);
        arrayOfInt[2] = (255 - arrayOfInt[2]);
        localWritableRaster.setPixel(j, k, arrayOfInt);
      }
    }
     return modifiedBuffer;
  }

  public static BufferedImage flipH(BufferedImage modifiedBuffer)
  {
    int width =  modifiedBuffer.getWidth(), height =  modifiedBuffer.getHeight();
    WritableRaster localWritableRaster = modifiedBuffer.getRaster();
    int[] arrayOfInt1 = new int[3];
    int[] arrayOfInt2 = new int[3];

    for (int i = 0; i <width / 2; i++) {
      for (int j = 0; j <  height; j++) {
        localWritableRaster.getPixel(i, j, arrayOfInt1);
        localWritableRaster.getPixel(width - i - 1, j, arrayOfInt2);
        localWritableRaster.setPixel(i, j, arrayOfInt2);
        localWritableRaster.setPixel(width- i - 1, j, arrayOfInt1);
      }
    }
    return modifiedBuffer;
  }

  public static BufferedImage flipV(BufferedImage modifiedBuffer) {
      int width =  modifiedBuffer.getWidth(), height =  modifiedBuffer.getHeight();
    WritableRaster localWritableRaster = modifiedBuffer.getRaster();
    int[] arrayOfInt1 = new int[3];
    int[] arrayOfInt2 = new int[3];
    
    for (int i = 0; i < width; i++) {
      for (int j = 0; j <  height / 2; j++) {
        localWritableRaster.getPixel(i, j, arrayOfInt1);
        localWritableRaster.getPixel(i,  height - j - 1, arrayOfInt2);
        localWritableRaster.setPixel(i, j, arrayOfInt2);
        localWritableRaster.setPixel(i,  height - j - 1, arrayOfInt1);
      }
    }
     return modifiedBuffer;
  }
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
      BufferedImage bImageFromConvert1 = null;
      while (true) {
        String response = null;
        
        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
        
        BasicProperties props = delivery.getProperties();
        BasicProperties replyProps = new BasicProperties
                                         .Builder()
                                         .correlationId(props.getCorrelationId())
                                         .type(props.getType())
                                         .build();
        
        
        
        try {
            InputStream in = new ByteArrayInputStream(delivery.getBody());
            BufferedImage bImageFromConvert = ImageIO.read(in); 
            String type = props.getType();
            System.out.println(type);
          if(type.equals("removeRed"))
              bImageFromConvert1 = removeRed(bImageFromConvert);
          else if(type.equals("grey"))
              bImageFromConvert1 = grey(bImageFromConvert);
          else if(type.equals("blur"))    
              bImageFromConvert1 = blur(bImageFromConvert);
          else if(type.equals("darken"))    
              bImageFromConvert1 = darken(bImageFromConvert);
          else if(type.equals("lighten"))    
              bImageFromConvert1 = lighten(bImageFromConvert);
          else if(type.equals("noise"))    
              bImageFromConvert1 = noise(bImageFromConvert);
          else if(type.equals("rotateCW"))    
              bImageFromConvert1 = rotateCW(bImageFromConvert);
          else if(type.equals("reduceColor"))    
              bImageFromConvert1 = reduceColor(bImageFromConvert);
          else if(type.equals("negative"))    
              bImageFromConvert1 = negative(bImageFromConvert);
          else if(type.equals("flipH"))    
              bImageFromConvert1 = flipH(bImageFromConvert);
          else if(type.equals("flipV"))    
              bImageFromConvert1 = flipV(bImageFromConvert);
          
        }
        catch (Exception e){
          System.out.println(" [.] " + e.toString());
          response = "";
        }
        finally { 
            try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( bImageFromConvert1, "jpg", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();   
          channel.basicPublish( "", props.getReplyTo(), replyProps, imageInByte);
            
          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }catch (Exception e){
          System.out.println(" [.] " + e.toString());
          response = "";
        }
        }
      }
    }
    catch  (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (connection != null) {
        try {
           System.out.println("close");
          connection.close();
        }
        catch (Exception ignore) {}
      }
    }      		      
  }
}
