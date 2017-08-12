package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClientThread extends Thread {
	
	public static void main(String[] args) {
		new ChatClientThread().start();
	}
	
	private static final String SERVER_IP = "192.168.1.23";
	private static final int SERVER_PORT = 6888;
	
	private Socket socket;

	private String name = null;

	BufferedReader br = null;

	Scanner scanner = new Scanner(System.in);
	
	@Override
	public void run() {
		try {
			socket = new Socket();
			
			InetSocketAddress isa = new InetSocketAddress(SERVER_IP, SERVER_PORT);
			
			socket.connect(isa);
			
			InputStreamReader isr = new InputStreamReader(socket.getInputStream(), "UTF-8");
			BufferedReader br = new BufferedReader(isr);

			OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
			PrintWriter pw = new PrintWriter(osw, true);

			while (name == null) {
				System.out.print("Enter Nickname: ");
				String input = scanner.nextLine();
				pw.println("join:" + input);
				String response = br.readLine();
				String[] tokens = response.split(":");
				if (!tokens[0].equals("NameAlreadyExist")) {
					name = input;
					System.out.println(response);
				} else {
					System.out.println("Nickname already exists.");
				}
			}
			
			ChatWindow clientWindow = new ChatWindow(name, pw);
			clientWindow.show();
			
			while(true) {
				String response = br.readLine();
				clientWindow.showMessage(response);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
			try {
				if(socket != null && !socket.isClosed()) {
					socket.close();
				} 
			}catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
