package server;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class UserList {
	
	static Scanner scanner = new Scanner(System.in);
	
	private static Map<Long, String> nicknameList = new HashMap<Long, String>();
	protected static Map<Long, Writer> listWriters = new HashMap<Long, Writer>();
	
	public static boolean add(long id, String nickname, Writer writer) {

		for (String name : nicknameList.values()) {
			if (nickname.equals(name)) {
				((PrintWriter) writer).println("NameAlreadyExist:Nickname already exists, try with a new nickname.");
				return false;
			}
		}
		
		synchronized(nicknameList) {
			nicknameList.put(id, nickname);
		}
		synchronized(listWriters) {
			listWriters.put(id, writer);
		}
		return true;
	}

	public static void remove(long id) {
		synchronized(nicknameList) {
			nicknameList.remove(id);
		}
		synchronized(listWriters) {
			listWriters.remove(id);
		}
	}
	
	public static String findNicknameByID(long id) {
		return nicknameList.get(id);
	}

	public static long findIDByNickname(String name) {
		for (Entry<Long, String> entry : nicknameList.entrySet()) {
			if (entry.getValue().equals(name)) {
				return entry.getKey();
			}
		}
		return -1;
	}
	

}
