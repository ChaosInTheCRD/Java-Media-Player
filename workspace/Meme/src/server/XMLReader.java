package server;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

//import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XMLReader extends DefaultHandler
{
	/*
	String inputFile = "videoList.xml";
	Video currentVideo;
	String currentSubElement;
	List<Video> videoList;
	*/
	private List<VideoFile> videoList;
	private VideoFile currentVideo;
	private String currentSubElement;
	private String inputFile;
	
	public XMLReader() {
	}
	
	public List<VideoFile> getList(String inputFile)
	{
		this.inputFile = inputFile;
		videoList = new ArrayList<VideoFile>();
		
		readXMLFile(inputFile);
		
		return videoList;
	}
	
	private void readXMLFile(String inputFile)
	{
		 try 
			{
				// Use the default parser
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
				// Tell the parser to start reading the XML file
				saxParser.parse(inputFile, this);
			}
			catch (ParserConfigurationException pce) {
				pce.printStackTrace();
			}
			catch (SAXException saxe) {
				saxe.printStackTrace();
			}
			catch (IOException ioe) {
				ioe.printStackTrace();
			}
			
	}
	
	@Override
    public void startDocument() throws SAXException {
        System.out.println("Started parsing: " + inputFile);
        videoList = new ArrayList<VideoFile>();
    }
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		// Sort out element name if (no) namespace in use
		String elementName = localName;
		if ("".equals(elementName)) {
			elementName = qName;
		}

		// Work out what to do with this element
		switch (elementName) {
		case "video":
			// This assumes only one attribute - it will not work for more than
			// one.
			currentVideo = new VideoFile(attributes.getValue(0));
			break;
		case "title":
			currentSubElement = "title";
			break;
		case "filename":
			currentSubElement = "filename";
			break;
		default:
			currentSubElement = "none";
			break;
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String newContent = new String(ch, start, length);

		switch (currentSubElement) {
		case "title":
			currentVideo.setTitle(newContent);
			break;
		case "filename":
			currentVideo.setFilename(newContent);
			break;
		default:
			// TODO no action appears necessary
			break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// Finishing an element means we're definitely not in a sub-element
		// anymore
		currentSubElement = "none";

		// Sort out element name if (no) namespace in use
		String elementName = localName;
		if ("".equals(elementName)) {
			elementName = qName;
		}

		if (elementName.equals("video")) {
			videoList.add(currentVideo);
			// We've finished and stored this video, so remove the reference
			currentVideo = null;
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		System.out.println("Finished parsing, stored " + videoList.size() + " videos.");

		for (VideoFile thisVideo : videoList) {
			System.out.println("ID: " + thisVideo.getID());
			System.out.println("Title: " + thisVideo.getTitle());
			System.out.println("File name: " + thisVideo.getFilename());
		}
	}
}



/*
  
*/
