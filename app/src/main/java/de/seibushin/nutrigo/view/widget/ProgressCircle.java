package de.seibushin.nutrigo.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import de.seibushin.nutrigo.R;

public class ProgressCircle extends RelativeLayout {

    private TextView tv_label;
    private TextView tv_progress;

    private String label;
    private int max;
    private int progress;
    private int color;
    private int progressColor;
    private float strokeWidth;

    private int size = 200;
    private float sweep = 260;
    private float start = 140;

    private Paint paint;
    private RectF rect;


    public ProgressCircle(Context context) {
        super(context);
    }

    public ProgressCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressCircle, 0, 0);
        label = a.getString(R.styleable.ProgressCircle_label);
        progress = a.getInt(R.styleable.ProgressCircle_progress, 0);
        max = a.getInt(R.styleable.ProgressCircle_max, 100);
        color = a.getColor(R.styleable.ProgressCircle_color, getResources().getColor(R.color.colorPrimaryDark));
        progressColor = a.getColor(R.styleable.ProgressCircle_progressColor, getResources().getColor(R.color.colorAccent));
        strokeWidth = a.getInt(R.styleable.ProgressCircle_strokeWidth, 20);

        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.progress_circle, this, true);

        tv_progress = findViewById(R.id.progress);
        tv_label = findViewById(R.id.label);

        tv_progress.setText("" + progress);
        tv_label.setText(label);

        init();
    }

    private void init() {
        rect = new RectF();

        paint = new Paint();
        paint.setShader(null);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
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
        tv_progress.setTextSize(size / 10);
        tv_label.setTextSize(size / 20);

        // perfectly fit the rect into the area
        float leftTop = strokeWidth / 2;
        float rightBottom = size - strokeWidth / 2;
        rect.set(leftTop, leftTop, rightBottom, rightBottom);

        // draw indicator circle
        paint.setColor(color);
        canvas.drawArc(rect, start, sweep, false, paint);

        // draw value circle
        paint.setColor(progressColor);
        float p = Math.min((float) progress / max * sweep, sweep);
        canvas.drawArc(rect, start, p, false, paint);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        tv_progress.setText("" + progress);
        invalidate();
    }

    public void setMax(int max) {
        this.max = max;
        invalidate();
    }
}
