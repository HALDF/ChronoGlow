package ru.winnersofgames.chronoglow;
/**
 * @Author Nick Emelyanov
 * @Date 2025/06/24 14:28
 */
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.widget.TextView;

public class GradientClockView extends TextView {
    private int[] gradientColors = {Color.WHITE, Color.WHITE};

    public GradientClockView(Context ctx) {
        super(ctx);
    }

    public GradientClockView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }

    public void updateGradient(int start, int end) {
        gradientColors[0] = start;
        gradientColors[1] = end;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas c) {
        Paint textPaint = getPaint();
        textPaint.setShader(new LinearGradient(
                                0, 0, getWidth(), getHeight(),
                                gradientColors[0], gradientColors[1],
                                Shader.TileMode.MIRROR));
        super.onDraw(c);
    }
}
