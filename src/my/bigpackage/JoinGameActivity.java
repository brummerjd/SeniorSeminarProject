package my.bigpackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class JoinGameActivity extends Activity
{	
	private Context MainContext;
	
	private Button RefreshB;
	private ListView ActiveGameLV;
	
	private ArrayList<Game> _Games = new ArrayList<Game>();
	private ArrayAdapter<String> _Adapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.join_game);
        
        MainContext = this;
        
        RefreshB = (Button)this.findViewById(R.id.refresh_Button);
        RefreshB.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				populateActiveGameList();
			}
        });
        
        ActiveGameLV = (ListView)this.findViewById(R.id.activeGame_ListView);
        ActiveGameLV.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainContext);
				builder.setMessage("Join game " + _Games.get(position).GetName() + "?")
					   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
														public void onClick(DialogInterface dialog, int which) {
															joinGame(_Games.get(position));
														}
					   								 })
					   .setNegativeButton("Cancel", null);
				builder.create().show();
			}
        });
        
        _Adapter = new ArrayAdapter<String>(MainContext, R.layout.active_game_list_item);
        _Adapter.setNotifyOnChange(true);
        ActiveGameLV.setAdapter(_Adapter);
        
        populateActiveGameList();
	}
	
	private void populateActiveGameList()
	{
		Toast.makeText(MainContext, "Populating!", Toast.LENGTH_LONG).show();
		
		try
		{
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxP = saxPF.newSAXParser();
			XMLReader xmlR = saxP.getXMLReader();
			URL url = new URL("http://" + MainContext.getResources().getString(R.string.ip_address) + "/cs/game/test.php?action=getActive");
			xmlR.setContentHandler(new XMLHandler()
			{
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
						_Games.add(this._Game);
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
			});
			xmlR.parse(new InputSource(url.openStream()));
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		
		String[] activeGames = new String[this._Games.size()];
		for (int i = 0; i < activeGames.length; i++)
		{
			activeGames[i] = this._Games.get(i).GetName();
		}
		
//		_Adapter = new ArrayAdapter<String>(MainContext, 
//				R.layout.active_game_list_item,
//				activeGames);
//		
//		ActiveGameLV.setAdapter(_Adapter);
//		
//		_Adapter.notifyDataSetChanged();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainContext, 
				R.layout.active_game_list_item,
				activeGames);
		
		ActiveGameLV.setAdapter(adapter);
	}
	
	private void joinGame(Game g)
	{
		String data = String.format("gameID=%s&userID=%s", g.GetId(), 0);
        
        URL url;
		try
		{
			url = new URL("http://" + MainContext.getResources().getString(R.string.ip_address) + "/cs/game/test.php?action=join");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
			osw.write(data);
			osw.flush();
			osw.close();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String success = br.readLine();
			
			if (success != null && !success.equals("false"))
			{
				Intent myIntent = new Intent(JoinGameActivity.this, JoinerWaitActivity.class);
				JoinGameActivity.this.startActivity(myIntent);
			}
			else
			{
				Toast.makeText(MainContext, "Unable to join game " + g.GetName(), Toast.LENGTH_LONG).show();
			}
		} catch (MalformedURLException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
