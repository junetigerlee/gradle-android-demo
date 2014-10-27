package cn.com.incito.wisdom.uicomp.widget.listview.astickyheaderslideexpandable;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Simple subclass of listview which does nothing more than wrap any ListAdapter
 * in a SlideExpandalbeListAdapter
 */
public class SlideExpandableListViewTest extends ListView {
    private SlideExpandableListAdapterTest adapter;

    public SlideExpandableListViewTest(Context context) {
        super(context);
    }

    public SlideExpandableListViewTest(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideExpandableListViewTest(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Collapses the currently open view.
     * 
     * @return true if a view was collapsed, false if there was no open view.
     */
    public boolean collapse() {
        if (adapter != null) {
            return adapter.collapseLastOpen();
        }
        return false;
    }

    public void setAdapter(ListAdapter adapter) {
        // this.adapter = new SlideExpandableListAdapter(adapter);
        this.adapter = (SlideExpandableListAdapterTest) adapter;
        super.setAdapter(this.adapter);
    }

    /**
     * Registers a OnItemClickListener for this listview which will expand the
     * item by default. Any other OnItemClickListener will be overriden.
     * 
     * To undo call setOnItemClickListener(null)
     * 
     * Important: This method call setOnItemClickListener, so the value will be
     * reset
     */
    public void enableExpandOnItemClick() {
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                    int i, long l) {
                SlideExpandableListAdapterTest adapter = (SlideExpandableListAdapterTest) getAdapter();
                adapter.getExpandToggleButton(view).performClick();
            }
        });
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return adapter.onSaveInstanceState(super.onSaveInstanceState());
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof AbstractSlideExpandableListAdapterTest.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        AbstractSlideExpandableListAdapterTest.SavedState ss = (AbstractSlideExpandableListAdapterTest.SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        adapter.onRestoreInstanceState(ss);
    }
}