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

public class radar_distance extends View {

    public radar_distance(Context context, @Nullable AttributeSet attrs) {
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
        path.lineTo(964, 645);
        path.close();
        canvas.drawPath(path, pnt);

        path = new Path();
        path.moveTo(544,225);
        path.lineTo(544,645);
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

        pnt.setColor(Color.parseColor("#004600"));
        pnt.setStyle(Paint.Style.FILL);
        canvas.drawRect(74, 250, 94, 270, pnt);

        pnt.setColor(Color.parseColor("#006E00"));
        pnt.setStyle(Paint.Style.FILL);
        canvas.drawRect(74, 290, 94, 310, pnt);

        pnt.setColor(Color.parseColor("#00AA00"));
        pnt.setStyle(Paint.Style.FILL);
        canvas.drawRect(74, 330, 94, 350, pnt);

        AssetManager am = this.getContext().getAssets();
        pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
        pnt.setTypeface(Typeface.createFromAsset(am,"NanumBrush.ttf" ) );
        pnt.setColor(Color.parseColor("#FFD700"));
        pnt.setTextSize(40);
        pnt.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("0˚", 99, 655,pnt);
        canvas.drawText("45˚", 270, 440,pnt);
        canvas.drawText("90˚", 544, 215,pnt);
        canvas.drawText("135˚", 823, 440,pnt);
        canvas.drawText("180˚", 1014, 655,pnt);
        canvas.drawText("50m", 589, 534,pnt);
        canvas.drawText("100cm", 628, 441,pnt);
        canvas.drawText("150cm", 667, 348,pnt);
        canvas.drawText("200cm", 706, 255,pnt);

        pnt.setTextSize(100);
        canvas.drawText("Distance", 544, 190,pnt);

        pnt.setTextSize(40);
        pnt.setTextAlign(Paint.Align.LEFT);
        pnt.setColor(Color.parseColor("#004600"));
        canvas.drawText("1ST Sweep", 109, 275,pnt);
        pnt.setColor(Color.parseColor("#006E00"));
        canvas.drawText("2ND Sweep", 109, 315,pnt);
        pnt.setColor(Color.parseColor("#00AA00"));
        canvas.drawText("Average", 109, 355,pnt);
    }

}
