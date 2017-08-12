package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
//import java.net.InetSocketAddress;
import java.net.Socket;

public class ChatServerThread extends Thread {
	
	private Socket socket;
	private String nickname;
	
	UserList userList = new UserList();
	
	public ChatServerThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {

		try {
			InputStreamReader isr = new InputStreamReader(socket.getInputStream(), "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
			PrintWriter pw = new PrintWriter(osw, true);
		
			while(true) {
				String request = br.readLine();
				
				if (request == null) {
					System.out.println("User exited");
					break;
				}
				String[] tokens = request.split( ":" );
				
				if (tokens[0].equals("join")) {
					
					if (UserList.add(getId(), tokens[1], pw)) {
						nickname = UserList.findNicknameByID(getId());
						broadcast("******" + nickname + " joined the room." + "******");
					}
					
				} else if (tokens[1].equals("/quit")) {
					socket.close();
					broadcast("******" + tokens[0] + " exited the room." + "******");
				} else if (tokens[1].startsWith("/")) { //private talk to someone
					
					int i = tokens[1].indexOf(' ');
					
					String name = tokens[1].substring(1, i);
					String message = tokens[1].substring(i);
					
					long id = UserList.findIDByNickname(name);
					
					if (id == -1) {
						pw.println("****** User name does not exist. ******");
					}
					else {
						unicast("[ " + tokens[0] + " ] to YOU: " + message, id);
					}
					
				} else {
					broadcast("[ " + tokens[0] + " ] to ALL: " + tokens[1]);
				}
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			UserList.remove(getId());
		}
	}
	
	private void broadcast(String data) {

		synchronized( UserList.listWriters ) {
			for (Writer writer : UserList.listWriters.values() ) {
				PrintWriter printWriter = (PrintWriter)writer;
				printWriter.println( data );
				printWriter.flush();
			}
		}
	}
	
	private void unicast(String data, long id) {

		synchronized( UserList.listWriters ) {
			for (long key : UserList.listWriters.keySet() ) {
				if (id == key) {
					PrintWriter printWriter = (PrintWriter) UserList.listWriters.get(key);
					printWriter.println( data );
					printWriter.flush();
					break;
				}
			}
		}
	}
}
