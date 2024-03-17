import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandler = new ArrayList<>();
    private Socket socket;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;


    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            // caractor stream - outputStreamWriter
            // byte stream - getOutputStream
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // this is what the client sending to the server
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.clientUserName = bufferedReader.readLine();// read the first line from the client
            clientHandler.add(this);

            broadcastMessage("Server: "+clientUserName+" has joined the chat");

            
        }catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);

        }
        
    }

    


    public void run(){
        String messageFromClient;

        while(socket.isConnected()){
            try{
                messageFromClient= bufferedReader.readLine();
                broadcastMessage(messageFromClient);

            }catch(IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                // when the client live it should break
                break;
            }
        }


    }








    public void broadcastMessage(String messageTosend) {
        // TODO Auto-generated method stub
        for(ClientHandler clientHandler : clientHandler){
            try{
                if(!clientHandler.clientUserName.equals(clientUserName)){
                    clientHandler.bufferedWriter.write(messageTosend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch(IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }
    

    public void removeClinetHandler(){
        clientHandler.remove(this);
        broadcastMessage("Server : "+ clientUserName+" has left the chat");
    }
    
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        
        removeClinetHandler();
        try{
            if(bufferedReader!=null){
                bufferedReader.close();
            }
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
            if(socket!=null){
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
