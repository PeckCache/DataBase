package database.Model;

public class QueryInfo
{
	private String query;
	private long queryTime;
	
	public QueryInfo(String query, long queryTime)
	{
		this.query = query;
		this.queryTime = queryTime;
	}
	
	public String getQuery()
	{
		return query;
	}
	
	public long getQueryTime()
	{
		return queryTime;
	}

}
