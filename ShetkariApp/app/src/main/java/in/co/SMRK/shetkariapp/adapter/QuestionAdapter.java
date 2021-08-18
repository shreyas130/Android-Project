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
import in.co.SMRK.shetkariapp.model.QuestionModel;

/**
 * Created by Jack on 8/28/2017.
 */

public class QuestionAdapter extends ArrayAdapter<QuestionModel> {

    private Context mContext;
    private ArrayList<QuestionModel> data;
    private QuestionModel model;
    private LayoutInflater inflater = null;

    public QuestionAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<QuestionModel> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.data = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.allquestion_row,parent,false);

        TextView tv_Title = (TextView) view.findViewById(R.id.tv_QuestionTitle);
        TextView tv_Date = (TextView) view.findViewById(R.id.tv_questionDate);


        model = data.get(position);

        if (model !=null){

            String title = data.get(position).getQuestionTitle();
            String dateTime = data.get(position).getQuestionDateTime();

            tv_Title.setText(title);
            tv_Date.setText(dateTime);
        }
        return view;
    }
}
