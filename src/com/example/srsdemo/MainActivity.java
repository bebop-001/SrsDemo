package com.example.srsdemo;
/* This is an java/android implementation of the SuperMemory
 * Spaced Repetition Algorithm.
 *
 * ref:http://www.supermemo.com/english/ol/sm2source.htm
 * Explanation:
 * ref:http://www.supermemo.com/english/ol/sm2.htm
 * Algorithm with pencil/paper.
 * ref:http://www.supermemo.com/articles/paper.htm
 */
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static String logTag = "MainActivity";
    private static final List <SRS> srsList = new ArrayList<SRS>();
    private static SRS srs = new SRS();
    private static SRS_ListAdapter srsListAdapter; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        srsListAdapter = new SRS_ListAdapter(this, R.layout.srs_list_item, srsList);
        ListView lv = (ListView)findViewById(R.id.record_list);
        lv.setAdapter(srsListAdapter);
        Log.i(logTag, "onCreate");
    }
    private class ViewHolder {
        protected TextView srsTv, idxTv;
        protected int index;
    }
    // listview for tracking object changes as new 'Assurance' button is pressed.
    private class SRS_ListAdapter extends ArrayAdapter<SRS> {
        private List<SRS> srsList;
        LayoutInflater inflater;
        public SRS_ListAdapter(Context context, int listElementId, List<SRS> list) {
            super(context, listElementId, list);
            this.srsList = list;
        }
        @Override 
        public View getView(int index, View convertView, ViewGroup parent) {
            Log.i(logTag, "getView: " + index);
            View itemView = convertView;
            ViewHolder vh = null;
            SRS srs = srsList.get(index);
            if (itemView != null)
                vh = (ViewHolder)itemView.getTag();
            // Initialize the items if the view is empty.
            if (vh == null || vh.index != index) {
                inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                itemView = inflater.inflate(R.layout.srs_list_item, parent, false);
                vh = new ViewHolder();
                vh.srsTv = (TextView)itemView.findViewById(R.id.srs_record_info);
                vh.idxTv = (TextView)itemView.findViewById(R.id.srs_index);
                vh.index = index;
                itemView.setTag(vh);
                Log.i(logTag, "inner: " + vh.idxTv.getText() + ":" + index + ":" + vh.index);
            }
            vh.idxTv.setText("" + (srsList.size() - index));
            vh.srsTv.setText(srs.toString());

            Log.i(logTag, "SRS_ListAdapter:getView: " 
                    + vh.idxTv.getText() + ":" + index + ":" + vh.index);
            return itemView;
        }
    }
    // button click S.  Has support for five Assurance Level buttons and a button
    // to start a new object.
    public void qualitySelect(View v) {
        int id = v.getId();
        int assuredness = 0;
        if (id == R.id.button1)         assuredness = 1;
        else if (id == R.id.button2)    assuredness = 2;
        else if (id == R.id.button3)    assuredness = 3;
        else if (id == R.id.button4)    assuredness = 4;
        else if (id == R.id.button5)    assuredness = 5;
        else if (id == R.id.new_record_button)
            srs = new SRS();
        else Log.d(logTag, "Invalid button id:" + id);
        if (assuredness > 0) {
            TextView viewSrs = (TextView) findViewById(R.id.srs_parameters);
            viewSrs.setText(srs.update(assuredness).toText());
            srsList.add(0, srs);
            srs = srs.clone();
            srsListAdapter.notifyDataSetChanged();
        }
    }
}
