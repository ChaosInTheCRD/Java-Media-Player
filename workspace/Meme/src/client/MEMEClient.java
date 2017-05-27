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

	// Declaration of Global Variable to keep track of which video is playing
	// Useful for implementation of 'Next/Previous' buttons
	public static int num = 0;
	
	// Declaration of Variables
	List<VideoFile> videoList;
	Socket serverSocket;
	String host = "127.0.0.1";
	int port = 1175;
	ObjectInputStream inputFromServer;
	ObjectOutputStream outputToServer;
	Container contentPane;
	String selectedTitle;
	public String vlcLibraryPath = "vlc-2.0.1";
	public JFrame mainFrame;
	public JFrame LoadingFrame;
	public EmbeddedMediaPlayerComponent mediaPlayerComponent;
	public EmbeddedMediaPlayer mediaPlayer;
	private String selectedFile;
	
	// Creation of Image Icon gifs and JLabel for Now Playing Effect
	ImageIcon MonstersPlaying = new ImageIcon("Film_Pics/MonstersPlaying.gif");
	ImageIcon AvengersPlaying = new ImageIcon("Film_Pics/AvengersPlaying.gif");
	ImageIcon PrometheusPlaying = new ImageIcon("Film_Pics/PrometheusPlaying.gif");
	ImageIcon MEMEPlayer = new ImageIcon("Film_Pics/MEMEPlayer.PNG");
	JLabel PlayingStatus = new JLabel(MEMEPlayer);
	
	ImageIcon Monsters = new ImageIcon("Film_Pics/MonstersInc.jpeg");
	ImageIcon Avengers = new ImageIcon("Film_Pics/TheAvengers.jpg");
	ImageIcon Prometheus = new ImageIcon("Film_Pics/Prometheus.jpg");
	ImageIcon MovingMonsters = new ImageIcon("Film_Pics/lalala.gif");
	ImageIcon MovingPrometheus = new ImageIcon("Film_Pics/MovingPrometheus.gif");
	ImageIcon MovingAvengers = new ImageIcon("Film_Pics/MovingAvengers.gif");
	ImageIcon DefaultDescription = new ImageIcon("Film_Pics/DefaultDescription.png");
	ImageIcon MonstersDescription = new ImageIcon("Film_Pics/MonstersDescription.png");
	ImageIcon AvengersDescription = new ImageIcon("Film_Pics/AvengersDescription.png");
	ImageIcon PrometheusDescription = new ImageIcon("Film_Pics/PrometheusDescription.png");
	JButton button1 = new JButton(Monsters);
	JButton button2 = new JButton(Avengers);
	JButton button3 = new JButton(Prometheus);
	JLabel labelDesc = new JLabel(DefaultDescription);

	// Main constructor for Client
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

	// Implementation of GUI Setup
	private void setupGUI() {
		JPanel buttonPanel = new JPanel();
		
		button1.setActionCommand("1");
		button2.setActionCommand("2");
		button3.setActionCommand("3");
		
		mainFrameEstab();

		//Insertion of Loading Screen//
		JLabel LoadingScreen = loadingScreen();
		
		//Loading of Libraries and addition of mediaPlayer
		EmbeddedMediaPlayerComponent mediaPlayerComponent = addMediaPlayer();
	
		
		// adding Buttons on sidebar
		sideBar(buttonPanel, button1, button2, button3, labelDesc);

		
		//Removal of Loading Screen and Addition of Control Panel with Screen
		contentPane.remove(LoadingScreen);
		contentPane.validate();
		contentPane.repaint();
		contentPane.add(mediaPlayerComponent, BorderLayout.CENTER);
		EmbeddedMediaPlayer mediaPlayer = mediaPlayerComponent.getMediaPlayer();
		PlayerControlsPanel controlsPanel = new PlayerControlsPanel(mediaPlayer);
		mainFrame.add(controlsPanel, BorderLayout.SOUTH);
		mainFrame.setBackground(Color.BLACK);
		validate();
		
		//Button Listeners and Mouse Listeners for GIF Effect and Playing Status
		
		// MONSTERS INC
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
			    button1.setDisabledIcon(MovingMonsters);
			  } 
			} );
		
	
		
		// AVENGERS
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
			    button2.setDisabledIcon(MovingAvengers);
			  } 
			} );
		
		
		// PROMETHEUS
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
			    button3.setDisabledIcon(MovingPrometheus);
			  } 
			} );
		
		
		// Action Listener taken from control panel for utilisation of
		// Next/Previous Button
		controlsPanel.NextPreviousActionListener(new ActionListener(){
	        @Override
			public void actionPerformed(ActionEvent e){
	        	
	        	videoIteration(e);
	        	whichVideo();
	        	
	        	mediaPlayer.stop();
	    		selectedTitle = videoList.get(num).getTitle();
	    		System.out.println("Selected title : " + selectedTitle);
	    		selectedFile = videoList.get(num).getFilename();
	    		
	    		try {
	    			sendToServer();
	    			mediaPlayer.play();
	    		} catch (IOException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		} 
	        }
	        
	        
	        // Iterates through each video upon button press
			private void videoIteration(ActionEvent e) {
				if(e.getActionCommand() == "1")
	        	{
	        		num = num+1;
	        		System.out.println("Skipped to Next Video");
	        		if(num == 3)
	        		{
	        			num = 0;
	        		}
	        	}
	        	
	        	else if(e.getActionCommand() == "2")
	        	{
	        		num = num -1;
	        		System.out.println("Skipped to Previous Video");
	        		if(num == -1)
	        		{
	        			num = 2;
	        		}
	        		
	        		System.out.println(num);
	        	}
			}
			
			
			// Changes Now Playing for correct video
			private void whichVideo() {
				if(num == 0)
	        	{
	        		PlayingStatus.setIcon(MonstersPlaying);
	        	}
	        	
	        	if(num == 1)
	        	{
	        		PlayingStatus.setIcon(AvengersPlaying);
	        	}
	        	
	        	if(num == 2)
	        	{
	        		PlayingStatus.setIcon(PrometheusPlaying);
	        	}
			}

	    });
	}

	// SideBar Component
	private void sideBar(JPanel buttonPanel, JButton button1, JButton button2, JButton button3, JLabel labelDesc) {
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add(PlayingStatus);
		buttonPanel.add(Box.createVerticalStrut(110));
		buttonPanel.add(button1);
		buttonPanel.add(button2);
		buttonPanel.add(button3);
		buttonPanel.add(labelDesc);
		buttonPanel.setBackground(Color.BLACK);
		add(buttonPanel, BorderLayout.WEST);
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		
		// Emptying of Border for transparent icons
		Border emptyBorder = BorderFactory.createEmptyBorder();
		button1.setBorder(emptyBorder);
		button2.setBorder(emptyBorder);
		button3.setBorder(emptyBorder);
		labelDesc.setBorder(emptyBorder);
	}

	// Media Player Component
	private EmbeddedMediaPlayerComponent addMediaPlayer() {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcLibraryPath);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		mediaPlayer =  mediaPlayerComponent.getMediaPlayer();
		mainFrame.setContentPane(mediaPlayerComponent);
		return mediaPlayerComponent;
	}

	// Loading Screen
	private JLabel loadingScreen() {
		JLabel LoadingScreen = new JLabel();
		contentPane.add(LoadingScreen, BorderLayout.CENTER);
		LoadingScreen.setIcon(new ImageIcon("Film_Pics/Loading.gif"));
		contentPane.setBackground(Color.BLACK);
		LoadingScreen.setVisible(true);
		LoadingScreen.setHorizontalAlignment(SwingConstants.CENTER);
		validate();
		return LoadingScreen;
	}
	
	
	// Establishment of mainFrame
	private void mainFrameEstab() {
		mainFrame = new JFrame();
		setTitle("MEMEPlayer");
		setSize(1280, 1024);
		setVisible(true);
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// Action Listener for changing currently viewed video
	public void actionPerformed(ActionEvent e) {
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
			
		mediaPlayer.stop();
		JButton button1 = (JButton) e.getSource();
		selectedTitle = videoList.get(num).getTitle();
		System.out.println("Selected title : " + selectedTitle);
		selectedFile = videoList.get(num).getFilename();
		
		try {
			sendToServer();
			mediaPlayer.play();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
			
		disable(button1, 2000);
		
	}


	
	// Designed to send chosen "selected title" back to the server
	public void sendToServer() throws IOException
	{
		System.out.println("sendToServer - Test");
		System.out.println(selectedFile);
		outputToServer.writeObject(selectedFile);
	}
	
	// Establishes the Socket and input/output stream
	public void socket() throws UnknownHostException, IOException, ClassNotFoundException {
		serverSocket = new Socket(host, port);
		inputFromServer = new ObjectInputStream(serverSocket.getInputStream());
		outputToServer = new ObjectOutputStream(serverSocket.getOutputStream());
	}

	// Gets the List from the socket
	private void getListFromSocket() throws IOException, ClassNotFoundException {
		videoList = (List<VideoFile>) inputFromServer.readObject();
		System.out.println("CLIENT: " + videoList.get(0).getID());
	}
	
	// Method that plays the media from the server address
		public void playMedia()
		{
		
			String media = "rtp://@127.0.0.1:1175";
			mediaPlayer.playMedia(media);
		}
		
	// Method to implement delay for button press (Stops Spamming of buttons
	// that bring multiple reloads of video)
	static void disable(final AbstractButton b, final long ms) {
		b.setEnabled(false);
		    new SwingWorker() {
		        @Override protected Object doInBackground() throws Exception {
		            Thread.sleep(ms);
		            return null;
		        }
		        @Override protected void done() {
		            b.setEnabled(true);
		        }
		    }.execute();
		}
		
	// Class Main
	public static void main(String[] args) {
		new MEMEClient();
	}
	
}
