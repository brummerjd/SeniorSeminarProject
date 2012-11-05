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
			String data = String.format("gameName=%s", GameNameET.getText());
	        
	        URL url;
			try
			{
				url = new URL("http://" + MainContext.getResources().getString(R.string.ip_address) + "/cs/game/test.php?action=create");
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
				osw.write(data);
				osw.flush();
				osw.close();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String gameName = br.readLine();
				
				if (gameName != null && !gameName.equals(""))
				{
					return gameName;
				}
			} catch (MalformedURLException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return "";
		}
		
		protected void onPostExecute(String result)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(MainContext);
			if (result != null && !result.equals(""))
			{
				builder.setMessage("Game '" + result + "' successfully created!")
					   .setCancelable(false)
					   .setPositiveButton("OK", null);
				builder.create().show();
				
				Intent myIntent = new Intent(CreateGameActivity.this, CreatorWaitActivity.class);
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
