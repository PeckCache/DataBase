/**
 * 
 */
package database.Controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import database.Model.QueryInfo;

public class DatabaseMaster
{

	private DatabaseController baseController;
	private String connectionString;
	private Connection databaseConnection;
	private String currentQuery;
	private ArrayList<QueryInfo> timingInfoList;
	

	/**
	 * Constructor build the 
	 * @param baseController
	 */
	public DatabaseMaster(DatabaseController baseController)
	{
		connectionString = "jdbc:mysql://127.0.0.1/??user=root";
		this.baseController = baseController;
		checkDriver();
		setupConnection();
	}

	public void start()
	{
		loadTimingInfo();
	}
	/**
	 * checks to make sure drive is vaild
	 */
	private void checkDriver()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (Exception currentException)
		{
			displayErrors(currentException);
			System.exit(1);
		}
	}
	
	public void connectionStringBuilder(String pathToDBServer, String databaseName, String userName, String password)
	{
		connectionString = "jdbc:mysql://";
		connectionString += pathToDBServer;
		connectionString += "/" + databaseName;
		connectionString += "?user=" + userName;
		connectionString += "&password=" + password;
	}

	/**
	 * setups the connection to the target database
	 */
	public void setupConnection()
	{
		try 
		{
			databaseConnection = DriverManager.getConnection(connectionString);
		}
		catch(Exception currentException)
		{
			
		}
		
	}
	
	/**
	 * Closes the Connection to the database
	 */
	public void closeConnection()
	{
		try
		{
			databaseConnection.close();
		}
		catch(SQLException error)
		{
			displayErrors(error);
		}
	}

	/**
	 * Displays any error on screen.
	 * @param currentException the error to be displayed.
	 */
	public void displayErrors(Exception currentException)
	{
		JOptionPane.showMessageDialog(baseController.getAppFrame(), currentException.getMessage());
		;
		if (currentException instanceof SQLException)
		{
			JOptionPane.showMessageDialog(baseController.getAppFrame(), "SQL State: " + ((SQLException) currentException).getSQLState());
			JOptionPane.showMessageDialog(baseController.getAppFrame(), "SQL Error Code: " + ((SQLException) currentException).getErrorCode());
		}
	}
	
	/**
	 * requests tables for the current database.
	 * @return the information from the requested table.
	 */
	public String [][] tableInfo()
	{
		String[][] results;
		String query = "SELECT * FROM `INNODB_SYS_COLUMNS";
		
		try 
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			int columnCount = answer.getMetaData().getColumnCount();
			int rowCount;
			answer.last();
			rowCount = answer.getRow();
			answer.beforeFirst();
			results = new String [rowCount][columnCount];
			
			while(answer.next())
			{
				for(int col = 0; col < columnCount; col++)
				{
				results[answer.getRow() - 1][col] = answer.getString(col+1);
				}
			}
			
			
			answer.close();
			firstStatement.close();
		}
		catch(SQLException currentSQLError)
		{
			results = new String [][] {{"problem occurred :("}};
			displayErrors(currentSQLError);
		}
		
		return results;
	}
	
	public String [] getDatabaseColumnNames(String tableName)
	{
		String [] column;
		String query = "SELECT * FROM `" + tableName + "`";
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			ResultSetMetaData myMeta = answer.getMetaData();
			
			column = new  String [myMeta.getColumnCount()];
			
			for(int spot = 0; spot <myMeta.getColumnCount(); spot++)
			{
				column[spot] = myMeta.getColumnName(spot +1);
			}
			
			answer.close();
			firstStatement.close();
			endTime = System.currentTimeMillis();
		}
		catch(SQLException currentSQLError)
		{
			endTime = System.currentTimeMillis();
			column = new String [] {"empty"};
			displayErrors(currentSQLError);
		}
		long queryTime = endTime - startTime;
		return column;
	}
	
	public String [] getMetaData()
	{
		String [] columnInformation;
		String query = "SHOWTABLES";
		
		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			ResultSetMetaData myMeta = answer.getMetaData();
			
			columnInformation = new  String [myMeta.getColumnCount()];
			for(int spot = 0; spot <myMeta.getColumnCount(); spot++)
			{
				columnInformation[spot] = myMeta.getColumnName(spot +1);
			}
			answer.close();
			firstStatement.close();
		}
		catch(SQLException currentSQLError)
		{
			columnInformation = new String [] {"bada exists"};
			displayErrors(currentSQLError);
		}
		
		return columnInformation;
	}
	/**
	 * displays the tables that are in the database.
	 * @return Returns the content of said tables.
	 */
	public String displayTable()
	{
		String results = "";
		String query = "SHOW TABLES";
		
		
		try 
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			while(answer.next())
			{
				results += answer.getString(1) + "\n";
			}
			answer.close();
			firstStatement.close();
		}
		catch(SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
		}
		
		return results;
		
	}

	/**
	 * 
	 * @return
	 */
	public int insertIntoTables()
	{
		int rowsAffected = 0;
		
		String insertQuery = "Insert into";
		
		try
		{
			Statement insertStatement = databaseConnection.createStatement();
			rowsAffected = insertStatement.executeUpdate(insertQuery);
			insertStatement.close();
		}
		catch(SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
		}
		return rowsAffected;
	}
	
	private boolean checkForDataViolation()
	{
		if(currentQuery.toUpperCase().contains(" DROP ")
			|| currentQuery.toUpperCase().contains(" TRUNCATE ")
			|| currentQuery.toUpperCase().contains(" SET ")
			|| currentQuery.toUpperCase().contains(" ALLTER "))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean checkForStructureViolation()
	{
		if(currentQuery.toUpperCase().contains(" DATABASE "))
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	private boolean checkAlterStructure()
	{
		if(currentQuery.toUpperCase().contains(" ADD ")
			|| currentQuery.toUpperCase().contains(" DROP "))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public void alterStatement()
	{
		String results;
		try
		{
			if(checkForStructureViolation())
			{
				throw new SQLException("you is not allowed to be changing the db's",
										"duh",
										Integer.MIN_VALUE);
			}
			else if(checkAlterStructure())
			{
				throw new SQLException("You need to include ADD or DROP",
										"Really",
										(Integer.MIN_VALUE + 1));
			}
			
			if(currentQuery.toUpperCase().contains(" INDEX "))
			{
				results = "The index was ";
			}
			else
			{
				results = "The table was ";
			}
			
			Statement alterStatement = databaseConnection.createStatement();
			int affected = alterStatement.executeUpdate(currentQuery);
			
			alterStatement.close();
			
			if(affected != 0)
			{
				results+= "altered";
			}
			JOptionPane.showMessageDialog(baseController.getAppFrame(), results);
		}
		catch(SQLException alterError)
		{
			displayErrors(alterError);
		}
	}
	
	/**
	 * Method used to use the DROP keyword, does not allow dropping of database.
	 * @throws SQLException if the currentQuery contains DROP DATABASE.
	 */
	public void dropStatement()
	{
		String results;
		try
		{
			if(checkForStructureViolation())
			{
				throw new SQLException("you is not allowed to be dropping db's",
										"duh",
										Integer.MIN_VALUE);
			}
			
			if(currentQuery.toUpperCase().contains(" INDEX "))
			{
				results = "The index was ";
			}
			else
			{
				results = "The table was ";
			}
			
			Statement dropStatement = databaseConnection.createStatement();
			int affected = dropStatement.executeUpdate(currentQuery);
			
			dropStatement.close();
			
			if(affected == 0)
			{
				results+= "dropped";
			}
			JOptionPane.showMessageDialog(baseController.getAppFrame(), results);
		}
		catch(SQLException dropError)
		{
			displayErrors(dropError);
		}
	}

	public ArrayList<QueryInfo> getTimingInfoList()
	{
		return timingInfoList;
	}
	
	private void loadTimingInfo()
	{
		File saveFile = new File("save.txt");
		try
		{
			Scanner readFileScanner;
			if (saveFile.exists())
			{
				timingInfoList.clear();
				readFileScanner = new Scanner(saveFile);
				while(readFileScanner.hasNext())
				{
					String tempQuery = readFileScanner.next();
					long tempTime = readFileScanner.nextLong();
					readFileScanner.next();
					
					timingInfoList.add(new QueryInfo(tempQuery, tempTime));
				}
				readFileScanner.close();
			}
		}
		catch (IOException current)
		{
			this.displayErrors(current);
		}
	}
	
	public void saveQueryTimingInfo()
	{
		
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
