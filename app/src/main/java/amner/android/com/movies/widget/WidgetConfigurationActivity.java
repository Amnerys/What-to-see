package amner.android.com.movies.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import amner.android.com.movies.R;
import amner.android.com.movies.utils.Constants;

/**
 * The configuration screen for the {@link Widget Widget} AppWidget.
 */
public class WidgetConfigurationActivity extends Activity {

    private int movieWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private EditText widgetEditText;
    private View.OnClickListener widgetOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = WidgetConfigurationActivity.this;

            // When the button is clicked, store the string locally
            String widgetString = widgetEditText.getText().toString();
            saveSharedPref(context, movieWidgetId, widgetString);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            Widget.updateWidget(context, appWidgetManager, movieWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent newIntent = new Intent();
            newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, movieWidgetId);
            setResult(RESULT_OK, newIntent);
            finish();
        }
    };

    public WidgetConfigurationActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    private static void saveSharedPref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(Constants.PREFS_NAME, 0).edit();
        prefs.putString(Constants.PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadSharedPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        String titleValue = prefs.getString(Constants.PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteSharedPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(Constants.PREFS_NAME, 0).edit();
        prefs.remove(Constants.PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.widget_configuration);
        widgetEditText = (EditText) findViewById(R.id.widget_edit_text);
        findViewById(R.id.widget_button).setOnClickListener(widgetOnClickListener);

        // Find the widget id from the intent.
        Intent widgetIntent = getIntent();
        Bundle extras = widgetIntent.getExtras();
        if (extras != null) {
            movieWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (movieWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        widgetEditText.setText(loadSharedPref(WidgetConfigurationActivity.this, movieWidgetId));
    }
}

