package dkt01.speedscout16;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class EntryActivity extends ActionBarActivity {
    private int matchTime;
    private ScoutingDataDBHelper matchesDB;
    private Cursor matchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        Intent genesisIntent = getIntent();
        matchTime = genesisIntent.getIntExtra(MainActivity.CREATE_MESSAGE,0);
        matchesDB = new ScoutingDataDBHelper(this);
        if(matchTime != 0)
        {
            matchData = matchesDB.getMatch(matchTime);
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

                return true;

            case R.id.action_cancel:
                finish();

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
