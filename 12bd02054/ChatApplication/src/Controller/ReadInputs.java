package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URLDecoder;
import java.util.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ReadInputs implements HttpHandler {
	
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

	@Override
	public void handle(HttpExchange arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
}
