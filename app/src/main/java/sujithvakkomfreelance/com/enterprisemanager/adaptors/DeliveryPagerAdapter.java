package sujithvakkomfreelance.com.enterprisemanager.adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import sujithvakkomfreelance.com.enterprisemanager.customviews.DeliveryAcceptedJobFragment;
import sujithvakkomfreelance.com.enterprisemanager.customviews.DeliveryJobsFragment;

public class DeliveryPagerAdapter extends FragmentPagerAdapter {
    public DeliveryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return DeliveryAcceptedJobFragment.newInstance();
        }
        return DeliveryJobsFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 0;
    }
}
