package de.seibushin.nutrigo.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import de.seibushin.nutrigo.Helper;

public class Chart extends RelativeLayout {
    private int width = 100;
    private int height = 100;
    private int strokeWidth = 20;
    private int textSize = 20;
    private float circle_size = 10;
    private float stroke = 5;

    private DashPathEffect dashed = new DashPathEffect(new float[]{20, 20}, 0);

    private Paint paint;

    private float min = Float.MAX_VALUE;
    private float max = Float.MIN_VALUE;
    private int lastX = 14;

    private List<ChartData> data = new ArrayList<>();
    private ChartData target;

    private boolean isKcal = true;
    private boolean isFat = false;
    private boolean isCarbs = false;
    private boolean isProtein = false;

    public Chart(Context context) {
        super(context);
    }

    public Chart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);
        init();
    }

    private void init() {
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

    /**
     * Calc min max
     */
    private void calcMinMax() {
        this.min = Float.MAX_VALUE;
        this.max = Float.MIN_VALUE;

        for (int i = Math.max(0, data.size() - lastX); i < data.size(); i++) {
            ChartData d = data.get(i);
            // min
            if (isKcal) {
                this.min = Math.min(this.min, d.kcal);
                this.max = Math.max(this.max, d.kcal);
            }
            if (isFat) {
                this.min = Math.min(this.min, d.fat);
                this.max = Math.max(this.max, d.fat);
            }
            if (isCarbs) {
                this.min = Math.min(this.min, d.carbs);
                this.max = Math.max(this.max, d.carbs);
            }
            if (isProtein) {
                this.min = Math.min(this.min, d.protein);
                this.max = Math.max(this.max, d.protein);
            }
        }

        // min
        if (isKcal) {
            this.min = Math.min(this.min, target.kcal);
            this.max = Math.max(this.max, target.kcal);
        }
        if (isFat) {
            this.min = Math.min(this.min, target.fat);
            this.max = Math.max(this.max, target.fat);
        }
        if (isCarbs) {
            this.min = Math.min(this.min, target.carbs);
            this.max = Math.max(this.max, target.carbs);
        }
        if (isProtein) {
            this.min = Math.min(this.min, target.protein);
            this.max = Math.max(this.max, target.protein);
        }
    }

    private void drawStat(Canvas canvas, Stat stat) {
        if (data.size() <= 0) {
            return;
        }

        float left = 0;
        float lineStart = 100;
        float top = 0 + 50;
        float right = width;
        float bottom = height - 50;

        int color = getResources().getColor(stat.color, null);

        if (target != null) {
            float val = target.getData(stat);
            paint.setColor(color);
            float y = (1 - ((val - min) / (max - min))) * (height - 100) + top;
            drawDashed(canvas, 100, y, right, y, color);
            canvas.drawText(Helper.formatInt(val), left, y, paint);
            canvas.drawText(stat.name(), left, y + textSize, paint);
        }

        float step = (float) (width - lineStart) / lastX;
        float x = lineStart;
        float sum = 0;
        float x_ = -1;
        float y_ = -1;
        float x_a = 0;
        float y_a = 0;
        float y_f = 0;
        for (int i = Math.max(0, data.size() - lastX); i < data.size(); i++) {
            ChartData d = data.get(i);
            paint.setColor(color);
            float val = d.getData(stat);
            float y = (1 - ((val - min) / (max - min))) * (height - 100) + top;
            paint.setStrokeWidth(circle_size);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(x, y, circle_size, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(stroke);
            x_a += x;
            y_a += y;
            if (y_ == -1) {
                y_f = y;
            }
            if (x_ >= 0 && y_ >= 0) {
                canvas.drawLine(x_, y_, x, y, paint);
            }
            x_ = x;
            y_ = y;
            sum += val;

            x += step;
        }

        // draw avg line
        float avg = sum / lastX;
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        float y = (1 - ((avg - min) / (max - min))) * (height - 100) + top;
        drawDashed(canvas, 100, y, right, y, color);
        canvas.drawText(Helper.formatInt(avg), left, y, paint);
        canvas.drawText("avg", left, y + textSize, paint);

        // trend line
//        float x1 = lineStart - x_a / lastX;
//        float y1 = y_f - y_a / lastX;
//        float x2 = x1 * x1;
//        float y2 = x1 * y1;
//        canvas.drawLine(x1, y1 + y_a / lastX, x2, y2 + y_a / lastX, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);

            calcMinMax();

            float left = 0;
            float lineStart = 100;
            float top = 0 + 50;
            float right = width;
            float bottom = height - 50;

            paint.setTextSize(textSize);
            if (isKcal || isFat || isCarbs || isProtein) {
                // draw min and max
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(5);
                canvas.drawText(Helper.formatInt(max), left, top, paint);
                canvas.drawText(Helper.formatInt(min), left, bottom, paint);

                drawDashed(canvas, lineStart, top, right, top, Color.GRAY);
                drawDashed(canvas, lineStart, bottom, right, bottom, Color.GRAY);
            } else {
                paint.setTextSize(50);
                paint.setColor(Color.RED);
                String info = "No stat selected!";
                float tw = paint.measureText(info);
                canvas.drawText(info, width / 2 - tw / 2, height / 2, paint);
            }


            if (isKcal)
                drawStat(canvas, Stat.KCAL);

            if (isFat)
                drawStat(canvas, Stat.FAT);

            if (isCarbs)
                drawStat(canvas, Stat.CARBS);

            if (isProtein)
                drawStat(canvas, Stat.PROTEIN);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage() + " - " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }

    private void drawDashed(Canvas canvas, float x1, float y1, float x2, float y2, int color) {
        Paint paint = new Paint();
        paint.setAlpha(255);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(dashed);
        Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        canvas.drawPath(path, paint);
    }

    /**
     * Update the data
     *
     * @param data
     */
    public void setData(List<ChartData> data) {
        this.data = data;
        invalidate();
    }

    public void setTarget(ChartData target) {
        this.target = target;
        invalidate();
    }

    public void setKcal(boolean isKcal) {
        if (isKcal != this.isKcal) {
            this.isKcal = isKcal;
            invalidate();
        }
    }

    public void setFat(boolean fat) {
        if (fat != isFat) {
            isFat = fat;
            invalidate();
        }
    }

    public void setCarbs(boolean carbs) {
        if (carbs != isCarbs) {
            isCarbs = carbs;
            invalidate();
        }
    }

    public void setProtein(boolean protein) {
        if (protein != isProtein) {
            isProtein = protein;
            invalidate();
        }
    }

    public void setLastX(int lastX) {
        if (lastX != this.lastX) {
            this.lastX = lastX;
            invalidate();
        }
    }
}
