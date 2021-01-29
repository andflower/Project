package com.example.Radar_cup_cat;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class data_print extends View {
    public static int r = 420;
    public static float xlo = 0;
    public static float ylo = 0;
    public static int animation = 0;
    public static String motionText = "";

    private Paint pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path;

    public data_print(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        chage_Animation();

        int cDistance = MainActivity.cDistance;
        float cFlux = limitFlux() * (float) 180 / 10000;


        AssetManager am = this.getContext().getAssets();
        pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
        pnt.setTypeface(Typeface.createFromAsset(am, "NanumBrush.ttf"));
        pnt.setColor(Color.parseColor("#FFD700"));
        pnt.setTextAlign(Paint.Align.CENTER);

        pnt.setTextSize(50);
        canvas.drawText("D: " + MainActivity.t_distance + "cm, A: " + MainActivity.t_angle + "˚", 544, 715, pnt);
        canvas.drawText(motionText, 544, 1315, pnt);
        canvas.drawText(MainActivity.fluxString, 1588, 715, pnt);
        canvas.drawText(MainActivity.temp + "˚C", 1588, 1315, pnt);

        pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
        pnt.setStrokeWidth(6f);
        pnt.setColor(Color.RED);
        pnt.setStyle(Paint.Style.STROKE);

        path = new Path();
        lineValue(645, cFlux, cFlux, cDistance * (float)2.0);
        pnt.setStrokeWidth(3f);
        pnt.setStyle(Paint.Style.FILL);
        canvas.drawCircle(1044 + xlo, ylo, 13, pnt);

        pnt = new Paint(Paint.ANTI_ALIAS_FLAG);

        pnt.setStrokeWidth(6f);
        pnt.setColor(Color.RED);
        pnt.setStyle(Paint.Style.STROKE);
        path = new Path();
        float cTemp = (MainActivity.temp * (float) 1.8);
        lineValue(525, cTemp, cTemp, 420);
        path.moveTo(1588,1245);
        path.lineTo(1044 + xlo, 720 + ylo);
        path.close();
        canvas.drawPath(path, pnt);

        RectF rect = new RectF();
        rect.set(544 + 1044 - (cDistance*2), 645 - (cDistance*2), 544 + 1044 + (cDistance*2), 645 + (cDistance*2));
        canvas.drawArc(rect, 180, 180, false, pnt);

        if (animation == 0) {
            if (MainActivity.angle + 20 >= 181) {
                for (int i = 0; i < (180 - MainActivity.angle); i++) {
                    pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
                    pnt.setStrokeWidth(6f);
                    pnt.setColor(Color.argb(90, 0, (10 * i), 0));
                    pnt.setStyle(Paint.Style.STROKE);
                    path = new Path();
                    lineValue(645, MainActivity.angle + i, MainActivity.angle + i, (float) r);
                    path.moveTo(544, 645);
                    path.lineTo(xlo, ylo);
                    path.close();
                    canvas.drawPath(path, pnt);
                }
            }

            else {
                for (int i = 0; i < 20; i++) {
                    pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
                    pnt.setStrokeWidth(6f);
                    pnt.setColor(Color.argb(90, 0, (10 * i), 0));
                    pnt.setStyle(Paint.Style.STROKE);
                    path = new Path();
                    lineValue(645, MainActivity.angle + i, MainActivity.angle + i, (float) r);
                    path.moveTo(544, 645);
                    path.lineTo(xlo, ylo);
                    path.close();
                    canvas.drawPath(path, pnt);
                }
            }
        }

        else {
            if (MainActivity.angle + 20 >= 181) {

                for (int i = (180 - MainActivity.angle); i >= 0; i--) {
                    pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
                    pnt.setStrokeWidth(6f);
                    pnt.setColor(Color.argb(90, 0, (200 - (10 * i)), 0));
                    pnt.setStyle(Paint.Style.STROKE);
                    path = new Path();
                    lineValue(645, MainActivity.angle + i, MainActivity.angle + i, (float) r);
                    path.moveTo(544, 645);
                    path.lineTo(xlo, ylo);
                    path.close();
                    canvas.drawPath(path, pnt);
                }
            }

            else {
                for (int i = 19; i >= 0; i--) {
                    pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
                    pnt.setStrokeWidth(6f);
                    pnt.setColor(Color.argb(90, 0, (200 - (10 * i)), 0));
                    pnt.setStyle(Paint.Style.STROKE);
                    path = new Path();
                    lineValue(645, MainActivity.angle + i, MainActivity.angle + i, (float) r);
                    path.moveTo(544, 645);
                    path.lineTo(xlo, ylo);
                    path.close();
                    canvas.drawPath(path, pnt);
                }
            }
        }

        if (animation == 0) {
            if (MainActivity.angle + 20 >= 181) {
                for (int i = 0; i < (180 - MainActivity.angle); i++) {
                    pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
                    pnt.setStrokeWidth(6f);
                    pnt.setColor(Color.argb(90, 0, (10 * i), 0));
                    pnt.setStyle(Paint.Style.STROKE);
                    path = new Path();
                    lineValue(525, MainActivity.angle + i, MainActivity.angle + i, (float) r);
                    path.moveTo(544, 720 + 525);
                    path.lineTo(xlo, 720 + ylo);
                    path.close();
                    canvas.drawPath(path, pnt);
                }
            }

            else {
                for (int i = 0; i < 20; i++) {
                    pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
                    pnt.setStrokeWidth(6f);
                    pnt.setColor(Color.argb(90, 0, (10 * i), 0));
                    pnt.setStyle(Paint.Style.STROKE);
                    path = new Path();
                    lineValue(525, MainActivity.angle + i, MainActivity.angle + i, (float) r);
                    path.moveTo(544, 720 + 525);
                    path.lineTo(xlo, 720 + ylo);
                    path.close();
                    canvas.drawPath(path, pnt);
                }
            }
        }

        else {
            if (MainActivity.angle + 20 >= 181) {

                for (int i = (180 - MainActivity.angle); i >= 0; i--) {
                    pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
                    pnt.setStrokeWidth(6f);
                    pnt.setColor(Color.argb(90, 0, (200 - (10 * i)), 0));
                    pnt.setStyle(Paint.Style.STROKE);
                    path = new Path();
                    lineValue(525, MainActivity.angle + i, MainActivity.angle + i, (float) r);
                    path.moveTo(544, 720 + 525);
                    path.lineTo(xlo, 720 + ylo);
                    path.close();
                    canvas.drawPath(path, pnt);
                }
            }

            else {
                for (int i = 19; i >= 0; i--) {
                    pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
                    pnt.setStrokeWidth(6f);
                    pnt.setColor(Color.argb(90, 0, (200 - (10 * i)), 0));
                    pnt.setStyle(Paint.Style.STROKE);
                    path = new Path();
                    lineValue(525, MainActivity.angle + i, MainActivity.angle + i, (float) r);
                    path.moveTo(544, 720 + 525);
                    path.lineTo(xlo, 720 + ylo);
                    path.close();
                    canvas.drawPath(path, pnt);
                }
            }
        }

        pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
        pnt.setColor(Color.argb(80, 0, 70, 0));
        pnt.setStyle(Paint.Style.FILL);
        path = new Path();
        path.moveTo(544, 645);
        for (int i = 0; i < 180; i++){
            lineValue(645, i, i, (float) (MainActivity.oldDistance[i]*2));
            path.lineTo(xlo, ylo);
        }
        path.lineTo(544, 645);
        path.close();
        canvas.drawPath(path, pnt);

        pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
        pnt.setColor(Color.argb(80, 0, 110, 0));
        pnt.setStyle(Paint.Style.FILL);
        path = new Path();
        path.moveTo(544, 645);
        for (int i = 0; i < 180; i++){
            lineValue(645, i, i, (float) (MainActivity.newDistance[i]*2));
            path.lineTo(xlo, ylo);
        }
        path.lineTo(544, 645);
        path.close();
        canvas.drawPath(path, pnt);

        pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
        pnt.setColor(Color.argb(80, 0, 170, 0));
        pnt.setStyle(Paint.Style.FILL);
        path = new Path();
        path.moveTo(544, 645);
        for (int i = 0; i < 180; i++){

            lineValue(645, i, i, (float) (((MainActivity.newDistance[i] + MainActivity.oldDistance[i])/2)*2));
            path.lineTo(xlo, ylo);
        }
        path.lineTo(544, 645);
        path.close();
        canvas.drawPath(path, pnt);

        if (MainActivity.firstRun >= 360) {
            pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
            pnt.setColor(Color.RED);
            pnt.setStrokeWidth(5f);
            pnt.setStyle(Paint.Style.STROKE);
            for (int i = 0; i < 180; i++) {
                if ((MainActivity.oldDistance[i]) - (MainActivity.newDistance[i]) > 35 || (MainActivity.newDistance[i]) - (MainActivity.oldDistance[i]) > 35) {
                    lineValue(525, i, i, (float) (MainActivity.newDistance[i] * 2));
                    canvas.drawCircle(xlo, 720 + ylo, 10, pnt);
                }
            }
            if ((MainActivity.oldDistance[MainActivity.angle]) - (MainActivity.newDistance[MainActivity.angle]) > 35 || (MainActivity.newDistance[MainActivity.angle]) - (MainActivity.oldDistance[MainActivity.angle]) > 35) {
                int detectAngle = MainActivity.angle;
                motionText = "Motion detected " + String.valueOf(detectAngle) + "˚";
            }
        }
    }

    public void lineValue(int st_Val, float x_Val, float y_Val, float radius) {
        float x = radius * (float)Math.cos(x_Val * (float)Math.PI / 180);
        float y = radius * (float)Math.sin(y_Val * (float)Math.PI / 180);
        xlo = 544 - x;
        ylo = st_Val - y;
    }

    public float limitFlux(){
        if (MainActivity.flux > 10000) {
            return 10000;
        }
        else {
            return MainActivity.flux;
        }
    }

    public void chage_Animation() {
        if (MainActivity.angle > 179) {
            animation = 1;
        }
        if (MainActivity.angle < 1) {
            animation = 0;
        }
    }
}
