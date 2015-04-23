package database.view;



import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import database.Controller.DatabaseController;
import database.Controller.DatabaseMaster;
import database.Model.QueryInfo;



public class DatabasePanel extends JPanel
{
	
	private SpringLayout baseLayout;
	private DatabaseController baseController;
	private JButton queryButton;
	private JTextArea displayArea;
	private JScrollPane displayPane;
	private JTable tableData;
	private TableCellWrapRenderer myCellRender;
	private ArrayList<QueryInfo> inputFieldList;
	
	public DatabasePanel(DatabaseController databaseController)
	{
		this.baseController = baseController;
		queryButton = new JButton("Test the query");
		displayArea = new JTextArea(10, 30);
		//displayPane = new JScrollPane(displayArea);
		baseLayout = new SpringLayout();
		myCellRender = new TableCellWrapRenderer();
		
		//setupDisplayPane();
		setupPanel();
		setupLayout();
		setupListener();
		setupTable();
		
	}
	
	private void setupDisplayPane()
	{
		displayArea.setWrapStyleWord(true);
		displayArea.setLineWrap(true);
		displayArea.setEditable(false);
	}
	
	private void setupTable()
	{
		tableData = new JTable(new DefaultTableModel(baseController.getDatabase().tableInfo(), baseController.getDatabase().getMetaData()));
		
		displayPane = new JScrollPane(tableData);
		for(int spot = 0; spot < tableData.getColumnCount(); spot++)
		{
			tableData.getColumnModel().getColumn(spot).setCellRenderer(myCellRender);
		}
	}
	
	private void setupPanel()
	{
		this.setBackground(Color.WHITE);
		this.setLayout(baseLayout);
		this.add(queryButton);
		this.setVisible(false);
		int startOffset = 20;
		for(int count = 0 ; count < 6; count++)
		{
			JLabel numberer = new JLabel("" + count + " label");
			JTextField textField = new JTextField(10);
			this.add(numberer);
			this.add(textField);
			baseLayout.putConstraint(SpringLayout.NORTH, numberer, startOffset, SpringLayout.NORTH, this);
			startOffset += 20;
			baseLayout.putConstraint(SpringLayout.NORTH, textField, startOffset, SpringLayout.NORTH, this);
			startOffset += 50;
		}
	}
	
	private void setupLayout()
	{
		
	}
	
	private void setupListener()
	{
		ArrayList<JTextField> myTextFields = new ArrayList<JTextField>();
		int fieldCount = 0;
		for(Component current : this.getComponents())
		{
			if(current instanceof JTextField)
			{
				fieldCount++;
				myTextFields.add((JTextField) current);
			}
		}
	}
	
	public String getValueList()
	{
		String values = "";
		
		return values;
	}
	
	private String getFieldList()
	{
		String field = "(";
		
		for(int spot = 0; spot < inputFieldList.size(); spot++)
		{
			String temp = inputFieldList.get(spot).getName();
			int cutoff = temp.indexOf("Field");
			temp = temp.substring(0,cutoff);
			
			if(spot == inputFieldList.size()-1)
			{
				field +="`" + temp + ")";
			}
			else
			{
				
			}
		}
	}
	
	
	
}
