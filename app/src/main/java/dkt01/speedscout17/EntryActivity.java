package dkt01.speedscout17;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.Calendar;

public class EntryActivity extends AppCompatActivity {
    private int matchTime;
    private ScoutingDataDBHelper matchesDB;
    private Cursor matchData;

    private final int INDEX_AUTO_BASELINE_NO = 0;
    private final int INDEX_AUTO_BASELINE_YES = 1;
    private final int INDEX_AUTO_BASELINE_ATT = 2;
    private final int INDEX_AUTO_GEAR_NO = 0;
    private final int INDEX_AUTO_GEAR_YES = 1;
    private final int INDEX_AUTO_GEAR_ATT = 2;
    private final int INDEX_TELE_AIR_NO = 0;
    private final int INDEX_TELE_AIR_YES = 1;
    private final int INDEX_TELE_AIR_ATT = 2;

    private final String YES = "Y";
    private final String NO = "N";
    private final String ATTEMPTED = "A";

    private int autoLowBoilerCount = 0;
    private int autoHighBoilerCount = 0;
    private int teleLowBoilerCount = 0;
    private int teleHighBoilerCount = 0;
    private int teleGearCount = 0;

    private EditText teamNumEditText;
    private ToggleButton teamColorButton;
    private EditText matchNumEditText;
    private Spinner autoBaselineSpinner;
    private Button autoLowBoilerDecButton;
    private Button autoHighBoilerDecButton;
    private Spinner autoGearSpinner;
    private Button teleLowBoilerDecButton;
    private Button teleHighBoilerDecButton;
    private Button teleGearDecButton;
    private Spinner teleAirshipSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        Intent genesisIntent = getIntent();

        teamNumEditText = (EditText) findViewById(R.id.entry_team_number);
        teamColorButton = (ToggleButton) findViewById(R.id.entry_team_color);
        matchNumEditText = (EditText) findViewById(R.id.entry_match_number);
        autoBaselineSpinner = (Spinner) findViewById(R.id.entry_auto_baseline);
        autoLowBoilerDecButton = (Button) findViewById(R.id.entry_auto_low_boiler_dec);
        autoHighBoilerDecButton = (Button) findViewById(R.id.entry_auto_high_boiler_dec);
        autoGearSpinner = (Spinner) findViewById(R.id.entry_auto_gear);
        teleLowBoilerDecButton = (Button) findViewById(R.id.entry_tele_low_boiler_dec);
        teleHighBoilerDecButton = (Button) findViewById(R.id.entry_tele_high_boiler_dec);
        teleGearDecButton = (Button) findViewById(R.id.entry_tele_gear_dec);
        teleAirshipSpinner = (Spinner) findViewById(R.id.entry_tele_airship);

        matchTime = genesisIntent.getIntExtra(MainActivity.CREATE_MESSAGE, 0);
        matchesDB = new ScoutingDataDBHelper(this);
        if (matchTime != 0) {
            matchData = matchesDB.getMatch(matchTime);
            if (loadFromDB()) {
                getSupportActionBar().setTitle("Edit File");
            } else {
                matchTime = 0;
            }
        } else {
            // Address corner case where previous call to EntryActivity set the color to
            // blue and then the text changes back this time without changing the color
            teamColorButton.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.entry_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (saveToDb()) {
                    Intent data = new Intent();
                    if (getParent() == null) {
                        setResult(Activity.RESULT_OK, data);
                    } else {
                        getParent().setResult(Activity.RESULT_OK, data);
                    }
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("You must include a team number and match number")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                return true;

            case R.id.action_cancel:
                Intent data = new Intent();
                if (getParent() == null) {
                    setResult(Activity.RESULT_CANCELED, data);
                } else {
                    getParent().setResult(Activity.RESULT_CANCELED, data);
                }
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Log.e("???", "WHY AM I HERE?");
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean loadFromDB() {
        matchData.moveToFirst();
        if (matchData.isAfterLast()) {
            return false;
        }
        teamNumEditText.setText(String.valueOf(matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TEAM_COL_NAME))));
        teamColorButton.setChecked(matchData.getString(matchData.getColumnIndex(ScoutingDataDBHelper.ALLIANCE_COL_NAME)).equals("Blue"));
        if (teamColorButton.isChecked())
            teamColorButton.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        else
            teamColorButton.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        matchNumEditText.setText(String.valueOf(matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.MATCH_COL_NAME))));
        String autoBaseLineString = matchData.getString(matchData.getColumnIndex(ScoutingDataDBHelper.AUTO_BASELINE_COL_NAME));
        String autoGearString = matchData.getString(matchData.getColumnIndex(ScoutingDataDBHelper.AUTO_GEAR_COL_NAME));
        String teleClimbString = matchData.getString(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_CLIMB_COL_NAME));
        autoLowBoilerCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.AUTO_LOWBOILER_COL_NAME));
        autoHighBoilerCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.AUTO_HIGHBOILER_COL_NAME));
        teleLowBoilerCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_LOWBOILER_COL_NAME));
        teleHighBoilerCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_HIGHBOILER_COL_NAME));
        teleGearCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_GEAR_COL_NAME));

        autoLowBoilerDecButton.setText(String.valueOf(autoLowBoilerCount));
        autoHighBoilerDecButton.setText(String.valueOf(autoHighBoilerCount));
        teleLowBoilerDecButton.setText(String.valueOf(teleLowBoilerCount));
        teleHighBoilerDecButton.setText(String.valueOf(teleHighBoilerCount));
        teleGearDecButton.setText(String.valueOf(teleGearCount));

        if (autoBaseLineString.equals(YES)) {
            autoBaselineSpinner.setSelection(INDEX_AUTO_BASELINE_YES);
        } else if (autoBaseLineString.equals(NO)) {
            autoBaselineSpinner.setSelection(INDEX_AUTO_BASELINE_NO);
        } else if (autoBaseLineString.equals(ATTEMPTED)) {
            autoBaselineSpinner.setSelection(INDEX_AUTO_BASELINE_ATT);
        } else {
            Log.e("LoadFromDb", "Invalid autoBaseLineString:" + autoBaseLineString);
        }

        if (autoGearString.equals(YES)) {
            autoGearSpinner.setSelection(INDEX_AUTO_GEAR_YES);
        } else if (autoGearString.equals(NO)) {
            autoGearSpinner.setSelection(INDEX_AUTO_GEAR_NO);
        } else if (autoGearString.equals(ATTEMPTED)) {
            autoGearSpinner.setSelection(INDEX_AUTO_GEAR_ATT);
        } else {
            Log.e("LoadFromDb", "Invalid autoGearString:" + autoGearString);
        }

        if (teleClimbString.equals(YES)) {
            teleAirshipSpinner.setSelection(INDEX_TELE_AIR_YES);
        } else if (teleClimbString.equals(NO)) {
            teleAirshipSpinner.setSelection(INDEX_TELE_AIR_NO);
        } else if (teleClimbString.equals(ATTEMPTED)) {
            teleAirshipSpinner.setSelection(INDEX_TELE_AIR_ATT);
        } else {
            Log.e("LoadFromDb", "Invalid teleClimbString:" + teleClimbString);
        }

        return true;
    }

    public boolean saveToDb() {
        if (teamNumEditText.getText().toString().length() == 0 ||
                matchNumEditText.getText().toString().length() == 0) {
            return false;
        }

        String autoBaselineString = "";
        switch (autoBaselineSpinner.getSelectedItemPosition()) {
            case INDEX_AUTO_BASELINE_YES:
                autoBaselineString = YES;
                break;
            case INDEX_AUTO_BASELINE_ATT:
                autoBaselineString = ATTEMPTED;
                break;
            case INDEX_AUTO_BASELINE_NO:
                // Fall through
            default:
                autoBaselineString = NO;
        }

        String autoGearString = "";
        switch (autoGearSpinner.getSelectedItemPosition()) {
            case INDEX_AUTO_GEAR_YES:
                autoGearString = YES;
                break;
            case INDEX_AUTO_GEAR_ATT:
                autoGearString = ATTEMPTED;
                break;
            case INDEX_AUTO_GEAR_NO:
                // Fall through
            default:
                autoGearString = NO;
        }

        String teleClimbString = "";
        switch (teleAirshipSpinner.getSelectedItemPosition()) {
            case INDEX_TELE_AIR_YES:
                teleClimbString = YES;
                break;
            case INDEX_TELE_AIR_ATT:
                teleClimbString = ATTEMPTED;
                break;
            case INDEX_TELE_AIR_NO:
                // Fall through
            default:
                teleClimbString = NO;
        }

        int teamNum = Integer.parseInt(teamNumEditText.getText().toString());
        int matchNum = Integer.parseInt(matchNumEditText.getText().toString());
        String alliance = teamColorButton.isChecked() ? "Blue" : "Red";
        if (matchTime == 0) {
            matchTime = (int) (Calendar.getInstance().getTimeInMillis() % Integer.MAX_VALUE);
            matchesDB.insertMatch(matchTime, teamNum, alliance, matchNum, autoBaselineString, autoLowBoilerCount,
                    autoHighBoilerCount, autoGearString, teleLowBoilerCount, teleHighBoilerCount,
                    teleGearCount, teleClimbString);
        } else {
            matchesDB.updateMatch(matchTime, teamNum, alliance, matchNum, autoBaselineString, autoLowBoilerCount,
                    autoHighBoilerCount, autoGearString, teleLowBoilerCount, teleHighBoilerCount,
                    teleGearCount, teleClimbString);
        }

        return true;
    }

    public void colorClick(View view) {
        if (teamColorButton.isChecked()) {
            teamColorButton.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        } else {
            teamColorButton.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        }
    }

    public void autoLowIncClick(View view) {
        autoLowBoilerCount += 2;
        autoLowBoilerDecButton.setText(String.valueOf(autoLowBoilerCount));
    }

    public void autoLowDecClick(View view) {
        if (autoLowBoilerCount > 0) {
            autoLowBoilerCount -= 2;
            autoLowBoilerDecButton.setText(String.valueOf(autoLowBoilerCount));
        }
    }

    public void autoHighIncClick(View view) {
        autoHighBoilerCount += 2;
        autoHighBoilerDecButton.setText(String.valueOf(autoHighBoilerCount));
    }

    public void autoHighDecClick(View view) {
        if (autoHighBoilerCount > 0) {
            autoHighBoilerCount -= 2;
            autoHighBoilerDecButton.setText(String.valueOf(autoHighBoilerCount));
        }
    }

    public void teleLowIncClick(View view) {
        teleLowBoilerCount += 2;
        teleLowBoilerDecButton.setText(String.valueOf(teleLowBoilerCount));
    }

    public void teleLowDecClick(View view) {
        if (teleLowBoilerCount > 0) {
            teleLowBoilerCount -= 2;
            teleLowBoilerDecButton.setText(String.valueOf(teleLowBoilerCount));
        }
    }

    public void teleHighIncClick(View view) {
        teleHighBoilerCount += 2;
        teleHighBoilerDecButton.setText(String.valueOf(teleHighBoilerCount));
    }

    public void teleHighDecClick(View view) {
        if (teleHighBoilerCount > 0) {
            teleHighBoilerCount -= 2;
            teleHighBoilerDecButton.setText(String.valueOf(teleHighBoilerCount));
        }
    }

    public void teleGearIncClick(View view) {
        teleGearCount++;
        teleGearDecButton.setText(String.valueOf(teleGearCount));
    }

    public void teleGearDecClick(View view) {
        if (teleGearCount > 0) {
            teleGearCount--;
            teleGearDecButton.setText(String.valueOf(teleGearCount));
        }
    }
}
