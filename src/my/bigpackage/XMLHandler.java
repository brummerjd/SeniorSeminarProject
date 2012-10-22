package my.bigpackage;

import java.util.ArrayList;
import java.util.jar.Attributes;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler
{
	public ArrayList<Game> Games = new ArrayList<Game>();
	
	private String _ElementValue;
	private Boolean _ElementOn;
	private Game _Game;
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		this._ElementOn = true;
		if (qName.equals("game"))
		{
			this._Game = new Game();
		}
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		this._ElementOn = false;
		if (qName.equals("id"))
		{
			this._Game.SetId(Integer.parseInt(this._ElementValue));
		}
		else if (qName.equals("name"))
		{
			this._Game.SetName(this._ElementValue);
		}
		else if (qName.equals("game"))
		{
			this.Games.add(this._Game);
		}
	}
	
	public void characters(char[] ch, int start, int length)
	{
		if (this._ElementOn)
		{
			this._ElementValue = new String(ch, start, length);
			this._ElementOn = false;
		}
	}
}
