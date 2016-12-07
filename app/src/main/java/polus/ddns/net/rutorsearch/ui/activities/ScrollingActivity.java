package polus.ddns.net.rutorsearch.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import polus.ddns.net.rutorsearch.R;
import polus.ddns.net.rutorsearch.data.model.Strategy;
import polus.ddns.net.rutorsearch.data.model.rutor.RutorStrategy;
import polus.ddns.net.rutorsearch.data.vo.EntrysFromSite;
import polus.ddns.net.rutorsearch.utils.ConstantManager;
import polus.ddns.net.rutorsearch.utils.NetworkUtils;
import polus.ddns.net.rutorsearch.utils.RecyclerItemClickListener;

public class ScrollingActivity extends BaseActivity {
    static final String TAG = ConstantManager.TAG_PREFIX + "ScrollingActivity";
    private List<EntrysFromSite> siteList;
    private Strategy rutor = new RutorStrategy();
    @BindView(R.id.wallpaper)
    ImageView wallpaper;
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
        if (savedInstanceState == null) {
            siteList = new ArrayList<>();
            showProgress();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (NetworkUtils.isNetworkAvailable(context)) {
                        siteList = new ArrayList<>();
                        siteList.addAll(rutor.getStartEntrys());
                        foundRezults.setText(ConstantManager.START_REZULTS);
                        initializeAdapter();
                        hideProgress();
                    } else {
                        hideProgress();
                        showToast(ConstantManager.INTERNET_OUT);
                    }
                }
            }, 2000);
        } else {
            siteList = savedInstanceState.getParcelableArrayList(ConstantManager.SITE_LIST);
            hideProgress();
            foundRezults.setText(savedInstanceState.getString(ConstantManager.FOUND_TEXT_VIEW));
            initializeAdapter();
        }
        //Picasso.with(this).load().fit().centerCrop().into(wallpaper);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putString(ConstantManager.FOUND_TEXT_VIEW, foundRezults.getText().toString());
        outState.putParcelableArrayList(ConstantManager.SITE_LIST, (ArrayList<? extends Parcelable>) siteList);
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
