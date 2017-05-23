package server;

import java.io.IOException;
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
  int port = 1173;
  
  ObjectOutputStream outputToClient;
  ServerSocket serverSocket;
  Socket clientSocket;
  Thread socketThread;
  String media = "rtp://@127.0.0.1:1173";

  MediaPlayerFactory mediaPlayerFactory;
  HeadlessMediaPlayer mediaPlayer;

public MEMEServer()
{	//TODO : CALL VLC LIBRARIES HERE
	
	String vlcLibraryPath = "vlc-2.0.1";
	NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcLibraryPath);
	Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
	
	mediaPlayerFactory = new MediaPlayerFactory(media);
	mediaPlayer  = mediaPlayerFactory.newHeadlessMediaPlayer();

	
	XMLReader reader;
	reader = new XMLReader();
	System.out.println("Building XML reader and getting list");

	videoList = reader.getList("videoList.xml");
	System.out.println("Finished XML reader and getting list");
	

	
	
	start();
}

 public void start()
 {
  socketThread = new Thread("Socket")
  	{
	  public void run() {
		  try {
			  openSocket();
			  writeListToSocket();
			  
//			  boolean receivedFromClient=false;
//			  while(!receivedFromClient)
//			  {
//				  //TODO : KEEP RECEIVING FROM THE CLIENT
//				  //inputFromClient.readObject
//				  if(object !=null){
//					  //TODO : START STREAMANDSTART
//					  receivedFromClient=TRUE;
//				  }
//			  }
//			  boolean recievedFromClient = false;
//			  
//			  while(!receievedFromClient)
//			  {
//				  // Keep receiving from client
//				  clientSocket.readObject;
//			  }
			  
			  
			  clientSocket.close();
			  serverSocket.close();
			  } catch (IOException e) {
			  System.out.println("ERROR on socket connection.");
			  e.printStackTrace();
		  }
		  }
  	};
  	socketThread.start();
 }
  	
  
  private void openSocket() throws IOException {
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
	  //TODO : input from client here
	  }
	
  private void writeListToSocket() throws IOException 
  	{
	  outputToClient.writeObject(videoList);
	}
	
	private String formatRtpStream(String serverAddress, int serverPort) {
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#rtp{dst=");
		sb.append(serverAddress);
		sb.append(",port=");
		sb.append(serverPort);
		sb.append(",mux=ts}");
		return sb.toString();
	}
	public void StreamandStart () {
		String serverAddress = "127.0.0.1";
		String options = formatRtpStream(serverAddress, port);
		String media = "monstersinc_high.mpg";
		mediaPlayer.playMedia(media, options, ":no-sout-rtp-sap", ":no-sout-standardsap",
		":sout-all", ":sout-keep");
	}
	
	

	
	public static void main(String[] args)
	{
		new MEMEServer();
	}

}
