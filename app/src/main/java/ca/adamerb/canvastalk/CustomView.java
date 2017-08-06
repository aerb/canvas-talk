package ca.adamerb.canvastalk;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

public class CustomView extends View {
    Paint paint;
    public CustomView(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
    }

    // Dont allocate in onDraw
    @Override
    protected void onDraw(Canvas canvas) {
//        Path p = new Path();
//        p.moveTo(width/2, height/2);

    }
}
