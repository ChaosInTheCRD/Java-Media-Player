package client;
import server.MEMEServer;
import static org.junit.Assert.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import server.VideoFile;

public class MEMEClientTest 
{

	private MEMEClient client;
	
	@Before
	public void setUp() throws Exception
	{
		server.MEMEServer.main(null);
		client = new MEMEClient();
	}	
	
	@Test
	 public void videoFileReturnsCorrectValue()
	{
		VideoFile videoFile = client.videoList.get(0);
		assertEquals("20120213a2", videoFile.getID());
		assertEquals("Monsters Inc.", videoFile.getTitle());
		assertEquals("monstersinc_high.mpg", videoFile.getFilename()); 
	}
	

	//Must Run Again for functionality
	@Test
	public void button1Test() 
	{
		JButton button1 = client.button1;
		button1.doClick();
		int num = client.num;
		//num = 0;
		VideoFile videoFile = client.videoList.get(num);
		assertEquals("Monsters Inc.", videoFile.getTitle());
	}
	
	@Test
	public void button2Test() 
	{
		JButton button2 = client.button2;
		button2.doClick();
		int num = client.num;
		VideoFile videoFile = client.videoList.get(num);
		assertEquals("Avengers", videoFile.getTitle());
	}
	
	@Test
	public void button3Test() 
	{
		JButton button3 = client.button3;
		button3.doClick();
		int num = client.num;
		VideoFile videoFile = client.videoList.get(num);
		assertEquals("Prometheus", videoFile.getTitle());
	}
	
	@Test
	public void button1ImageTest()
	{
		JButton button1 = client.button1;
		button1.doClick();
		ImageIcon Monsters = client.Monsters;
		assertEquals(button1.getIcon(), Monsters);
	}
	
	@Test
	public void button2ImageTest()
	{
		JButton button2 = client.button2;
		button2.doClick();
		ImageIcon Avengers = client.Monsters;
		assertEquals(button2.getIcon(), Avengers);
	}
	
	@Test
	public void button3ImageTest()
	{
		JButton button3 = client.button2;
		button3.doClick();
		ImageIcon Prometheus = client.Prometheus;
		assertEquals(button3.getIcon(), Prometheus);
	}
	
	
	
}
