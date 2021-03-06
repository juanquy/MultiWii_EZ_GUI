/*  MultiWii EZ-GUI
    Copyright (C) <2012>  Bartosz Szczygiel (eziosoft)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ezio.multiwii;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class PIDActivity extends SherlockActivity {

	App app;
	ActionBarSherlock actionBar;

	EditText P1;
	EditText P2;
	EditText P3;
	EditText P4;
	EditText P5;
	EditText P6;
	EditText P7;
	EditText P8;
	EditText P9;

	EditText D1;
	EditText D2;
	EditText D3;
	EditText D4;
	EditText D5;
	EditText D6;
	EditText D7;
	EditText D8;
	EditText D9;

	EditText I1;
	EditText I2;
	EditText I3;
	EditText I4;
	EditText I5;
	EditText I6;
	EditText I7;
	EditText I8;
	EditText I9;

	EditText RatePitchRoll1;
	EditText RatePitchRoll2;
	EditText RateYaw;

	EditText MIDThrottle;
	EditText EXPOThrottle;

	EditText RATE2PitchRoll;
	EditText EXPOPitchRoll;

	EditText TPA;

	// used for write
	float[] P;
	float[] I;
	float[] D;

	float confRC_RATE = 0, confRC_EXPO = 0, rollPitchRate = 0, yawRate = 0,
			dynamic_THR_PID = 0, throttle_MID = 0, throttle_EXPO = 0;

	// ///

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pid);

		app = (App) getApplication();
		actionBar = getSherlock();

		P = new float[app.mw.PIDITEMS];
		I = new float[app.mw.PIDITEMS];
		D = new float[app.mw.PIDITEMS];

		P1 = (EditText) findViewById(R.id.P1);
		P2 = (EditText) findViewById(R.id.P2);
		P3 = (EditText) findViewById(R.id.P3);
		P4 = (EditText) findViewById(R.id.P4);
		P5 = (EditText) findViewById(R.id.P5);
		P6 = (EditText) findViewById(R.id.P6);
		P7 = (EditText) findViewById(R.id.P7);
		P8 = (EditText) findViewById(R.id.P8);
		P9 = (EditText) findViewById(R.id.P9);

		D1 = (EditText) findViewById(R.id.D1);
		D2 = (EditText) findViewById(R.id.D2);
		D3 = (EditText) findViewById(R.id.D3);
		D4 = (EditText) findViewById(R.id.D4);
		D5 = (EditText) findViewById(R.id.D5);
		D6 = (EditText) findViewById(R.id.D6);
		D7 = (EditText) findViewById(R.id.D7);
		D8 = (EditText) findViewById(R.id.D8);
		D9 = (EditText) findViewById(R.id.D9);

		I1 = (EditText) findViewById(R.id.I1);
		I2 = (EditText) findViewById(R.id.I2);
		I3 = (EditText) findViewById(R.id.I3);
		I4 = (EditText) findViewById(R.id.I4);
		I5 = (EditText) findViewById(R.id.I5);
		I6 = (EditText) findViewById(R.id.I6);
		I7 = (EditText) findViewById(R.id.I7);
		I8 = (EditText) findViewById(R.id.I8);
		I9 = (EditText) findViewById(R.id.I9);

		RatePitchRoll1 = (EditText) findViewById(R.id.editTextRatePitchRoll1);
		RatePitchRoll2 = (EditText) findViewById(R.id.editTextRatePitchRoll2);
		RateYaw = (EditText) findViewById(R.id.editTextRateYaw);

		MIDThrottle = (EditText) findViewById(R.id.editTextMIDThrottle);
		EXPOThrottle = (EditText) findViewById(R.id.editTextEXPOThrottle);

		RATE2PitchRoll = (EditText) findViewById(R.id.editTextRate2PitchRoll);
		EXPOPitchRoll = (EditText) findViewById(R.id.editTextEXPOPitchRoll);

		TPA = (EditText) findViewById(R.id.editTextTPA);

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		app.ForceLanguage();
		app.Say(getString(R.string.PID));
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		while (app.bt.available() > 0) {
			app.mw.ProcessSerialData(false);
		}
		ShowData();
	}

	public void ReadOnClick(View v) {
		app.mw.SendRequestGetPID();
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		app.mw.ProcessSerialData(false);

		ShowData();

	}

	public void ResetOnClick(View v) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				getString(R.string.ResetALLnotonlyPIDparamstodefault))
				.setCancelable(false)
				.setPositiveButton(getString(R.string.Yes),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int id) {

								app.mw.SendRequestResetSettings();
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								ReadOnClick(null);
								Toast.makeText(
										getApplicationContext(),
										getString(R.string.ValuesaresettodefaultPressreadbuttonnow),
										Toast.LENGTH_LONG).show();

							}
						})
				.setNegativeButton(getString(R.string.No),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();

	}

	void ShareIt() {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody ="";
		for(int i =0;i<=8;i++)
		{
			shareBody+=String.valueOf(P[i])+"\t|\t"+String.valueOf(I[i])+"\t|\t"+String.valueOf(D[i])+"\n";
		}
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"MultiWii PID");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}

	public void SetOnClick(View v) {

		// Log.d("aaaaa",RATE2PitchRoll.getText().toString());
		confRC_RATE = Float.parseFloat(RATE2PitchRoll.getText().toString()
				.replace(",", "."));
		confRC_EXPO = Float.parseFloat(EXPOPitchRoll.getText().toString()
				.replace(",", "."));
		rollPitchRate = Float.parseFloat(RatePitchRoll1.getText().toString()
				.replace(",", "."));
		yawRate = Float.parseFloat(RateYaw.getText().toString()
				.replace(",", "."));
		dynamic_THR_PID = Float.parseFloat(TPA.getText().toString()
				.replace(",", "."));
		throttle_MID = Float.parseFloat(MIDThrottle.getText().toString()
				.replace(",", "."));
		throttle_EXPO = Float.parseFloat(EXPOThrottle.getText().toString()
				.replace(",", "."));

		P[0] = Float.parseFloat(P1.getText().toString().replace(",", "."));
		P[1] = Float.parseFloat(P2.getText().toString().replace(",", "."));
		P[2] = Float.parseFloat(P3.getText().toString().replace(",", "."));
		P[3] = Float.parseFloat(P4.getText().toString().replace(",", "."));
		P[4] = Float.parseFloat(P5.getText().toString().replace(",", "."));
		P[5] = Float.parseFloat(P6.getText().toString().replace(",", "."));
		P[6] = Float.parseFloat(P7.getText().toString().replace(",", "."));
		P[7] = Float.parseFloat(P8.getText().toString().replace(",", "."));
		P[8] = Float.parseFloat(P9.getText().toString().replace(",", "."));

		I[0] = Float.parseFloat(I1.getText().toString().replace(",", "."));
		I[1] = Float.parseFloat(I2.getText().toString().replace(",", "."));
		I[2] = Float.parseFloat(I3.getText().toString().replace(",", "."));
		I[3] = Float.parseFloat(I4.getText().toString().replace(",", "."));
		I[4] = Float.parseFloat(I5.getText().toString().replace(",", "."));
		I[5] = Float.parseFloat(I6.getText().toString().replace(",", "."));
		I[6] = Float.parseFloat(I7.getText().toString().replace(",", "."));
		I[7] = Float.parseFloat(I8.getText().toString().replace(",", "."));
		I[8] = Float.parseFloat(I9.getText().toString().replace(",", "."));

		D[0] = Float.parseFloat(D1.getText().toString().replace(",", "."));
		D[1] = Float.parseFloat(D2.getText().toString().replace(",", "."));
		D[2] = Float.parseFloat(D3.getText().toString().replace(",", "."));
		D[3] = Float.parseFloat(D4.getText().toString().replace(",", "."));
		D[4] = Float.parseFloat(D5.getText().toString().replace(",", "."));
		D[5] = Float.parseFloat(D6.getText().toString().replace(",", "."));
		D[6] = Float.parseFloat(D7.getText().toString().replace(",", "."));
		D[7] = Float.parseFloat(D8.getText().toString().replace(",", "."));
		D[8] = Float.parseFloat(D9.getText().toString().replace(",", "."));

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.Continue))
				.setCancelable(false)
				.setPositiveButton(getString(R.string.Yes),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int id) {

								app.mw.SendRequestSetPID(confRC_RATE,
										confRC_EXPO, rollPitchRate, yawRate,
										dynamic_THR_PID, throttle_MID,
										throttle_EXPO, P, I, D);

								app.mw.SendRequestWriteToEEprom();

								Toast.makeText(getApplicationContext(),
										getString(R.string.Done),
										Toast.LENGTH_SHORT).show();

							}
						})
				.setNegativeButton(getString(R.string.No),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();

	}

	// private void SaveOnClick() {
	// AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// builder.setMessage(getString(R.string.Continue))
	// .setCancelable(false)
	// .setPositiveButton(getString(R.string.Yes),
	// new DialogInterface.OnClickListener() {
	//
	// public void onClick(DialogInterface dialog, int id) {
	//
	// app.mw.SendRequestWriteToEEprom();
	// Toast.makeText(getApplicationContext(),
	// getString(R.string.Done),
	// Toast.LENGTH_SHORT).show();
	//
	// }
	// })
	// .setNegativeButton(getString(R.string.No),
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int id) {
	// dialog.cancel();
	// }
	// });
	// AlertDialog alert = builder.create();
	// alert.show();
	// }

	private void ShowData() {

		P1.setText(String.format("%.1f", (float) app.mw.byteP[0] / 10.0));
		P2.setText(String.format("%.1f", (float) app.mw.byteP[1] / 10.0));
		P3.setText(String.format("%.1f", (float) app.mw.byteP[2] / 10.0));
		P4.setText(String.format("%.1f", (float) app.mw.byteP[3] / 10.0));
		P5.setText(String.format("%.2f", (float) app.mw.byteP[4] / 100.0));
		P6.setText(String.format("%.1f", (float) app.mw.byteP[5] / 10.0));
		P7.setText(String.format("%.1f", (float) app.mw.byteP[6] / 10.0));
		P8.setText(String.format("%.1f", (float) app.mw.byteP[7] / 10.0));
		P9.setText(String.format("%.1f", (float) app.mw.byteP[8] / 10.0));

		I1.setText(String.format("%.3f", (float) app.mw.byteI[0] / 1000.0));
		I2.setText(String.format("%.3f", (float) app.mw.byteI[1] / 1000.0));
		I3.setText(String.format("%.3f", (float) app.mw.byteI[2] / 1000.0));
		I4.setText(String.format("%.3f", (float) app.mw.byteI[3] / 1000.0));
		I5.setText(String.format("%.1f", (float) app.mw.byteI[4] / 100.0));
		I6.setText(String.format("%.2f", (float) app.mw.byteI[5] / 100.0));
		I7.setText(String.format("%.2f", (float) app.mw.byteI[6] / 100.0));
		I8.setText(String.format("%.3f", (float) app.mw.byteI[7] / 1000.0));
		I9.setText(String.format("%.3f", (float) app.mw.byteI[8] / 1000.0));

		D1.setText(String.format("%.0f", (float) app.mw.byteD[0]));
		D2.setText(String.format("%.0f", (float) app.mw.byteD[1]));
		D3.setText(String.format("%.0f", (float) app.mw.byteD[2]));
		D4.setText(String.format("%.0f", (float) app.mw.byteD[3]));
		D5.setText(String.format("%.0f", (float) app.mw.byteD[4]));
		D6.setText(String.format("%.3f", (float) app.mw.byteD[5] / 1000.0));
		D7.setText(String.format("%.3f", (float) app.mw.byteD[6] / 1000.0));
		D8.setText(String.format("%.0f", (float) app.mw.byteD[7]));
		D9.setText(String.format("%.3f", (float) app.mw.byteD[8]));

		RatePitchRoll1.setText(String.format("%.2f",
				(float) app.mw.byteRollPitchRate / 100.0));
		RatePitchRoll2.setText(String.format("%.2f",
				(float) app.mw.byteRollPitchRate / 100.0));

		RateYaw.setText(String.format("%.2f",
				(float) app.mw.byteYawRate / 100.0));

		MIDThrottle.setText(String.format("%.2f",
				(float) app.mw.byteThrottle_MID / 100.0));
		EXPOThrottle.setText(String.format("%.2f",
				(float) app.mw.byteThrottle_EXPO / 100.0));

		RATE2PitchRoll.setText(String.format("%.2f",
				(float) app.mw.byteRC_RATE / 100.0));
		EXPOPitchRoll.setText(String.format("%.2f",
				(float) app.mw.byteRC_EXPO / 100.0));

		TPA.setText(String.format("%.2f", (float) app.mw.byteDynThrPID / 100.0));
	}

	// /////menu////////
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_pid, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.MenuReadPID) {
			ReadOnClick(null);
			return true;
		}

		if (item.getItemId() == R.id.MenuSavePID) {
			SetOnClick(null);
			return true;
		}

		// if (item.getItemId() == R.id.MenuSetPID) {
		// SetOnClick(null);
		// return true;
		// }

		if (item.getItemId() == R.id.MenuResetPID) {
			ResetOnClick(null);
			return true;
		}

		if (item.getItemId() == R.id.MenuSharePID) {
			ShareIt();
			return true;
		}

		return false;
	}

	// ///menu end//////

}
