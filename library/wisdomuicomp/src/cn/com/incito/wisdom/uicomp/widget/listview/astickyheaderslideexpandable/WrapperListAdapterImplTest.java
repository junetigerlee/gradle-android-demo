package cn.com.incito.wisdom.uicomp.widget.listview.astickyheaderslideexpandable;

import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.WrapperListAdapter;
import cn.com.incito.wisdom.sdk.adapter.PageCursorAdapter;
import cn.com.incito.wisdom.uicomp.widget.listview.astickyheaderslideexpandable.PinnedSectionSlideexpandableListView.PinnedSectionListAdapter;

/**
 * Implementation of a WrapperListAdapter interface in which method delegates to
 * the wrapped adapter.
 * 
 * Extend this class if you only want to change a few methods of the wrapped
 * adapter.
 * 
 * The wrapped adapter is available to subclasses as the "wrapped" field.
 * 
 * @author tjerk
 * @date 6/9/12 4:41 PM
 */
public abstract class WrapperListAdapterImplTest extends BaseAdapter implements
        WrapperListAdapter, PinnedSectionListAdapter {
    protected ListAdapter wrapped;

    private boolean mValid = true;
    int mSectionResourceId;
    LayoutInflater mLayoutInflater;
    SparseArray<Section> mSections = new SparseArray<Section>();
    int mHeaderTextViewResId;

    public static class Section {
        int firstPosition;
        int sectionedPosition;
        CharSequence title;

        public Section(int firstPosition, CharSequence title) {
            this.firstPosition = firstPosition;
            this.title = title;
        }

        public CharSequence getTitle() {
            return title;
        }
    }

    public WrapperListAdapterImplTest(ListAdapter wrapped) {
        this.wrapped = wrapped;
    }

    public WrapperListAdapterImplTest(Context context, ListAdapter baseAdapter,
            int sectionResourceId, int headerTextViewResId) {
        mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSectionResourceId = sectionResourceId;
        mHeaderTextViewResId = headerTextViewResId;
        wrapped = baseAdapter;
        // wrapped.registerDataSetObserver(new DataSetObserver() {
        // @Override
        // public void onChanged() {
        // mValid = !wrapped.isEmpty();
        // notifyDataSetChanged();
        // }
        //
        // @Override
        // public void onInvalidated() {
        // mValid = false;
        // notifyDataSetInvalidated();
        // }
        // });
    }

    public void setSections(Section[] sections) {
        mSections.clear();

        // notifyDataSetChanged();
        Arrays.sort(sections, new Comparator<Section>() {
            @Override
            public int compare(Section o, Section o1) {
                return (o.firstPosition == o1.firstPosition) ? 0
                        : ((o.firstPosition < o1.firstPosition) ? -1 : 1);
            }
        });

        int offset = 0; // offset positions for the headers we're adding
        for (Section section : sections) {
            section.sectionedPosition = section.firstPosition + offset;
            mSections.append(section.sectionedPosition, section);
            ++offset;
        }

        // notifyDataSetChanged();
    }

    @Override
    public ListAdapter getWrappedAdapter() {
        return wrapped;
    }

    public int positionToSectionedPosition(int position) {
        int offset = 0;
        for (int i = 0; i < mSections.size(); i++) {
            if (mSections.valueAt(i).firstPosition > position) {
                break;
            }
            ++offset;
        }
        return position + offset;
    }

    public int sectionedPositionToPosition(int sectionedPosition) {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return ListView.INVALID_POSITION;
        }

        int offset = 0;
        for (int i = 0; i < mSections.size(); i++) {
            if (mSections.valueAt(i).sectionedPosition > sectionedPosition) {
                break;
            }
            --offset;
        }
        return sectionedPosition + offset;
    }

    public boolean isSectionHeaderPosition(int position) {
        return mSections.get(position) != null;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return wrapped.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return isSectionHeaderPosition(position) ? false : wrapped
                .isEnabled(sectionedPositionToPosition(position));
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        wrapped.registerDataSetObserver(dataSetObserver);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        wrapped.unregisterDataSetObserver(dataSetObserver);
    }

    @Override
    public int getCount() {
        return (((PageCursorAdapter) wrapped).ismDataValid() ? wrapped
                .getCount() + mSections.size() : 0);
        // return (mValid ? wrapped.getCount() + mSections.size() : 0);
        // return wrapped.getCount();
    }

    @Override
    public Object getItem(int position) {
        return isSectionHeaderPosition(position) ? mSections.get(position)
                : wrapped.getItem(sectionedPositionToPosition(position));
    }

    @Override
    public long getItemId(int position) {
        return isSectionHeaderPosition(position) ? Integer.MAX_VALUE
                - mSections.indexOfKey(position) : wrapped
                .getItemId(sectionedPositionToPosition(position));
    }

    @Override
    public boolean hasStableIds() {
        return wrapped.hasStableIds();
    }

    // @Override
    // public View getView(int position, View convertView, ViewGroup parent) {
    // if (isSectionHeaderPosition(position)) {
    // TextView view;
    // if (null == convertView) {
    // convertView = mLayoutInflater.inflate(mSectionResourceId,
    // parent, false);
    // } else {
    // if (null == convertView.findViewById(mHeaderTextViewResId)) {
    // convertView = mLayoutInflater.inflate(mSectionResourceId,
    // parent, false);
    // }
    // }
    // view = (TextView) convertView.findViewById(mHeaderTextViewResId);
    // view.setText(mSections.get(position).title);
    // return convertView;
    //
    // } else {
    // return wrapped.getView(sectionedPositionToPosition(position),
    // convertView, parent);
    // }
    // }

    @Override
    public int getItemViewType(int position) {
        return isSectionHeaderPosition(position) ? getViewTypeCount() - 1
                : wrapped.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return wrapped.getViewTypeCount() + 1;
    }

    @Override
    public boolean isEmpty() {
        return wrapped.isEmpty();
    }

    @Override
    public boolean isItemViewTypePinned(int position) {
        return isSectionHeaderPosition(position);
    }
}
