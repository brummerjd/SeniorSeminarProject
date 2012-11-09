package my.bigpackage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CreatorWaitActivity extends Activity {
	
	private int _GameID;
	private CountDownTimer _Timer;
	
	private Button StartGameB;
	private TextView TimeLeftTV;
	private TextView CountdownEventTV;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.creator_wait);
        
        Intent i = this.getIntent();
        this._GameID = i.getIntExtra("gameID", -1);
        
        this.StartGameB = (Button)this.findViewById(R.id.startGame_Button);
        this.StartGameB.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		initializeGame();
        	}
        });
        
        this.TimeLeftTV = (TextView)this.findViewById(R.id.timeLeft_TextView);
        
        this.CountdownEventTV = (TextView)this.findViewById(R.id.countdownEvent_TextView);
        
        beginGameCountdownTimer();
	}
	
	private void beginGameCountdownTimer()
	{
		this._Timer = new CountDownTimer(30000, 1000)
		{
			@Override
			public void onFinish() {
				initializeGame();
			}
			@Override
			public void onTick(long millisUntilFinished) {
				TimeLeftTV.setText(Long.toString((millisUntilFinished / 1000) - 1));
			}
		};
		
		this._Timer.start();
	}
	
	private void initializeGame()
	{
		this._Timer.cancel();
		String timeString = ServerCommunicator.URLGet(this, "setStartTime", String.format("gameID=%s", this._GameID));
		
		this.CountdownEventTV.setText(" seconds until game begins.");
		
		long timeUntilGame = (Long.parseLong(timeString) - (System.currentTimeMillis() / (long)1000)) * 1000;
		CountDownTimer timer = new CountDownTimer(timeUntilGame, 1000)
		{
			@Override
			public void onFinish() {
				startGame();
			}
			@Override
			public void onTick(long millisUntilFinished) {
				TimeLeftTV.setText(Long.toString((millisUntilFinished / 1000) - 1));
			}
		};
		
		timer.start();
	}
	
	private void startGame()
	{
		Intent myIntent = new Intent(CreatorWaitActivity.this, PlayGameActivity.class);
		CreatorWaitActivity.this.startActivity(myIntent);
	}
}
