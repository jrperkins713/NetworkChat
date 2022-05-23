import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

public class Server {
  private final HashMap<ClientHandler, String> connections;
  private ServerSocket ss;
  private Socket s;

  public Server(int port) {
    connections = new HashMap<>();
    s = null;
    try {
      ss = new ServerSocket(port);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() {
    while (true) {
      try {
        s = ss.accept();
        System.out.println("new client " + s);
      } catch (IOException e) {
        e.printStackTrace();
      }
      DataInputStream tempIn = null;
      DataOutputStream tempOut = null;
      try {
        tempIn = new DataInputStream(s.getInputStream());
        tempOut = new DataOutputStream(s.getOutputStream());
      } catch (IOException e) {
        e.printStackTrace();
      }

      ClientHandler t = new ClientHandler(this, s, tempIn, tempOut);
      connections.put(t, "new user");
      t.start();
    }
  }

  public void registerUser(ClientHandler client, String name) {
    connections.put(client, name);
    sendInput(client, " CONNECTED");
  }

  public void sendInput(ClientHandler clientHandler, String str) {
    for (ClientHandler client : connections.keySet()) {
      if(!client.equals(clientHandler)) {
        client.addOutput(connections.getOrDefault(clientHandler, "") + ": " + str);
      }
    }
  }
}
