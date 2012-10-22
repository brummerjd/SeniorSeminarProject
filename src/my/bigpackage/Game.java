package my.bigpackage;

public class Game {
	
	private int _Id;
	private String _Name;
	
	public void SetId(int id)
	{
		this._Id = id;
	}
	
	public void SetName(String name)
	{
		this._Name = name;
	}
	
	public int GetId()
	{
		return this._Id;
	}
	
	public String GetName()
	{
		return this._Name;
	}
}
