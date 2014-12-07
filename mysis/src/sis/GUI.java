package sis;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
 
public class GUI extends JFrame {
	RPCClient sender = new RPCClient();
    public GUI() {
        super("Paint");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
         
        panel.add(Box.createVerticalGlue());
 
        final JLabel label = new JLabel("Выбранный файл");
        label.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(label);
 
        panel.add(Box.createRigidArea(new Dimension(10, 10)));
 
        JButton button = new JButton("Показать JFileChooser");
        button.setAlignmentX(CENTER_ALIGNMENT);
        
        final JRadioButton black = new JRadioButton("black", false);
        final JRadioButton flipRight = new JRadioButton("flipRight", false);
        final JRadioButton bright = new JRadioButton("brighter", false);
        panel.add(black);
        panel.add(bright);
        
 
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();            
                int ret = fileopen.showDialog(null, "Открыть файл");               
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = new File(fileopen.getSelectedFile().getAbsolutePath().replace("\\", "/"));
                    
                    FileInputStream imageInFile;
					try {
						imageInFile = new FileInputStream(file);
						byte imageData[] = new byte[(int) file.length()];
		                imageInFile.read(imageData);
		                String imageDataString = encodeImage(imageData);
		                if (black.isSelected()){
		                	imageDataString = "1" +	imageDataString; 
		                }else
		                if (bright.isSelected()){
		                	imageDataString = "2" +	imageDataString;                 	
		                }
		                System.out.println(imageDataString);
			   	         
		                String ans = sender.run(imageDataString);
		                
		                byte[] imageB = decodeImage(ans);
		                
		                InputStream in = new ByteArrayInputStream(imageB);
		                BufferedImage image = ImageIO.read(in);
		                
		                JFrame frame = new JFrame();
		                frame.setSize(600, 600);
		                JLabel label = new JLabel(new ImageIcon(image));
		                frame.add(label);
		                frame.setVisible(true);
		                
					
		                
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                   
         
                   
                    System.out.println(file.getAbsolutePath());
                    label.setText(file.getName());
                }
            }
        });
 
        panel.add(button);
        panel.add(Box.createVerticalGlue());
        getContentPane().add(panel);
 
        setPreferredSize(new Dimension(260, 220));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
 
    public static void main(String[] args) {       
         new GUI();
       
    }
    public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeBase64URLSafeString(imageByteArray);
    }
    public static byte[] decodeImage(String imageDataString) {
        return Base64.decodeBase64(imageDataString);
      }
}