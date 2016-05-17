package com.jarrar.unievents;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jarrar.unievents.gcm.GcmRegistrationAsyncTask;

public class WelcomeActivity extends AppCompatActivity {

    CheckBox checkBoxCS, checkBoxCG, checkBoxEN, checkBoxBS, checkBoxAC;
    CheckBox[] checkBoxes;
    Switch mSwitch;
    TextView textView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sharedPreferences = this.getSharedPreferences("app_conf", MODE_PRIVATE);
        checkBoxCS = (CheckBox) findViewById(R.id.checkBoxCS);
        checkBoxCG = (CheckBox) findViewById(R.id.checkBoxCG);
        checkBoxEN = (CheckBox) findViewById(R.id.checkBoxEN);
        checkBoxBS = (CheckBox) findViewById(R.id.checkBoxBS);
        checkBoxAC = (CheckBox) findViewById(R.id.checkBoxAC);
        mSwitch = (Switch) findViewById(R.id.switchNotifications);
        textView = (TextView) findViewById(R.id.textViewWelcome);
        Intent intent = getIntent();
        if (intent.hasExtra("reset"))
            textView.setVisibility(View.GONE);
        mSwitch.setChecked(sharedPreferences.getBoolean("Notify", true));

        //GCM registration
        new GcmRegistrationAsyncTask(this).execute();
    }

    public void saveData(View v) {
        StringBuilder stringBuilder = new StringBuilder();
        checkBoxes = new CheckBox[]{checkBoxCS, checkBoxCG, checkBoxEN, checkBoxBS, checkBoxAC};
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked())
                stringBuilder.append(checkBox.getText()).append(',');
        }
        boolean mSwitchChecked = mSwitch.isChecked();
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("Interest", stringBuilder.toString());
        edit.putBoolean("Notify", mSwitchChecked);
        edit.apply();
        Toast.makeText(this, "Your interest was saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
