package com.tsdi.mcs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity implements OnClickListener {
	Button btnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setTitle(R.string.title_login);

		btnLogin = (Button) findViewById(R.id.login);
		btnLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		startActivity(new Intent(this, MainActivity.class));
	}
}
