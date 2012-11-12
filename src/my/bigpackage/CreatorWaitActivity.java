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
	
	private long _TimeUntilGame;
	private CountDownTimer _Timer;
	
	private TextView TimeLeftTV;
	private TextView CountdownEventTV;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.creator_wait);
        
        Intent i = this.getIntent();
        this._TimeUntilGame = i.getLongExtra("timeUntilGame", 30000);
        
        this.TimeLeftTV = (TextView)this.findViewById(R.id.timeLeft_TextView);
        
        this.CountdownEventTV = (TextView)this.findViewById(R.id.countdownEvent_TextView);
        
        this.beginGameCountdownTimer();
	}
	
	private void beginGameCountdownTimer()
	{
		this._Timer = new CountDownTimer(this._TimeUntilGame * 1000, 1000)
		{
			@Override
			public void onFinish() {
				initializeGame();
				this.cancel();
			}
			@Override
			public void onTick(long millisUntilFinished) {
				TimeLeftTV.setText(Long.toString((millisUntilFinished / 1000)));
			}
		};
		
		this._Timer.start();
	}
	
	private void initializeGame()
	{
		this.CountdownEventTV.setText(" seconds until game begins.");
		
		CountDownTimer timer = new CountDownTimer(5000, 1000)
		{
			@Override
			public void onFinish() {
				startGame();
				this.cancel();
			}
			@Override
			public void onTick(long millisUntilFinished) {
				TimeLeftTV.setText(Long.toString((millisUntilFinished / 1000)));
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
