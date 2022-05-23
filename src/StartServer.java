public class StartServer {
  public static void main(String[] args) {
    Server serve = new Server(5056);
    serve.run();
  }
}
