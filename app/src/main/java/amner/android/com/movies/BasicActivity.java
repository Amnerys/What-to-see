package amner.android.com.movies;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class BasicActivity extends AppCompatActivity{

    @VisibleForTesting
    private ProgressDialog newProgressDialog;

    protected void showProgressDialog() {
        if (newProgressDialog == null) {
            newProgressDialog = new ProgressDialog(this);
            newProgressDialog.setMessage(getString(R.string.now_loading));
            newProgressDialog.setIndeterminate(true);
        }

        newProgressDialog.show();
    }

    protected void hideProgressDialog() {
        if (newProgressDialog != null && newProgressDialog.isShowing()) {
            newProgressDialog.dismiss();
        }
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
