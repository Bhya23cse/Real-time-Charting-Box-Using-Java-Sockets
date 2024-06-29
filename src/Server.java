import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import static javax.swing.SwingConstants.CENTER;
public class Server extends JFrame {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    private JLabel heading=new JLabel("Server Side");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto", Font.PLAIN,20);
    //Constructor......
    public Server(){
        try{
            server=new ServerSocket(8080);
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting...");
            socket=server.accept();
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
            createGUI();
            handelEvents();
            startReading();

        }catch (Exception e){
            e.printStackTrace();

        }

    }
    private void handelEvents(){
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
        this.setTitle("Server messanger [END]");
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
                        System.out.println("Client terminated the chat");
                        JOptionPane.showMessageDialog(this,"Client Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    messageArea.append("Client: "+msg+"\n");

                }

            }catch (Exception e){
                System.out.println("Connection Closed");
            }
        };
        new Thread(r1).start();
    }


    public static void main(String[] Args) {
        System.out.println("i am a Server");
        new Server();
    }
}