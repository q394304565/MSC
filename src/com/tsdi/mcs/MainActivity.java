package com.tsdi.mcs;

import java.util.ArrayList;
import java.util.List;

import com.google.zxing.client.android.Intents;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemSelected;
import com.lidroid.xutils.view.annotation.event.OnNothingSelected;
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;
import com.tsdi.mcs.fragments.EventFragment;
import com.tsdi.mcs.fragments.MainFragment;
import com.tsdi.mcs.fragments.MapFragment;
import com.tsdi.mcs.fragments.MyFragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

@ContentView(R.layout.activity_main)
public class MainActivity extends Activity {
	@ViewInject(R.id.line)
	Spinner						spinner;
	@ViewInject(R.id.scanButton)
	private ImageView			scanButton;
	@ViewInject(R.id.radio_group)
	RadioGroup					group;

	ArrayAdapter<String>		adapter;
	private List<String>		list			= new ArrayList<String>();
	private List<Fragment>		mFragments;

	private final static int	REQ_CODE_SCAN	= 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		mFragments = new ArrayList<Fragment>();
		mFragments.add(new MainFragment());
		mFragments.add(new EventFragment());
		mFragments.add(new MapFragment());
		mFragments.add(new MyFragment());

		list.add("京沪高铁");
		list.add("石济客专");

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spinner.setAdapter(adapter);
		
		group.check(1);
	}

	@OnItemSelected(R.id.line)
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@OnNothingSelected(R.id.line)
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@OnRadioGroupCheckedChange(R.id.radio_group)
	public void onCheckedChanged(RadioGroup arg0, int checkedId) {
		getFragmentManager().beginTransaction().replace(R.id.container, mFragments.get(checkedId - 1)).commit();
	}

	@OnClick(R.id.scanButton)
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.scanButton: {
			Intent intent = new Intent(Intents.Scan.ACTION);
			intent.putExtra(Intents.Scan.PROMPT_MESSAGE, "");
			// intent.putExtra(Intents.Scan.FORMATS, Intents.Scan.QR_CODE_MODE);
			intent.putExtra(Intents.Scan.NO_SHARE, true);
			intent.putExtra(Intents.Scan.NO_HISTORY, true);
			intent.putExtra(Intents.Scan.NO_HELP, true);
			// intent.putExtra(Intents.Scan.NO_SETTING, true);
			intent.putExtra(Intents.Scan.NO_STATUS, true);
			startActivityForResult(intent, REQ_CODE_SCAN);
		}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQ_CODE_SCAN:
				String result = data.getStringExtra(Intents.Scan.RESULT);
				String format = data.getStringExtra(Intents.Scan.RESULT_FORMAT);
				Toast.makeText(this, result + "\n" + format, Toast.LENGTH_LONG).show();
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
