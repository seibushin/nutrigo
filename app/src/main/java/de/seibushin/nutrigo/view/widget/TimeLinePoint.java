package de.seibushin.nutrigo.view.widget;

import java.util.Calendar;

public class TimeLinePoint {

    public float size = 20;
    public float minSize = 5;
    public float maxSize = 20;
    public int color;

    public float max = 300;
    public Float timepoint;
    public Long timestamp;
    public float value;

    public void setTimestamp(Long timestamp) {
        // set timestamp
        this.timestamp = timestamp;

        // calculate timepoint (30 minute interval)
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);

        int min = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR_OF_DAY);

        if (min > 15 && min < 45) {
            min = 30;
        } else if (min <= 15 || min >= 45) {
            if (min >= 45) {
                hour += 1;
            }
            min = 0;
        }
        timepoint = (float) hour + ((float) min / 60);
    }

    public float pointsize(float newVal) {
        return Math.min(Math.max(size * (newVal / max), minSize), maxSize);
    }

    public float pointsize() {
        return Math.min(Math.max(size * (value / max), minSize), maxSize);
    }

    public String diff(TimeLinePoint tlp) {
        Calendar c = Calendar.getInstance();

        // gregorian calendar starts at hour 1 IDK.
        // therefore we reduce the difference by 1 hour
        long correction = 60*60*1000;
        c.setTimeInMillis(timestamp - tlp.timestamp - correction);

        return String.format("%d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    }
}
