package cn.com.incito.wisdom.uicomp.widget.slideexpandable_el;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;

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
public abstract class WrapperListAdapterImpl extends BaseExpandableListAdapter {
	protected ExpandableListAdapter wrapped;

	public WrapperListAdapterImpl(ExpandableListAdapter wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return wrapped.areAllItemsEnabled();
	}

	public ExpandableListAdapter getWrapAdapter() {
		return wrapped;
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
	public int getChildrenCount(int groupPosition) {
		return wrapped.getChildrenCount(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return wrapped.getChild(groupPosition, childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return wrapped.getChildId(groupPosition, childPosition);
	}

	@Override
	public boolean hasStableIds() {
		return wrapped.hasStableIds();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		return wrapped.getChildView(groupPosition, childPosition, isLastChild,
				convertView, parent);
	}

	@Override
	public boolean isEmpty() {
		return wrapped.isEmpty();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return wrapped.getGroup(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return wrapped.getGroupCount();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return wrapped.getGroupId(groupPosition);
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return wrapped.getGroupView(groupPosition, isExpanded, convertView,
				parent);
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return wrapped.isChildSelectable(groupPosition, childPosition);
	}
}
