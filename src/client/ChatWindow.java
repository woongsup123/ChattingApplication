package client;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;

public class ChatWindow {

	private Frame frame;
	private Panel panel;
	private Button buttonSend;
	private TextField textField;
	private TextArea textArea;
	private PrintWriter pw = null;
	public ChatWindow(String name, PrintWriter pw) {
		frame = new Frame(name);
		panel = new Panel();
		buttonSend = new Button("Send");
		textField = new TextField();
		textArea = new TextArea(30, 80);
		this.pw = pw;
	}

	public void show() {
		// Button
		buttonSend.setBackground(Color.GRAY);
		buttonSend.setForeground(Color.WHITE);
		buttonSend.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent actionEvent ) {
				sendMessage();
			}
		});
		

		// Textfield
		textField.setColumns(80);
		textField.addKeyListener( new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				char keyCode = e.getKeyChar();
				if (keyCode == KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}
		});

		// Panel
		panel.setBackground(Color.LIGHT_GRAY);
		panel.add(textField);
		panel.add(buttonSend);
		frame.add(BorderLayout.SOUTH, panel);

		// TextArea
		textArea.setEditable(false);
		frame.add(BorderLayout.CENTER, textArea);

		// Frame
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
		frame.pack();
	}
	
	private void sendMessage() {
		pw.println( frame.getTitle()+ ":" + textField.getText());
		textField.setText("");
		textField.requestFocus();
	}
	
	public void showMessage(String message) {

		textArea.append( message );
		textArea.append("\n");

		textField.setText("");
		textField.requestFocus();
	}
}