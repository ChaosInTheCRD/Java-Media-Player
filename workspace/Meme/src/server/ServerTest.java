package server;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ServerTest {
	
	
	
	@Before
	public void setUp() throws Exception
	{
		MEMEServer.main(null);
	//	reader = new XMLReader();
	//	videoList = reader.getList("videoList.xml");
	}

	@Test
	public void test() {
		
		assertTrue(MEMEServer.videoList instanceof List);
		//fail("Not yet implemented");
	}
	
}
