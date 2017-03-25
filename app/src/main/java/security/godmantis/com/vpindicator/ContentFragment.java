package security.godmantis.com.vpindicator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Alex on 2017/3/25.
 */

public class ContentFragment extends Fragment {

    private String mTitle;
    private static final String BUNDLE_TITLE="title";

    public static ContentFragment newInstance(String title){
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE,title);
        ContentFragment contentFragment = new ContentFragment();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle!=null) {
             mTitle = bundle.getString(BUNDLE_TITLE);
        }

        TextView textView = new TextView(getActivity());
        textView.setText(mTitle);
        textView.setTextSize(22);
        textView.setTextColor(Color.parseColor("#262626"));
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
}
