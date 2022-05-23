import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class ClientHandler extends Thread {
  private final Server listener;
  private final DataInputStream dis;
  private final DataOutputStream dos;
  private final Socket s;
  private final Queue<String> output;
  private String userName;

  public ClientHandler(Server listen, Socket s, DataInputStream in, DataOutputStream out) {
    listener = listen;
    this.s = s;
    dis = in;
    dos = out;
    output = new LinkedList<>();
    userName = "unknown";

  }

  public void run() {
    System.out.println("started thread service");
    String received = "";
    try {
      dos.writeUTF("Enter your name: ");
      userName = dis.readUTF();
      listener.registerUser(this, userName);
    } catch (IOException e) {
      e.printStackTrace();
    }

    while (!received.equalsIgnoreCase("!q")) {

      try {
        while (dis.available() > 0) {
          received = dis.readUTF();
          System.out.println("received: " + received);
          if (received.equalsIgnoreCase("!q")) {
            listener.sendInput(this, " DISCONNECTED");
            this.s.close();
          }
          listener.sendInput(this, received);
        }

        while (!output.isEmpty()) {
          String o = output.poll();
          try {
            dos.writeUTF(o);
          } catch (SocketException e) {
            System.out.println(o);
            e.printStackTrace();
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  public void addOutput(String str) {
    output.offer(str);
  }
}
