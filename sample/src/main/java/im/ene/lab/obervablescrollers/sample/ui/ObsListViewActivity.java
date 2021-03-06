package im.ene.lab.obervablescrollers.sample.ui;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.AdapterView;

import im.ene.lab.obervablescrollers.sample.R;
import im.ene.lab.obervablescrollers.sample.adapter.DummyListViewAdapter;
import im.ene.lab.observablescrollers.lib.util.LogHelper;
import im.ene.lab.observablescrollers.lib.util.OnScrollObservedListener;
import im.ene.lab.observablescrollers.lib.util.Scrollable;
import im.ene.lab.observablescrollers.lib.widget.ObsListView;

public class ObsListViewActivity extends BaseActivity {

    public static final String TAG = LogHelper.createLogTag(ObsListViewActivity.class);

    private ObsListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs_list_view);
        getActionbarToolbar();
        mListView = (ObsListView) findViewById(R.id.listview);
    }

    DummyListViewAdapter dummyListViewAdapter;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        dummyListViewAdapter = new DummyListViewAdapter(this);
        mListView.setAdapter(dummyListViewAdapter);

        mListView.setPadding(mListView.getPaddingLeft(), (int) (mListView.getPaddingTop() + getMaxTranslationYRange()),
                mListView.getPaddingRight(), mListView.getPaddingBottom());

        mListView.setOnScrollObservedListener(new OnScrollObservedListener() {
            @Override
            public void onScrollChanged(Scrollable scroller, int dx, int dy) {
                float currTransY = (int) ViewCompat.getTranslationY(getActionbarToolbar());
                float transition = Math.min(getMinTransition(), Math.max(-getMaxTransition(), currTransY - dy));
                ViewCompat.setTranslationY(getActionbarToolbar(), transition);
            }

            @Override
            public void onScrollStateChanged(Scrollable scroller, Scrollable.ScrollState newState) {
                if (isCollapseViewTotallyShownOrHidden())
                    return;

                if (newState == Scrollable.ScrollState.SCROLL_STATE_IDLE) {
                    if (scroller.getVerticalScrollOffset() > getMaxTranslationYRange()) {
                        hideToolbar();
                    } else {
                        showToolbar();
                    }
                }
            }
        });

        // i want to test notifyDatasetChanged method
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dummyListViewAdapter.notifyDataSetChanged();
            }
        });
    }
}
