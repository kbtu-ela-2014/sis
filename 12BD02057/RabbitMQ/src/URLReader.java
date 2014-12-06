import java.net.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
import java.io.*;

public class URLReader {
	static HashSet<String> urls=new HashSet<String>();
	public String curDoc="";
	public String urlName;
	public String fileName;
	String newHrefs="";
    public  void read(String initName) throws Exception {
    	urlName=initName;
    	
        URL oracle = new URL(initName);
        fileName="";
        if(initName.contains("https")){
        	fileName=initName.substring(8, initName.length());
        	System.out.println("The name is "+fileName);
        }
        
        if(initName.contains("http")){
        	fileName=initName.substring(7, initName.length());
        }
        BufferedReader in = new BufferedReader(
        new InputStreamReader(oracle.openStream()));
        
        /*
        File file=new File(fileName+".html");
        System.out.println( file.createNewFile());
        */
       // BufferedWriter bw=new BufferedWriter(new FileWriter(file));
        String inputLine;
        while ((inputLine = in.readLine()) != null){
        	  curDoc+=inputLine;	
        }
        in.close();
        //bw.close();
       
        /*
        if(curDoc.contains("href")){
        	curDoc.
        }
        */
    
        	
        	 
            while(curDoc.contains("href=")){
                int begin=curDoc.indexOf("href")+6;
                int end=curDoc.indexOf("\"", begin);
                String href=curDoc.substring(begin, end);
               
                newHrefs+=href+" ";
                curDoc=curDoc.replaceFirst("href", "");
            } 
    }
}