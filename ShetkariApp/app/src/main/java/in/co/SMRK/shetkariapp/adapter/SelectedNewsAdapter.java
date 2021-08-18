package in.co.SMRK.shetkariapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.model.SelectedNewsModel;

/**
 * Created by Jack on 8/18/2017.
 */

public class SelectedNewsAdapter extends ArrayAdapter<SelectedNewsModel> {

    private ArrayList<SelectedNewsModel> list;
    private Context mContext;
    private static LayoutInflater inflater = null;

    public SelectedNewsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<SelectedNewsModel> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        UserHolder userHolder = null;
        if (row == null) {

            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.listview_row, parent, false);
            userHolder = new UserHolder();

            userHolder.tvNewsTitle = (TextView) row.findViewById(R.id.tv_newsShortTitle);
            userHolder.tv_news_Date = (TextView) row.findViewById(R.id.tv_newsDate);
            userHolder.tvLongDesc = (TextView) row.findViewById(R.id.tv_newsLongDescription);

            row.setTag(userHolder);

        }else {
            userHolder = (UserHolder)row.getTag();
        }

        SelectedNewsModel model = list.get(position);
        userHolder.tvNewsTitle.setText(model.getShortTitle());
        userHolder.tv_news_Date.setText(model.getNewsDate());
        userHolder.tvLongDesc.setText(model.getLongDescription());

        return row;
    }


    static class UserHolder {
        TextView tvNewsTitle;
        TextView tv_news_Date;
        TextView tvLongDesc;

    }



}
