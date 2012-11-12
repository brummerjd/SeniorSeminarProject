package my.bigpackage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class JoinerWaitActivity extends Activity {
	
	private long _TimeUntilGame;
	private CountDownTimer _Timer;
	
	private TextView TimeLeftTV;
	private TextView CountdownEventTV;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.joiner_wait);
        
        Intent i = this.getIntent();
        this._TimeUntilGame = i.getLongExtra("timeUntilGame", -1);
        
        this.TimeLeftTV = (TextView)this.findViewById(R.id.timeLeft_TextView);
        
        this.CountdownEventTV = (TextView)this.findViewById(R.id.countdownEvent_TextView);
        
        if (this._TimeUntilGame > 0)
        {
        	this.beginGameCountdownTimer();	
        }
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
		Intent myIntent = new Intent(JoinerWaitActivity.this, PlayGameActivity.class);
		JoinerWaitActivity.this.startActivity(myIntent);
	}
}
