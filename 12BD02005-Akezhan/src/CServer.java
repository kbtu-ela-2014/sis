import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class CServer {
  public static void main(String args[]) throws IOException {
    int port = 1123;
    int maxClient = 10;
    clientThread[] threads = new clientThread[maxClient];
    System.out.println("Now using port number=" + port);
    ServerSocket serverSocket = new ServerSocket(port);

    while (true) {
        Socket clientSocket = serverSocket.accept();
        int i;
        for (i = 0; i < maxClient; i++) {
          if(threads[i] == null) {
            (threads[i] = new clientThread(clientSocket, threads)).start();
            break;
          }
        }
        if(i == maxClient) {
          PrintStream os = new PrintStream(clientSocket.getOutputStream());
          os.println("Room is full. Try later.");
          os.close();
          clientSocket.close();
        }
    }
  }
}


class clientThread extends Thread {

  private String clientName = null;
  private DataInputStream is = null;
  private PrintStream os = null;
  private Socket clientSocket = null;
  private final clientThread[] threads;
  private int maxClient;

  public clientThread(Socket clientSocket, clientThread[] threads) {
    this.clientSocket = clientSocket;
    this.threads = threads;
    maxClient = threads.length;
  }

  public void run() {	 	 
    clientThread[] threads = this.threads;

    try {
      is = new DataInputStream(clientSocket.getInputStream());
      os = new PrintStream(clientSocket.getOutputStream());
      os.println("Enter your clientName.");
      clientName = is.readLine().trim();

      os.println("Welcome " + clientName + " to chat. \nTo leave enter '/quit'.");
      synchronized (this) {
        for (int i = 0; i < maxClient; i++) {
          if (threads[i]!= null && threads[i]!= this) {
            threads[i].os.println("*** A new user " + clientName + " joined the room !!! ***");
          }
        }
      }
      
      
      while (true) {
        String mesg = is.readLine();
        if (mesg.startsWith("/quit"))  break;
        else {
          synchronized (this) {
            for (int i = 0; i < maxClient; i++) {
              if (threads[i]!= null && threads[i].clientName!= null) threads[i].os.println("<" + clientName + "> " + mesg);
            }
          }
        }
      }
      
      
      synchronized (this) {
        for (int i = 0; i < maxClient; i++) {
          if (threads[i]!= null && threads[i]!= this && threads[i].clientName!= null) {
            threads[i].os.println("*** The user " + clientName + " is leaving the  room !!! ***");
          }
        }
      }
      os.println("*** Bye " + clientName + " ***");
      synchronized (this) {
        for (int i = 0; i < maxClient; i++) {
          if (threads[i] == this) threads[i] = null;
        }
      }
      is.close();
      os.close();
      clientSocket.close();
    } catch (IOException e) {
    	System.out.println(e.toString());
    }
  }
}