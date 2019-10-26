import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import java.io.File;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
//This method launches the application.
public class Main extends Application{
	FXMLLoader loader;
	public static void main(String[] args) {
		launch(args);
	}

	/*This makes sure that a settings files exists
	**A new one will be created if it does not exist.
	*/
	public void init(){
		try{
			File file=new File("c:/EasyShare/setting/dir.es");
			if(!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();

				
				}
			}catch (Exception e) {
			}
	}

	//This starts the application.
	@Override
	public void start(Stage stage){
		try{
			stage.initStyle(StageStyle.UNIFIED);
			loader=new FXMLLoader(getClass().getResource("Main.fxml"));
			Parent root=loader.load();
			Scene scene=new Scene(root);
			stage.setScene(scene);
			stage.show();
			stage.setTitle("Easy file share");
			stage.setResizable(false);
			stage.setOnCloseRequest(event->shutdown());

			try{
				Image icon=new Image("0.png");
				stage.getIcons().add(icon);
			}catch (Exception e) {
				
			}


		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	//This ensures that the application shutdown properly
	private void shutdown(){
		try{
			MainController gainCtr=loader.getController();
			gainCtr.isOnline=false;
			if(!gainCtr.sc.equals(null)) {
				gainCtr.sc.close();
			}
			}catch (Exception e) {
					
				}
	}

}