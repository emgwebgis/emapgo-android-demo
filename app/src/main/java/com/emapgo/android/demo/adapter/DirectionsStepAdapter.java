package com.emapgo.android.demo.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.emapgo.android.demo.R;
import com.emapgo.api.directions.v5.models.LegStep;
import com.emapgo.mapsdk.search.route.maneuver.ManeuverView;
import com.emapgo.mapsdk.search.route.maneuver.parse.DirectionsManeuverParseInfo;

/**
 * Created by ben on 2018/4/20.
 */

public class DirectionsStepAdapter extends BaseQuickAdapter<DirectionsManeuverParseInfo.DirectionsManeuverItem, BaseViewHolder> {
    public DirectionsStepAdapter() {
        super(R.layout.item_directions_step_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, DirectionsManeuverParseInfo.DirectionsManeuverItem item) {

        helper.setText(R.id.id_route_step_name, item.getDescription());
        helper.setText(R.id.id_route_step_distance, item.getDistance());
        helper.setGone(R.id.id_route_step_distance, item.getOrignStep().distance()>0);


        ManeuverView maneuverView = helper.getView(R.id.id_route_step_icon);
        LegStep orignStep = item.getOrignStep();
       /* maneuverView.setManeuverModifier(orignStep.maneuver().modifier());
        maneuverView.setManeuverType(orignStep.maneuver().type());*/

        maneuverView.setManeuver(orignStep.maneuver());
    }


}
