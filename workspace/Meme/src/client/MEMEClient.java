package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
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
import javax.swing.border.Border;
import javax.swing.Box;

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
		ImageIcon Monsters = new ImageIcon("Film_Pics/MonstersInc.jpeg");
		Monsters.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
		ImageIcon Avengers = new ImageIcon("Film_Pics/TheAvengers.jpg");
		ImageIcon Prometheus = new ImageIcon("Film_Pics/Prometheus.jpg");
		ImageIcon MovingMonsters = new ImageIcon("Film_Pics/lalala.gif");
		ImageIcon MovingPrometheus = new ImageIcon("Film_Pics/MovingPrometheus.gif");
		ImageIcon MovingAvengers = new ImageIcon("Film_Pics/MovingAvengers.gif");
		ImageIcon DefaultDescription = new ImageIcon("Film_Pics/DefaultDescription.png");
		ImageIcon MonstersDescription = new ImageIcon("Film_Pics/MonstersDescription.png");
		ImageIcon AvengersDescription = new ImageIcon("Film_Pics/AvengersDescription.png");
		ImageIcon PrometheusDescription = new ImageIcon("Film_Pics/PrometheusDescription.png");
		ImageIcon MonstersPlaying = new ImageIcon("Film_Pics/MonstersPlaying.gif");
		ImageIcon AvengersPlaying = new ImageIcon("Film_Pics/AvengersPlaying.gif");
		ImageIcon PrometheusPlaying = new ImageIcon("Film_Pics/PrometheusPlaying.gif");
		JButton button1 = new JButton(Monsters);
		button1.setActionCommand("1");
		JButton button2 = new JButton(Avengers);
		button2.setActionCommand("2");
		JButton button3 = new JButton(Prometheus);
		button3.setActionCommand("3");
		
		JLabel labelDesc = new JLabel(DefaultDescription);
		JLabel PlayingStatus = new JLabel(MonstersPlaying);
		
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
		
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add(PlayingStatus);
		buttonPanel.add(Box.createVerticalStrut(90));
		buttonPanel.add(button1);
		//buttonPanel.add(Box.createVerticalStrut(220));
		buttonPanel.add(button2);
		buttonPanel.add(button3);
		buttonPanel.add(labelDesc);
		buttonPanel.setBackground(Color.BLACK);
		add(buttonPanel, BorderLayout.WEST);
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);

		
		validate();
		
		Border emptyBorder = BorderFactory.createEmptyBorder();
		button1.setBorder(emptyBorder);
		button2.setBorder(emptyBorder);
		button3.setBorder(emptyBorder);
		labelDesc.setBorder(emptyBorder);
		
		
		
		button1.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        button1.setIcon(MovingMonsters);
		        labelDesc.setIcon(MonstersDescription);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        button1.setIcon(Monsters);
		        labelDesc.setIcon(DefaultDescription);
		    }
		});
		
		button1.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e)
			  { 
			    PlayingStatus.setIcon(MonstersPlaying);
			  } 
			} );
		
		
		button2.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        button2.setIcon(MovingAvengers);
		        labelDesc.setIcon(AvengersDescription);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        button2.setIcon(Avengers);
		        labelDesc.setIcon(DefaultDescription);
		    }
		});
		
		button2.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e)
			  { 
			    PlayingStatus.setIcon(AvengersPlaying);
			  } 
			} );
		
		button3.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        button3.setIcon(MovingPrometheus);
		        labelDesc.setIcon(PrometheusDescription);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        button3.setIcon(Prometheus);
		        labelDesc.setIcon(DefaultDescription);
		    }
		});
		
		button3.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e)
			  { 
			    PlayingStatus.setIcon(PrometheusPlaying);
			  } 
			} );

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
		//bottomPanel.setBackground(Color.BLACK);

	}

	public void actionPerformed(ActionEvent e) {
		int num = 0;
		if(e.getActionCommand() == "1")
		{
			num = 0;
		}
		else if(e.getActionCommand() == "2")
		{
			num  = 1;
		}
		else if(e.getActionCommand() == "3")
		{
			num = 2;
		}
			
		JButton button1 = (JButton) e.getSource();
		selectedTitle = videoList.get(num).getTitle();
		System.out.println("Selected title : " + selectedTitle);
		selectedFile = videoList.get(num).getFilename();
		
		try {
			sendToServer();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
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
