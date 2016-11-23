package com.sam_chordas.android.stockhawk.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.Utils;

/**
 * Created by Prateek on 28-09-2016.
 */
public class StockWidgetRemoteViewsService extends RemoteViewsService {
    private static final String TAG = "Values";
    private static final String SYMBOL = "symbol";

    private final String[] STOCK_COLUMNS = {
            QuoteColumns._ID,
            QuoteColumns.BIDPRICE,
            QuoteColumns.SYMBOL,
            QuoteColumns.CHANGE,
            QuoteColumns.CREATED,
            QuoteColumns.PERCENT_CHANGE,
            QuoteColumns.ISUP,
            QuoteColumns.ISCURRENT
    };

    static final int INDEX_ID = 0;
    static final int INDEX_BID_PRICE = 1;
    static final int INDEX_SYMBOL = 2;
    static final int INDEX_COMPANY_NAME = 3;
    static final int INDEX_CHANGE = 4;
    static final int INDEX_CREATED = 5;
    static final int INDEX_PERCENT_CHANGE = 6;
    static final int INDEX_IS_UP = 7;
    static final int INDEX_IS_CURRENT = 8;


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                //Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();

                data = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                        STOCK_COLUMNS,
                        QuoteColumns.ISCURRENT + " = ?",
                        new String[]{"1"},
                        null);
                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }

            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.stock_widget_list_item);

                String symbol = data.getString(INDEX_SYMBOL);
                views.setTextViewText(R.id.widget_stock_symbol, symbol);
                String bidPrice = data.getString(INDEX_BID_PRICE);
                views.setTextViewText(R.id.widget_bid_price, bidPrice);

                String change = data.getString(INDEX_CHANGE);
                String percentChange = data.getString(INDEX_PERCENT_CHANGE);
                if (Utils.showPercent) {
                    views.setTextViewText(R.id.widget_change, percentChange);
                } else {
                    views.setTextViewText(R.id.widget_change, change);
                }
                setRemoteContentDescription(views,symbol);
                final Intent fillIntent = new Intent();
                fillIntent.putExtra(SYMBOL,symbol);
                views.setOnClickFillInIntent(R.id.widget_list_item,fillIntent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(),R.layout.stock_widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views, String description) {
                views.setContentDescription(R.id.widget_list_item, description);
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(INDEX_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
