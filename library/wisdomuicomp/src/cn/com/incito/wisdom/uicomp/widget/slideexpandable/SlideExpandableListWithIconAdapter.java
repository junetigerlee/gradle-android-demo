package cn.com.incito.wisdom.uicomp.widget.slideexpandable;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import cn.com.incito.wisdom.uicomp.R;

/**
 * ListAdapter that adds sliding functionality to a list. Uses
 * R.id.expandalbe_toggle_button and R.id.expandable id's if no ids are given in
 * the contructor.
 * 
 * @author tjerk
 * @date 6/13/12 8:04 AM
 */
public class SlideExpandableListWithIconAdapter extends
		AbstractSlideExpandableListWithIconAdapter {
	private int toggle_button_id;
	private int expandable_view_id;
	private int expanable_icon_view_id;// expanable_icon_view_id imageview
	private int expanable_icon__expand_id;// expanable_icon_view_id expand
											// imageview src
											// resource id
	private int expanable_icon_collapse_id;// expanable_icon_collapse_id
											// collapse imageview src resource
											// id

	public SlideExpandableListWithIconAdapter(ListAdapter wrapped,
			int toggle_button_id, int expandable_view_id,
			int expanable_icon_view_id, int expanable_icon_collapse_id,
			int expanable_icon__expand_id) {
		super(wrapped);
		this.toggle_button_id = toggle_button_id;
		this.expandable_view_id = expandable_view_id;
		this.expanable_icon_view_id = expanable_icon_view_id;
		this.expanable_icon_collapse_id = expanable_icon_collapse_id;
		this.expanable_icon__expand_id = expanable_icon__expand_id;
	}

	public SlideExpandableListWithIconAdapter(ListAdapter wrapped) {
		this(wrapped, R.id.expandable_toggle_button, R.id.expandable,
				R.id.expandable_icon, R.id.expandable_icon_collapse,
				R.id.expandable_icon_expand);
	}

	@Override
	public View getExpandToggleButton(View parent) {
		return parent.findViewById(toggle_button_id);
	}

	@Override
	public View getExpandableView(View parent) {
		return parent.findViewById(expandable_view_id);
	}

	@Override
	public ImageView getExpandableIcon(View parent) {
		return (ImageView) parent.findViewById(expanable_icon_view_id);
	}

	@Override
	public int getExpandableIconExpand() {
		return expanable_icon__expand_id;
	}

	@Override
	public int getExpandableIconCollapse() {
		return expanable_icon_collapse_id;
	}

}
