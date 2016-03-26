package dkt01.speedscout16;

import android.content.Intent;
import android.net.Uri;
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
        for(int i = 0; i < 1; i++)
        {
            for(int j = 0; j< 1; j++)
            {
                matchesDB.insertMatch(r.nextInt(),i,j%2==0?"Red":"Blue",j,"N","Y","N","N",1,2,1,1,2,2,3,3,4,4,5,1,2,"N","Y");
            }
        }

        matchesList = matchesDB.getMatches();
        matchesListAdapter = new ArrayAdapter<>(this, R.layout.matches_list_view_item, matchesList);
        matchesListView.setAdapter(matchesListAdapter);

        ArrayList<Integer> times = new ArrayList<>();

        for(Pair<Integer,String> match : matchesList)
        {
            times.add(match.first);
        }

        ArrayList<Uri> matchCsvs = matchesDB.getCsv(times);

        Intent shareDataIntent = new Intent();
        shareDataIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareDataIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,matchCsvs);
        shareDataIntent.setType("text/plain");
        shareDataIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareDataIntent, "Share scouting data to.."));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }
}
