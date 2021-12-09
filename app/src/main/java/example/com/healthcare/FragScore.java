package example.com.healthcare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import bean.UserItem;
import example.com.R;

/**
 * Created by Admin on 2014-09-10.
 */
public class FragScore extends Fragment {
    private int position;
    private ListView list;

    private ArrayList<UserItem> rankList;
    private ScoreListAdapter adapter;

    public static FragScore newInstance(int position)
    {
        FragScore frag = new FragScore();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_score, container,false);
        list = (ListView)v.findViewById(R.id.list);
        position = getArguments().getInt("position");


        switch(position)
        {
            case 0:

                adapter = new ScoreListAdapter(getActivity(), R.layout.rank_list, Info.Rank_Total, position);
                break;
            case 1:

                adapter = new ScoreListAdapter(getActivity(), R.layout.rank_list, Info.Rank_Walk, position);
                break;
            case 2:

                adapter = new ScoreListAdapter(getActivity(), R.layout.rank_list, Info.Rank_Run, position);
                break;
            case 3:

                 adapter = new ScoreListAdapter(getActivity(), R.layout.rank_list, Info.Rank_Pushup, position);
                break;
            case 4:

                adapter = new ScoreListAdapter(getActivity(), R.layout.rank_list, Info.Rank_Situp, position);
                break;
        }

        list.setAdapter(adapter);

       /* rank = new Rank_connection(Info.PHONE_NUM);
        rank.start();*/


        return v;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");
    }



}
