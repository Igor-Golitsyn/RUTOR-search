package polus.ddns.net.rutortorrentsearch.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Игорь on 29.06.2016.
 */

public class UserScrollBehavior extends CoordinatorLayout.Behavior<LinearLayout> {
    private int mMaxHeight = 0;

    public UserScrollBehavior(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View view) {
        child.setY(view.getY());
        return true;
    }
}