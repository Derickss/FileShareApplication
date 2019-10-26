import java.io.*;
import java.util.*;
import java.net.*;
import javafx.application.Platform;
import javafx.beans.*;
import javafx.beans.property.*;

public class Net{
	Socket socket;
	String nameR;
	boolean stopR;
	double init;

	//These variable moniotor the operation within the network
	protected DoubleProperty ratioS=new SimpleDoubleProperty(0.0);
	protected DoubleProperty ratioR=new SimpleDoubleProperty(0);
	protected StringProperty nameP=new SimpleStringProperty();
	protected LongProperty sizeP=new SimpleLongProperty();
	String dir;//this defines the where the files will be stored.
		Net(Socket socket){
		this.socket=socket;
	}
	// We use this for sending file 
	protected void send(File f) throws Exception{
		 FileInputStream fin=new FileInputStream(f);
		 ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
		 byte[] buf=new byte[1024];
		 int c;
		 // Sending meta file infor
		 out.writeLong(f.length());
		 out.writeObject(f.getName());
		 // Sending the actual file.
		 do{
		 	c=fin.read(buf);
		 	if(c==-1)
		 		break;
		 	out.write(buf);
		 }while(c!=-1);
		 fin.close();
	}
	// This is for receiving files
	protected void recieve(){
		
	}
}