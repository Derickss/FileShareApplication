import java.net.URL;
import javafx.stage.Stage;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.collections.*;
import javafx.scene.input.DragEvent;
import javafx.scene.Node;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.scene.Parent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.nio.channels.SocketChannel;
import javafx.fxml.FXMLLoader;
import java.util.*;
import javafx.beans.*;
import javafx.beans.property.*;
import java.io.*;
import javafx.scene.image.Image;
import java.util.function.*;



public class MainController {
    //variables from the fxml files
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox main;

    @FXML
    private VBox vHm;

    @FXML
    private AnchorPane aboutRoot;

    @FXML
    private VBox vNet;
    
    @FXML
    private VBox VUpload;

    @FXML
    private VBox VDownload;

    @FXML
    private VBox vSettings;

    @FXML
    private VBox Vselect;

    @FXML
    private VBox main2;

    @FXML
    private AnchorPane main3;

    @FXML
    private Label mb1;

    @FXML
    private Label mb2;

    @FXML
    private AnchorPane homeRoot;

    @FXML
    private ImageView cd_dir;

    @FXML
    private CheckBox kpOnTop;

    @FXML
    private ColorPicker theme;

    @FXML
    private ImageView helpImageView;

    @FXML
    private ImageView help;


    @FXML
    private AnchorPane hm2;

    @FXML
    private ImageView froFile;

    @FXML
    private ProgressBar toPb;

    @FXML
    private ProgressBar froPb;


    @FXML
    private Label toName;

    @FXML
    private Label toSize;

    @FXML
    private ImageView toFile;

    @FXML
    private Label froName;

    @FXML
    private Label froSize;

    @FXML
    private StackPane rootG;

    @FXML
    private Button nextG;

    @FXML
    private Button previousG;

    @FXML
    private Button closeG;

    @FXML
    private ImageView toWifi;

    @FXML
    private ImageView froWifi;

    @FXML
    private AnchorPane connRoot;

    @FXML
    private AnchorPane upRoot;

    @FXML
    private ListView<String> upView;

    @FXML
    private AnchorPane settingsRoot;

    @FXML
    private AnchorPane downloadRoot;

    @FXML
    private ListView<String> downView;

    @FXML
    private Button remove_btn;

    @FXML
    private Button removeAll_btn;

    @FXML
    private Button send_btn;


    //Variables for the Controller class
    protected ArrayList<File> fileList;
    protected ArrayList<File> downloadList;             
    protected SocketChannel sc;
    int n;
    String folder;
    Stage window;
    Net1 network;
    boolean isOnline;
    ConfirmConn confirmCtr;
    FadeTransition fade1;
    FadeTransition fade2;
    
    @FXML//creating a list for storing files
    private void initialize(){
        fileList=new ArrayList<>();
        downloadList=new ArrayList<>();
    }

    @FXML//showing the home scene
    void showHome(MouseEvent event) {
        homeRoot.setVisible(true);
        upRoot.setVisible(false);
        aboutRoot.setVisible(false);
        settingsRoot.setVisible(false);
        downloadRoot.setVisible(false);
    }

    @FXML//showing the setting scene
    void showSettings(MouseEvent event) {
        homeRoot.setVisible(false);
        aboutRoot.setVisible(false);
        upRoot.setVisible(false);
        settingsRoot.setVisible(true);
        downloadRoot.setVisible(false);
    }

    @FXML//show the upload scene
    void showUpload(MouseEvent event) {
        homeRoot.setVisible(false);
        upRoot.setVisible(true);
        aboutRoot.setVisible(false);
        settingsRoot.setVisible(false);
        downloadRoot.setVisible(false);
    }

    @FXML//showing the downlaod scene
    void showdownload(MouseEvent event) {
        homeRoot.setVisible(false);
        upRoot.setVisible(false);
        aboutRoot.setVisible(false);
        settingsRoot.setVisible(false);
        downloadRoot.setVisible(true);
    }



    //Returns the list of files to be sent
    @FXML
    void getFile(MouseEvent event) {
        upView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        FileChooser fch=new FileChooser();
        fch.setTitle("Select files");
        
        Consumer<File> cons=f->{
            upView.getItems().add(f.getName()+"     "+(f.length()/1000000)+"MB");
            };

        List<File> list=null;
        list=fch.showOpenMultipleDialog((Stage)((Node)event.getSource()).getScene().getWindow());
        if(list==null)return;
        fileList.addAll(list);
        if(list!=null){
            list.forEach(cons::accept);
        }else{
            return;
        }
        //list.clear();
        upRoot.setVisible(true);
    }

    //These method change between scene
    //This will also define a socketChannel to be used for communication.
    @FXML
    void connect(MouseEvent event){
        try{
                homeRoot.setVisible(true);
                upRoot.setVisible(false);
                aboutRoot.setVisible(false);
                settingsRoot.setVisible(false);
                downloadRoot.setVisible(false);
                
                /*The code below will define the return the socketchannel from the 
                created in the second window.
                */
                if(sc!=null){sc.close();}
                FXMLLoader loader =new FXMLLoader(getClass().getResource("ConnWindow.fxml"));
                Parent root=loader.load();
                confirmCtr=loader.getController();
                if(isOnline){
                	confirmCtr.isConnected=true;
                	
                }
                window=new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setScene(new Scene(root));
                window.setResizable(false);
                window.setAlwaysOnTop(true);
                window.showAndWait();
                n=0;
                
                sc=confirmCtr.getSocketChannel();
                isOnline=confirmCtr.isConnected;
                network=new Net1(sc);
                network.dir=saveToFolder();
                try{toPb.progressProperty().bind(network.ratioS);}catch(Exception e){}
                    
                Runnable r=()->{
                    downladFiles();
                };
                Thread td_download=new Thread(r);
                td_download.setDaemon(true);
                td_download.start();

                froPb.progressProperty().addListener(ev->{
                    if(froPb.getProgress()==1){
                        fade2.pause();
                    }
                });

        }catch (Exception e) {
            //System.out.println("Connect method error"+e);
        }
    }

    //Shows the progress of the files being sent or recieved
    void viewState(){
            try{froPb.setVisible(true);
                mb2.setVisible(true);
                froPb.progressProperty().bind(network.ratioR);}catch(Exception e){}
            try{
                froName.textProperty().bind(network.nameP);}catch(Exception e){}
            try{
                network.sizeP.addListener(ev->{froSize.setText(""+network.sizeP.getValue());});
            }catch (Exception e) {}

            try{
                if(n++>0)
                    downView.getItems().add(froName.getText());
            }catch (Exception e) {}
    }

 
    //this loads up the location of where the files are to be saved
    String saveToFolder(){
        try{
            BufferedReader br=new BufferedReader(new FileReader("c:/EasyShare/setting/dir.es"));
            folder=br.readLine();
            if(folder==null){
            	DirectoryChooser dirChr=new DirectoryChooser();
            	dirChr.setTitle("Select Folder where to save to recieved files");
            	folder= dirChr.showDialog(window).getPath()+"\\";
            	PrintWriter pw=new PrintWriter("c:/EasyShare/setting/dir.es");
            	pw.println(folder);
            	pw.close();
            }
        }catch (Exception e) {
            //System.out.println("saveToFolder error"+e);
        }
        return folder;
    }

 

    //this method is used to send the selected file.
    @FXML
    void upLoadFiles(ActionEvent event) {
        if(!isOnline)return;
        if(fileList.isEmpty())return;
        //Code to upload files to the other file
        upRoot.setVisible(false);
        homeRoot.setVisible(true);
        try{toPb.progressProperty().bind(network.ratioS);}catch(Exception e){}
        Consumer<File> consumer=f->{
            try{Platform.runLater(()->{
            	toPb.setVisible(true);
                mb1.setVisible(true);
            });}catch(Exception e){}
            Platform.runLater(()->toName.setText(f.getName()));
            Platform.runLater(()->toSize.setText(""+((long)f.length()/1000000))); 
            try{
                network.send(f);
            }catch(Exception e){}
            //Platform.runLater(()->upView.getItems().remove(f.getName())); //this will remove all sent items from the list
                  
        };
        Runnable r=()->{
            try{fade1=animate(toWifi);}catch(Exception e){}
            fileList.forEach(consumer::accept);
            fileList.clear();
            Platform.runLater(()->upView.getItems().clear());
            try{fade1.stop();}catch(Exception e){}
        };
        
        Thread td_up=new Thread(r);
        td_up.setDaemon(true);
        td_up.start();
    }


    //this method is used to recieve the sent files.
    private void downladFiles(){
        while(isOnline){
            try{fade2=animate(froWifi);}catch(Exception e){}//this'':::::::::::::::::::::::::::::::
            try{Platform.runLater(()->{viewState();});
                }catch(Exception e){}
            try{isOnline=network.receive();}catch(Exception e){
                //code for handling the recieve file errors
                //System.out.println("download errors"+e);
            }
        }
    }


    @FXML//this changes the folder where the files are saved
    void cd_directory(MouseEvent event) {
			try{        
				DirectoryChooser dirChr=new DirectoryChooser();
				dirChr.setTitle("change where to save your files");
				String newFolder= dirChr.showDialog((Stage)((Node)event.getSource()).getScene().getWindow()).getPath()+"\\";
                if(newFolder.contains("null"))return;
				PrintWriter pw=new PrintWriter("c:/EasyShare/setting/dir.es");
				pw.println(newFolder);
				pw.close();   
                if(network.dir.equals(null)){
                }else{network.dir=newFolder;}
			}catch (Exception e) {}
    }

    @FXML//Used code is used to change the theme of the application.
    void changeTheme(ActionEvent event) {
    	Color color=theme.getValue();
    	settingsRoot.setStyle("-fx-background-color:"+"#"+color.toString().substring(2));
    	homeRoot.setStyle("-fx-background-color:"+"#"+color.toString().substring(2));
    	upRoot.setStyle("-fx-background-color:"+"#"+color.toString().substring(2));
    	downloadRoot.setStyle("-fx-background-color:"+"#"+color.toString().substring(2));


    }

 //::::::::::::::::Help picture handling:::::::::::::::::
    List<File> hList;
    int img;

    @FXML//this will show help images 
    void getHelp(MouseEvent event) throws Exception{
        img=0;
        homeRoot.setVisible(false);
        upRoot.setVisible(false);
        settingsRoot.setVisible(false);
        downloadRoot.setVisible(false);
        rootG.setVisible(true);
        aboutRoot.setVisible(false);
        previousG.setVisible(false);
        //Defining the location of the help images
        URL url=MainController.class.getResource("help-icon");
        File hImage=new File(url.toURI());
        hList=Arrays.asList(hImage.listFiles());
    }

    //Handling the pictures for the help menue.
    
    @FXML//this shows the previous image::
    void previousG(ActionEvent event)throws Exception {
        helpImageView.setImage((new Image((hList.get(--img)).toURI().toString())));
        if(img==0){
            previousG.setVisible(false);
        }
    }


    @FXML//This showing the next image:::::::::::::::::::::::::::::::::::::::::::::::::::::
    void nextG(ActionEvent event) throws Exception{
        previousG.setVisible(true);
        helpImageView.setImage((new Image((hList.get(++img)).toURI().toString())));
        if(img==7){
                    homeRoot.setVisible(true);
                    upRoot.setVisible(false);
                    settingsRoot.setVisible(false);
                    aboutRoot.setVisible(false);
                    downloadRoot.setVisible(false);
                    rootG.setVisible(false);
        }
    }

    @FXML//this closes the help scene:::::::::::::::::::::::::::::::::::::::::::::::::::::::
    void closeG(ActionEvent event) throws Exception{
        homeRoot.setVisible(true);
        upRoot.setVisible(false);
        settingsRoot.setVisible(false);
        downloadRoot.setVisible(false);
        aboutRoot.setVisible(false);
        rootG.setVisible(false);
        helpImageView.setImage((new Image((hList.get(0)).toURI().toString())));
        img=0;

    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @FXML//this code keeps the application on top of all other application.
    void keepOnTop(ActionEvent event) {
    	if(kpOnTop.isSelected()){
    		((Stage)((Node)event.getSource()).getScene().getWindow()).setAlwaysOnTop(true);
    	}else{((Stage)((Node)event.getSource()).getScene().getWindow()).setAlwaysOnTop(false);}
    }

    //the animation of on the home scene are controlled by this code::::::::::::::::::::::::::
    FadeTransition animate(Node node){
        Runnable r=()->{
                  FadeTransition ft=new FadeTransition(Duration.seconds(0.1),node);
                  fade1=ft;
                  ft.setFromValue(1.0);
                  ft.setToValue(0.0);
                  ft.setCycleCount(FadeTransition.INDEFINITE);
                  ft.setAutoReverse(true);
                  ft.play();};
                  (new Thread(r)).start();
        return fade1;
    }

    //this is for handling files moved over the window.
    @FXML//This code enables drag events:::::::::::::::::::::::::::::::::::::::::::::::::::::
    void handleDragOver(DragEvent event) {
        if(event.getDragboard().hasFiles()){
                event.acceptTransferModes(TransferMode.ANY);
            }
    }

    @FXML//this code adds dragged files to the list of files to be sent.
    void handleDragDropped(DragEvent event) {
        ArrayList<File> list2=(ArrayList<File>)event.getDragboard().getFiles();
        fileList.addAll(list2);
        list2.forEach(f->{
            upView.getItems().add(f.getName());
        });
        //list2.clear();
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @FXML//This will clear all file to be uploaded
    void removeAll(ActionEvent event) {
        //code to remove all files not selected
        upView.getItems().clear();
        fileList.clear();
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @FXML
    void closeAbout(ActionEvent event) {
        aboutRoot.setVisible(false);
        homeRoot.setVisible(true);
        upRoot.setVisible(false);
        settingsRoot.setVisible(false);
        downloadRoot.setVisible(false);
    }

    @FXML
    void openAbout(MouseEvent event) {
        aboutRoot.setVisible(true);
        homeRoot.setVisible(false);
        upRoot.setVisible(false);
        settingsRoot.setVisible(false);
        downloadRoot.setVisible(false);
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
}
