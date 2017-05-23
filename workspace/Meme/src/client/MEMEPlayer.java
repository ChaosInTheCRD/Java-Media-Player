package client;

import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.junit.Test;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import server.MEMEServer;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.test.basic.PlayerControlsPanel;

public class MEMEPlayer {

	public String vlcLibraryPath = "vlc-2.0.1";
	public JFrame mainFrame;
	public EmbeddedMediaPlayerComponent mediaPlayerComponent = null;
	public Container contentPane;
	public EmbeddedMediaPlayer mediaPlayer;
	
	public void LibrarySpec()
	{
	NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcLibraryPath);
	Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
	
	mediaPlayer =  mediaPlayerComponent.getMediaPlayer();
	}
	
	public void getDisplay()
	{
	final EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
	mainFrame.setContentPane(mediaPlayerComponent);
	
	mainFrame.addWindowListener(new WindowAdapter()
	{
		@Override
		public void windowClosing(WindowEvent e)
		{
		mediaPlayerComponent.release();
		}
	}	);
	
	System.out.println("loop");
	
	contentPane.add(mediaPlayerComponent, BorderLayout.CENTER);
	EmbeddedMediaPlayer mediaPlayer = mediaPlayerComponent.getMediaPlayer();
	PlayerControlsPanel controlsPanel = new PlayerControlsPanel(mediaPlayer);
	mainFrame.add(controlsPanel, BorderLayout.SOUTH);
	
	String media = "rtp://@127.0.0.1:1172";
	mediaPlayer.playMedia(media);
	}
	

	
	
	
	
	
	
	
	
	
	
	
	}
