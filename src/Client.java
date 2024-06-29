import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import java.awt.BorderLayout;
import static javax.swing.SwingConstants.CENTER;
public class Client extends JFrame{
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    private JLabel heading=new JLabel("Client Side");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto", Font.PLAIN,20);


    //Constuctor
    public Client(){
        try{
            System.out.println("Sending Request to server");
            socket=new Socket("172.16.1.57",8080);
            System.out.println("Connection done");
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());

            createGUI();
            handelEvents();
            startReading();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handelEvents() {
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10){
                    String contentToSend=messageInput.getText();
                    messageArea.append(("Me : "+contentToSend+"\n"));
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }

            }
        });
    }

    private void createGUI(){
        //Gui Code...
        this.setTitle("Client messanger [END]");
        this.setSize(600,720);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Coding for component..
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("src/clogo.png"));
        heading.setHorizontalTextPosition(CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(CENTER);

        //Setting Frame Layout
        this.setLayout(new BorderLayout());
        //Adding components
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        this.setVisible(true);

    }

    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("reader started..");
            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this,"Server Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    messageArea.append("Server: "+msg+"\n");
                }

            }catch (Exception e){
                System.out.println("Connection Closed");
            }
        };
        new Thread(r1).start();
    }
/*----------- Main Method -------------------*/
    public static void main(String[] Args){
        System.out.println("I am a client");
        new Client();
    }
}
