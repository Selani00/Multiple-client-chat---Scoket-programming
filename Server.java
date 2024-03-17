import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{

    // responsible for listening for incoming connections and creating a object to handle the connection
    private ServerSocket serverSocket;

    // constructor
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try{

            // waiting for the client to connect
            while(!serverSocket.isClosed()){
                // when the cilent connect the object will return
                Socket socket= serverSocket.accept();
                System.out.println("New Client connected");
                

                // each object in the clientHandler will responsible to communicate with the client
                // emplement the interface Runnable
                ClientHandler clientHandler = new ClientHandler(socket);
                
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch(IOException e){

        }
    }

    public void closeServerSocket(){
        try{
            if(serverSocket!=null){
                serverSocket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
            
        }
    }

    public static void main(String[] args) {
        try{
            // create a server socket
            // server is listing to the client on port 1234
            ServerSocket serverSocket = new ServerSocket(1234);
            Server server = new Server(serverSocket);
            server.startServer();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    
}