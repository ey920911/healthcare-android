package example.com.healthcare;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import bean.UserItem;
import example.com.R;

/**
 * Created by Admin on 2014-09-10.
 */
public class ScoreListAdapter extends ArrayAdapter<UserItem> {

    private LayoutInflater inflater;
    private int resource;
    private int index;

    private ArrayList<UserItem> list;
    private UserItem user;
    private ListView lv;

    public ScoreListAdapter(Context context, int resource, ArrayList<UserItem> objects, int position) {
        super(context, resource, objects);
        this.inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.list = objects;
        this.index = position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        lv = (ListView)parent;
        ViewHolder viewholder;
        if(convertView == null)
        {
            convertView = inflater.inflate(resource,null);
            viewholder = new ViewHolder();
            viewholder.nickname = (TextView)convertView.findViewById(R.id.tvNickname);
            viewholder.rank = (TextView)convertView.findViewById(R.id.tvRank);
            viewholder.image = (ImageView)convertView.findViewById(R.id.tvImage);
            viewholder.score = (TextView)convertView.findViewById(R.id.tvScore);

            convertView.setTag(viewholder);
        }
        else
        {
            viewholder = (ViewHolder)convertView.getTag();
        }

        user = list.get(position);
        viewholder.nickname.setText(user.getNickname());
        viewholder.rank.setText(String.valueOf(position+1)+"위");
        viewholder.image.setImageBitmap(user.getImage());
        String score_str = String.valueOf(user.getScore())+" Kcal";
        SpannableString sText = new SpannableString(score_str);
        sText.setSpan(new ForegroundColorSpan(Color.RED), 0, score_str.length() - 4, 0);

        viewholder.score.setText(sText);



        return convertView;
    }

    class ViewHolder
    {
        TextView rank,nickname,score;
        ImageView image;
    }
}
