
package chatroomsocketfinalproject;
import java.awt.Color;
import java.io.*; 
import java.net.*; 
import java.util.Scanner; 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

  
public class Client
{ 
    final static int ServerPort = 1234; 
  
    public static void main(String args[]) throws UnknownHostException, IOException  
    { 
        Scanner scn = new Scanner(System.in); 
        String name = JOptionPane.showInputDialog(null,"Enter Username: ");
        //                                  FRONT END                                    //
        JFrame frame = new JFrame(name);
        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.gray);
        JTextArea inbox = new JTextArea(15,40);
        JScrollPane jsp = new JScrollPane(inbox,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JTextField txt_msg = new JTextField(25);
        JButton btn_send = new JButton("SEND");
        
        inbox.setEditable(false);
        
        JLabel lbl2 = new JLabel("1. <Message> + '@' + <Recipient Name> to send a PM.");
        JLabel lbl3 = new JLabel("2. Type LOGOUT to exit on chat.");
        
        panel2.add(jsp);
        panel2.add(txt_msg);
        panel2.add(btn_send);
        
        panel2.add(lbl2);
        panel2.add(lbl3);
        txt_msg.setLocation(4, 0);
        frame.add(panel2);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //----------------------------------------------------------------------------//
        
        
        
        
        // getting localhost ip 
        InetAddress ip = InetAddress.getByName("localhost"); 
          
        // establish the connection 
        Socket s = new Socket(ip, ServerPort); 
          
        // obtaining input and out streams 
        DataInputStream dis = new DataInputStream(s.getInputStream()); 
        DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
        
        //set clientName
        //System.out.println("Enter Username: ");
        
       
        dos.writeUTF(name);//send client name to server.../
        
        // sendMessage thread 
        Thread sendMessage = new Thread(new Runnable()  
        { 
            @Override
            public void run() { 
                while (true) { 
  
                    // read the message to deliver.                  
                        //WHEN SEND BUTTON IS CLICKED....
                    btn_send.addActionListener(e->{
                        try {
                            String msg = txt_msg.getText();
                            if(msg.equalsIgnoreCase("logout")){
                                frame.hide();
                            }else{
                                dos.writeUTF(msg);
                            }
                            
                            txt_msg.setText("");
                        } catch (IOException ex) {
                            //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    break;
                } 
            } 
        }); 
          
        // readMessage thread 
        Thread readMessage = new Thread(new Runnable()
        { 
            @Override
            public void run() { 
  
                while (true) { 
                    try { 
                        // read the message sent to this client 
                        String msg = dis.readUTF();
                        inbox.append(msg+"\n");
                        //System.out.println(msg); 
                    } catch (IOException e) { 
                        break;
                    }
                    
                } 
            } 
        }); 
  
         //threads para multitask
        sendMessage.start();
        readMessage.start();
  
    }
   
    
    
  
    
} 
