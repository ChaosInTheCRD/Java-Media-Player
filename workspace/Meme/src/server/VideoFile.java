package server;

import java.io.Serializable;

public class VideoFile implements Serializable  {
	private String id;
    private String title;
    private String filename;
	
	public VideoFile(String id) {
		this.id = id;
	}

	public String getID()
	{
		return id;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public  String getFilename()
	{
		return filename;
	}

	public void setTitle(String newContent) {
		this.title = newContent;		
	}

	public void setFilename(String newContent) {
        this.filename = newContent;
		
	}
}
