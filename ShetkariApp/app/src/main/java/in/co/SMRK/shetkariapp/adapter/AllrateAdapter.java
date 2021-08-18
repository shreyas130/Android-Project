package in.co.SMRK.shetkariapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.model.MarketRateModel;

/**
 * Created by Jack on 8/14/2017.
 */

public class AllrateAdapter extends ArrayAdapter<MarketRateModel> {

    private Context mContext;
    private ArrayList<MarketRateModel> list;
    private LayoutInflater inflater = null;


    public AllrateAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<MarketRateModel> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.list = objects;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        View view = convertView;
        if (convertView ==null){

            view = inflater.inflate(R.layout.listview_market,null);

            holder = new ViewHolder();

            holder.tv_MarketPlace = (TextView) view.findViewById(R.id.tv_marketName);
            holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
            holder.tv_max = (TextView) view.findViewById(R.id.tv_market_MaxRate);
            holder.tv_min = (TextView) view.findViewById(R.id.tv_market_MinRate);

            view.setTag(holder);

        }
        else {

            holder = (ViewHolder) view.getTag();
        }

        MarketRateModel model = list.get(position);

        if (model !=null){

            holder.tv_MarketPlace.setText(list.get(position).getMarketPlace());
            holder.tv_date.setText(list.get(position).getCurrentDate());
            holder.tv_max.setText(list.get(position).getMaximumRate());
            holder.tv_min.setText(list.get(position).getMinimumRate());

        }

        return view;
    }

    public static class ViewHolder{

        TextView tv_MarketPlace;
        TextView tv_date;
        TextView tv_max;
        TextView tv_min;


    }
}
