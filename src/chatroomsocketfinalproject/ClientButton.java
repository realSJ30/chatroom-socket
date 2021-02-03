
package chatroomsocketfinalproject;

import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;

public class ClientButton {
    
    public static void main(String args[]){
        
        JFrame frame = new JFrame("Add Client");
        JButton butt = new JButton("NEW CLIENT");
        butt.setBackground(Color.black);
        butt.setForeground(Color.white);
        
        frame.setBounds(40, 10, 250, 150);
        frame.add(butt);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        butt.addActionListener(e->{
            
            try {
                Client.main(null);
            } catch (IOException ex) {
                //Logger.getLogger(ClientButton.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        });
        
    }
    
    
    
}
