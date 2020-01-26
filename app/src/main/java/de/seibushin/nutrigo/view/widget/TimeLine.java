package de.seibushin.nutrigo.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;
import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.model.nutrition.EatPoint;
import de.seibushin.nutrigo.model.nutrition.NutritionType;

public class TimeLine extends RelativeLayout {
    private int color;
    private int mealColor;
    private int foodColor;
    private int width = 100;
    private int height = 100;
    private int strokeWidth = 20;
    private int textSize = 20;

    private Paint paint;
    private RectF rect;

    private List<TimeLinePoint> timePointsFood = new ArrayList<>();
    private List<TimeLinePoint> timePointsMeal = new ArrayList<>();

    public TimeLine(Context context) {
        super(context);
    }

    public TimeLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);
        color = getResources().getColor(R.color.colorPrimary, null);
        mealColor = getResources().getColor(R.color.colorAccent, null);
        foodColor = getResources().getColor(R.color.colorAccent, null);

        init();
    }

    private void init() {
        rect = new RectF();

        paint = new Paint();
        paint.setShader(null);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(textSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // Record our dimensions if they are known;
        if (widthMode != MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED) {
            height = heightSize;
        }

        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float left = 0;
        float top = 20 + 2 * textSize + 5;
        float right = width;
        float bottom = height;
        rect.set(left, top, right, bottom);

        // draw indicator circle
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, paint);

        // draw hour lines
        paint.setStrokeWidth(2);
        paint.setColor(color);
        for (int i = 1; i < 24; i++) {
            float x = (float) width / 24 * i;
            canvas.drawLine(x, top, x, height, paint);

            if (i % 4 == 0) {
                paint.setColor(Color.BLACK);
                String time = String.format("%02d:00", i);
                float offset = paint.measureText(time);
                canvas.drawText(time, (x - offset / 2), top, paint);
                paint.setColor(color);
            }
        }

        // current time
        paint.setColor(getResources().getColor(R.color.colorAccent, null));
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        float x = (float) width / 24 * (hour + (float) minutes / 60);
        canvas.drawLine(x, textSize + 20, x, height, paint);

        paint.setStyle(Paint.Style.FILL);
        String time = String.format("%02d:%02d", hour, minutes);
        x = x - (paint.measureText(time) / 2);
        canvas.drawText(time, x, textSize + 20, paint);

        // draw timePoints
        List<TimeLinePoint> timePoints = new ArrayList<>();
        timePoints.addAll(timePointsFood);
        timePoints.addAll(timePointsMeal);
        paint.setColor(mealColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        timePoints.sort((o1, o2) -> o1.timestamp.compareTo(o2.timestamp));
        float prevTp = -1;
        float prevCount = 0;
        for (TimeLinePoint timepoint : timePoints) {
            float size = timepoint.pointsize();
            paint.setColor(timepoint.color);
            paint.setStrokeWidth(size);
            float xx = (float) width / 24 * timepoint.timepoint;

            if (prevTp == timepoint.timepoint) {
                prevCount++;
            } else {
                prevCount = 1;
            }
            prevTp = timepoint.timepoint;

            float yy = top + (height - top) / 2;
            if (prevCount > 1) {
                if (prevCount % 2 == 0) {
                    yy -= prevCount / 2 * strokeWidth;
                } else {
                    yy += (prevCount - 1) / 2 * strokeWidth;
                }
            }

            canvas.drawPoint(xx, yy, paint);
        }


        if (timePoints.size() > 1) {
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(5);
            float xStart = (float) width / 24 * timePoints.get(0).timepoint;
            float xEnd = (float) width / 24 * timePoints.get(timePoints.size() - 1).timepoint;
            canvas.drawLine(xStart, 10, xEnd, 10, paint);


            String duration = timePoints.get(timePoints.size() - 1).diff(timePoints.get(0));
            float textOffset = paint.measureText(duration) / 2;
            float xText = xStart + (xEnd - xStart) / 2 - textOffset;
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(xText - 5, 0, xText + 2 * textOffset + 5, 25, paint);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText(duration, xText, 20, paint);
        }
    }

    /**
     * Adds the given points to the existing one. Call reset to have no old points
     *
     * @param nds
     */
    private List<TimeLinePoint> setNu(List<EatPoint> nds) {
        List<TimeLinePoint> localTP = new ArrayList<>();
        for (EatPoint nd : nds) {
            TimeLinePoint tlp = new TimeLinePoint();
            tlp.setTimestamp(nd.getTimestamp());
            tlp.value = (float) nd.getKcal();
            if (nd.getType() == NutritionType.MEAL) {
                tlp.max = 800;
                tlp.color = getResources().getColor(R.color.mealPoint, null);
            } else if (nd.getType() == NutritionType.FOOD) {
                tlp.max = 200;
                tlp.color = getResources().getColor(R.color.foodPoint, null);
            }

            localTP.add(tlp);
        }

        return localTP;
    }

    public void setFoods(List<EatPoint> nds) {
        timePointsFood.clear();
        timePointsFood.addAll(setNu(nds));
        invalidate();
    }

    public void setMeals(List<EatPoint> nds) {
        timePointsMeal.clear();
        timePointsMeal.addAll(setNu(nds));
        invalidate();
    }
}
