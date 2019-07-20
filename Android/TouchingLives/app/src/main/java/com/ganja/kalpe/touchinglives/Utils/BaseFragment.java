package com.ganja.kalpe.touchinglives.Utils;

import android.app.Activity;
import android.content.Context;

/**
 * Created by aditya on 24/01/18.
 */

public class BaseFragment extends android.app.Fragment {

    Context mContext;
    Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext=context;
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mActivity = activity;
    }

    public Context getBaseContext(){
        return mContext;
    }

    public  Activity getBaseActivity(){
        return mActivity;
    }


}
