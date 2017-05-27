package server;
import client.MEMEClient;

import static org.junit.Assert.*;

import java.util.List;

import javax.swing.JButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import client.MEMEClient;

public class ServerTest {
	
	
	
	@Before
	public void setUp() throws Exception
	{
		MEMEServer.main(null);
		//server.MEMEServer.main(null);
		MEMEClient.main(null);
	}
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void listCheck() {
		assertTrue(MEMEServer.videoList instanceof List);
	}
	
}
