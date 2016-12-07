package polus.ddns.net.rutorsearch.ui.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import polus.ddns.net.rutorsearch.R;
import polus.ddns.net.rutorsearch.utils.ConstantManager;

/**
 * Created by Игорь on 16.11.2016.
 */

public class BaseActivity extends AppCompatActivity {
    static final String TAG = ConstantManager.TAG_PREFIX + "BaseActivity";
    protected ProgressDialog mProgressDialog;

    public void showProgress(){
        Log.d(TAG, "showProgress");
        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this, R.style.custom_dialog);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mProgressDialog.show();
        mProgressDialog.setContentView(R.layout.progress_splash);
    }

    public void hideProgress(){
        Log.d(TAG, "hideProgress");
        if (mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.hide();
        }
    }

    public void showError(String massage, Exception error){
        Log.d(TAG, "showError");
        showToast(massage);
        Log.e(TAG, String.valueOf(error));
    }

    public void showToast(String massage){
        Log.d(TAG, "showToast");
        Toast.makeText(this, massage, Toast.LENGTH_LONG).show();
    }

}
