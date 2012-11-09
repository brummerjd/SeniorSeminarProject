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
			String gameIDString = ServerCommunicator.URLGet(MainContext, "create", String.format("gameName=%s", GameNameET.getText()));
					
			if (gameIDString != null)
			{
				//Toast.makeText(MainContext, gameIDString, Toast.LENGTH_LONG).show();
				return gameIDString;
			}
			return "";
		}
		
		protected void onPostExecute(String result)
		{
			int gameID;
			try
			{
				gameID = Integer.parseInt(result);	
			}
			catch (Exception exc)
			{
				gameID = -1;
			}
			
			
			AlertDialog.Builder builder = new AlertDialog.Builder(MainContext);
			if (gameID != -1)
			{
				builder.setMessage("Game '" + result + "' successfully created!")
					   .setCancelable(false)
					   .setPositiveButton("OK", null);
				builder.create().show();
				
				Intent myIntent = new Intent(CreateGameActivity.this, CreatorWaitActivity.class);
				myIntent.putExtra("gameID", gameID);
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
