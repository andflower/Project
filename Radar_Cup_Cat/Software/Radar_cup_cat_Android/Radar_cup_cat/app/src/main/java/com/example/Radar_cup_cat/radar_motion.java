package com.example.Radar_cup_cat;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class radar_motion extends View {

    public radar_motion(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
        pnt.setStrokeWidth(6f);
        pnt.setColor(Color.parseColor("#495057"));
        pnt.setStyle(Paint.Style.STROKE);

        RectF rect = new RectF();
        rect.set(144, 125, 944, 925);
        canvas.drawArc(rect, 180, 180, false, pnt);

        rect = new RectF();
        rect.set(244, 225, 844, 825);
        canvas.drawArc(rect, 180, 180, false, pnt);

        rect = new RectF();
        rect.set(344, 325, 744, 725);
        canvas.drawArc(rect, 180, 180, false, pnt);

        rect = new RectF();
        rect.set(444, 425, 644, 625);
        canvas.drawArc(rect, 180, 180, false, pnt);

        pnt = new Paint();
        pnt.setStrokeWidth(6f);
        pnt.setStyle(Paint.Style.STROKE);
        pnt.setColor(Color.parseColor("#495057"));

        Path path = new Path();
        path.moveTo(124,525);
        path.lineTo(964,525);
        path.close();
        canvas.drawPath(path, pnt);

        path = new Path();
        path.moveTo(544,105);
        path.lineTo(544, 525);
        path.close();
        canvas.drawPath(path, pnt);

        int r = 420;
        double x = r * Math.cos(45 * Math.PI / 180);
        double y = r * Math.sin(45  *Math.PI / 180);
        float xlo1 = 544 + (float)x;
        float xlo2 = 544 - (float)x;
        float ylo = 525 - (float)y;

        path = new Path();
        path.moveTo(544,525);
        path.lineTo(xlo1, ylo);
        path.close();
        canvas.drawPath(path, pnt);

        path = new Path();
        path.moveTo(544,525);
        path.lineTo(xlo2, ylo);
        path.close();
        canvas.drawPath(path, pnt);

        pnt.setColor(Color.RED);
        pnt.setStrokeWidth(5f);
        pnt.setStyle(Paint.Style.STROKE);
        rect = new RectF();
        rect.set(74, 130, 94, 150);
        canvas.drawArc(rect, 0, 360, false, pnt);
    }
}
