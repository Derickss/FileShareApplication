import java.net.*;
import java.io.*;
public class Sender{
	public static void main(String[] args) {
		try{
			ServerSocket ss=new ServerSocket(5555);
			Socket s=ss.accept();
			File file=new File("file name");
			FileInputeStream fin=new FileInputeStream(file);
			ObjectOutputStream out=new ObjectOutputStream(s.getOutputStream());
			String fileName=file.getName();
			long size=file.length();

			out.writeObject(fileName);
			out.flush();
			out.writLong(size);
			out.flush();






			fin.close();
			out.close();
			s.close();
		}catch (Exception e) {
			System.out.println(e);
		}
	}
}