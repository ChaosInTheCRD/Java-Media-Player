package client;

import static org.junit.Assert.*;

import javax.swing.JComboBox;

import org.junit.Test;
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
	public void test() {
		fail("Not yet implemented");
	}

	
	@Test
	 public void videoFileReturnsCorrectValue()
	{
		VideoFile videoFile = client.videoList.get(0);
		assertEquals("20120213a2", videoFile.getID());
		assertEquals("First Video Title", videoFile.getTitle());
		assertEquals("firstVideoFile.mpg", videoFile.getFilename()); 
	}
	
	/*@Test
	public void checkSelectedVideoInList() {
	 JComboBox comboBox = client.selectionBox;
	 comboBox.setSelectedIndex(2);
	 assertEquals("Third Video", comboBox.getSelectedItem());
	} */
	
	@Test
	public void DisplayErrorMessageforWrongFormat () {
		
	}
	
	@Test
	public void VerifyCorrectVideoIsSelectedAfterClick () {
		
	}
	
}
