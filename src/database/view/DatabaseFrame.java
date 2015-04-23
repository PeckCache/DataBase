package database.view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import database.Controller.DatabaseController;
import database.Controller.DatabaseMaster;

public class DatabaseFrame extends JFrame
{
	private DatabasePanel basePanel;
	private DatabaseMaster baseMaster;

	public DatabaseFrame(DatabaseController databaseController)
	{
		basePanel = new DatabasePanel(databaseController);
		SetupFrame();
		setupListeners();
	}

	private void SetupFrame()
	{
		this.setSize(1000, 1200);
		this.setContentPane(basePanel);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private void setupListeners()
	{
		this.addWindowListener(new WindowListener()
		{

			@Override
			public void windowOpened(WindowEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e)
			{
				//Call the save info method
				baseMaster.saveQueryTimingInfo();
			}

			@Override
			public void windowClosed(WindowEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
		});
	}

}
