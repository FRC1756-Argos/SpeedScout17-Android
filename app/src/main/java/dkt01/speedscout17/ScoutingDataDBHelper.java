package dkt01.speedscout17;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

public class ScoutingDataDBHelper extends SQLiteOpenHelper {
    // Database properties
    private static final String DATABASE_NAME             = "Matches.db";
    private static final String MATCHES_TABLE_NAME        = "matches";
    private static final int DATABASE_VERSION             = 5;

    // Table properties
    public static final String TIME_COL_NAME              = "time";
    public static final String TEAM_COL_NAME              = "team";
    public static final String ALLIANCE_COL_NAME          = "alliance";
    public static final String MATCH_COL_NAME             = "match";
    public static final String AUTO_BASELINE_COL_NAME     = "autoBaseline";
    public static final String AUTO_LOWBOILER_COL_NAME    = "autoLowBoiler";
    public static final String AUTO_HIGHBOILER_COL_NAME   = "autoHighBoiler";
    public static final String AUTO_GEAR_COL_NAME         = "autoGear";
    public static final String TELE_LOWBOILER_COL_NAME    = "teleLowBoiler";
    public static final String TELE_HIGHBOILER_COL_NAME   = "teleHighBoiler";
    public static final String TELE_GEAR_COL_NAME         = "teleGear";
    public static final String TELE_CLIMB_COL_NAME        = "teleClimb";
    public static final String COMMENTS_COL_NAME          = "comments";

    private final Context m_context;
    private static SQLiteDatabase m_db;

    public ScoutingDataDBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        m_context = context;
        open();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder createCommand = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        createCommand.append(MATCHES_TABLE_NAME + "( ");
        createCommand.append(TIME_COL_NAME +              " INTEGER, ");
        createCommand.append(TEAM_COL_NAME +              " INTEGER, ");
        createCommand.append(ALLIANCE_COL_NAME +          " TEXT, ");
        createCommand.append(MATCH_COL_NAME +             " INTEGER, ");
        createCommand.append(AUTO_BASELINE_COL_NAME +     " TEXT, ");
        createCommand.append(AUTO_LOWBOILER_COL_NAME +    " INTEGER, ");
        createCommand.append(AUTO_HIGHBOILER_COL_NAME +   " INTEGER, ");
        createCommand.append(AUTO_GEAR_COL_NAME +         " TEXT, ");
        createCommand.append(TELE_LOWBOILER_COL_NAME +    " INTEGER, ");
        createCommand.append(TELE_HIGHBOILER_COL_NAME +   " INTEGER, ");
        createCommand.append(TELE_GEAR_COL_NAME +         " INTEGER, ");
        createCommand.append(TELE_CLIMB_COL_NAME +        " TEXT, ");
        createCommand.append(COMMENTS_COL_NAME +          " TEXT");
        createCommand.append(");");

        db.execSQL(createCommand.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Add columns as necessary
        switch (oldVersion)
        {
            case 4:
                db.execSQL("ALTER TABLE " + MATCHES_TABLE_NAME + " ADD COLUMN " + COMMENTS_COL_NAME + " STRING");
                break;
            default:
                Log.e("onUpgrade", "Invalid old database version: " + Integer.toString(oldVersion));
        }
    }

    public ArrayList<Pair<Integer, String> > getMatches()
    {
        ArrayList<Pair<Integer, String> > matches = new ArrayList<>();
        Cursor result = m_db.rawQuery("select * from " + MATCHES_TABLE_NAME, null);
        result.moveToFirst();

        while(result.isAfterLast() == false)
        {
            int id = result.getInt(result.getColumnIndex(TIME_COL_NAME));
            StringBuilder name = new StringBuilder("Team: ");
            name.append(result.getInt(result.getColumnIndex(TEAM_COL_NAME)));
            name.append("\tMatch: ");
            name.append(result.getInt(result.getColumnIndex(MATCH_COL_NAME)));

            Pair<Integer,String> datum = new Pair<>(id,name.toString());
            matches.add(datum);
            result.moveToNext();
        }
        return matches;
    }

    public boolean insertMatch  (int time, int team, String alliance, int match, String autoBaseline,
                                 int autoLow, int autoHigh, String autoGear, int teleLow,
                                 int teleHigh, int teleGear, String teleClimb, String comments)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_COL_NAME,time);
        contentValues.put(TEAM_COL_NAME,team);
        contentValues.put(ALLIANCE_COL_NAME,alliance);
        contentValues.put(MATCH_COL_NAME,match);
        contentValues.put(AUTO_BASELINE_COL_NAME,autoBaseline);
        contentValues.put(AUTO_LOWBOILER_COL_NAME,autoLow);
        contentValues.put(AUTO_HIGHBOILER_COL_NAME,autoHigh);
        contentValues.put(AUTO_GEAR_COL_NAME,autoGear);
        contentValues.put(TELE_LOWBOILER_COL_NAME,teleLow);
        contentValues.put(TELE_HIGHBOILER_COL_NAME,teleHigh);
        contentValues.put(TELE_GEAR_COL_NAME,teleGear);
        contentValues.put(TELE_CLIMB_COL_NAME,teleClimb);
        contentValues.put(COMMENTS_COL_NAME,comments);
        m_db.insert(MATCHES_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateMatch  (int time, int team, String alliance, int match, String autoBaseline,
                                 int autoLow, int autoHigh, String autoGear, int teleLow,
                                 int teleHigh, int teleGear, String teleClimb, String comments)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_COL_NAME,time);
        contentValues.put(TEAM_COL_NAME,team);
        contentValues.put(ALLIANCE_COL_NAME,alliance);
        contentValues.put(MATCH_COL_NAME,match);
        contentValues.put(AUTO_BASELINE_COL_NAME,autoBaseline);
        contentValues.put(AUTO_LOWBOILER_COL_NAME,autoLow);
        contentValues.put(AUTO_HIGHBOILER_COL_NAME,autoHigh);
        contentValues.put(AUTO_GEAR_COL_NAME,autoGear);
        contentValues.put(TELE_LOWBOILER_COL_NAME,teleLow);
        contentValues.put(TELE_HIGHBOILER_COL_NAME,teleHigh);
        contentValues.put(TELE_GEAR_COL_NAME,teleGear);
        contentValues.put(TELE_CLIMB_COL_NAME,teleClimb);
        contentValues.put(COMMENTS_COL_NAME,comments);
        m_db.update(MATCHES_TABLE_NAME, contentValues, TIME_COL_NAME + " = ? ", new String[] { Integer.toString(time) } );
        return true;
    }

    public Integer deleteMatch (int time)
    {
        return m_db.delete(MATCHES_TABLE_NAME,
                TIME_COL_NAME + " = ? ",
                new String[] { Integer.toString(time) });
    }

    public Cursor getMatch(int time){
        Cursor res =  m_db.rawQuery( "select * from " + MATCHES_TABLE_NAME + " where " + TIME_COL_NAME + "="+time+"", null );
        return res;
    }

    public void clearMatches()
    {
        m_db.execSQL("DROP TABLE IF EXISTS " + MATCHES_TABLE_NAME);
        this.onCreate(m_db);
    }

    FilenameFilter csvFilter = new FilenameFilter() {
        File f;
        public boolean accept(File dir, String name) {
            if(name.endsWith(".csv")) {
                return true;
            }
            return false;
        }
    };

    public ArrayList<Uri> getCsv(ArrayList<Integer> times)
    {
        // Delete old files before generating new files
        // Just prevents accumulation of temporary files
        String oldFiles[] = m_context.getFilesDir().list(csvFilter);
        for(String csvFile : oldFiles)
        {
            File fileToDelete = new File(m_context.getFilesDir(), csvFile);
            boolean retval = fileToDelete.delete();
            Log.d("DELETE",csvFile+" : "+ String.valueOf(retval));
        }

        ArrayList<Uri> csvFiles = new ArrayList<>();
        for(Integer time : times)
        {
            Cursor result = getMatch(time);
            result.moveToFirst();
            if(result.isAfterLast() == false)
            {
                String fileName = String.valueOf(result.getInt(result.getColumnIndex(TEAM_COL_NAME))) + "_" +
                        String.valueOf(result.getInt(result.getColumnIndex(MATCH_COL_NAME)));
                StringBuilder fileData = new StringBuilder();
                fileData.append("Team #, ");
                fileData.append(String.valueOf(result.getInt(result.getColumnIndex(TEAM_COL_NAME))));
                fileData.append("\nA. Color, ");
                fileData.append(result.getString(result.getColumnIndex(ALLIANCE_COL_NAME)));
                fileData.append("\nMatch #, ");
                fileData.append(String.valueOf(result.getInt(result.getColumnIndex(MATCH_COL_NAME))));
                fileData.append("\n, Autonomous");
                fileData.append("\nCrossed BLine, ");
                fileData.append(result.getString(result.getColumnIndex(AUTO_BASELINE_COL_NAME)));
                fileData.append("\nLow Boiler, ");
                fileData.append(result.getInt(result.getColumnIndex(AUTO_LOWBOILER_COL_NAME)));
                fileData.append("\nHigh Boiler, ");
                fileData.append(result.getInt(result.getColumnIndex(AUTO_HIGHBOILER_COL_NAME)));
                fileData.append("\nGear, ");
                fileData.append(result.getString(result.getColumnIndex(AUTO_GEAR_COL_NAME)));
                fileData.append("\n, Teleop");
                fileData.append("\nLow Boiler, ");
                fileData.append(result.getInt(result.getColumnIndex(TELE_LOWBOILER_COL_NAME)));
                fileData.append("\nHigh Boiler, ");
                fileData.append(result.getInt(result.getColumnIndex(TELE_HIGHBOILER_COL_NAME)));
                fileData.append("\nGears, ");
                fileData.append(result.getInt(result.getColumnIndex(TELE_GEAR_COL_NAME)));
                fileData.append("\nClimbed AShip, ");
                fileData.append(result.getString(result.getColumnIndex(TELE_CLIMB_COL_NAME)));
                fileData.append("\n, Comments");
                fileData.append("\nComments,");
                fileData.append(convertStringForCsv(result.getString(result.getColumnIndex(COMMENTS_COL_NAME))));

                File matchFile = null;
                FileWriter matchFileWriter = null;
                try
                {
//                    Log.d("FILE",fileName+" "+m_context.getFilesDir().getAbsolutePath());
                    matchFile = new File(m_context.getFilesDir(),fileName+".csv");// (fileName, ".csv", m_context.getFilesDir());
                    matchFile.setReadable(true,false);
                    matchFileWriter = new FileWriter(matchFile, false);
                    matchFileWriter.write(fileData.toString());
                    matchFileWriter.close();
//                    Log.d("FILE",matchFile.getAbsolutePath());
                    Uri matchFileUri = FileProvider.getUriForFile(m_context, "com.dkt01.speedscout17.fileprovider", matchFile);
                    csvFiles.add(matchFileUri);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return csvFiles;
    }

    public ScoutingDataDBHelper open() throws SQLException
    {
        m_db = getWritableDatabase();
        return this;
    }

    public void close()
    {
        if (m_db != null)
            m_db.close();
    }

    private String convertStringForCsv(String inString)
    {
        String retVal = inString.replace(',',';');
        retVal = retVal.replace('\n',' ');
        return retVal;
    }
}
