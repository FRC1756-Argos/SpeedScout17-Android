package dkt01.speedscout16;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class MatchListAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<Pair<Integer, String> > kvPair;
    private ArrayList<String> mIdList;

    public MatchListAdapter(Context context, ArrayList<Pair<Integer,String> > arrayList) {
        this.mContext = context;
        kvPair = new ArrayList<>();
        mIdList = new ArrayList<>();
        for (Pair<Integer,String> entry : arrayList) {
            add(entry);
        }
    }

    public MatchListAdapter(Context context, Pair<Integer,String>[] arrayList){
        this.mContext = context;
        kvPair = new ArrayList<>();
        mIdList = new ArrayList<>();
        for (Pair<Integer,String> entry : arrayList) {
            add(entry);
        }
    }

    public Pair<Integer,String> getObject(int position) {
        return kvPair.get(position);
    }

    public boolean add(Pair<Integer,String> object) {
        String id = object.second;

        mIdList.add(id);
        kvPair.add(object);
        this.notifyDataSetChanged();
        return true;
    }

    public void remove(int id){
        boolean removed = false;
        for(Pair<Integer,String> entry : kvPair)
        {
            if(entry.first == id)
            {
                mIdList.remove(kvPair.indexOf(entry));
                kvPair.remove(entry);
                removed = true;
            }
        }
        if(removed)
        {
            this.notifyDataSetChanged();
        }
    }

    @SuppressWarnings("unchecked")
    public Pair<Integer,String> getEntry(int position)
    {
        return kvPair.get(position);
    }

    @Override
    public int getCount() {
        return kvPair.size();
    }

    @Override
    public Object getItem(int position) {
        return kvPair.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Pair<Integer,String> object = kvPair.get(position);
        if (view == null) {
            int layoutResource = R.layout.matches_list_view_item;
            view = LayoutInflater.from(mContext).inflate(layoutResource, null);
        }
        TextView tv = (TextView) view.findViewById(R.id.matches_list_view_text);
        tv.setText(object.second);
        return view;
    }
}