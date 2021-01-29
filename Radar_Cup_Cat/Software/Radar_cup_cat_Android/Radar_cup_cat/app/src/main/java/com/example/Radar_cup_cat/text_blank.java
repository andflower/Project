package com.example.Radar_cup_cat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class text_blank extends View {
    public text_blank(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
        pnt.setStrokeWidth(5f);
        pnt.setStyle(Paint.Style.STROKE);
        pnt.setColor(Color.parseColor("#FFD700"));
        Path path = new Path();
        path.moveTo(5,5);
        path.lineTo(775, 5);
        path.lineTo(775, 1015);
        path.lineTo(5, 1015);
        path.close();
        canvas.drawPath(path, pnt);
    }
}
