import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class Server {
    private static List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
    private static Set<String> usedNames = Collections.synchronizedSet(new HashSet<>());
    private static final int PORT = 42000;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciado na porta " + PORT + "...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress());
            new ClientHandler(clientSocket).start();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientName;
        private boolean nameAccepted = false;
        private static final String LINE_END = "\r\n"; // CRLF para maior compatibilidade

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

                // Timeout
                socket.setSoTimeout(30000); // 30 segundos

                // Mensagem inicial
                sendMessage("<HELLO>Bem-vindo ao servidor! Envie seu nome com <NOME>seunome");

                // Processamento do nome
                if (!handleNameRegistration()) {
                    return;
                }

                // Loop principal de mensagens
                processClientMessages();
                
            } catch (SocketTimeoutException ste) {
                handleError("Timeout - Conexão encerrada");
            } catch (IOException e) {
                handleError("Erro de E/S: " + e.getMessage());
            } finally {
                cleanup();
            }
        }

        private void sendMessage(String message) {
            if (out != null) {
                out.print(message + LINE_END);
                out.flush();
            }
        }

        private boolean handleNameRegistration() throws IOException {
            int attempts = 0;
            while (!nameAccepted && attempts++ < 3) {
                String message = readMessage();
                if (message == null) return false;

                if (message.startsWith("<NOME>")) {
                    String proposedName = message.substring(6).trim();
                    if (validateName(proposedName)) {
                        acceptName(proposedName);
                        return true;
                    } else {
                        sendMessage("<NACK>Nome inválido ou em uso. Tente outro.");
                    }
                } else {
                    sendMessage("<ERRO>Comando inicial inválido. Use <NOME>seunome");
                }
            }
            sendMessage("<ERRO>Muitas tentativas falhas. Conexão encerrada.");
            return false;
        }

        private String readMessage() throws IOException {
            StringBuilder message = new StringBuilder();
            int c;
            
            // Leitura de caractere por caractere para lidar com diferentes
            // terminadores de linha (LF, CR, ou CRLF)
            while ((c = in.read()) != -1) {
                if (c == '\n') {
                    break;
                } else if (c == '\r') {
                    // Verificar se o próximo é LF (para CRLF)
                    in.mark(1);
                    int next = in.read();
                    if (next != '\n') {
                        in.reset();
                    }
                    break;
                } else {
                    message.append((char) c);
                }
            }
            String result = message.toString();
            return result.isEmpty() && c == -1 ? null : result;
        }

        private boolean validateName(String name) {
            return name != null && 
                   !name.isEmpty() && 
                   !name.startsWith("<") && 
                   !usedNames.contains(name) &&
                   name.length() <= 20 &&
                   name.matches("^[\\p{L}0-9_ -]+$"); // Aceita caracteres Unicode
        }

        private void acceptName(String name) throws IOException {
            clientName = name;
            usedNames.add(clientName);
            nameAccepted = true;
            
            synchronized (clients) {
                clients.add(this);
            }
            
            sendMessage("<ACK>Nome aceito. Bem-vindo " + clientName + "!");
            broadcastSystemMessage("<NOVO>" + clientName);
            System.out.println("Cliente " + clientName + " conectado (" + socket.getInetAddress() + ")");
        }

        private void processClientMessages() throws IOException {
            String message;
            while ((message = readMessage()) != null) {
                System.out.println("[" + clientName + "] " + message);
                
                if (message.equals("<SAIR>")) {
                    sendMessage("<BYE>Até logo!");
                    break;
                } 
                else if (message.startsWith("<ALL>")) {
                    String content = message.substring(5);
                    broadcastMessage("[" + clientName + "] " + content);
                }
                else {
                    sendMessage("<ERRO>Comando não reconhecido. Use <ALL>mensagem ou <SAIR>");
                }
            }
        }

        private void broadcastMessage(String message) {
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    if (client != this) { // Não enviar de volta para o remetente
                        client.sendMessage(message);
                    }
                }
            }
        }

        private void broadcastSystemMessage(String message) {
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    client.sendMessage(message);
                }
            }
        }

        private void handleError(String errorMsg) {
            System.out.println("Erro com " + (clientName != null ? clientName : socket.getInetAddress()) + ": " + errorMsg);
            sendMessage("<ERRO>" + errorMsg);
        }

        private void cleanup() {
            try {
                if (clientName != null) {
                    synchronized (clients) {
                        clients.remove(this);
                    }
                    usedNames.remove(clientName);
                    broadcastSystemMessage("<SAIU>" + clientName);
                    System.out.println("Cliente " + clientName + " desconectado (" + socket.getInetAddress() + ")");
                }
                if (out != null) out.close();
                if (in != null) in.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("Erro ao limpar recursos: " + e.getMessage());
            }
        }
    }
}
