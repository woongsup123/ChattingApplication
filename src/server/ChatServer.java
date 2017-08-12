package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

	private static final int SERVER_PORT = 6888;
	public static UserList userList = new UserList();
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		
		try {
			
			serverSocket = new ServerSocket();
			
			InetAddress inetAddress = InetAddress.getLocalHost();
			
			InetSocketAddress isa = new InetSocketAddress(inetAddress, SERVER_PORT);
			serverSocket.bind(isa);
			
			while(true) {
				Socket socket = serverSocket.accept();
				ChatServerThread cst = new ChatServerThread(socket);
				
				cst.start();
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (serverSocket != null && !serverSocket.isClosed()) {
					serverSocket.close();
				} 
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void log(String msg) {
		System.out.println(msg);
	}
}
