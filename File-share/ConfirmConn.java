import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.nio.channels.SocketChannel;
import java.net.URL;
import java.io.*;
import java.nio.channels.*;
import java.net.*;
import javafx.application.Platform;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ConfirmConn{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane rootM;

    @FXML
    private AnchorPane rootIntro;

    @FXML
    private RadioButton server;

    @FXML
    private RadioButton client;

    @FXML
    private AnchorPane rootC;

    @FXML
    private Label subnate;

    @FXML
    private TextField ip;

    @FXML
    private Button cont;

    @FXML
    private AnchorPane rootS;

    @FXML
    private Label label_ip;

    @FXML
    private Label last_bit;

    @FXML
    private AnchorPane rootDiscon;

    @FXML
    private Button nxt;

    @FXML
    private Label statusL;

    @FXML
    private Button done;

    @FXML
    private Label c_error;

    //Controller valuables
    boolean value=false;
    Stage window;
    InetAddress address;
    int port=55666;
    SocketChannel sc;
    boolean isConnected;    

    @FXML
    void disconnect(ActionEvent event)throws Exception {
        //Code for disconnecting the socketChannel
        if(!sc.equals(null)){sc.close();}
        rootDiscon.setVisible(false);
        rootIntro.setVisible(true);
        isConnected=false;

    }

    @FXML//Will continue from here
    void doneS(ActionEvent event) {
        try{
            Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
            setSocketChannel(sc);
            isConnected=true;
            window.hide();
        }catch (Exception e) {
            System.out.println(e);
        }
    }

    

    @FXML// Showing the client scene in the second window
    void rClient(ActionEvent event) {
    	rootM.setVisible(false);
    	rootS.setVisible(false);
    	rootC.setVisible(true);
        rootIntro.setVisible(false);
        rootDiscon.setVisible(false);

        /*Generating the subnet mask bits 
        If these bits are similiar both in the server and client instance then the two computers 
        are on the same network.
        */
        subnate.setText(address.getHostAddress().substring(0,address.getHostAddress().lastIndexOf(".")+1));
    }


    //This will show the the server and also start the serversockt
    @FXML// It will show done if the client is connected.
    void rServer(ActionEvent event) {
    	rootM.setVisible(false);
        rootIntro.setVisible(false);
    	rootS.setVisible(true);
    	rootC.setVisible(false);
        rootDiscon.setVisible(false);

        /*Setting up the Ip address and the last ip address bit.
        The server must provide the last bit of its IP address which is 
        added to the first three bits of the client's subnet mask to get the IP address of the server.
        */
        label_ip.setText(address.getHostAddress());
        byte addressbits[]=address.getAddress();
        last_bit.setText(""+addressbits[3]);
        /*The thread below hinders the ServerSocketChannel code from blocking or interferring with
        other the rest of the GUI.  */
        (new Thread(()->{
            try{ServerSocketChannel ssc=ServerSocketChannel.open();
                        ssc.socket().bind(new InetSocketAddress(port));
                        sc=ssc.accept();
                        setSocketChannel(sc);
                        }catch (Exception e) {
                            
                        }
                        Platform.runLater(()->{
                            statusL.setText("CONNECTED");
                            done.setVisible(true);
                            rootS.setCursor(Cursor.HAND);
                        });
        })).start();   
    }


    //Takes you to the server/client scene;
    @FXML
    void next(ActionEvent event) {
        rootIntro.setVisible(false);
        rootM.setVisible(true);
    }


    @FXML// This is for connecting the client to the server using the provided ip address
    void startConnection(ActionEvent event)throws Exception {
    	int i=0;
        try{
            i=Integer.parseInt(ip.getText());
            if(i>254){
                ip.setStyle("-fx-background-color:orange");
                c_error.setText("Enter the number from the other computer");
                return;
                    }
            }catch (Exception e) {
            ip.setStyle("-fx-background-color:red");
            c_error.setText("Error please Enter a number");
            return;
        }

        String completeIp=subnate.getText()+ip.getText();
        //Getting the socketchannel for the client
        SocketAddress remoteAddress=new InetSocketAddress(completeIp,port);
        isConnected=false;

        //This will ensure that the client can also be started before the Server
        while(isConnected==false){
            try{sc=SocketChannel.open(remoteAddress);
                        isConnected=true;
                    }catch (Exception e) {
                    	System.out.println("Client net"+e);
                    return;        
                        }            
        }

        Thread.sleep(2000);
        ((Stage)((Node)event.getSource()).getScene().getWindow()).hide();

        
    }


    @FXML
    void initialize()throws Exception{
        ping();
        if(isConnected){
            rootDiscon.setVisible(true);
        }else{
            rootIntro.setVisible(true);
        }
    }

    @FXML
    void setSocketChannel(SocketChannel sc){
        this.sc=sc;
    }

    @FXML
    SocketChannel getSocketChannel(){
        return sc;
    }

    /*
    **The ping method checks if the computer is online or offline.
    **A computer is offline if its Host address is '127.0.0.1'.
    **
    */
    protected void ping(){
        try{address=InetAddress.getLocalHost();
            if(!address.getHostAddress().equals("127.0.0.1")){
                value=true;
            }
            }catch (Exception e) {
                        System.out.println("error in the pinging"+e);
                    }        
    }

}