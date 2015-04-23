package database.Controller;

import java.awt.Component;

import javax.swing.JOptionPane;

import database.view.DatabaseFrame;

public class DatabaseController
{
	
	private DatabaseFrame baseFrame;
	private DatabaseMaster database;
	
	public DatabaseController()
	{
		database = new DatabaseMaster(this);
		baseFrame = new DatabaseFrame(this);
		
	}
	
	
	public void start()
	{
		database.connectionStringBuilder("127.0.0.1", "", "root", "");
		database.setupConnection();
	}


	public Component getAppFrame()
	{
		return baseFrame;
	}
	
	public DatabaseMaster getDatabase()
	{
		return database;
		
	}

}
