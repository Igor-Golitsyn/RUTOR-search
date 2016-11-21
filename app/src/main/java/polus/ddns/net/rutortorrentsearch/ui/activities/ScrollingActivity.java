package polus.ddns.net.rutortorrentsearch.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import polus.ddns.net.rutortorrentsearch.R;
import polus.ddns.net.rutortorrentsearch.data.model.RutorStrategy;
import polus.ddns.net.rutortorrentsearch.data.vo.EntrysFromSite;
import polus.ddns.net.rutortorrentsearch.utils.ConstantManager;
import polus.ddns.net.rutortorrentsearch.utils.RecyclerItemClickListener;

public class ScrollingActivity extends BaseActivity {
    static final String TAG = ConstantManager.TAG_PREFIX + "ScrollingActivity";
    private List<EntrysFromSite> siteList;
    private View vi;
    @BindView(R.id.rutor_logo)
    ImageView rutorLogo;
    @BindView(R.id.find_button)
    Button searchButton;
    @BindView(R.id.rezult_linear_layout)
    LinearLayout rezultLinearLayout;
    @BindView(R.id.find_text)
    EditText editText;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_scrolling);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItemClick");
                vi = view;
                Intent intent = new Intent(ScrollingActivity.this, EntryActivity.class);
                intent.putExtra(ConstantManager.ENTRY_LINK, siteList.get(position).getUri());
                startActivityForResult(intent, ConstantManager.REQUEST_URI_FOR_IMAGE_TORRENT);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                Log.d(TAG, "onLongItemClick");
                // do whatever
            }
        }));
        Picasso.with(this).load(ConstantManager.LOGO_URI).fit().into(rutorLogo);
        if (savedInstanceState == null) {
            siteList = new ArrayList<>();
        } else {
            siteList = (List<EntrysFromSite>) savedInstanceState.getSerializable(ConstantManager.SITE_LIST);
            initializeAdapter();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putSerializable(ConstantManager.SITE_LIST, (Serializable) siteList);
    }

    @OnClick(R.id.find_button)
    public void clickFindButton() {
        Log.d(TAG, "clickFindButton");
        siteList = new RutorStrategy().getEntrysFromSite(editText.getText().toString());
        showToast("Найдено: " + siteList.size());
        initializeAdapter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult" + " " + requestCode + " " + resultCode);
        switch (requestCode) {
            case ConstantManager.REQUEST_URI_FOR_IMAGE_TORRENT:
                if (resultCode == RESULT_OK) {
                    vi.setBackgroundColor(Color.parseColor("#DBDBDB"));
                }
                break;
        }
    }

    private void initializeAdapter() {
        Log.d(TAG, "initializeAdapter");
        RVAdapter adapter = new RVAdapter(siteList);
        recyclerView.setAdapter(adapter);
    }
}
