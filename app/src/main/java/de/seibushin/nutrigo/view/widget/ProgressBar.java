package de.seibushin.nutrigo.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import de.seibushin.nutrigo.R;

public class ProgressBar extends RelativeLayout {
    private int width = 100;
    private int height = 100;

    private String label = "";
    private int max = 100;
    private int progress = 50;
    private int color;
    private int progressColor;
    private float strokeWidth = 30;

    private boolean showMissing = false;

    private Paint paint;
    private RectF rect;

    public ProgressBar(Context context) {
        super(context);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar, 0, 0);
        label = a.getString(R.styleable.ProgressBar_label);
        color = a.getColor(R.styleable.ProgressBar_color, getResources().getColor(R.color.colorPrimaryDark, null));
        progressColor = a.getColor(R.styleable.ProgressBar_progressColor, getResources().getColor(R.color.colorAccent, null));
        a.recycle();

        init();
    }

    private void init() {
        rect = new RectF();

        paint = new Paint();
        paint.setShader(null);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL);

        this.setOnClickListener(v -> {
            showMissing = !showMissing;
            invalidate();
        });
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

        float h1Size = height / 2;
        float h2Size = height / 4;

        float left = 0;
        float top = h2Size + 30;
        float right = width;
        float bottom = h2Size + h1Size - 5;
        rect.set(left, top, right, bottom);

        // draw indicator
        paint.setColor(color);
        float strokeWidth = bottom - top;
        float offset = strokeWidth / 2;
        paint.setStrokeWidth(strokeWidth);
        canvas.drawLine(offset, top + strokeWidth / 2, right - offset, top + strokeWidth / 2, paint);

        // draw value
        paint.setColor(progressColor);
        float pp = progress;
        int overdraw = 0;
        while (pp > 0) {
            if (overdraw > 0) {
                paint.setColor(getResources().getColor(R.color.darken, null));
            }

            float p = (float) Math.floor(pp / max * width);
            if (pp >= max) {
                p = width;
            }
            rect.right = p;
            canvas.drawLine(offset, top + strokeWidth / 2, p - offset, top + strokeWidth / 2, paint);

            pp -= max;
            overdraw++;
        }

        paint.setColor(Color.GRAY);
        paint.setTextSize(h2Size);
        canvas.drawText(label.toUpperCase(), width / 2 - paint.measureText(label.toUpperCase()) / 2, h2Size, paint);

        paint.setColor(progressColor);
        String text = "" + progress;
        if (showMissing) {
            paint.setColor(getResources().getColor(R.color.grey, null));
            text = "" + (max - progress);
        }
        paint.setTextSize(h1Size);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(text, width / 2 - paint.measureText(text) / 2, h2Size + h1Size, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.WHITE);
        canvas.drawText(text, width / 2 - paint.measureText(text) / 2, h2Size + h1Size, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));


    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public void setMax(int max) {
        this.max = max;
        invalidate();
    }
}
