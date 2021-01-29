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

public class radar_signal extends View {

    public radar_signal(Context context, @Nullable AttributeSet attrs) {
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
        rect.set(144, 245, 944, 1045);
        canvas.drawArc(rect, 180, 180, false, pnt);

        rect = new RectF();
        rect.set(244, 345, 844, 945);
        canvas.drawArc(rect, 180, 180, false, pnt);

        rect = new RectF();
        rect.set(344, 445, 744, 845);
        canvas.drawArc(rect, 180, 180, false, pnt);

        rect = new RectF();
        rect.set(444, 545, 644, 745);
        canvas.drawArc(rect, 180, 180, false, pnt);

        pnt = new Paint();
        pnt.setStrokeWidth(6f);
        pnt.setStyle(Paint.Style.STROKE);
        pnt.setColor(Color.parseColor("#495057"));

        Path path = new Path();
        path.moveTo(124,645);
        path.lineTo(964,645);
        path.close();
        canvas.drawPath(path, pnt);

        path = new Path();
        path.moveTo(544,225);
        path.lineTo(544, 645);
        path.close();
        canvas.drawPath(path, pnt);

        int r = 420;
        double x = r * Math.cos(45 * Math.PI / 180);
        double y = r * Math.sin(45 * Math.PI / 180);
        float xlo1 = 544 + (float)x;
        float xlo2 = 544 - (float)x;
        float ylo = 645 - (float)y;

        path = new Path();
        path.moveTo(544,645);
        path.lineTo(xlo1, ylo);
        path.close();
        canvas.drawPath(path, pnt);

        path = new Path();
        path.moveTo(544,645);
        path.lineTo(xlo2, ylo);
        path.close();
        canvas.drawPath(path, pnt);

        pnt.setColor(Color.RED);
        pnt.setStrokeWidth(3f);
        pnt.setStyle(Paint.Style.FILL);
        canvas.drawCircle(84, 260, 13, pnt);
    }

}
