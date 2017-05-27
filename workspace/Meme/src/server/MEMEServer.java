package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

import org.xml.sax.helpers.DefaultHandler;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class MEMEServer {

	static List<VideoFile> videoList;
	int port = 1175;

	ObjectOutputStream outputToClient;
	ObjectInputStream inputFromClient;
	ServerSocket serverSocket;
	Socket clientSocket;
	Thread socketThread;
	String media = "rtp://@127.0.0.1:1175";

	MediaPlayerFactory mediaPlayerFactory;
	HeadlessMediaPlayer mediaPlayer;
	String selectedTitle;

	// Server Constructor
	public MEMEServer() {

		String vlcLibraryPath = "vlc-2.0.1";
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcLibraryPath);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

		mediaPlayerFactory = new MediaPlayerFactory(media);
		mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();

		XMLReader reader;
		reader = new XMLReader();
		System.out.println("Building XML reader and getting list");

		videoList = reader.getList("videoList.xml");
		System.out.println("Finished XML reader and getting list");

		start();
	}

	// Method that runs the server communication and pushes data back and forth
	public void start() 
	{
		socketThread = new Thread("Socket")
		{
			public void run()
			{
				try 
				{
					openSocket();
					writeListToSocket();
					
					boolean receivedFromClient = false;
					inputFromClient = new ObjectInputStream(clientSocket.getInputStream());
					selectedTitle = null;
					
					// Loop to stay in to check for changes in the selected video
					checkForChange(receivedFromClient);
					
					// Closing Socket at end of Application
					clientSocket.close();
					serverSocket.close();
				} 
				
				catch (IOException e) 
				{
					System.out.println("ERROR on socket connection.");
					e.printStackTrace();
				} 
				catch (ClassNotFoundException e) 
				{
					e.printStackTrace();
				}

			}

			private void checkForChange(boolean receivedFromClient) throws IOException, ClassNotFoundException {
				while (!receivedFromClient)
				{
					selectedTitle = (String) inputFromClient.readObject();

					if (selectedTitle != null)
					{
						System.out.println("Successfully chosen title at server: " + selectedTitle);
						streamAndStart();	
					}
				}
			}
		};
		
		socketThread.start();
	}

	// Opens the Socket on the Server end and waits to establish connection with client
	private void openSocket() throws IOException
	{
		try
		{
			serverSocket = new ServerSocket(port);
		}

		catch (IOException e)
		{
			System.out.println("Could not listen on port : " + port);
			System.exit(-1);
		}

		System.out.println("Opened socket on : " + port + ", waiting for client.");

		try 
		{
			clientSocket = serverSocket.accept();
		} 
		catch (IOException e) 
		{
			System.out.println("Could not accept client.");
			System.exit(-1);
		}
		
		outputToClient = new ObjectOutputStream(clientSocket.getOutputStream());
	}

	
	// Writes the Video List to the socket
	private void writeListToSocket() throws IOException 
	{
		outputToClient.writeObject(videoList);
	}
	

	// Setting out the serverAddress and serverPort inputs as one string
	private String formatRtpStream(String serverAddress, int serverPort)
	{
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#rtp{dst=");
		sb.append(serverAddress);
		sb.append(",port=");
		sb.append(serverPort);
		sb.append(",mux=ts}");
		return sb.toString();
	}
	
	
	// Sends out the media to the Server Address for the Client to Receive
	public void streamAndStart()
	{
		String serverAddress = "127.0.0.1";
		String options = formatRtpStream(serverAddress, port);
		String media = selectedTitle;
		mediaPlayer.playMedia(media, options, ":no-sout-rtp-sap", ":no-sout-standardsap", ":sout-all", ":sout-keep");
	}

	
	// Main Method
	public static void main(String[] args)
	{
		new MEMEServer();
	}

}
