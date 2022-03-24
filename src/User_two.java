import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;


public class User_two extends JFrame implements ActionListener{

    JPanel panel1;
    static JTextArea text_area;
    static JTextField text_field;
    JButton button_send;

    static MulticastSocket socket ;
    static SocketAddress address;


    class Reader extends Thread{
        public void run(){
            byte[] read=new byte[512];
            DatagramPacket packet=null ;

            while(true){
                try {
                    packet = new DatagramPacket(read, read.length);
                    socket.receive(packet);
                }catch(Exception e){
                    System.out.println(e);
                }

                String line = new String(packet.getData(),0,packet.getLength());
                text_area.setText(text_area.getText() + "\n" + line);
            }
        }
    }


    public User_two()
    {   panel1 =new JPanel();
        panel1.setLayout(null);
        panel1.setBackground(new Color(8,99,90));
        panel1.setBounds(0,0,460,50);
        add(panel1);

        ImageIcon icon_1 =new ImageIcon(ClassLoader.getSystemResource("icon/1.png"));
        Image image1 = icon_1.getImage().getScaledInstance(40,40,Image.SCALE_DEFAULT);
        ImageIcon icon_1_convert = new ImageIcon(image1);
        JLabel label_1 = new JLabel(icon_1_convert);
        label_1.setBounds(2,4,45,45);
        panel1.add(label_1);

        label_1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent act) {
                System.exit(0);
            }
        });


        JLabel label_2 = new JLabel("College Buddy");
        label_2.setFont(new Font(Font.MONOSPACED,Font.BOLD,20));
        label_2.setBounds(140,2,180,45);
        label_2.setForeground(Color.WHITE);
        panel1.add(label_2);

        ImageIcon icon_2= new ImageIcon(ClassLoader.getSystemResource("icon/3.jpg"));
        Image image_2 = icon_2.getImage().getScaledInstance(40,40,Image.SCALE_DEFAULT);
        ImageIcon icon_2_convert = new ImageIcon(image_2);
        JLabel label_3 = new JLabel(icon_2_convert);
        label_3.setBounds(405,4,45,45);
        panel1.add(label_3);

        text_area= new JTextArea();
        //text_area.setBounds(2,52,458,560);
        //text_area.setBackground(Color.BLUE);
        text_area.setFont(new Font(Font.DIALOG_INPUT,Font.PLAIN,16));
        text_area.setEditable(false);
        text_area.setLineWrap(true);
        text_area.setWrapStyleWord(true);
        JScrollPane scroll_bar =new JScrollPane(text_area);
        scroll_bar.setBounds(2,52,456,604);
        scroll_bar.setBorder(BorderFactory.createEmptyBorder());
        add(scroll_bar);

        text_field= new JTextField();
        text_field.setBounds(2,658,368,40);
        text_field.setFont(new Font(Font.SERIF,Font.PLAIN,17));
        add(text_field);

        button_send= new JButton("SEND");
        button_send.setFont(new Font(Font.MONOSPACED,Font.BOLD,15));
        button_send.setBounds(372,658,85,40);
        button_send.setBackground(new Color(8,99,90));
        button_send.setForeground(Color.WHITE);
        button_send.addActionListener(this);
        add(button_send);


        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        setUndecorated(true);
        setSize(460,700);
        setLocation(480,200);
        setVisible(true);


        address =new InetSocketAddress("228.2.2.2",9876);

        try{
            socket= new MulticastSocket(9876);
            //socket.setLoopbackMode(true);
            NetworkInterface neitf= NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            socket.joinGroup(address,neitf);


            String exit_msg = " ---Avishek join--- ";
            byte[] write = exit_msg.getBytes();
            DatagramPacket data = new DatagramPacket(write, 0, write.length, address);
            socket.send(data);
        }catch(Exception e){
            System.out.println(e);
        }

        Reader read_obj =new Reader();
        read_obj.start();

    }
    public void actionPerformed(ActionEvent act)
    {
        try{

            String out =text_field.getText();
            if("quit".equalsIgnoreCase(out) || "exit".equalsIgnoreCase(out))
            {
                //send left

                String exit_msg = " Avishek left the Group....... ";
                byte[] write = exit_msg.getBytes();
                DatagramPacket data = new DatagramPacket(write, 0, write.length, address);
                socket.send(data);

                System.exit(1);

            }
            if(out.length()!=0) {
                String output = "Avishek : " + out;
                byte[] write = output.getBytes();
                DatagramPacket data = new DatagramPacket(write, 0, write.length, address);
                socket.send(data);
            }
            //send output

            //text_area.setText(text_area.getText()+ "\nMe : "+ out);
            text_field.setText("");

        }catch (Exception e){
            System.out.println(e);
        }

    }
    public static void main(String[] args){
        new User_two().setVisible(true);

    }
}

