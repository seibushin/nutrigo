package de.seibushin.nutrigo.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import de.seibushin.nutrigo.R;

public class MultiProgressCircle extends RelativeLayout {
    private String progressStr = "0";
    private String remainingStr = "";
    private double remaining;
    private String label = "kCal";
    private String hint = "missing";

    private int max = 100;
    private List<EatProgress> progress = new ArrayList<>();
    private int color;
    private float mainStroke = 30;
    private float subStroke = 10;

    private int size = 200;
    private float sweep = 260;
    private float start = 140;
    private float totalMacro = 0;
    private boolean showMissing = false;

    private Paint paint;
    private RectF rect;

    public MultiProgressCircle(Context context) {
        super(context);
    }

    public MultiProgressCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);
        color = getResources().getColor(R.color.grey, null);

        init();
    }

    private void init() {
        rect = new RectF();

        paint = new Paint();
        paint.setShader(null);
        paint.setStrokeWidth(mainStroke);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);

        this.setOnClickListener(v -> {
            showMissing = !showMissing;
            invalidate();
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 100;
        int height = 100;

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

        size = Math.min(width, height);
        super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY));

        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // update textSize according to the Circle size
        float t1size = size / 5;
        float t2size = size / 10;

        // perfectly fit the rect into the area
        float leftTop = mainStroke / 2;
        float rightBottom = size - mainStroke / 2;
        rect.set(leftTop, leftTop, rightBottom, rightBottom);

        // draw indicator circle
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mainStroke);
        paint.setColor(color);
        canvas.drawArc(rect, start, sweep, false, paint);

        float arcStart = start;
        // draw value circle
        for (EatProgress eatProgress : progress) {
            paint.setColor(getResources().getColor(eatProgress.color, null));

            if (!eatProgress.isTotal) {
                float p = (float) Math.floor(eatProgress.value / totalMacro * sweep);
                paint.setStrokeWidth(subStroke);
                canvas.drawArc(rect.left + mainStroke, rect.top + mainStroke, rect.right - mainStroke, rect.bottom - mainStroke, arcStart, p, false, paint);
                arcStart += p;
            } else {
                paint.setStrokeWidth(mainStroke);
                float pp = (float) eatProgress.value;
                int overdraw = 0;
                while (pp > 0) {
                    if (overdraw > 0) {
                        paint.setColor(getResources().getColor(R.color.darken, null));
                    }

                    float p = (float) Math.floor(pp / max * sweep);
                    if (pp >= max) {
                        p = sweep;
                    }
                    canvas.drawArc(rect, start, p, false, paint);

                    pp -= max;
                    overdraw++;
                }
            }
        }

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.kcal, null));
        String text = progressStr;
        if (showMissing) {
            paint.setColor(Color.GRAY);
            text = remainingStr;
        }
        paint.setTextSize(t1size);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        float x = (float) size / 2 - paint.measureText(text) / 2;
        float y = size / 2 + t1size / 2;
        canvas.drawText(text, x, y, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        if (showMissing) {
            paint.setTextSize(t2size);
            paint.setColor(Color.GRAY);
            x = (float) size / 2 - paint.measureText(hint) / 2;
            float y2 =  y - t1size / 2 - t2size;
            canvas.drawText(hint, x, y2, paint);
        }

        paint.setTextSize(t2size);
        paint.setColor(Color.GRAY);
        x = (float) size / 2 - paint.measureText(label) / 2;
        y += t2size + 5;
        canvas.drawText(label, x, y, paint);
    }

    public void setProgress(EatProgress... progress) {
        this.totalMacro = 0;
        this.progress = new ArrayList<>();
        for (EatProgress eatProgress : progress) {
            this.progress.add(eatProgress);
            if (eatProgress.isTotal) {
                remaining = max - Math.floor(eatProgress.value);
                progressStr = "" + (int) Math.floor(eatProgress.value);
                remainingStr = "" + (int) remaining;
            } else {
                this.totalMacro += eatProgress.value;
            }
        }
        this.progress.sort((o1, o2) -> Integer.compare(o1.drawOrder, o2.drawOrder));

        invalidate();
    }

    public void setMax(int max) {
        this.max = max;
        invalidate();
    }
}
