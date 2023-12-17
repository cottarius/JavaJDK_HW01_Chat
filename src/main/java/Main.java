public class Main {
    public static void main(String[] args) {
        ServerWindow serverWindow = new ServerWindow();
        new ClientGUI(serverWindow, -500);
        new ClientGUI(serverWindow, 500);
    }
}
