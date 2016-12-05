package polus.ddns.net.rutortorrentsearch.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import polus.ddns.net.rutortorrentsearch.R;
import polus.ddns.net.rutortorrentsearch.data.model.rutor.RutorStrategy;
import polus.ddns.net.rutortorrentsearch.data.model.Strategy;
import polus.ddns.net.rutortorrentsearch.data.vo.EntryTorrent;
import polus.ddns.net.rutortorrentsearch.utils.ConstantManager;

/**
 * Created by Игорь on 21.11.2016.
 */

public class EntryActivity extends BaseActivity {
    static final String TAG = ConstantManager.TAG_PREFIX + "EntryActivity";
    private EntryTorrent entryTorrent;
    private URI uri;
    private Strategy rutor = new RutorStrategy();
    @BindView(R.id.torrent_image)
    ImageView torrentImage;
    @BindView(R.id.torrent_text)
    TextView torrentText;
    @BindView(R.id.torrent_button)
    Button torrentButton;
    @BindView(R.id.torrent_brauzer)
    Button torrentBrauzer;

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
            try {
                entryTorrent = rutor.getEntryFromUri(uri);
                if (entryTorrent == null) throw new Exception();
            } catch (Exception e) {
                showToast(ConstantManager.SERVER_OUT);
                finish();
                entryTorrent = new EntryTorrent(URI.create(ConstantManager.LOGO_URI), "", URI.create(ConstantManager.LOGO_URI));
            }
        } else {
            uri = (URI) savedInstanceState.getSerializable(ConstantManager.ENTRY_LINK);
            entryTorrent = (EntryTorrent) savedInstanceState.getSerializable(ConstantManager.ENTRY);
        }
        if (entryTorrent.getImageUri() == null) {
            entryTorrent.setImageUri(URI.create(ConstantManager.LOGO_URI));
        }
        if (entryTorrent.getText() == null) {
            entryTorrent.setText("");
        }
        Picasso.with(this).load(entryTorrent.getImageUri().toString()).into(torrentImage);
        torrentText.setText(entryTorrent.getText());
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
        if (entryTorrent == null || entryTorrent.getImageUri() == null) {
            intent.putExtra(ConstantManager.ENTRY_IMAGE_LINK, URI.create(ConstantManager.LOGO_URI));
        } else {
            intent.putExtra(ConstantManager.ENTRY_IMAGE_LINK, entryTorrent.getImageUri());
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.torrent_button)
    public void clickTorrentButton() {
        Log.d(TAG, "clickTorrentButton");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(entryTorrent.getLinkTorrent().toString()));
        startActivity(intent);
    }
    @OnClick(R.id.torrent_brauzer)
    public void clickBrauzer(){
        Log.d(TAG, "clickBrauzer");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString()));
        startActivity(intent);
    }
}
