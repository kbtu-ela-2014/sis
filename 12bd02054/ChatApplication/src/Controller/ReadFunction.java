package Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.StringTokenizer;
import java.util.Vector;

import com.sun.net.httpserver.HttpExchange;

public class ReadFunction {

	public static String readFromFile(String fileName) throws IOException{
		String response = "";
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = br.readLine();
		while (line != null) {
           response+=line;
           line = br.readLine();
     }
		br.close();
		return response;
	}
	
	public static Vector<String> readUserInput(HttpExchange t) throws IOException {
		 InputStream is = t.getRequestBody();
		 BufferedReader br = new BufferedReader(new InputStreamReader(is));
		 
		 Vector<String> data = new Vector<>();
		 String s;
		 s = br.readLine();
		 
		 s = URLDecoder.decode(s);
		// System.out.println(s);
		 StringTokenizer st = new StringTokenizer(s, "=&");
	    while (st.hasMoreTokens()) {
	   	 data.add(st.nextToken());
	    }
	    return data;
		}
	
	public static void writeTXT(String name, String body){
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileWriter(name),false);
			writer.println(body);
			
		} catch (IOException e) {
			e.printStackTrace();
		}                                                
		writer.close();
	}
}
