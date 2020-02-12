package com.example.finek.Start;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.finek.R;

import java.util.List;
import java.util.Map;

public class MapEntryListAdapter extends ArrayAdapter<Map.Entry<String, Object>>
{
    public MapEntryListAdapter (Context context, List<Map.Entry<String, Object>> entryList)
    {
        super(context, android.R.layout.simple_list_item_2, entryList);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent)
    {
        View currentItemView = convertView != null ? convertView :
                LayoutInflater.from (getContext ()).inflate (
R.layout.acc_list_item, parent, false);

        Map.Entry<String, Object> currentEntry = this.getItem (position);

        TextView prenom = currentItemView.findViewById (R.id.prenomL);
        TextView id = currentItemView.findViewById (R.id.idL);

        id.setText (currentEntry.getKey ());
        prenom.setText (currentEntry.getValue ().toString ());

        return currentItemView;
    }
}