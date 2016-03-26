package dkt01.speedscout16;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends ActionBarActivity
{
    private ScoutingDataDBHelper matchesDB;
    private ArrayList<Pair<Integer, String> > matchesList;
    private ListView matchesListView;
    ArrayAdapter<Pair<Integer, String> > matchesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        matchesListView = (ListView) findViewById(R.id.matchesListView);
        Random r = new Random();
        matchesDB = new ScoutingDataDBHelper(this);
        matchesDB.clearMatches();
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j< 8; j++)
            {
                matchesDB.insertMatch(r.nextInt(),i,j%2==0?"Red":"Blue",j,"N","Y","N","N",1,2,1,1,2,2,3,3,4,4,5,1,2,"N","Y");
            }
        }

        matchesList = matchesDB.getMatches();
        matchesListAdapter = new ArrayAdapter<>(this, R.layout.matches_list_view_item, matchesList);
        matchesListView.setAdapter(matchesListAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }
}
