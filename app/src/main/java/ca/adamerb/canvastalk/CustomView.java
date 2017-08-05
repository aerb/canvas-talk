package ca.adamerb.canvastalk;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class CustomView extends View {
    Paint paint;
    public CustomView(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawRect(0, 0, w, h, paint);
//        canvas.drawCircle(0, 0, r, paint);
//        canvas.drawLine(0, 0, w, h, paint);
    }
}
