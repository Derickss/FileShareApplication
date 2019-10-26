import java.io.*;
import java.util.*;
import java.nio.*;
import java.nio.file.*;
import java.nio.channels.*;
import javafx.application.Platform;
import javafx.beans.*;
import javafx.beans.property.*;

public class Net2{
	SocketChannel sc;
	String nameR;
	boolean stopR;
	double init;

	protected DoubleProperty ratioS=new SimpleDoubleProperty(0.3);
	protected DoubleProperty ratioR=new SimpleDoubleProperty(0);
	protected StringProperty nameP=new SimpleStringProperty();
	protected LongProperty sizeP=new SimpleLongProperty();
	String dir;
		Net2(SocketChannel sc){
		this.sc=sc;
		try{sc.configureBlocking(true);}catch(Exception e){}
	}

	//This method receives a file then sends it through a socket channnel.
	//it also produces a varying variable that can be bonded to other javafx property
	protected void send(File f)throws Exception{
		 	FileChannel fc=(new FileInputStream(f)).getChannel();
			ByteBuffer buf=ByteBuffer.allocate(1024);
			long size= fc.size();
			buf.putLong(size);			// putting the file size in the baf
			String name=f.getName();
			buf.putInt(name.length());	// putting the file name length
			

	}


	//The receiving method will be working continuesly ensuring that any sent file is received
	protected 
	void recieve(){
				
}