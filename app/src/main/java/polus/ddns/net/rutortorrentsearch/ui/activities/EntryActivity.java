package polus.ddns.net.rutortorrentsearch.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;
import polus.ddns.net.rutortorrentsearch.R;
import polus.ddns.net.rutortorrentsearch.data.vo.EntryTorrent;
import polus.ddns.net.rutortorrentsearch.utils.ConstantManager;

/**
 * Created by Игорь on 21.11.2016.
 */

public class EntryActivity extends BaseActivity {
    static final String TAG = ConstantManager.TAG_PREFIX + "EntryActivity";
    private EntryTorrent entryTorrent;
    private URI uri;
    @BindView(R.id.torrent_image)
    ImageView torrentImage;
    @BindView(R.id.torrent_text)
    TextView torrentText;
    @BindView(R.id.torrent_button)
    Button torrentButton;
    @BindView(R.id.web_wiew)
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.entry_activity);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            uri = (URI) intent.getSerializableExtra(ConstantManager.ENTRY_LINK);
        } else {
            uri = (URI) savedInstanceState.getSerializable(ConstantManager.ENTRY_LINK);
            entryTorrent = (EntryTorrent) savedInstanceState.getSerializable(ConstantManager.ENTRY);
        }
        try {
            webView.loadUrl(uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putSerializable(ConstantManager.ENTRY, entryTorrent);
        outState.putSerializable(ConstantManager.ENTRY_LINK, uri);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        Intent intent = new Intent();
        //intent.putExtra(ConstantManager.ENTRY_IMAGE_LINK, (Serializable) entryTorrent.getImageUri());
        if (entryTorrent == null || entryTorrent.getImageUri() == null) {
            intent.putExtra(ConstantManager.ENTRY_IMAGE_LINK, URI.create(ConstantManager.LOGO_URI));
        } else {
            intent.putExtra(ConstantManager.ENTRY_IMAGE_LINK, entryTorrent.getImageUri());
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}
