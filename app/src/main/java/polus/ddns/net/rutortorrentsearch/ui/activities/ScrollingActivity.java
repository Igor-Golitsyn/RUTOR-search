package polus.ddns.net.rutortorrentsearch.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import polus.ddns.net.rutortorrentsearch.R;
import polus.ddns.net.rutortorrentsearch.data.model.RutorStrategy;
import polus.ddns.net.rutortorrentsearch.data.model.Strategy;
import polus.ddns.net.rutortorrentsearch.data.vo.EntrysFromSite;
import polus.ddns.net.rutortorrentsearch.utils.ConstantManager;
import polus.ddns.net.rutortorrentsearch.utils.NetworkUtils;
import polus.ddns.net.rutortorrentsearch.utils.RecyclerItemClickListener;

public class ScrollingActivity extends BaseActivity {
    static final String TAG = ConstantManager.TAG_PREFIX + "ScrollingActivity";
    private List<EntrysFromSite> siteList;
    @BindView(R.id.rutor_logo)
    ImageView rutorLogo;
    @BindView(R.id.find_button)
    Button searchButton;
    @BindView(R.id.find_text)
    EditText editText;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.nestedscroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.found_rezults)
    TextView foundRezults;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_scrolling);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setNestedScrollingEnabled(false);
        final Context context = this;
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItemClick");
                if (NetworkUtils.isNetworkAvailable(context)) {
                    Intent intent = new Intent(ScrollingActivity.this, EntryActivity.class);
                    intent.putExtra(ConstantManager.ENTRY_LINK, siteList.get(position).getUri());
                    view.setBackgroundColor(Color.parseColor("#DBDBDB"));
                    startActivity(intent);
                } else {
                    showToast(ConstantManager.INTERNET_OUT);
                }
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
            showProgress();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (NetworkUtils.isNetworkAvailable(context)) {
                            Strategy rutor = new RutorStrategy();
                            siteList = new ArrayList<>();
                            siteList.addAll(rutor.getStartEntrys());
                            foundRezults.setText(ConstantManager.START_REZULTS);
                            initializeAdapter();
                            hideProgress();
                        } else {
                            hideProgress();
                            showToast(ConstantManager.INTERNET_OUT);
                        }
                    } catch (IOException e) {
                        hideProgress();
                        showToast(ConstantManager.SERVER_OUT);
                    }
                }
            }, 2000);
        } else {
            siteList = (List<EntrysFromSite>) savedInstanceState.getSerializable(ConstantManager.SITE_LIST);
            initializeAdapter();
            hideProgress();
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
        final Context context = this;
        showProgress();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (NetworkUtils.isNetworkAvailable(context)) {
                    Strategy rutor = new RutorStrategy();
                    siteList = new ArrayList<>();
                    siteList.addAll(rutor.getEntrysFromSite(editText.getText().toString()));
                    nestedScrollView.fullScroll(View.FOCUS_UP);
                    foundRezults.setText(String.format(Locale.getDefault(), ConstantManager.FOUND_REZULTS, siteList.size()));
                    initializeAdapter();
                    hideProgress();
                } else {
                    hideProgress();
                    showToast(ConstantManager.INTERNET_OUT);
                }
            }
        }, 2000);
    }

    private void initializeAdapter() {
        Log.d(TAG, "initializeAdapter");
        RVAdapter adapter = new RVAdapter(siteList);
        recyclerView.setAdapter(adapter);
    }
}
