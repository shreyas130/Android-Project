package in.co.SMRK.shetkariapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.SMRK.shetkariapp.R;


/**
 * Created by Jack on 8/8/2017.
 */

public class GridDashboardAdapter extends BaseAdapter{

    private Context mContext;
    private static LayoutInflater inflater=null;

    public Integer[] mThumbnails = {

            R.drawable.rupees_icon,
            R.drawable.weather_icon,
            R.drawable.news_icon,
            R.drawable.icon_schedule,
            R.drawable.profile_icon,
            R.drawable.ask_icon


    };

    public Integer[] mTitle = {

            R.string.title_rate,
            R.string.title_weather,
            R.string.title_news,
            R.string.title_schedule,
            R.string.title_profile,
            R.string.title_technician

    };

    public GridDashboardAdapter(Context context){

        mContext = context;
    }


    @Override
    public int getCount() {
        return mThumbnails.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbnails[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        inflater = ( LayoutInflater )mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.grid_dashboard_row,null);

        TextView tv_title = (TextView) view.findViewById(R.id.tv_grid_title);
        ImageView img = (ImageView) view.findViewById(R.id.img_grid_dashboard);

        tv_title.setText(mTitle[position]);
        img.setImageResource(mThumbnails[position]);

        return view;
    }
}
