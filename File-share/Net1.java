import java.io.*;
import java.util.*;
import java.nio.*;
import java.nio.file.*;
import java.nio.file.Paths;
import java.nio.channels.*;
import javafx.application.Platform;
import javafx.beans.*;
import javafx.beans.property.*;

public class Net1{
	SocketChannel sc;
	String nameR;
	boolean stopR;
	double init;
	double cSize;

	//These variable moniotor the operation within the network
	protected DoubleProperty ratioS=new SimpleDoubleProperty(0.0);
	protected DoubleProperty ratioR=new SimpleDoubleProperty(0.0);
	protected StringProperty nameP=new SimpleStringProperty();
	protected LongProperty sizeP=new SimpleLongProperty();
	String dir;//this defines the where the files will be stored.
		Net1(SocketChannel sc){
		this.sc=sc;
		try{sc.configureBlocking(true);}catch(Exception e){}
	}

	//This method receives a file then sends it through a socket channnel.
	//it also produces a varying variable that can be bonded to other javafx property
	protected void send(File f){
		 try{
		 	FileChannel fc=(new FileInputStream(f)).getChannel();
			ByteBuffer buf=ByteBuffer.allocateDirect(1024000);
			//Extracting file infor
			long size= fc.size();
			double incr=1024000.0/size;
			init=0;
			buf.putLong(size);//This line put the fileSize into the buffer
			String name=f.getName();
			buf.putInt(name.length());//This line sends the fileName length
			//Adding the name characters to the buffer
			for(char i:name.toCharArray()){
				buf.putChar(i);
				}
			
			//sending the remaining part of the file.
			int c=0;
			do{
				while(buf.hasRemaining()){
					c=fc.read(buf);
					if(c==-1)
						break;
				}
				buf.flip();
				while(buf.hasRemaining()){
					sc.write(buf);
				}
				ratioS.setValue(init+=incr);
				buf.clear();
				}while(c!=-1);

				fc.close();
			}catch (Exception e) {
				System.out.println("Sending error--> "+e);
				}
	}


	/*The receiving method will be working continuesly ensuring that any sent file is received
	It start be reading form the socket channel. if there is data the it extracts the file size, file name size, file name
	and lastly the file file itself.
	the file name size is used to extract the file name from the sent data.
	The file size is used to avoid adding bytes from another file to the current file. 
	*/
	protected boolean receive(){
		try{
			//Getting size info
			ByteBuffer sBuf=ByteBuffer.allocate(12);//size buffer
			int test=sc.read(sBuf);
			if(test==-1)return true;
			sBuf.flip();
			long size=sBuf.getLong();
			//int noOfChar=sBuf.getInt()*2;
			ByteBuffer nBuf=ByteBuffer.allocate(sBuf.getInt()*2);//No of char buffer
			sc.read(nBuf);
			nBuf.flip();
			//extract the file name
			nameR="";
			while(nBuf.hasRemaining()){
				nameR+=nBuf.getChar();
			}
			sBuf.clear();
			nBuf.clear();
			Platform.runLater(()->sizeP.setValue(size));
			Platform.runLater(()->nameP.setValue(nameR));
			//Getting the actucal file:::::::::::::::::::::::::::
			FileChannel fc=(new FileOutputStream(dir+nameR)).getChannel();

			ByteBuffer fBuf1=ByteBuffer.allocateDirect(1024000);
			int loop=((int)size)/1024000;
			int last=((int)size)%1024000;
			ByteBuffer fBuf2=ByteBuffer.allocateDirect(last);
			//:::::::::::::::::::::::::::::::::
			
			cSize=0;
			double dloop=1024000.0/size;
			double dlast=((double)last)/size;
			//:::::::::::::::::::::::::::::::::
			
			while(loop>0){
				while(fBuf1.hasRemaining())
					sc.read(fBuf1);
			
				fBuf1.flip();
				while(fBuf1.hasRemaining())
					fc.write(fBuf1);

				fBuf1.clear();
				Platform.runLater(()->ratioR.setValue(cSize+=dloop));
				--loop;
			}
			// last file portion
			if(last>0){
				while(fBuf2.hasRemaining())
					sc.read(fBuf2);
				fBuf2.flip();
				while(fBuf2.hasRemaining())
					fc.write(fBuf2);
				fBuf2.clear();
				Platform.runLater(()->ratioR.setValue(cSize+=dlast));
				--last;
			}
			fc.close();
			System.gc();
			return true;
		}catch (Exception e) {
			System.out.println("receiver error-->"+e);
			//try{sc.close();}catch(Exception ee){}
			return false;
		}
	}
}