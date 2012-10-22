package my.bigpackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.xml.sax.helpers.DefaultHandler;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Context MainContext;
	
	private Button CreateNewGameB;
	private Button JoinGameB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.CreateNewGameB = (Button)this.findViewById(R.id.createNewGame_Button);
        this.CreateNewGameB.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent myIntent = new Intent(MainActivity.this, CreateGameActivity.class);
        		MainActivity.this.startActivity(myIntent);
        	}
        });
        
        this.JoinGameB = (Button)this.findViewById(R.id.joinGame_Button);
        this.JoinGameB.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent myIntent = new Intent(MainActivity.this, JoinGameActivity.class);
        		MainActivity.this.startActivity(myIntent);
        	}
        });
        
//        LocationManager locMan = (LocationManager)this.getSystemService(this.LOCATION_SERVICE);
//        long networkTS = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getTime();
//        Log.e("TAG", networkTS + "");
        
//        DefaultHandler handler = new DefaultHandler() {
//        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
