package dkt01.speedscout17;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

public class EntryActivity extends ActionBarActivity {
    private int matchTime;
    private ScoutingDataDBHelper matchesDB;
    private Cursor matchData;

    private final int INDEX_AUTO_MOTIONLESS   = 0;
    private final int INDEX_AUTO_REACHED      = 1;
    private final int INDEX_AUTO_PORTCULLIS   = 2;
    private final int INDEX_AUTO_CHEVAL       = 3;
    private final int INDEX_AUTO_MOAT         = 4;
    private final int INDEX_AUTO_RAMPARTS     = 5;
    private final int INDEX_AUTO_DRAWBRIDGE   = 6;
    private final int INDEX_AUTO_SALLYPORT    = 7;
    private final int INDEX_AUTO_ROCKWALL     = 8;
    private final int INDEX_AUTO_ROUGHTERRAIN = 9;
    private final int INDEX_AUTO_LOWBAR       = 10;
    private final int INDEX_AUTO_NOGOAL       = 0;
    private final int INDEX_AUTO_LOWGOAL      = 1;
    private final int INDEX_AUTO_HIGHGOAL     = 2;
    private final int INDEX_TOWER_FAIL        = 0;
    private final int INDEX_TOWER_CHALLENGE   = 1;
    private final int INDEX_TOWER_SCALE       = 2;

    private final String AUTO_PORTCULLIS      = "Portcullis";
    private final String AUTO_CHEVAL          = "Cheval De Frise";
    private final String AUTO_MOAT            = "Moat";
    private final String AUTO_RAMPARTS        = "Ramparts";
    private final String AUTO_DRAWBRIDGE      = "Drawbridge";
    private final String AUTO_SALLYPORT       = "Sally Port";
    private final String AUTO_ROCKWALL        = "Rock Wall";
    private final String AUTO_ROUGHTERRAIN    = "Rough Terrain";
    private final String AUTO_LOWBAR          = "Low Bar";

    private int lowGoalCount = 0;
    private int highGoalCount = 0;
    private int portcullisCount = 0;
    private int chevalCount = 0;
    private int moatCount = 0;
    private int rampartsCount = 0;
    private int drawbridgeCount = 0;
    private int sallyPortCount = 0;
    private int rockWallCount = 0;
    private int roughTerrainCount = 0;
    private int lowBarCount = 0;
    private int blockRobotCount = 0;
    private int blockShotCount = 0;

    private EditText       teamNumEditText;
    private ToggleButton   teamColorButton;
    private EditText       matchNumEditText;
    private Spinner        autoDefenseSpinner;
    private Spinner        autoGoalSpinner;
    private Button         lowGoalDecButton;
    private Button         highGoalDecButton;
    private Button         portcullisDecButton;
    private Button         chevalDecButton;
    private Button         moatDecButton;
    private Button         rampartsDecButton;
    private Button         drawbridgeDecButton;
    private Button         sallyPortDecButton;
    private Button         rockWallDecButton;
    private Button         roughTerrainDecButton;
    private Button         lowBarDecButton;
    private Button         blockRobotDecButton;
    private Button         blockShotDecButton;
    private Spinner        towerSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        Intent genesisIntent = getIntent();

        teamNumEditText       = (EditText)findViewById(R.id.entry_team_number);
        teamColorButton       = (ToggleButton) findViewById(R.id.entry_team_color);
        matchNumEditText      = (EditText)findViewById(R.id.entry_match_number);
        autoDefenseSpinner    = (Spinner)findViewById(R.id.entry_auto_defense);
        autoGoalSpinner       = (Spinner)findViewById(R.id.entry_auto_goal);
        lowGoalDecButton      = (Button)findViewById(R.id.entry_low_goal_dec);
        highGoalDecButton     = (Button)findViewById(R.id.entry_high_goal_dec);
        portcullisDecButton   = (Button)findViewById(R.id.entry_portcullis_dec);
        chevalDecButton       = (Button)findViewById(R.id.entry_cheval_dec);
        moatDecButton         = (Button)findViewById(R.id.entry_moat_dec);
        rampartsDecButton     = (Button)findViewById(R.id.entry_ramparts_dec);
        drawbridgeDecButton   = (Button)findViewById(R.id.entry_drawbridge_dec);
        sallyPortDecButton    = (Button)findViewById(R.id.entry_sallyport_dec);
        rockWallDecButton     = (Button)findViewById(R.id.entry_rockwall_dec);
        roughTerrainDecButton = (Button)findViewById(R.id.entry_roughterrain_dec);
        lowBarDecButton       = (Button)findViewById(R.id.entry_lowbar_dec);
        blockRobotDecButton   = (Button)findViewById(R.id.entry_robotblock_dec);
        blockShotDecButton    = (Button)findViewById(R.id.entry_shotblock_dec);
        towerSpinner          = (Spinner)findViewById(R.id.entry_tower);

        matchTime = genesisIntent.getIntExtra(MainActivity.CREATE_MESSAGE,0);
        matchesDB = new ScoutingDataDBHelper(this);
        if(matchTime != 0)
        {
            matchData = matchesDB.getMatch(matchTime);
            if(loadFromDB())
            {
                getSupportActionBar().setTitle("Edit File");
            }
            else
            {
                matchTime = 0;
            }
        }
        else
        {
            // Address corner case where previous call to EntryActivity set the color to
            // blue and then the text changes back this time without changing the color
            teamColorButton.setBackgroundColor(getResources().getColor(R.color.red));
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
                if(saveToDb())
                {
                    Intent data = new Intent();
                    if (getParent() == null) {
                        setResult(Activity.RESULT_OK, data);
                    } else {
                        getParent().setResult(Activity.RESULT_OK, data);
                    }
                    finish();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("You must include a team number and match number")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {}
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
                Log.e("???","WHY AM I HERE?");
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean loadFromDB()
    {
        matchData.moveToFirst();
        if(matchData.isAfterLast())
        {
            return false;
        }
        teamNumEditText.setText(String.valueOf(matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TEAM_COL_NAME))));
        teamColorButton.setChecked(matchData.getString(matchData.getColumnIndex(ScoutingDataDBHelper.ALLIANCE_COL_NAME)).equals("Blue"));
        if(teamColorButton.isChecked())
            teamColorButton.setBackgroundColor(getResources().getColor(R.color.blue));
        else
            teamColorButton.setBackgroundColor(getResources().getColor(R.color.red));
        matchNumEditText.setText(String.valueOf(matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.MATCH_COL_NAME))));
        String autoReachString = matchData.getString(matchData.getColumnIndex(ScoutingDataDBHelper.AUTO_REACHED_COL_NAME));
        String autoCrossString = matchData.getString(matchData.getColumnIndex(ScoutingDataDBHelper.AUTO_CROSSED_COL_NAME));
        String autoLowString = matchData.getString(matchData.getColumnIndex(ScoutingDataDBHelper.AUTO_LOWGOAL_COL_NAME));
        String autoHighString = matchData.getString(matchData.getColumnIndex(ScoutingDataDBHelper.AUTO_HIGHGOAL_COL_NAME));
        lowGoalCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_LOWGOAL_COL_NAME));
        highGoalCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_HIGHGOAL_COL_NAME));
        portcullisCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_PORTCULLIS_COL_NAME));
        chevalCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_CHEVAL_COL_NAME));
        moatCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_MOAT_COL_NAME));
        rampartsCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_RAMP_COL_NAME));
        drawbridgeCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_DRAWBRIDGE_COL_NAME));
        sallyPortCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_SALLYPORT_COL_NAME));
        rockWallCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_ROCKWALL_COL_NAME));
        roughTerrainCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_ROUGHTERRAIN_COL_NAME));
        lowBarCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_LOWBAR_COL_NAME));
        blockRobotCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_BLOCKROBOT_COL_NAME));
        blockShotCount = matchData.getInt(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_BLOCKSHOT_COL_NAME));
        String challengeString = matchData.getString(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_CHALLENGE_COL_NAME));
        String scaleString = matchData.getString(matchData.getColumnIndex(ScoutingDataDBHelper.TELE_SCALE_COL_NAME));

        lowGoalDecButton.setText(String.valueOf(lowGoalCount));
        highGoalDecButton.setText(String.valueOf(highGoalCount));
        portcullisDecButton.setText(String.valueOf(portcullisCount));
        chevalDecButton.setText(String.valueOf(chevalCount));
        moatDecButton.setText(String.valueOf(moatCount));
        rampartsDecButton.setText(String.valueOf(rampartsCount));
        drawbridgeDecButton.setText(String.valueOf(drawbridgeCount));
        sallyPortDecButton.setText(String.valueOf(sallyPortCount));
        rockWallDecButton.setText(String.valueOf(rockWallCount));
        roughTerrainDecButton.setText(String.valueOf(roughTerrainCount));
        lowBarDecButton.setText(String.valueOf(lowBarCount));
        blockRobotDecButton.setText(String.valueOf(blockRobotCount));
        blockShotDecButton.setText(String.valueOf(blockShotCount));

        if(autoReachString.equals("Y"))
        {
            autoDefenseSpinner.setSelection(INDEX_AUTO_REACHED);
        }
        else if(autoCrossString.equals("N"))
        {
            autoDefenseSpinner.setSelection(INDEX_AUTO_MOTIONLESS);
        }
        else
        {
            switch(autoCrossString)
            {
                case AUTO_PORTCULLIS:
                    autoDefenseSpinner.setSelection(INDEX_AUTO_PORTCULLIS);
                    break;
                case AUTO_CHEVAL:
                    autoDefenseSpinner.setSelection(INDEX_AUTO_CHEVAL);
                    break;
                case AUTO_MOAT:
                    autoDefenseSpinner.setSelection(INDEX_AUTO_MOAT);
                    break;
                case AUTO_RAMPARTS:
                    autoDefenseSpinner.setSelection(INDEX_AUTO_RAMPARTS);
                    break;
                case AUTO_DRAWBRIDGE:
                    autoDefenseSpinner.setSelection(INDEX_AUTO_DRAWBRIDGE);
                    break;
                case AUTO_SALLYPORT:
                    autoDefenseSpinner.setSelection(INDEX_AUTO_SALLYPORT);
                    break;
                case AUTO_ROCKWALL:
                    autoDefenseSpinner.setSelection(INDEX_AUTO_ROCKWALL);
                    break;
                case AUTO_ROUGHTERRAIN:
                    autoDefenseSpinner.setSelection(INDEX_AUTO_ROUGHTERRAIN);
                    break;
                case AUTO_LOWBAR:
                    autoDefenseSpinner.setSelection(INDEX_AUTO_LOWBAR);
                    break;
                default:
                    Log.e("LoadFromDb","Invalid autoCrossString:"+autoCrossString);
            }
        }

        if(autoLowString.equals("Y"))
        {
            autoGoalSpinner.setSelection(INDEX_AUTO_LOWGOAL);
        }
        else if(autoHighString.equals("Y"))
        {
            autoGoalSpinner.setSelection(INDEX_AUTO_HIGHGOAL);
        }
        else
        {
            autoGoalSpinner.setSelection(INDEX_AUTO_NOGOAL);
        }

        if(challengeString.equals("Y"))
        {
            towerSpinner.setSelection(INDEX_TOWER_CHALLENGE);
        }
        else if(scaleString.equals("Y"))
        {
            towerSpinner.setSelection(INDEX_TOWER_SCALE);
        }
        else
        {
            towerSpinner.setSelection(INDEX_TOWER_FAIL);
        }

        return true;
    }

    public boolean saveToDb()
    {
        if(teamNumEditText.getText().toString().length() == 0 ||
           matchNumEditText.getText().toString().length() == 0)
        {
            return false;
        }

        String autoReachString = (autoDefenseSpinner.getSelectedItemPosition() == INDEX_AUTO_REACHED) ? "Y" : "N";
        String autoCrossString;
        switch(autoDefenseSpinner.getSelectedItemPosition())
        {
            case INDEX_AUTO_PORTCULLIS:
                autoCrossString = AUTO_PORTCULLIS;
                break;
            case INDEX_AUTO_CHEVAL:
                autoCrossString = AUTO_CHEVAL;
                break;
            case INDEX_AUTO_MOAT:
                autoCrossString = AUTO_MOAT;
                break;
            case INDEX_AUTO_RAMPARTS:
                autoCrossString = AUTO_RAMPARTS;
                break;
            case INDEX_AUTO_DRAWBRIDGE:
                autoCrossString = AUTO_DRAWBRIDGE;
                break;
            case INDEX_AUTO_SALLYPORT:
                autoCrossString = AUTO_SALLYPORT;
                break;
            case INDEX_AUTO_ROCKWALL:
                autoCrossString = AUTO_ROCKWALL;
                break;
            case INDEX_AUTO_ROUGHTERRAIN:
                autoCrossString = AUTO_ROUGHTERRAIN;
                break;
            case INDEX_AUTO_LOWBAR:
                autoCrossString = AUTO_LOWBAR;
                break;
            default:
                autoCrossString = "N";
        }

        String autoLowGoalString;
        String autoHighGoalString;
        switch(autoGoalSpinner.getSelectedItemPosition())
        {
            case INDEX_AUTO_LOWGOAL:
                autoLowGoalString = "Y";
                autoHighGoalString = "N";
                break;
            case INDEX_AUTO_HIGHGOAL:
                autoLowGoalString = "N";
                autoHighGoalString = "Y";
                break;
            default:
                autoLowGoalString = "N";
                autoHighGoalString = "N";
        }

        String challengeString;
        String scaleString;
        switch(towerSpinner.getSelectedItemPosition())
        {
            case INDEX_TOWER_CHALLENGE:
                challengeString = "Y";
                scaleString = "N";
                break;
            case INDEX_TOWER_SCALE:
                challengeString = "N";
                scaleString = "Y";
                break;
            default:
                challengeString = "N";
                scaleString = "N";
        }

        int teamNum = Integer.parseInt(teamNumEditText.getText().toString());
        int matchNum = Integer.parseInt(matchNumEditText.getText().toString());
        String alliance = teamColorButton.isChecked()?"Blue":"Red";
        if(matchTime == 0)
        {
            matchTime = (int)(Calendar.getInstance().getTimeInMillis()%Integer.MAX_VALUE);
            matchesDB.insertMatch(matchTime,teamNum,alliance,matchNum,autoReachString,autoCrossString,
                                  autoLowGoalString,autoHighGoalString,lowGoalCount,highGoalCount,
                                  portcullisCount,chevalCount,moatCount,rampartsCount,drawbridgeCount,
                                  sallyPortCount,rockWallCount,roughTerrainCount,lowBarCount,
                                  blockRobotCount,blockShotCount,challengeString,scaleString);
        }
        else
        {
            matchesDB.updateMatch(matchTime,teamNum,alliance,matchNum,autoReachString,autoCrossString,
                    autoLowGoalString,autoHighGoalString,lowGoalCount,highGoalCount,
                    portcullisCount,chevalCount,moatCount,rampartsCount,drawbridgeCount,
                    sallyPortCount,rockWallCount,roughTerrainCount,lowBarCount,
                    blockRobotCount,blockShotCount,challengeString,scaleString);
        }

        return true;
    }

    public void colorClick(View view) {
        if(teamColorButton.isChecked())
        {
            teamColorButton.setBackgroundColor(getResources().getColor(R.color.blue));
        }
        else
        {
            teamColorButton.setBackgroundColor(getResources().getColor(R.color.red));
        }
    }

    public void BlockShotDecClick(View view) {
        if(blockShotCount > 0)
        {
            blockShotCount--;
            blockShotDecButton.setText(String.valueOf(blockShotCount));
        }
    }

    public void BlockShotIncClick(View view) {
        blockShotCount++;
        blockShotDecButton.setText(String.valueOf(blockShotCount));
    }

    public void BlockRobotDecClick(View view) {
        if(blockRobotCount > 0)
        {
            blockRobotCount--;
            blockRobotDecButton.setText(String.valueOf(blockRobotCount));
        }
    }

    public void BlockRobotIncClick(View view) {
        blockRobotCount++;
        blockRobotDecButton.setText(String.valueOf(blockRobotCount));
    }

    public void LowBarDecClick(View view) {
        if(lowBarCount > 0)
        {
            lowBarCount--;
            lowBarDecButton.setText(String.valueOf(lowBarCount));
        }
    }

    public void LowBarIncClick(View view) {
        lowBarCount++;
        lowBarDecButton.setText(String.valueOf(lowBarCount));
    }

    public void RoughTerrainDecClick(View view) {
        if(roughTerrainCount > 0)
        {
            roughTerrainCount--;
            roughTerrainDecButton.setText(String.valueOf(roughTerrainCount));
        }
    }

    public void RoughTerrainIncClick(View view) {
        roughTerrainCount++;
        roughTerrainDecButton.setText(String.valueOf(roughTerrainCount));
    }

    public void RockWallDecClick(View view) {
        if(rockWallCount > 0)
        {
            rockWallCount--;
            rockWallDecButton.setText(String.valueOf(rockWallCount));
        }
    }

    public void RockWallIncClick(View view) {
        rockWallCount++;
        rockWallDecButton.setText(String.valueOf(rockWallCount));
    }

    public void SallyPortDecClick(View view) {
        if(sallyPortCount > 0)
        {
            sallyPortCount--;
            sallyPortDecButton.setText(String.valueOf(sallyPortCount));
        }
    }

    public void SallyPortIncClick(View view) {
        sallyPortCount++;
        sallyPortDecButton.setText(String.valueOf(sallyPortCount));
    }

    public void DrawbridgeDecClick(View view) {
        if(drawbridgeCount > 0)
        {
            drawbridgeCount--;
            drawbridgeDecButton.setText(String.valueOf(drawbridgeCount));
        }
    }

    public void DrawbridgeIncClick(View view) {
        drawbridgeCount++;
        drawbridgeDecButton.setText(String.valueOf(drawbridgeCount));
    }

    public void RampartsDecClick(View view) {
        if(rampartsCount > 0)
        {
            rampartsCount--;
            rampartsDecButton.setText(String.valueOf(rampartsCount));
        }
    }

    public void RampartsIncClick(View view) {
        rampartsCount++;
        rampartsDecButton.setText(String.valueOf(rampartsCount));
    }

    public void MoatDecClick(View view) {
        if(moatCount > 0)
        {
            moatCount--;
            moatDecButton.setText(String.valueOf(moatCount));
        }
    }

    public void MoatIncClick(View view) {
        moatCount++;
        moatDecButton.setText(String.valueOf(moatCount));
    }

    public void ChevalDecClick(View view) {
        if(chevalCount > 0)
        {
            chevalCount--;
            chevalDecButton.setText(String.valueOf(chevalCount));
        }
    }

    public void ChevalIncClick(View view) {
        chevalCount++;
        chevalDecButton.setText(String.valueOf(chevalCount));
    }

    public void PortcullisDecClick(View view) {
        if(portcullisCount > 0)
        {
            portcullisCount--;
            portcullisDecButton.setText(String.valueOf(portcullisCount));
        }
    }

    public void PortcullisIncClick(View view) {
        portcullisCount++;
        portcullisDecButton.setText(String.valueOf(portcullisCount));
    }

    public void HighGoalDecClick(View view) {
        if(highGoalCount > 0)
        {
            highGoalCount--;
            highGoalDecButton.setText(String.valueOf(highGoalCount));
        }
    }

    public void HighGoalIncClick(View view) {
        highGoalCount++;
        highGoalDecButton.setText(String.valueOf(highGoalCount));
    }

    public void lowGoalDecClick(View view) {
        if(lowGoalCount > 0)
        {
            lowGoalCount--;
            lowGoalDecButton.setText(String.valueOf(lowGoalCount));
        }
    }

    public void lowGoalIncClick(View view) {
        lowGoalCount++;
        lowGoalDecButton.setText(String.valueOf(lowGoalCount));
    }
}
