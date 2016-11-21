package polus.ddns.net.rutortorrentsearch.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import polus.ddns.net.rutortorrentsearch.R;
import polus.ddns.net.rutortorrentsearch.utils.ConstantManager;

/**
 * Created by Игорь on 18.11.2016.
 */

public class CardViewActivity extends Activity {
    static final String TAG = ConstantManager.TAG_PREFIX + "CardViewActivity";
    @BindView(R.id.card_name)
    TextView cardName;
    @BindView(R.id.card_size)
    TextView cardSize;
    @BindView(R.id.card_seeders)
    TextView cardSeeders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview_activity);

    }
}