package my.bigpackage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

public class PlayGameActivity extends Activity implements SensorEventListener {
	
	private final double THRESHOLD = 15;
	
	private long _GameID;
	private long _UserID;
	
	private SensorManager _SensorManager;
	private Sensor _Accelerometer;
	private MediaPlayer _Player;
	
	private TextView GameMessageTV;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.play_game);
        
        Intent i = this.getIntent();
        this._GameID = i.getLongExtra("gameID", -1);
        this._UserID = i.getLongExtra("userID", -1);
        
        this._SensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
        this._Accelerometer = this._SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        this._SensorManager.registerListener(this, this._Accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        
        this._Player = MediaPlayer.create(this, R.raw.explosion);
        
        this.GameMessageTV = (TextView)this.findViewById(R.id.gameMessage_TextView);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	public void onSensorChanged(SensorEvent event) {
		double x = event.values[0];
		double y = event.values[1];
		double z = event.values[2];
		
		double vectorAcceleration = Math.sqrt(x*x + y*y + z*z);
		
		if (vectorAcceleration > this.THRESHOLD)
		{
			this.loseGame();
		}
	}
	
	public void loseGame()
	{
		this._Accelerometer = null;
		
		this._Player.start();
		this.GameMessageTV.setText("You lose!");
		
		String status = ServerCommunicator.URLGet(this, "lose", String.format("gameID=%s&userID=%s", this._GameID, this._UserID));
		
		if (status.equals("WINNER"))
		{
			this.GameMessageTV.setText("WINNER WINNER WINNER!!!");
		}
		else
		{
			this.GameMessageTV.setText("You totally just pulled a Scott...");
		}
	}
}
