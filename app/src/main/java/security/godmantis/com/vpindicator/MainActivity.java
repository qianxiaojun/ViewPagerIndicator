package security.godmantis.com.vpindicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> mTitles= Arrays.asList("Tab1","Tab2","Tab3","Tab4","Tab5","Tab6","Tab7","Tab8","Tab9");
    private List<Fragment> mFragments=new ArrayList<>();
    private ViewPagerIndicator mIndicator;
    private ViewPager mVp;
    private FragmentPagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();

        initDatas();
        mIndicator.setVisibleTabCount(4);
        mIndicator.setTabItemTitles(mTitles);
        mVp.setAdapter(mPagerAdapter);
        mIndicator.setViewPager(mVp,0);

    }

    private void initDatas() {
        for (String title : mTitles) {
            ContentFragment contentFragment = ContentFragment.newInstance(title);
            mFragments.add(contentFragment);
        }
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }
        };
    }

    private void initView() {
        mIndicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        mVp = (ViewPager) findViewById(R.id.vp);
    }
}
