package amner.android.com.movies.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import amner.android.com.movies.R;
import amner.android.com.movies.SplashActivity;


public class Widget extends AppWidgetProvider {

    static void updateWidget(Context movieContext, AppWidgetManager appWidgetManager,
                             int appWidgetId) {


        CharSequence widgetChar = WidgetConfigurationActivity.loadSharedPref(movieContext, appWidgetId);
        RemoteViews remoteViews = new RemoteViews(movieContext.getPackageName(), R.layout.widget);
        remoteViews.setTextViewText(R.id.widget_edit_text, widgetChar);

        setAdapter(movieContext, remoteViews);

        Intent splashIntent = new Intent(movieContext, SplashActivity.class);
        PendingIntent moviePendingIntent = PendingIntent.getActivity(movieContext, 0, splashIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_grid_view, moviePendingIntent);
        remoteViews.setEmptyView(R.id.widget_grid_view, R.id.empty_movies);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setAdapter(Context context, RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_grid_view,
                new Intent(context, WidgetRemoteService.class));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            WidgetConfigurationActivity.deleteSharedPref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

