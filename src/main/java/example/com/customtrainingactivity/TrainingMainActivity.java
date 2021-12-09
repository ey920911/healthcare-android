package example.com.customtrainingactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import example.com.R;

public class TrainingMainActivity extends Activity implements OnClickListener {

	private ImageView walking_btn;
	private ImageView pushup_btn;
	private ImageView situp_btn;
	private ImageView running_btn;
	private Intent intent=null;
	private String state =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trainingmain);

		walking_btn = (ImageView) findViewById(R.id.Walking_btn);
		pushup_btn = (ImageView) findViewById(R.id.Pushup_btn);
		situp_btn = (ImageView) findViewById(R.id.Situp_btn);
		running_btn = (ImageView) findViewById(R.id.Running_btn);


		walking_btn.setOnClickListener(this);
		pushup_btn.setOnClickListener(this);
		situp_btn.setOnClickListener(this);
		running_btn.setOnClickListener(this);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Walking_btn:
			state = "Walking";
			break;
		case R.id.Pushup_btn:
			state = "PushUp";
			break;
		case R.id.Situp_btn:
			state = "SitUp";
			break;
		case R.id.Running_btn:
			state = "Running";
			break;

		default:
			break;
		}
		intent = new Intent(this, TrainingActivity.class);
		intent.putExtra("state", state);
		startActivity(intent);

	}

}
