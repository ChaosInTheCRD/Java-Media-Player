package server;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class XMLReaderTest {
	private XMLReader reader;
	private List<VideoFile> videoList;
	

	@Before
	public void setUp() throws Exception
	{
		reader = new XMLReader();
		videoList = reader.getList("videoList.xml");
	}

	// Test 1
	@Test
	public void createListofVideos()
	{
		assertTrue(videoList instanceof List);
	}
	
	
	// Test 2
	@Test
	public void listContainsVideoFiles()
	{
		assertTrue(videoList.get(0) instanceof VideoFile);
	}
	
	// Test 2
	@Test
	public void videoFileReturnsCorrectFields()
	{
		VideoFile videoFile = videoList.get(0);
		assertNotNull(videoFile.getID());
		assertNotNull(videoFile.getTitle());
		assertNotNull(videoFile.getFilename());
	}
	
	//Test 3 & 4
	@Test
	public void videoFileTestRead()
	{
		VideoFile videoFile = videoList.get(0);
		assertTrue(videoFile.getID().equals("20120213a2"));
		assertTrue(videoFile.getFilename().equals("monstersinc_high.mpg"));
		assertTrue(videoFile.getTitle().equals("Monsters Inc."));
		
		assertTrue(videoFile.getID().equals("20120102b7"));
		assertTrue(videoFile.getFilename().equals("avengers-featurehp.mp4"));
		assertTrue(videoFile.getTitle().equals("Avengers"));

	}
	
}
	


