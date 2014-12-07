import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class CClient2 implements Runnable {

  private static PrintStream os = null;
  private static DataInputStream is = null;
  private static boolean somebool = false;
  
  public static void main(String[] args) throws UnknownHostException, IOException {
    int port = 1123;
    String host = "localhost";
    System.out.println("Now using port = " + port);
    Socket clientSocket = new Socket(host, port);
    BufferedReader inputLine = new BufferedReader(new InputStreamReader(System.in));
    os = new PrintStream(clientSocket.getOutputStream());
    is = new DataInputStream(clientSocket.getInputStream());

    if (clientSocket!= null && os!= null && is!= null) {
      new Thread(new CClient2()).start();
      while (!somebool) os.println(inputLine.readLine().trim());
      os.close();
      is.close();
      clientSocket.close();
    }
  }

  public void run() {
    String responseLine;
    try {
      while ((responseLine = is.readLine())!= null) {
        System.out.println(responseLine);
        if (responseLine.indexOf("*** Bye")!= -1) break;
      }
      somebool = true;
    } catch (IOException e) {
      System.out.println(e);
    }
  }
}