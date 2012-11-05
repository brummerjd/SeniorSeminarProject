package my.bigpackage;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreatorWaitActivity extends Activity {
	
	private Button StartGameB;
	private TextView TimeLeftTV;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.creator_wait);
        
        this.StartGameB = (Button)this.findViewById(R.id.startGame_Button);
        this.StartGameB.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		startGame();
        	}
        });
        
        this.TimeLeftTV = (TextView)this.findViewById(R.id.timeLeft_TextView);
        
        beginGameCountdownTimer();
	}
	
	private void beginGameCountdownTimer()
	{
		CountDownTimer timer = new CountDownTimer(30000, 1000)
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
		
	}
}
