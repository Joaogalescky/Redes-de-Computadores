import java.net.*;
import java.io.*;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader userInput;

    public void start(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        userInput = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Conectado ao servidor!");

        // Thread para ouvir mensagens do servidor
        new Thread(() -> {
            String serverMsg;
            try {
                while ((serverMsg = in.readLine()) != null) {
                    System.out.println(serverMsg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Envia mensagens para o servidor
        String userMsg;
        while ((userMsg = userInput.readLine()) != null) {
            out.println(userMsg);
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.start("127.0.0.1", 6666);
   }
}