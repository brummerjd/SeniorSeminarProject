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
			String timeUntilGameString = ServerCommunicator.URLGet(MainContext, "create", String.format("gameName=%s", GameNameET.getText()));
			
			if (timeUntilGameString != null)
			{
				return timeUntilGameString;
			}
			return "";
		}
		
		protected void onPostExecute(String result)
		{
			long timeUntilGame;
			try
			{
				timeUntilGame = Long.parseLong(result);	
			}
			catch (Exception exc)
			{
				timeUntilGame = -1;
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(MainContext);
			if (timeUntilGame != -1)
			{
				Intent myIntent = new Intent(CreateGameActivity.this, CreatorWaitActivity.class);
				myIntent.putExtra("timeUntilGame", timeUntilGame);
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
