import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
  public static void main(String[] args) throws IOException {
    try {
      BufferedReader scn = new BufferedReader(new InputStreamReader(System.in));

      // getting localhost ip
      InetAddress ip = InetAddress.getByName("localhost");

      // establish the connection with server port 5056
      Socket s = new Socket(ip, 5056);

      // obtaining input and out streams
      DataInputStream dis = new DataInputStream(s.getInputStream());
      DataOutputStream dos = new DataOutputStream(s.getOutputStream());

      while (true) {
        String tosend = "";
        if (scn.ready()) {
          tosend = scn.readLine();
          dos.writeUTF(tosend);
          if (tosend.equals("!q")) {
            System.out.println("Closing this connection : " + s);
            s.close();
            System.out.println("Connection closed");
            break;
          }

        }


        // printing date or time as requested by client
        if (dis.available() > 0) {
          String received = dis.readUTF();
          System.out.println(received);
        }


      }
    } catch(Exception e){
      e.printStackTrace();
    }
  }
}
