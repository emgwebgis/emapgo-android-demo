package com.emapgo.android.demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.ScreenUtil;
import com.emapgo.mapsdk.search.route.maneuver.parse.DirectionsManeuverParseInfo;

import java.util.List;

/**
 * Created by ben on 2018/4/20.
 */

public class DirectionsProgramAdapter implements View.OnClickListener {
    private LinearLayout mParentView;
    private OnClickListener onClickListener;
    private int mLayout = R.layout.item_directions_program_layout;

    public DirectionsProgramAdapter(LinearLayout linearLayout) {
        this.mParentView = linearLayout;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setNewData(List<DirectionsManeuverParseInfo> dms, int position) {
        try {
            mParentView.removeAllViews();
            for (int i = 0; i < dms.size(); i++) {
                View view = LayoutInflater.from(mParentView.getContext()).inflate(mLayout, null, false);
                convert(view, dms.get(i), i);
                mParentView.addView(view);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                layoutParams.width = ScreenUtil.getScreenWidth(mParentView.getContext()) / dms.size();
                layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
                view.requestLayout();
                if (i == position) {
                    view.setBackgroundResource(R.drawable.bg_directions_program);
                }
                view.setTag(i);
                view.setOnClickListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void convert(View view, DirectionsManeuverParseInfo item, int position) {
        ((TextView) view.findViewById(R.id.id_route_program_title)).setText(parsePosition(position));
        ((TextView) view.findViewById(R.id.id_route_program_duration)).setText(item.getDuration());
        ((TextView) view.findViewById(R.id.id_route_program_distance)).setText(item.getDistance());
    }

    private String parsePosition(int position) {
        switch (position) {
            case 0:
                return "方案一";
            case 1:
                return "方案二";
            default:
                return "其他方案";
        }
    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer) v.getTag();
        if (onClickListener != null) {
            onClickListener.onClickListener(position);
        }
    }

    public interface OnClickListener{
        void onClickListener(int position);
    }
}
