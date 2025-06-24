package ru.winnersofgames.chronoglow;
/**
 * @Author Nick Emelyanov
 * @Date 2025/06/24 14:17
 */
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    private GradientClockView clock;
    private TextView day;
    
    private Handler timeHandler;

    private int activeTheme = 0;
    private int[][] colorThemes = {
        {0xFFF857A6, 0xFFFF5858},
        {0xFF4FACFE, 0xFF00F2FE},
        {0xFF43E97B, 0xFF38F9D7},
        {0xFFFFA751, 0xFFFF7B4E}
    };
    
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        setupViews();
        prepareDisplay();
        beginTimeRefresh();
    }

    private void setupViews() {
        day = findViewById(R.id.date);
        clock = findViewById(R.id.clock);
    }

    private void prepareDisplay() {
        Paint clockPaint = clock.getPaint();
        clockPaint.setShadowLayer(15, 0, 0, getResources().getColor(R.color.shadow_white));
    }

    private void beginTimeRefresh() {
        timeHandler = new Handler();
        timeHandler.post(new ClockUpdater());
    }

    class ClockUpdater implements Runnable {
        public void run() {
            refreshTime();
            timeHandler.postDelayed(this, 1000);
        }
    }

    private void refreshTime() {
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String currentDate = new SimpleDateFormat("EEEE, d MMMM").format(new Date());

        clock.setText(currentTime);
        day.setText(currentDate);

        changeColorsSmoothly();
    }

    private void changeColorsSmoothly() {
        final int nextThemeIndex = (activeTheme + 1) % colorThemes.length;
        final int[] currentTheme = colorThemes[activeTheme];
        final int[] nextTheme = colorThemes[nextThemeIndex];

        ValueAnimator colorChange = ValueAnimator.ofFloat(0, 1);
        colorChange.setDuration(3000);

        colorChange.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animator) {
                    float progress = animator.getAnimatedFraction();
                    int start = mixColors(currentTheme[0], nextTheme[0], progress);
                    int end = mixColors(currentTheme[1], nextTheme[1], progress);
                    clock.updateGradient(start, end);
                }
            });

        colorChange.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator anim) {
                    activeTheme = nextThemeIndex;
                    changeColorsSmoothly();
                }
            });
        colorChange.start();
    }

    private int mixColors(int first, int second, float ratio) {
        float inverse = 1.0f - ratio;
        int r = (int)(Color.red(first) * inverse + Color.red(second) * ratio);
        int g = (int)(Color.green(first) * inverse + Color.green(second) * ratio);
        int b = (int)(Color.blue(first) * inverse + Color.blue(second) * ratio);
        return Color.rgb(r, g, b);
    }

    @Override
    protected void onStop() {
        timeHandler.removeCallbacksAndMessages(null);
        super.onStop();
    }
                                 }
