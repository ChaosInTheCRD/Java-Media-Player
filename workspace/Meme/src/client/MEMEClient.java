package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import server.MEMEServer;
import server.VideoFile;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class MEMEClient extends JFrame implements ActionListener {

	List<VideoFile> videoList;
	Socket serverSocket;
	String host = "127.0.0.1";
	int port = 1173;
	ObjectInputStream inputFromServer;
	ObjectOutputStream outputToServer;
	Container contentPane;
	JComboBox selectionBox;
	String selectedTitle;
	
	MEMEPlayer memePlayer;

	public MEMEClient() {
		super();
		try {
			memePlayer=new MEMEPlayer();
			socket();
			getListFromSocket();
			setupGUI();
			memePlayer.LibrarySpec();
			memePlayer.getDisplay();
			memePlayer.getPlayer();
			//MEMEServer.start();
		} catch (UnknownHostException e) {
			System.out.println("Don't know about host : " + host);
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("Couldn't open I/O connection : " + host + ":" + port);
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			System.out.println("Class definition not found for incoming object.");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void setupGUI() {
		setTitle("A Client");
		setSize(600, 400);
		setVisible(true);
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		String[] selectionListData = new String[videoList.size()];
		for (int i = 0; i < videoList.size(); i++) {
			selectionListData[i] = videoList.get(i).getTitle();
		}
		selectionBox = new JComboBox(selectionListData);
		selectionBox.setSelectedIndex(0);

		add(selectionBox, BorderLayout.NORTH);
		selectionBox.addActionListener(this);
		validate();

	}

	public void actionPerformed(ActionEvent e) {
		JComboBox comboBox = (JComboBox) e.getSource();
		selectedTitle = (String) comboBox.getSelectedItem();
		System.out.println("Selected title : " + selectedTitle);
			
		
	}
	
	// Designed to send chosen "selected title back to the server
	public void sendToServer() throws IOException
	{
		outputToServer = new ObjectOutputStream(serverSocket.getOutputStream());
		outputToServer.writeObject(selectedTitle);
	}
	
	public void socket() throws UnknownHostException, IOException, ClassNotFoundException {
		serverSocket = new Socket(host, port);
		inputFromServer = new ObjectInputStream(serverSocket.getInputStream());
		

	}

	private void getListFromSocket() throws IOException, ClassNotFoundException {
		videoList = (List<VideoFile>) inputFromServer.readObject();
		System.out.println("CLIENT: " + videoList.get(0).getID());
		serverSocket.close();
	}
	
	public static void main(String[] args) {
		new MEMEClient();
		System.out.println("Hello");
	}
}
