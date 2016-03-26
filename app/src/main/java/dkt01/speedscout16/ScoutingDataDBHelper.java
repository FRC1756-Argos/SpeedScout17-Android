package dkt01.speedscout16;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import java.util.ArrayList;

public class ScoutingDataDBHelper extends SQLiteOpenHelper {
    // Database properties
    private static final String DATABASE_NAME             = "Matches.db";
    private static final String MATCHES_TABLE_NAME        = "matches";
    private static final int DATABASE_VERSION             = 4;

    // Table properties
    public static final String TIME_COL_NAME              = "time";
    public static final String TEAM_COL_NAME              = "team";
    public static final String ALLIANCE_COL_NAME          = "alliance";
    public static final String MATCH_COL_NAME             = "match";
    public static final String AUTO_REACHED_COL_NAME      = "autoReached";
    public static final String AUTO_CROSSED_COL_NAME      = "autoCrossed";
    public static final String AUTO_LOWGOAL_COL_NAME      = "autoLowGoal";
    public static final String AUTO_HIGHGOAL_COL_NAME     = "autoHighGoal";
    public static final String TELE_LOWGOAL_COL_NAME      = "teleLowGoal";
    public static final String TELE_HIGHGOAL_COL_NAME     = "teleHighGoal";
    public static final String TELE_PORTCULLIS_COL_NAME   = "telePortcullis";
    public static final String TELE_CHEVAL_COL_NAME       = "teleCheval";
    public static final String TELE_MOAT_COL_NAME         = "teleMoat";
    public static final String TELE_RAMP_COL_NAME         = "teleRamp";
    public static final String TELE_DRAWBRIDGE_COL_NAME   = "teleDrawBridge";
    public static final String TELE_SALLYPORT_COL_NAME    = "teleSallyPort";
    public static final String TELE_ROCKWALL_COL_NAME     = "teleRockWall";
    public static final String TELE_ROUGHTERRAIN_COL_NAME = "teleRoughTerrain";
    public static final String TELE_LOWBAR_COL_NAME       = "teleLowBar";
    public static final String TELE_BLOCKROBOT_COL_NAME   = "teleBlockRobot";
    public static final String TELE_BLOCKSHOT_COL_NAME    = "teleBlockShot";
    public static final String TELE_CHALLENGE_COL_NAME    = "teleChallenge";
    public static final String TELE_SCALE_COL_NAME        = "teleScale";

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
        createCommand.append(AUTO_REACHED_COL_NAME +      " TEXT, ");
        createCommand.append(AUTO_CROSSED_COL_NAME +      " TEXT, ");
        createCommand.append(AUTO_LOWGOAL_COL_NAME +      " TEXT, ");
        createCommand.append(AUTO_HIGHGOAL_COL_NAME +     " TEXT, ");
        createCommand.append(TELE_LOWGOAL_COL_NAME +      " INTEGER, ");
        createCommand.append(TELE_HIGHGOAL_COL_NAME +     " INTEGER, ");
        createCommand.append(TELE_PORTCULLIS_COL_NAME +   " INTEGER, ");
        createCommand.append(TELE_CHEVAL_COL_NAME +       " INTEGER, ");
        createCommand.append(TELE_MOAT_COL_NAME +         " INTEGER, ");
        createCommand.append(TELE_RAMP_COL_NAME +         " INTEGER, ");
        createCommand.append(TELE_DRAWBRIDGE_COL_NAME +   " INTEGER, ");
        createCommand.append(TELE_SALLYPORT_COL_NAME +    " INTEGER, ");
        createCommand.append(TELE_ROCKWALL_COL_NAME +     " INTEGER, ");
        createCommand.append(TELE_ROUGHTERRAIN_COL_NAME + " INTEGER, ");
        createCommand.append(TELE_LOWBAR_COL_NAME +       " INTEGER, ");
        createCommand.append(TELE_BLOCKROBOT_COL_NAME +   " INTEGER, ");
        createCommand.append(TELE_BLOCKSHOT_COL_NAME +    " INTEGER, ");
        createCommand.append(TELE_CHALLENGE_COL_NAME +    " TEXT, ");
        createCommand.append(TELE_SCALE_COL_NAME +        " TEXT");
        createCommand.append(");");

        db.execSQL(createCommand.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This might need changed...
        db.execSQL("DROP TABLE IF EXISTS " + MATCHES_TABLE_NAME);
        this.onCreate(db);
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

    public boolean insertMatch  (int time, int team, String alliance, int match, String autoReach,
                                 String autoCross, String autoLow, String autoHigh, int teleLow,
                                 int teleHigh, int portcullis, int cheval, int moat, int ramp,
                                 int drawBridge, int sallyPort, int rockWall, int roughTerrain,
                                 int lowBar, int blockRobot, int blockShot, String challenge,
                                 String scale)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_COL_NAME,time);
        contentValues.put(TEAM_COL_NAME,team);
        contentValues.put(ALLIANCE_COL_NAME,alliance);
        contentValues.put(MATCH_COL_NAME,match);
        contentValues.put(AUTO_REACHED_COL_NAME,autoReach);
        contentValues.put(AUTO_CROSSED_COL_NAME,autoCross);
        contentValues.put(AUTO_LOWGOAL_COL_NAME,autoLow);
        contentValues.put(AUTO_HIGHGOAL_COL_NAME,autoHigh);
        contentValues.put(TELE_LOWGOAL_COL_NAME,teleLow);
        contentValues.put(TELE_HIGHGOAL_COL_NAME,teleHigh);
        contentValues.put(TELE_PORTCULLIS_COL_NAME,portcullis);
        contentValues.put(TELE_CHEVAL_COL_NAME,cheval);
        contentValues.put(TELE_MOAT_COL_NAME,moat);
        contentValues.put(TELE_RAMP_COL_NAME,ramp);
        contentValues.put(TELE_DRAWBRIDGE_COL_NAME,drawBridge);
        contentValues.put(TELE_SALLYPORT_COL_NAME,sallyPort);
        contentValues.put(TELE_ROCKWALL_COL_NAME,rockWall);
        contentValues.put(TELE_ROUGHTERRAIN_COL_NAME,roughTerrain);
        contentValues.put(TELE_LOWBAR_COL_NAME,lowBar);
        contentValues.put(TELE_BLOCKROBOT_COL_NAME,blockRobot);
        contentValues.put(TELE_BLOCKSHOT_COL_NAME,blockShot);
        contentValues.put(TELE_CHALLENGE_COL_NAME,challenge);
        contentValues.put(TELE_SCALE_COL_NAME,scale);
        m_db.insert(MATCHES_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateMatch  (int time, int team, String alliance, int match, String autoReach,
                                 String autoCross, String autoLow, String autoHigh, int teleLow,
                                 int teleHigh, int portcullis, int cheval, int moat, int ramp,
                                 int drawBridge, int sallyPort, int rockWall, int roughTerrain,
                                 int lowBar, int blockRobot, int blockShot, String challenge,
                                 String scale)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_COL_NAME,time);
        contentValues.put(TEAM_COL_NAME,team);
        contentValues.put(ALLIANCE_COL_NAME,alliance);
        contentValues.put(MATCH_COL_NAME,match);
        contentValues.put(AUTO_REACHED_COL_NAME,autoReach);
        contentValues.put(AUTO_CROSSED_COL_NAME,autoCross);
        contentValues.put(AUTO_LOWGOAL_COL_NAME,autoLow);
        contentValues.put(AUTO_HIGHGOAL_COL_NAME,autoHigh);
        contentValues.put(TELE_LOWGOAL_COL_NAME,teleLow);
        contentValues.put(TELE_HIGHGOAL_COL_NAME,teleHigh);
        contentValues.put(TELE_PORTCULLIS_COL_NAME,portcullis);
        contentValues.put(TELE_CHEVAL_COL_NAME,cheval);
        contentValues.put(TELE_MOAT_COL_NAME,moat);
        contentValues.put(TELE_RAMP_COL_NAME,ramp);
        contentValues.put(TELE_DRAWBRIDGE_COL_NAME,drawBridge);
        contentValues.put(TELE_SALLYPORT_COL_NAME,sallyPort);
        contentValues.put(TELE_ROCKWALL_COL_NAME,rockWall);
        contentValues.put(TELE_ROUGHTERRAIN_COL_NAME,roughTerrain);
        contentValues.put(TELE_LOWBAR_COL_NAME,lowBar);
        contentValues.put(TELE_BLOCKROBOT_COL_NAME,blockRobot);
        contentValues.put(TELE_BLOCKSHOT_COL_NAME,blockShot);
        contentValues.put(TELE_CHALLENGE_COL_NAME,challenge);
        contentValues.put(TELE_SCALE_COL_NAME,scale);
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
}
