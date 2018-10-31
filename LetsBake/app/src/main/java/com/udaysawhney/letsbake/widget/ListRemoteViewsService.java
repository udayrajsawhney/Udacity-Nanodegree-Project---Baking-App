package com.udaysawhney.letsbake.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ListRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
