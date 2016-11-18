package polus.ddns.net.rutortorrentsearch.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import polus.ddns.net.rutortorrentsearch.R;
import polus.ddns.net.rutortorrentsearch.utils.ConstantManager;

/**
 * Created by Игорь on 18.11.2016.
 */

public class CardViewActivity extends Activity {
    static final String TAG = ConstantManager.TAG_PREFIX + "CardViewActivity";
    TextView cardName;
    TextView cardSize;
    TextView cardSeeders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview_activity);
        cardName = (TextView)findViewById(R.id.card_name);
        cardSize = (TextView)findViewById(R.id.card_size);
        cardSeeders = (TextView)findViewById(R.id.card_seeders);

    }
}