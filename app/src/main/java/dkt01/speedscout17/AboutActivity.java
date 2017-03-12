package dkt01.speedscout17;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView tv1 = (TextView)findViewById(R.id.about_1_textview);
        tv1.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv2 = (TextView)findViewById(R.id.about_2_textview);
        tv2.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv3 = (TextView)findViewById(R.id.about_3_textview);
        tv3.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv4 = (TextView)findViewById(R.id.about_4_textview);
        tv4.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv5 = (TextView)findViewById(R.id.about_5_textview);
        tv5.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
