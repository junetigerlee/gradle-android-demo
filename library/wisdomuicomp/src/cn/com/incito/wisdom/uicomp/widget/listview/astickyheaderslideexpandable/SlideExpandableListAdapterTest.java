package cn.com.incito.wisdom.uicomp.widget.listview.astickyheaderslideexpandable;

import android.content.Context;
import android.view.View;
import android.widget.ListAdapter;

/**
 * ListAdapter that adds sliding functionality to a list. Uses
 * R.id.expandalbe_toggle_button and R.id.expandable id's if no ids are given in
 * the contructor.
 * 
 * @author tjerk
 * @date 6/13/12 8:04 AM
 */
public class SlideExpandableListAdapterTest extends
        AbstractSlideExpandableListAdapterTest {
    private int toggle_button_id;
    private int expandable_view_id;

    public SlideExpandableListAdapterTest(Context context, ListAdapter wrapped,
            int toggle_button_id, int expandable_view_id,
            int sectionResourceId, int headerTextViewResId) {
        super(context, wrapped, sectionResourceId, headerTextViewResId);
        this.toggle_button_id = toggle_button_id;
        this.expandable_view_id = expandable_view_id;
    }

    // public SlideExpandableListAdapterTest(ListAdapter wrapped) {
    // this(wrapped, R.id.expandable_toggle_button, R.id.expandable);
    // }

    @Override
    public View getExpandToggleButton(View parent) {
        return parent.findViewById(toggle_button_id);
    }

    @Override
    public View getExpandableView(View parent) {
        return parent.findViewById(expandable_view_id);
    }
}
