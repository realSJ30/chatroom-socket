package chatroomsocketfinalproject;


import java.awt.Color;
import java.io.*; 
import java.util.*; 
import java.net.*; 
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;  
import javax.swing.JFrame;
import javax.swing.JPanel;
// Server class 
public class Server  
{ 
    // Vector to store active clients 
    static Vector<ClientHandler> ar = new Vector<>(); 
      
    // counter for clients 
    static int i = 0; 
     
    public static void main(String[] args) throws IOException  
    { 
        // server is listening on port 1234 
        ServerSocket ss = new ServerSocket(1234); 
          
        Socket s;   
        // running infinite loop for getting 
        // client request 
        while (true)  
        {             
            
            // Accept the incoming request 
            s = ss.accept(); 
            System.out.println("New client request received : " + s); 
              
            // obtain input and output streams 
            DataInputStream dis = new DataInputStream(s.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
              
            System.out.println("Creating a new handler for this client..."); 
            //accepts the client name...
            String nme = dis.readUTF();
            // Create a new handler object for handling this request. 
            ClientHandler mtch = new ClientHandler(s,nme, dis, dos); 
            // Create a new Thread with this object. 
            Thread t = new Thread(mtch); 
              
            System.out.println("Adding this client to active client list"); 
  
            // add this client to active clients list 
            ar.add(mtch); 
  
            // start the thread. 
            t.start(); 
  
            // increment i for new client. 
            // i is used for naming only, and can be replaced 
            // by any naming scheme 
            i++; 
  
        } 
    }
    
    
   //creates a class for handling clients 
   static class ClientHandler implements Runnable{

    //Scanner scn = new Scanner(System.in); 
    String name; 
    final DataInputStream dis; 
    final DataOutputStream dos; 
    Socket s; 
    boolean isloggedin; 
      
    
    
    
    // constructor 
    public ClientHandler(Socket s, String name, DataInputStream dis, DataOutputStream dos) { 
        this.dis = dis; 
        this.dos = dos; 
        this.name = name; 
        this.s = s; 
        this.isloggedin=true; 
    } 
  
    @Override
    public void run() { 
  
        String received; 
        while (true){ 
            try{ 
                // receive the string messsage
                received = dis.readUTF(); 
                //System.out.println(s);    for socket port tracing debug nko...
                System.out.println(received);   //prints the message sent by the client to the server side 
                if(String.valueOf(received.charAt(0)).equals("@")){
                    for(ClientHandler mc : Server.ar){
                        if(!mc.isloggedin==false){  //if ktung client is dli pa naglogout apil sya sa send to all
                            if(mc.name==this.name){
                                mc.dos.writeUTF("Me to All : "+received+"\n-sent on: "+ showDateTime());   
                            }else{
                                mc.dos.writeUTF(this.name+" : "+received+"\n-sent on: "+ showDateTime());   
                            }
                                 
                        }
                    }
                }else{
                    // kng naka PM sya...
                    if(received.contains("@")){ 
                        // break the string into message and recipient part 
                        StringTokenizer st = new StringTokenizer(received, "@"); 
                        String MsgToSend = st.nextToken(); 
                        String recipient = st.nextToken(); 
                        // search for the recipient in the connected devices list.
                        // ar is the vector storing client of active users
                        for (ClientHandler mc : Server.ar) {
                        // if the recipient is found, write on its
                        // output stream
                            if(mc.name.equals(recipient) && mc.isloggedin == true){
                                mc.dos.writeUTF(this.name+" : "+MsgToSend+"\n ----sent on: "+ showDateTime());
                            }
                        }
                        for(ClientHandler mc : Server.ar){
                            if(this.name == mc.name){
                                mc.dos.writeUTF("Me to "+recipient+" : "+MsgToSend+"\n ----sent on: "+ showDateTime());
                            }
                        }
                        
                    }else{  //if wala naka pm
                        if(received.equals("logout")){
                            
                            this.isloggedin=false; //logout the current user
                            this.dis.close();
                            this.dos.close();
                            this.s.close(); //socket close

                            break; 
                        }else{
                            for(ClientHandler mc : Server.ar){
                                if(!mc.isloggedin==false){  //if ktung client is dli pa naglogout apil sya sa send to all
                                    if(mc.name==this.name){
                                        mc.dos.writeUTF("Me to All : "+received+"\n-sent on: "+ showDateTime());   
                                    }else{
                                        mc.dos.writeUTF(this.name+" : "+received+"\n-sent on: "+ showDateTime());   
                                    }
                                 
                                }
                            }
                        }                        
                    }
                }                                   
            }catch (IOException e){     
                e.printStackTrace(); 
            } 
              
        } 
        /*try{ 
            // closing resources 
            this.dis.close(); 
            this.dos.close(); 
            this.s.close(); //socket close
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } */
    } 
    }
   //DATE and TIME FORMATTER...
   public static String showDateTime(){
        String value = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
        Date date = new Date();  
        value = formatter.format(date);  
        return value;
    }
} 