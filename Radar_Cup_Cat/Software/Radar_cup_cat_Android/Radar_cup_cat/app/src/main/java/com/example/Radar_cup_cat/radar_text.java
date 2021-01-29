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

public class radar_text extends View {

    public radar_text(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        AssetManager am = this.getContext().getAssets();
        Paint pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
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

        canvas.drawText("0˚", 99, 535+720,pnt);
        canvas.drawText("45˚", 270, 320+720,pnt);
        canvas.drawText("90˚", 544, 95+720,pnt);
        canvas.drawText("135˚", 823, 320+720,pnt);
        canvas.drawText("180˚", 1014, 535+720,pnt);
        canvas.drawText("50m", 589, 414+720,pnt);
        canvas.drawText("100cm", 628, 321+720,pnt);
        canvas.drawText("150cm", 667, 228+720,pnt);
        canvas.drawText("200cm", 706, 135+720,pnt);

        canvas.drawText("0", 99+1044, 655,pnt);
        canvas.drawText("2,500", 270+1044, 440,pnt);
        canvas.drawText("5,000", 544+1044, 215,pnt);
        canvas.drawText("7,500", 823+1044, 440,pnt);
        canvas.drawText("10,000", 1014+1044, 655,pnt);
        canvas.drawText("50m", 589+1044, 534,pnt);
        canvas.drawText("100cm", 628+1044, 441,pnt);
        canvas.drawText("150cm", 667+1044, 348,pnt);
        canvas.drawText("200cm", 706+1044, 255,pnt);

        canvas.drawText("0˚C", 99+1044, 535+720,pnt);
        canvas.drawText("25˚C", 270+1044, 320+720,pnt);
        canvas.drawText("50˚C", 544+1044, 95+720,pnt);
        canvas.drawText("75˚C", 823+1044, 320+720,pnt);
        canvas.drawText("100˚C", 1014+1044, 535+720,pnt);

        pnt.setTextSize(100);
        canvas.drawText("Distance", 544, 190,pnt);
        canvas.drawText("Motion", 544, 70+720,pnt);
        canvas.drawText("Signal distance over strength", 544+1044, 190,pnt);
        canvas.drawText("Temp", 544+1044, 70+720,pnt);

        pnt.setTextSize(40);
        pnt.setTextAlign(Paint.Align.LEFT);
        pnt.setColor(Color.parseColor("#004600"));
        canvas.drawText("1ST Sweep", 109, 275,pnt);
        pnt.setColor(Color.parseColor("#006E00"));
        canvas.drawText("2ND Sweep", 109, 315,pnt);
        pnt.setColor(Color.parseColor("#00AA00"));
        canvas.drawText("Average", 109, 355,pnt);
        pnt.setColor(Color.parseColor("#FF0000"));
        canvas.drawText("Motion", 109, 155+720,pnt);
        pnt.setColor(Color.RED);
        canvas.drawText("Signal", 109+1044, 275,pnt);
    }

}
