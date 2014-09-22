/* This is an java/android implementation of the SuperMemory
 * Spaced Repetition Algorithm.
 *
 * ref:http://www.supermemo.com/english/ol/sm2source.htm
 * Explanation:
 * ref:http://www.supermemo.com/english/ol/sm2.htm
 * Algorithm with pencil/paper.
 * ref:http://www.supermemo.com/articles/paper.htm
 */
package com.example.srsdemo;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

public class SRS {
    private static final int FIRST_INTERVAL_INCREMENT = 6; //days.
    // quality selection range: 1..5
    private static final float EF_MIN = 1.3f;
    // Grade or assuredness of recognition.  1 = huh?? 5 = for sure.
    @SuppressWarnings("unused")
    private static final float ASSUREDNESS_MIN = 1.0f;
    private static final float ASSUREDNESS_MAX = 5.0f;
    private Date    creationDate = new Date();
    private Date    viewDate = creationDate;
    private int     nextInterval;   // in days.
    private float   EF = 1.3f;      // Efficiency factor.
    private int     repetitions;    // number of successful times recognized.
    private int     assuredness;
    // creator
    protected SRS () { }
    // update record based on assuredness of recognition.
    protected SRS  update(int assuredness) {
        viewDate = new Date();
        this.assuredness = assuredness;
        if (assuredness >= 3.0) {
            if (repetitions == 0)
                nextInterval = 1;
            else if (repetitions == 1)
                nextInterval = FIRST_INTERVAL_INCREMENT;
            else {
                nextInterval = (int)Math.ceil(nextInterval * EF);
            }
            repetitions++;
        }
        // assuredness < 3:
        else if(assuredness > 1) {
            repetitions = 0;
            nextInterval = 1;
        }
        // sjs -- I modified so level of 1 means nextInterval is 0:
        // Stay with this card until you give it at least a 2.
        else {
            repetitions = 0;
            nextInterval = 0;
        }
        EF = EF + (float)(0.1
            - (ASSUREDNESS_MAX - assuredness)
            * (0.8 + ((ASSUREDNESS_MAX - assuredness) * 0.2)));
        if (EF < EF_MIN)
            EF = EF_MIN;
        return this;
    }
    protected SRS clone ()  {
        SRS rv = new SRS();
        rv.assuredness  = this.assuredness;
        rv.creationDate = (Date)this.creationDate.clone();
        rv.EF           = this.EF;
        rv.nextInterval = this.nextInterval;
        rv.repetitions  = this.repetitions;
        rv.viewDate     = (Date)this.viewDate.clone();
        return rv;
    }
    private static String[] format = {
        "Date: %s"
        , "Last Viewed: %s"
        , "Assuredness: %d"
        , "EF: %5.3f"
        , "Repetitions: %2d"
        , "Next view: %d days"};
    @SuppressLint("SimpleDateFormat")
    private String dateToString(Date dateIn) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("EEE LL/dd/y kk:mm z");
        // timeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateOut = timeFormat.format(dateIn);
        return dateOut;
    }
    public String toString() {
        String f = "";
        for(String s : format)
            f += s + ", ";
        return String.format(f,
           dateToString(creationDate), dateToString(viewDate), 
           assuredness, EF, repetitions, nextInterval);
    }
    public String toText() {
        String f = "";
        for(String s : format)
            f += s + "\n";
        return String.format(f,
                dateToString(creationDate), dateToString(viewDate), 
                assuredness, EF, repetitions, nextInterval);
    }
}
