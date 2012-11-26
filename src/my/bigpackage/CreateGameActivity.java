package my.bigpackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGameActivity extends Activity
{
	private Context MainContext;
	
	private Button CreateGameB;
	private EditText GameNameET;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.create_game);
        
        MainContext = this;
        
        this.CreateGameB = (Button)this.findViewById(R.id.createGame_Button);
        this.CreateGameB.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		if (!GameNameET.getText().equals(""))
        		{
        			CreateGameTask task = new CreateGameTask();
            		task.execute();
        		}
        		else
        		{
        			Toast.makeText(MainContext, "Enter name of game.", Toast.LENGTH_LONG).show();
        		}
        	}
        });
        
        this.GameNameET = (EditText) this.findViewById(R.id.gameName_EditText);
	}
	
	private class CreateGameTask extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... arg0)
		{
			String timeAndIdsString = ServerCommunicator.URLGet(MainContext, "create", String.format("gameName=%s", GameNameET.getText()));
			
			if (timeAndIdsString != null)
			{
				return timeAndIdsString;
			}
			return "";
		}
		
		protected void onPostExecute(String result)
		{
			String[] timeAndIds = result.split(",");
			
			long timeUntilGame;
			try
			{
				timeUntilGame = Long.parseLong(timeAndIds[0]);	
			}
			catch (Exception exc)
			{
				timeUntilGame = -1;
			}
			
			long gameID;
			try
			{
				gameID = Long.parseLong(timeAndIds[2]);
				Log.e("TAG", Long.toString(gameID));
			}
			catch (Exception exc)
			{
				gameID = -1;
			}
			
			long userID;
			try
			{
				userID = Long.parseLong(timeAndIds[1]);	
			}
			catch (Exception exc)
			{
				userID = -1;
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(MainContext);
			if (timeUntilGame != -1 && gameID != -1 && userID != -1)
			{
				Intent myIntent = new Intent(CreateGameActivity.this, CreatorWaitActivity.class);
				myIntent.putExtra("timeUntilGame", timeUntilGame);
				myIntent.putExtra("gameID", gameID);
				myIntent.putExtra("userID", userID);
        		CreateGameActivity.this.startActivity(myIntent);
			}
			else
			{
				builder.setMessage("Unable to create new game.")
				   .setCancelable(false)
				   .setPositiveButton("OK", null);
				builder.create().show();
			}
		}
    }
}
