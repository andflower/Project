package com.example.Radar_cup_cat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class connetion_button extends View {
    public connetion_button(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
        pnt.setStrokeWidth(4f);
        pnt.setColor(Color.parseColor("#FFD700"));
        pnt.setStyle(Paint.Style.STROKE);

        RectF rect = new RectF();
        rect.set(2, 2, 118, 118);
        canvas.drawArc(rect, 90, 180, false, pnt);

        rect = new RectF();
        rect.set(662, 2, 778, 118);
        canvas.drawArc(rect, 270, 180, false, pnt);

        Path path = new Path();
        path.moveTo(60,2);
        path.lineTo(720,2);
        path.close();
        canvas.drawPath(path, pnt);

        path = new Path();
        path.moveTo(60,118);
        path.lineTo(720,118);
        path.close();
        canvas.drawPath(path, pnt);
    }
}
