package imageone;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ImageOne
{
  private JPanel menuPanel;
  private JPanel imagePanel;
  private JFrame menuFrame;
  private JFrame imageFrame;
  private JButton[] buttons = new JButton[16];
  private JLabel photo;
  private BufferedImage originalBuffer;
  private BufferedImage modifiedBuffer;
  private BufferedImage puzzleBuffer;
  private ImageIcon modifiedIcon;
  private int imageWidth;
  private int imageHeight;
  private Point puzzleBlank;
  private Point[][] orig = new Point[3][3];
  private Point[][] shuf = new Point[3][3];
  private MouseListener zoomer;
  private MouseListener puzzler;
  private boolean playing = false;
  private int playButton = 10; private int quitButton = 13;
  private JFileChooser fc = new JFileChooser(".");

   private Connection connection;
	private Channel channel;
	private String requestQueueName = "rpc_queue";
        private String requestQueueType = "rpc_type";
	private String replyQueueName;
	private QueueingConsumer consumer;
  ImageOne() {
    initialize("");
      try {
          connect();
      } catch (IOException ex) {
          Logger.getLogger(ImageOne.class.getName()).log(Level.SEVERE, null, ex);
      }
  }

  ImageOne(String paramString) {
    initialize(paramString);
    try {
          connect();
      } catch (IOException ex) {
          Logger.getLogger(ImageOne.class.getName()).log(Level.SEVERE, null, ex);
      }
  }
public void connect() throws IOException{
        ConnectionFactory factory = new ConnectionFactory();
	factory.setHost("localhost");
	connection = factory.newConnection();
	channel = connection.createChannel();
	
	replyQueueName = channel.queueDeclare().getQueue(); 
        
	consumer = new QueueingConsumer(channel);
	channel.basicConsume(replyQueueName, true, consumer);
    }
  private void initialize(String paramString)
  {
    this.menuFrame = new JFrame("Image Manipulation Menu");

    int i;
    for (i = 0; i < this.buttons.length; i++) {
      this.buttons[i] = new JButton();
      this.buttons[i].setBackground(Color.yellow);
      this.buttons[i].setFont(new Font("Dialog", 0, 24));
      this.buttons[i].addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
          ImageOne.this.performAction(paramAnonymousActionEvent.getActionCommand());
        }

      });
    }

    i = 0;
    this.buttons[i].setBackground(Color.blue);
    this.buttons[i].setText("Load");
    i++;
    this.buttons[i].setBackground(Color.blue);
    this.buttons[i].setText("Save");
    i++;
    this.buttons[(i++)].setText("Flip Vertical");
    this.buttons[(i++)].setText("Flip Horizontal");
    this.buttons[(i++)].setText("Negative");
    this.buttons[(i++)].setText("24 Colors");
    this.buttons[(i++)].setText("Lighten");
    this.buttons[(i++)].setText("Darken");
    this.buttons[(i++)].setText("Rotate 90");
    this.buttons[(i++)].setText("Blur");
    this.buttons[(i++)].setText("Remove Red");
    this.buttons[(i++)].setText("Greyscale");
    this.buttons[(i++)].setText("Add Noise");
    
    this.buttons[i].setBackground(Color.green);
    this.buttons[i].setText("Restore");
    i++;
    this.quitButton = (i++);
    this.buttons[this.quitButton].setBackground(Color.red);
    this.buttons[this.quitButton].setText("Quit");

    this.menuPanel = new JPanel();
    //this.menuPanel.setBackground(Color.black);
    this.menuPanel.setLayout(new GridLayout(this.buttons.length / 2, 2));
    for (int j = 0; j < this.buttons.length; j++) {
      this.menuPanel.add(this.buttons[j]);
    }

    this.menuFrame.getContentPane().add(this.menuPanel, "Center");
    this.menuFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent paramAnonymousWindowEvent) {
        System.exit(0);
      }
    });
    this.menuFrame.pack();

    this.imageFrame = new JFrame("Image");

    int j = 2;

    MediaTracker localMediaTracker = null;
    Image localImage = null;
    while (j >= 0) {
      localMediaTracker = new MediaTracker(this.imageFrame);
      localImage = Toolkit.getDefaultToolkit().getImage(paramString);
      localMediaTracker.addImage(localImage, 0);
      try {
        localMediaTracker.waitForID(0);
      } catch (InterruptedException localInterruptedException) {
        System.err.println(localInterruptedException); System.exit(1);
      }

      if ((localImage.getWidth(null) == -1) && (j > 0)) {
        JOptionPane.showMessageDialog(null, "Image file required.", "Error", 0);

        paramString = getFileName();
        j--;
      } else {
        j = -1;
      }
    }

    if ((j < 0) && (localImage.getWidth(null) == -1)) {
      JOptionPane.showMessageDialog(null, "Image file required.\nExiting.", "Error", 0);

      System.exit(1);
    }

    this.originalBuffer = toBufferedImage(localImage);
    this.modifiedBuffer = toBufferedImage(localImage);

    this.imageWidth = this.originalBuffer.getWidth(null);
    this.imageHeight = this.originalBuffer.getHeight(null);

    this.modifiedIcon = new ImageIcon();
    this.modifiedIcon.setImage(this.modifiedBuffer);

    this.photo = new JLabel(this.modifiedIcon);
    this.photo.setIcon(this.modifiedIcon);

    
    this.photo.addMouseListener(this.zoomer);

    this.imagePanel = new JPanel();
    this.imagePanel.setBackground(Color.black);
    this.imagePanel.add(this.photo);

    this.imageFrame.getContentPane().add(this.imagePanel, "Center");
    this.imageFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent paramAnonymousWindowEvent) {
        System.exit(0);
      }
    });
    this.imageFrame.pack();
    Point localPoint = this.menuFrame.getLocation();
    this.imageFrame.setLocation(localPoint.x + this.menuFrame.getSize().width + 25, localPoint.y);

    this.menuFrame.setLocation(20, 20);
    this.menuFrame.setVisible(true);
    this.imageFrame.setLocation(localPoint.x + this.menuFrame.getSize().width + 25, localPoint.y + 25);
    this.imageFrame.setVisible(true);
  }

  
  public BufferedImage call(String type) throws Exception {  
	String corrId = UUID.randomUUID().toString();
	 BufferedImage bImageFromConvert;
	AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(corrId).type(type).replyTo(replyQueueName).build();
       System.out.print(props.getType());
      
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( this.modifiedBuffer, "jpg", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close(); 
        
	channel.basicPublish("", requestQueueName, props, imageInByte);
        System.out.println("ok");
	while (true) {
	  QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	  if (delivery.getProperties().getCorrelationId().equals(corrId)) {
            InputStream in = new ByteArrayInputStream(delivery.getBody());
             bImageFromConvert = ImageIO.read(in);    
	    break;
	  }
	}
	
	return bImageFromConvert; 
	}
  private String getFileName()
  {
    int i = this.fc.showOpenDialog(this.menuFrame);
    String str = "";
    if (i == 0) {
      File localFile = this.fc.getSelectedFile();
      str = localFile.getAbsolutePath();
    }
    return str;
  }

  private void loadImage() {
    int i = this.fc.showOpenDialog(this.menuFrame);
    if (i == 0) {
      File localFile = this.fc.getSelectedFile();
      String str = localFile.getAbsolutePath();
      
    }
  }

  private boolean loadImage(String paramString)
  {
    int[] arrayOfInt = new int[3];
    JFrame localJFrame = new JFrame(paramString);

    MediaTracker localMediaTracker = new MediaTracker(localJFrame);
    Image localImage = Toolkit.getDefaultToolkit().getImage(paramString);
    localMediaTracker.addImage(localImage, 0);
    try {
      localMediaTracker.waitForID(0);
    } catch (InterruptedException localInterruptedException) {
      System.err.println(localInterruptedException); System.exit(1);
    }

    if (localImage.getWidth(null) == -1) {
      JOptionPane.showMessageDialog(null, "Graphics file format unknown.", "Error", 0);

      return false;
    }

    this.originalBuffer = toBufferedImage(localImage);
    this.modifiedBuffer = toBufferedImage(localImage);
    this.imageWidth = this.modifiedBuffer.getWidth(null);
    this.imageHeight = this.modifiedBuffer.getHeight(null);
    this.modifiedIcon = new ImageIcon();
    this.modifiedIcon.setImage(this.modifiedBuffer);
    this.photo.setIcon(this.modifiedIcon);

    Point localPoint = this.menuFrame.getLocation();
    this.imageFrame.setLocation(this.menuFrame.getSize().width + 25, localPoint.y + 25);

    return true;
  }

  public static void saveAsJPEG(RenderedImage paramRenderedImage, String paramString)
    throws IOException
  {
    String str = paramString;
    if (!str.endsWith(".jpg")) str = new String(paramString + ".jpg");
    FileOutputStream localFileOutputStream = new FileOutputStream(str);
    ImageIO.write(paramRenderedImage, "jpg", localFileOutputStream);
    localFileOutputStream.close();
  }

  public void performAction(String paramString) {
    if (paramString.equals("Remove Red")) {
      if (!this.playing) removeRed(); 
    }
    else if (paramString.equals("Flip Vertical")) {
      if (!this.playing) flipV(); 
    }
    else if (paramString.equals("Flip Horizontal")) {
      if (!this.playing) flipH(); 
    }
    else if (paramString.equals("24 Colors")) {
      if (!this.playing) reduceColor(); 
    }
    else if (paramString.equals("Negative")) {
      if (!this.playing) negative(); 
    }
    else if (paramString.equals("Darken")) {
      if (!this.playing) darken(0.9D); 
    }
    else if (paramString.equals("Lighten")) {
      if (!this.playing) lighten(0.9D); 
    }
    else if (paramString.equals("Rotate 90")) {
      if (!this.playing) rotateCW(); 
    }
    else if (paramString.equals("Blur")) {
      if (!this.playing) blur(); 
    }
    else if (paramString.equals("Greyscale")) {
      if (!this.playing) grey(); 
    }
   
    else if (paramString.equals("Add Noise")) {
      if (!this.playing) noise(20); 
    }
    else if (paramString.equals("Restore")) {
      if (!this.playing) restoreImage(this.originalBuffer); 
    }
    else if (paramString.equals("Quit"))
      System.exit(0);
    else if (paramString.equals("Load")) {
      if (!this.playing)
        loadImage();
    }
    else if (paramString.equals("Save")) {
      if (!this.playing) {
        int i = this.fc.showSaveDialog(this.menuFrame);
        if (i == 0) {
         
          File localFile = this.fc.getSelectedFile();
          String str = localFile.getAbsolutePath();
          if ((!localFile.exists()) || (JOptionPane.showConfirmDialog(null, localFile.getName() + " already exists.\n" + "Are you sure you want to overwrite it?  ") == 0))
          {
            try
            {
              saveAsJPEG(this.modifiedBuffer, str);
              JOptionPane.showMessageDialog(null, "File saved", "Success", -1);
            }
            catch (IOException localIOException)
            {
              localIOException.printStackTrace();
            }
          }
        }
      }
    }
    else System.out.println("Button has no assigned action.");

    this.imageFrame.pack();
  }

  public void restoreImage(BufferedImage paramBufferedImage)
  {
    this.imageWidth = paramBufferedImage.getWidth(null);
    this.imageHeight = paramBufferedImage.getHeight(null);
    this.modifiedBuffer = new BufferedImage(this.imageWidth, this.imageHeight, 1);

    WritableRaster localWritableRaster1 = paramBufferedImage.getRaster();
    WritableRaster localWritableRaster2 = this.modifiedBuffer.getRaster();
    int[] arrayOfInt = new int[3];

    for (int i = 0; i < this.imageWidth; i++) {
      for (int j = 0; j < this.imageHeight; j++) {
        localWritableRaster1.getPixel(i, j, arrayOfInt);
        localWritableRaster2.setPixel(i, j, arrayOfInt);
      }
    }

    this.modifiedIcon = new ImageIcon();
    this.modifiedIcon.setImage(this.modifiedBuffer);
    MouseListener[] arrayOfMouseListener = this.photo.getMouseListeners();
    for (int j = 0; j < arrayOfMouseListener.length; j++) {
      this.photo.removeMouseListener(arrayOfMouseListener[j]);
    }
    this.photo.addMouseListener(this.zoomer);

    this.photo.setIcon(this.modifiedIcon);
  }

  public void removeRed() {
      try {
         this.modifiedBuffer = call("removeRed");
      } catch (Exception ex) {
          Logger.getLogger(ImageOne.class.getName()).log(Level.SEVERE, null, ex);
      }
    this.modifiedIcon = new ImageIcon();
    this.modifiedIcon.setImage(this.modifiedBuffer);
    this.photo.setIcon(this.modifiedIcon);
  }

  public void grey()
  {
     try {
         this.modifiedBuffer = call("grey");
      } catch (Exception ex) {
          Logger.getLogger(ImageOne.class.getName()).log(Level.SEVERE, null, ex);
      }
    this.modifiedIcon = new ImageIcon();
    this.modifiedIcon.setImage(this.modifiedBuffer);
    this.photo.setIcon(this.modifiedIcon);
  }

  public void blur()
  {
  
      try {
         this.modifiedBuffer = call("blur");
      } catch (Exception ex) {
          Logger.getLogger(ImageOne.class.getName()).log(Level.SEVERE, null, ex);
      }
    this.modifiedIcon = new ImageIcon();
    this.modifiedIcon.setImage(this.modifiedBuffer);
    this.photo.setIcon(this.modifiedIcon);
  }

  public void darken(double paramDouble)
  {
      try {
         this.modifiedBuffer = call("darken");
      } catch (Exception ex) {
          Logger.getLogger(ImageOne.class.getName()).log(Level.SEVERE, null, ex);
      }
    this.modifiedIcon = new ImageIcon();
    this.modifiedIcon.setImage(this.modifiedBuffer);
    this.photo.setIcon(this.modifiedIcon);
  }

  public void lighten(double paramDouble) {
   try {
         this.modifiedBuffer = call("lighten");
      } catch (Exception ex) {
          Logger.getLogger(ImageOne.class.getName()).log(Level.SEVERE, null, ex);
      }
    this.modifiedIcon = new ImageIcon();
    this.modifiedIcon.setImage(this.modifiedBuffer);
    this.photo.setIcon(this.modifiedIcon);
  }

  public void noise(int paramInt)
  {
   
    try {
         this.modifiedBuffer = call("noise");
      } catch (Exception ex) {
          Logger.getLogger(ImageOne.class.getName()).log(Level.SEVERE, null, ex);
      }
    this.modifiedIcon = new ImageIcon();
    this.modifiedIcon.setImage(this.modifiedBuffer);
    this.photo.setIcon(this.modifiedIcon);
  }

  public void rotateCW()
  {
    try {
         this.modifiedBuffer = call("rotateCW");
      } catch (Exception ex) {
          Logger.getLogger(ImageOne.class.getName()).log(Level.SEVERE, null, ex);
      }

    this.imageWidth = this.modifiedBuffer.getWidth(null);
    this.imageHeight = this.modifiedBuffer.getHeight(null);
    this.modifiedIcon = new ImageIcon();
    this.modifiedIcon.setImage(this.modifiedBuffer);
    Point localPoint = this.menuFrame.getLocation();
    this.imageFrame.setLocation(localPoint.x + this.menuFrame.getSize().width + 25, localPoint.y + 25);
    this.photo.setIcon(this.modifiedIcon);
  }



  private BufferedImage copyBufferedImage(BufferedImage paramBufferedImage)
  {
    BufferedImage localBufferedImage = new BufferedImage(paramBufferedImage.getWidth(), paramBufferedImage.getHeight(), 1);

    WritableRaster localWritableRaster1 = paramBufferedImage.getRaster();
    WritableRaster localWritableRaster2 = localBufferedImage.getRaster();
    int[] arrayOfInt = new int[3];

    for (int i = 0; i < this.imageWidth; i++) {
      for (int j = 0; j < this.imageHeight; j++) {
        localWritableRaster1.getPixel(i, j, arrayOfInt);
        localWritableRaster2.setPixel(i, j, arrayOfInt);
      }
    }

    return localBufferedImage;
  }



  public void reduceColor()
  {
    try {
         this.modifiedBuffer = call("reduceColor");
      } catch (Exception ex) {
          Logger.getLogger(ImageOne.class.getName()).log(Level.SEVERE, null, ex);
      }

    this.modifiedIcon = new ImageIcon();
    this.modifiedIcon.setImage(this.modifiedBuffer);
    this.photo.setIcon(this.modifiedIcon);
  }

  public void negative() {
     try {
         this.modifiedBuffer = call("negative");
      } catch (Exception ex) {
          Logger.getLogger(ImageOne.class.getName()).log(Level.SEVERE, null, ex);
      }
    this.modifiedIcon = new ImageIcon();
    this.modifiedIcon.setImage(this.modifiedBuffer);
    this.photo.setIcon(this.modifiedIcon);
  }

  public void flipH()
  {
     try {
         this.modifiedBuffer = call("flipH");
      } catch (Exception ex) {
          Logger.getLogger(ImageOne.class.getName()).log(Level.SEVERE, null, ex);
      }
    this.modifiedIcon = new ImageIcon();
    this.modifiedIcon.setImage(this.modifiedBuffer);
    this.photo.setIcon(this.modifiedIcon);
  }

  public void flipV() {
    try {
         this.modifiedBuffer = call("flipV");
      } catch (Exception ex) {
          Logger.getLogger(ImageOne.class.getName()).log(Level.SEVERE, null, ex);
      }
    this.modifiedIcon = new ImageIcon();
    this.modifiedIcon.setImage(this.modifiedBuffer);
    this.photo.setIcon(this.modifiedIcon);
  }

  public static BufferedImage toBufferedImage(Image paramImage)
  {
    if ((paramImage instanceof BufferedImage)) {
      return (BufferedImage)paramImage;
    }

    paramImage = new ImageIcon(paramImage).getImage();

    BufferedImage localBufferedImage = null;
    GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    try
    {
      GraphicsDevice localGraphicsDevice = localGraphicsEnvironment.getDefaultScreenDevice();
      GraphicsConfiguration localGraphicsConfiguration = localGraphicsDevice.getDefaultConfiguration();
      localBufferedImage = localGraphicsConfiguration.createCompatibleImage(paramImage.getWidth(null), paramImage.getHeight(null), 1);
    }
    catch (HeadlessException localHeadlessException)
    {
    }

    if (localBufferedImage == null)
    {
      int i = 1;
      localBufferedImage = new BufferedImage(paramImage.getWidth(null), paramImage.getHeight(null), i);
    }

    Graphics2D localGraphics2D = localBufferedImage.createGraphics();

    localGraphics2D.drawImage(paramImage, 0, 0, null);
    localGraphics2D.dispose();

    return localBufferedImage;
  }

  public static void main(String[] paramArrayOfString)
  {
    ImageOne localImageOne;
    if (paramArrayOfString.length == 1) {
      File localFile = new File(paramArrayOfString[0]);
      if (localFile.exists()) {
        localImageOne = new ImageOne(paramArrayOfString[0]);
      } else {
        System.err.println(paramArrayOfString[0] + ": File not found.");
        System.exit(0);
      }
    } else {
      localImageOne = new ImageOne();
    }
  }
}