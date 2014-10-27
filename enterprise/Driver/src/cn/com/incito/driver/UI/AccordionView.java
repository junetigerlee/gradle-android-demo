/**
 * Copyright (c) 2011, 2012 Sentaca Communications Ltd.
 */
package cn.com.incito.driver.UI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.incito.driver.R;
import cn.com.incito.driver.listeners.OnChangeListener;

public class AccordionView extends LinearLayout {

	private boolean initialized = false;

	// -- from xml parameter
	private int headerLayoutId;
	private int headerLabelId;
	private String headerLabelStr;
	private int headerContentId;
	private int sectionLayoutId;

	private View headerLayoutView;
	private View cotentLayoutView;

	public AccordionView(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.accordion);
			headerLayoutId = a.getResourceId(
					R.styleable.accordion_header_layout_id, 0);
			headerLabelId = a.getResourceId(
					R.styleable.accordion_header_layout_label_id, 0);
			headerLabelStr = (String) a
					.getText(R.styleable.accordion_header_layout_label_string);
			headerContentId = a.getResourceId(
					R.styleable.accordion_header_layout_content_id, 0);
			sectionLayoutId = a.getResourceId(
					R.styleable.accordion_section_layout_id, 0);
		}

		if (headerLayoutId == 0 || headerLabelId == 0 || sectionLayoutId == 0
				|| headerContentId == 0) {
			throw new IllegalArgumentException(
					"Please set all header_layout_id,  header_layout_label_id, section_layout_id ,headerContentIdattributes.");
		}

		setOrientation(VERTICAL);
	}

	private View getView(final LayoutInflater inflater) {
		cotentLayoutView = inflater.inflate(sectionLayoutId, null);
		cotentLayoutView.setLayoutParams(new ListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 0));
		cotentLayoutView.setVisibility(GONE);
		return cotentLayoutView;
	}

	private View getViewHeader(LayoutInflater inflater) {
		headerLayoutView = inflater.inflate(headerLayoutId, null);
		((TextView) headerLayoutView.findViewById(headerLabelId))
				.setText(headerLabelStr);

		headerLayoutView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				toggleSection();
			}
		});

		return headerLayoutView;
	}

	@Override
	protected void onFinishInflate() {
		if (initialized) {
			super.onFinishInflate();
			return;
		}

		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		removeAllViews();

		View header = getViewHeader(inflater);
		View content = getView(inflater);
		final LinearLayout section = new LinearLayout(getContext());
		section.setOrientation(LinearLayout.VERTICAL);
		section.addView(header);
		section.addView(content);

		addView(section);

		initialized = true;

		super.onFinishInflate();
	}

	public void toggleSection() {

		if (cotentLayoutView.getVisibility() == VISIBLE) {
			ViewAnimatorHelper.hideAnimator(cotentLayoutView);
			((TextView) headerLayoutView.findViewById(headerContentId))
					.setBackground(getResources().getDrawable(
							R.drawable.left_search_hide_editbg));
			mchangeListener.onChange(getId(), this);
			isExpand = false;
		} else {
			ViewAnimatorHelper.expendAnimator(cotentLayoutView);
			((TextView) headerLayoutView.findViewById(headerContentId))
					.setBackground(getResources().getDrawable(
							R.drawable.left_search_expand_editbg));
			mchangeListener.onChange(getId(), this);
			isExpand = true;
		}
	}

	public boolean isExpand = false;

	public OnChangeListener mchangeListener;

	public void setOnChangeListener(OnChangeListener chaneListener) {
		mchangeListener = chaneListener;
	}

	public void setContentText(String string) {
		((TextView) headerLayoutView.findViewById(headerContentId))
				.setText(string);
	}

	public String getContentText() {
		return ((TextView) headerLayoutView.findViewById(headerContentId))
				.getText().toString();
	}

	static class ViewAnimatorHelper {

		public static void expendAnimator(final View v) {
			v.setVisibility(View.VISIBLE);
			final int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			final int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			v.measure(widthSpec, heightSpec);

			ValueAnimator valueAnimator = createValueAnimator(v, 0,
					v.getMeasuredHeight());

			valueAnimator.start();
		}

		public static void hideAnimator(final View v) {

			final int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			final int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			v.measure(widthSpec, heightSpec);

			ValueAnimator valueAnimator = createValueAnimator(v,
					v.getMeasuredHeight(), 0);

			valueAnimator.addListener(new AnimatorListenerAdapter() {

				@Override
				public void onAnimationEnd(Animator animation) {

					v.setVisibility(View.GONE);

				}
			});

			valueAnimator.start();
		}

		public static ValueAnimator createValueAnimator(final View v,
				int start, int end) {
			ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
			valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {

					int current = (Integer) animation.getAnimatedValue();

					ViewGroup.LayoutParams params = v.getLayoutParams();
					params.height = current;
					v.setLayoutParams(params);
				}
			});
			return valueAnimator;
		}

	}

}
