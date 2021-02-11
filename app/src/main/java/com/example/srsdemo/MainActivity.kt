package com.example.srsdemo

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import java.util.*

/* This is an java/android implementation of the SuperMemory
 * Spaced Repetition Algorithm.
 *
 * ref:http://www.supermemo.com/english/ol/sm2source.htm
 * Explanation:
 * ref:http://www.supermemo.com/english/ol/sm2.htm
 * Algorithm with pencil/paper.
 * ref:http://www.supermemo.com/articles/paper.htm
 */   class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        srsListAdapter = SRS_ListAdapter(this, R.layout.srs_list_item, srsList)
        val lv = findViewById<View>(R.id.record_list) as ListView
        lv.adapter = srsListAdapter
        Log.i(logTag, "onCreate")
    }

    private inner class ViewHolder {
        var srsTv: TextView? = null
        var idxTv: TextView? = null
        var index = 0
    }

    // listview for tracking object changes as new 'Assurance' button is pressed.
    private inner class SRS_ListAdapter(
        context: Context?,
        listElementId: Int,
        private val srsList: List<SRS>
    ) : ArrayAdapter<SRS?>(
        context!!, listElementId, srsList
    ) {
        var inflater: LayoutInflater? = null
        override fun getView(index: Int, convertView: View?, parent: ViewGroup): View {
            Log.i(logTag, "getView: $index")
            var itemView = convertView
            var vh: ViewHolder? = null
            val srs = srsList[index]
            if (itemView != null) vh = itemView.tag as ViewHolder
            // Initialize the items if the view is empty.
            if (vh == null || vh.index != index) {
                inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                itemView = inflater!!.inflate(R.layout.srs_list_item, parent, false)
                vh = ViewHolder()
                vh!!.srsTv = itemView.findViewById<View>(R.id.srs_record_info) as TextView
                vh.idxTv = itemView.findViewById<View>(R.id.srs_index) as TextView
                vh.index = index
                itemView.tag = vh
                Log.i(logTag, "inner: " + vh.idxTv!!.text + ":" + index + ":" + vh.index)
            }
            vh!!.idxTv!!.text = "" + (srsList.size - index)
            vh.srsTv!!.text = srs.toString()
            Log.i(
                logTag, "SRS_ListAdapter:getView: "
                        + vh.idxTv!!.text + ":" + index + ":" + vh.index
            )
            return itemView!!
        }
    }

    // button click S.  Has support for five Assurance Level buttons and a button
    // to start a new object.
    fun qualitySelect(v: View) {
        val id = v.id
        var assuredness = 0
        if (id == R.id.button1) assuredness = 1 else if (id == R.id.button2) assuredness =
            2 else if (id == R.id.button3) assuredness =
            3 else if (id == R.id.button4) assuredness =
            4 else if (id == R.id.button5) assuredness =
            5 else if (id == R.id.new_record_button) srs = SRS() else Log.d(
            logTag, "Invalid button id:$id"
        )
        if (assuredness > 0) {
            val viewSrs = findViewById<View>(R.id.srs_parameters) as TextView
            viewSrs.text = srs.update(assuredness).toText()
            srsList.add(0, srs)
            srs = srs.clone()
            srsListAdapter!!.notifyDataSetChanged()
        }
    }

    companion object {
        private const val logTag = "MainActivity"
        private val srsList: MutableList<SRS> = mutableListOf()
        private var srs = SRS()
        private var srsListAdapter: SRS_ListAdapter? = null
    }
}