import java.net.*;
import java.io.*;
public class Receiver{
	public static void main(String[] args) {
		try{
			InetAddress address=InetAddress.getLocalHost();
			Socket s=new Socket(address,5555);
			ObjectInputStream in=new ObjectInputStream(s.getInputStream());
			
			


			in.close();
			s.close();
		}catch (Exception e) {
			System.out.println(e);
		}
	}
}