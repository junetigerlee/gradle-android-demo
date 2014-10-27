package cn.com.incito.wisdom.uicomp.widget.slideexpandable_el;

import java.util.BitSet;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;

/**
 * Wraps a ListAdapter to give it expandable list view functionality. The main
 * thing it does is add a listener to the getToggleButton which expands the
 * getExpandableView for each list item.
 * 
 * @author tjerk
 * @date 6/9/12 4:41 PM
 */
public abstract class AbstractSlideExpandableListAdapter extends
		WrapperListAdapterImpl {
	/**
	 * Reference to the last expanded list item. Since lists are recycled this
	 * might be null if though there is an expanded list item
	 */
	private View lastOpen = null;
	/**
	 * The position of the last expanded list item. If -1 there is no list item
	 * expanded. Otherwise it points to the position of the last expanded list
	 * item
	 */
	private int lastOpenPosition = -1;
	/**
	 * The position of the last expanded list item. If -1 there is no list item
	 * expanded. Otherwise it points to the position of the last expanded list
	 * item
	 */
	private int lastOpenGroupPosition = -1;

	/**
	 * Default Animation duration Set animation duration with @see
	 * setAnimationDuration
	 */
	private int animationDuration = 330;

	/**
	 * A list of positions of all list items that are expanded. Normally only
	 * one is expanded. But a mode to expand multiple will be added soon.
	 * 
	 * If an item onj position x is open, its bit is set
	 */
	private BitSet openItems = new BitSet();
	/**
	 * We remember, for each collapsable view its height. So we dont need to
	 * recalculate. The height is calculated just before the view is drawn.
	 */
	private final SparseIntArray viewHeights = new SparseIntArray(10);

	public AbstractSlideExpandableListAdapter(ExpandableListAdapter wrapped) {
		super(wrapped);
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		convertView = wrapped.getChildView(groupPosition, childPosition,
				isLastChild, convertView, parent);
		enableFor(convertView, groupPosition, childPosition);
		return convertView;
	}

	/**
	 * This method is used to get the Button view that should expand or collapse
	 * the Expandable View. <br/>
	 * Normally it will be implemented as:
	 * 
	 * <pre>
	 * return parent.findViewById(R.id.expand_toggle_button)
	 * </pre>
	 * 
	 * A listener will be attached to the button which will either expand or
	 * collapse the expandable view
	 * 
	 * @see #getExpandableView(View)
	 * @param parent
	 *            the list view item
	 * @ensure return!=null
	 * @return a child of parent which is a button
	 */
	public abstract View getExpandToggleButton(View parent);

	/**
	 * This method is used to get the view that will be hidden initially and
	 * expands or collapse when the ExpandToggleButton is pressed @see
	 * getExpandToggleButton <br/>
	 * Normally it will be implemented as:
	 * 
	 * <pre>
	 * return parent.findViewById(R.id.expandable)
	 * </pre>
	 * 
	 * @see #getExpandToggleButton(View)
	 * @param parent
	 *            the list view item
	 * @ensure return!=null
	 * @return a child of parent which is a view (or often ViewGroup) that can
	 *         be collapsed and expanded
	 */
	public abstract View getExpandableView(View parent);

	/**
	 * Gets the duration of the collapse animation in ms. Default is 330ms.
	 * Override this method to change the default.
	 * 
	 * @return the duration of the anim in ms
	 */
	public int getAnimationDuration() {
		return animationDuration;
	}

	/**
	 * Set's the Animation duration for the Expandable animation
	 * 
	 * @param duration
	 *            The duration as an integer in MS (duration > 0)
	 * @exception IllegalArgumentException
	 *                if parameter is less than zero
	 */
	public void setAnimationDuration(int duration) {
		if (duration < 0) {
			throw new IllegalArgumentException("Duration is less than zero");
		}

		animationDuration = duration;
	}

	/**
	 * Check's if any position is currently Expanded To collapse the open item @see
	 * collapseLastOpen
	 * 
	 * @return boolean True if there is currently an item expanded, otherwise
	 *         false
	 */
	public boolean isAnyItemExpanded() {
		return (lastOpenPosition != -1) ? true : false;
	}

	public void enableFor(View parent, int groupPosition, int position) {
		View more = getExpandToggleButton(parent);
		View itemToolbar = getExpandableView(parent);
		itemToolbar.measure(parent.getWidth(), parent.getHeight());

		enableFor(more, itemToolbar, groupPosition, position);
	}

	private void enableFor(final View button, final View target,
			final int groupPosition, final int position) {
		if (target == lastOpen && position != lastOpenPosition
				&& groupPosition == lastOpenGroupPosition) {
			// lastOpen is recycled, so its reference is false
			lastOpen = null;
		}
		if (position == lastOpenPosition
				&& groupPosition == lastOpenGroupPosition) {
			// re reference to the last view
			// so when can animate it when collapsed
			lastOpen = target;
		}
		int height = viewHeights.get(groupPosition * 10 + position, -1);
		if (height == -1) {
			viewHeights.put(groupPosition * 10 + position,
					target.getMeasuredHeight());
			updateExpandable(target, groupPosition, position);
		} else {
			updateExpandable(target, groupPosition, position);
		}

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {

				Animation a = target.getAnimation();

				if (a != null && a.hasStarted() && !a.hasEnded()) {

					a.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {
						}

						@Override
						public void onAnimationEnd(Animation animation) {
							view.performClick();
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
						}
					});

				} else {

					target.setAnimation(null);

					int type = 0;

					if (target.getVisibility() == View.VISIBLE) {
						type = ExpandCollapseAnimation.COLLAPSE;
					} else {
						type = ExpandCollapseAnimation.EXPAND;
					}

					// remember the state
					if (type == ExpandCollapseAnimation.EXPAND) {
						openItems.set(groupPosition * 10 + position, true);
					} else {
						openItems.set(groupPosition * 10 + position, false);
					}
					// check if we need to collapse a different view
					if (type == ExpandCollapseAnimation.EXPAND) {

						if (lastOpenPosition != -1
								&& lastOpenGroupPosition != -1
								&& lastOpenGroupPosition == groupPosition) {
							if (lastOpenPosition != position) {
								if (lastOpen != null) {
									animateView(lastOpen,
											ExpandCollapseAnimation.COLLAPSE);
								}
								openItems.set(lastOpenGroupPosition * 10
										+ lastOpenPosition, false);
							}
						} else if (lastOpenPosition != -1
								&& lastOpenGroupPosition != -1
								&& lastOpenGroupPosition != groupPosition) {
							if (lastOpen != null) {
								animateView(lastOpen,
										ExpandCollapseAnimation.COLLAPSE);
							}
							openItems.set(lastOpenGroupPosition * 10
									+ lastOpenPosition, false);
						}

						lastOpen = target;
						lastOpenPosition = position;
						lastOpenGroupPosition = groupPosition;
					} else if (lastOpenPosition == position
							&& lastOpenGroupPosition == groupPosition) {
						lastOpenPosition = -1;
						lastOpenGroupPosition = -1;
					}
					animateView(target, type);
				}
			}
		});
	}

	@Override
	public ExpandableListAdapter getWrapAdapter() {
		return super.getWrapAdapter();
	}

	private void updateExpandable(View target, int groupPosition, int position) {

		final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) target
				.getLayoutParams();
		if (openItems.get(groupPosition * 10 + position)) {
			target.setVisibility(View.VISIBLE);
			params.bottomMargin = 0;
		} else {
			target.setVisibility(View.GONE);
			params.bottomMargin = 0 - viewHeights.get(groupPosition * 10
					+ position);
		}
	}

	/**
	 * Performs either COLLAPSE or EXPAND animation on the target view
	 * 
	 * @param target
	 *            the view to animate
	 * @param type
	 *            the animation type, either ExpandCollapseAnimation.COLLAPSE or
	 *            ExpandCollapseAnimation.EXPAND
	 */
	private void animateView(final View target, final int type) {
		Animation anim = new ExpandCollapseAnimation(target, type);
		anim.setDuration(getAnimationDuration());
		target.startAnimation(anim);
	}

	/**
	 * Closes the current open item. If it is current visible it will be closed
	 * with an animation.
	 * 
	 * @return true if an item was closed, false otherwise
	 */
	public boolean collapseLastOpen() {
		if (isAnyItemExpanded()) {
			// if visible animate it out
			if (lastOpen != null) {
				animateView(lastOpen, ExpandCollapseAnimation.COLLAPSE);
			}
			openItems.set(lastOpenGroupPosition * 10 + lastOpenPosition, false);
			lastOpenPosition = -1;
			lastOpenGroupPosition = -1;
			return true;
		}
		return false;
	}

	public Parcelable onSaveInstanceState(Parcelable parcelable) {

		SavedState ss = new SavedState(parcelable);
		ss.lastOpenPosition = this.lastOpenPosition;
		ss.lastOpenGroupPosition = this.lastOpenGroupPosition;
		ss.openItems = this.openItems;
		return ss;
	}

	public void onRestoreInstanceState(SavedState state) {

		this.lastOpenPosition = state.lastOpenPosition;
		this.lastOpenGroupPosition = state.lastOpenGroupPosition;
		this.openItems = state.openItems;
	}

	/**
	 * Utility methods to read and write a bitset from and to a Parcel
	 */
	private static BitSet readBitSet(Parcel src) {
		int cardinality = src.readInt();

		BitSet set = new BitSet();
		for (int i = 0; i < cardinality; i++) {
			set.set(src.readInt());
		}

		return set;
	}

	private static void writeBitSet(Parcel dest, BitSet set) {
		int nextSetBit = -1;

		dest.writeInt(set.cardinality());

		while ((nextSetBit = set.nextSetBit(nextSetBit + 1)) != -1) {
			dest.writeInt(nextSetBit);
		}
	}

	/**
	 * The actual state class
	 */
	static class SavedState extends View.BaseSavedState {
		public BitSet openItems = null;
		public int lastOpenPosition = -1;
		public int lastOpenGroupPosition = -1;

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			in.writeInt(lastOpenPosition);
			in.writeInt(lastOpenGroupPosition);
			writeBitSet(in, openItems);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			lastOpenPosition = out.readInt();
			lastOpenGroupPosition = out.readInt();
			openItems = readBitSet(out);
		}

		// required field that makes Parcelables from a Parcel
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}
