package ClientGUI;

import javax.swing.*;
import Server.*;


public class Client {	
	public static void main(String [] args){		
			String IPServer = "localhost";
                        String[] arguments = new String[] {IPServer};
			new GUI().main(arguments);
	}
}
