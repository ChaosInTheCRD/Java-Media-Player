package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.*;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import server.MEMEServer;
import server.VideoFile;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.test.basic.PlayerControlsPanel;

public class MEMEClient extends JFrame implements ActionListener {

	List<VideoFile> videoList;
	Socket serverSocket;
	String host = "127.0.0.1";
	int port = 1175;
	ObjectInputStream inputFromServer;
	ObjectOutputStream outputToServer;
	Container contentPane;
	//JComboBox selectionBox;
	String selectedTitle;
	
	//MEMEPlayer memePlayer;
	public String vlcLibraryPath = "vlc-2.0.1";
	public JFrame mainFrame;
	public EmbeddedMediaPlayerComponent mediaPlayerComponent;
	public EmbeddedMediaPlayer mediaPlayer;
	private String selectedFile;

	public MEMEClient() {
		super();
		try {
			//memePlayer=new MEMEPlayer();
			socket();
			getListFromSocket();
			setupGUI();
			playMedia();
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
		JPanel buttonPanel = new JPanel();
		JButton button1 = new JButton("TEST");
		
		setTitle("Video Player");
		setSize(1280, 1024);
		setVisible(true);
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		String[] selectionListData = new String[videoList.size()];
		
		for (int i = 0; i < videoList.size(); i++)
		{
			selectionListData[i] = videoList.get(i).getTitle();
		}
		
//		selectionBox = new JComboBox(selectionListData);
//		selectionBox.setSelectedIndex(0);
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(button1);
		add(buttonPanel, BorderLayout.WEST);
		button1.addActionListener(this);
		validate();
		
		

//		add(selectionBox, BorderLayout.WEST);
//		selectionBox.addActionListener(this);
		
		
		mainFrame = new JFrame();
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcLibraryPath);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		mediaPlayer =  mediaPlayerComponent.getMediaPlayer();
		mainFrame.setContentPane(mediaPlayerComponent);
		
		contentPane.add(mediaPlayerComponent, BorderLayout.CENTER);
		EmbeddedMediaPlayer mediaPlayer = mediaPlayerComponent.getMediaPlayer();
		PlayerControlsPanel controlsPanel = new PlayerControlsPanel(mediaPlayer);
		mainFrame.add(controlsPanel, BorderLayout.SOUTH);

	}

	public void actionPerformed(ActionEvent e) {
		JButton button1 = (JButton) e.getSource();
		selectedTitle = videoList.get(0).getTitle();
		System.out.println("Selected title : " + selectedTitle);
		selectedFile = videoList.get(0).getFilename();
		
		try {
			sendToServer();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	// Designed to send chosen "selected title back to the server
	public void sendToServer() throws IOException
	{
		
		System.out.println("sendToServer - Test");
//		outputToServer = new ObjectOutputStream(serverSocket.getOutputStream());
		System.out.println(selectedFile);
		outputToServer.writeObject(selectedFile);
		
	}
	
	public void socket() throws UnknownHostException, IOException, ClassNotFoundException {
		serverSocket = new Socket(host, port);
		inputFromServer = new ObjectInputStream(serverSocket.getInputStream());
		outputToServer = new ObjectOutputStream(serverSocket.getOutputStream());
	}

	private void getListFromSocket() throws IOException, ClassNotFoundException {
		videoList = (List<VideoFile>) inputFromServer.readObject();
		System.out.println("CLIENT: " + videoList.get(0).getID());
		//serverSocket.close();
	}
	
	public static void main(String[] args) {
		new MEMEClient();
	}
	
	public void playMedia()
	{
	
		String media = "rtp://@127.0.0.1:1175";
		mediaPlayer.playMedia(media);
	}
}
