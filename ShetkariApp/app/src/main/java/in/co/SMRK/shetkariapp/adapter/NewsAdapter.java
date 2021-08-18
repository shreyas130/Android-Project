package in.co.SMRK.shetkariapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.co.SMRK.shetkariapp.R;
import in.co.SMRK.shetkariapp.model.NewsModel;

/**
 * Created by Jack on 8/17/2017.
 */

public class NewsAdapter extends ArrayAdapter<NewsModel> {
    private Context mContext;
    private ArrayList<NewsModel> list;
    private  LayoutInflater inflater = null;


    public NewsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<NewsModel> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.list = objects;


    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View row;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.listview_row,parent,false);
        ImageView flag = (ImageView) row.findViewById(R.id.img_news);
        TextView tv_newsTitle = (TextView) row.findViewById(R.id.tv_newsTitle);
        TextView tv_news_Date = (TextView) row.findViewById(R.id.tv_Date);
        ImageView btn_Share = (ImageView) row.findViewById(R.id.img_share);
        NewsModel newsModel = list.get(position);
        if (newsModel !=null) {

            //int  newsId = list.get(position).getNewsId();
            tv_newsTitle.setText(list.get(position).getNewsShortTitle());

            tv_news_Date.setText(list.get(position).getNewsDate());
            Glide.with(mContext)
                    .load(list.get(position).getThumbnails())
                    .into(flag);

            btn_Share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = list.get(position).getNewsShortTitle();
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"www.google.com");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                    mContext.startActivity(Intent.createChooser(sharingIntent,"Share via"));

                }
            });

        }
        return row;
    }

}
