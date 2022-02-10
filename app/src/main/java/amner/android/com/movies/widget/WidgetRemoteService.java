package amner.android.com.movies.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetRemoteService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
         //return remote view factory here
        return new WidgetProvider(this);
    }
}
