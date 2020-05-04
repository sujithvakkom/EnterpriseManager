package sujithvakkomfreelance.com.enterprisemanager.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

public class DrawableView extends View {
    private static final float STROKE_WIDTH = 5f;
    private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
    private Paint paint = new Paint();
    private Path path = new Path();
    private onStarDrawing onStarDrawing;

    private float lastTouchX;
    private float lastTouchY;
    private final RectF dirtyRect = new RectF();
    private Bitmap mBitmap;
    private boolean clean;

    public DrawableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(STROKE_WIDTH);
        clean=true;
    }

    public Bitmap save(Context context,
                       File mypath) {
        Log.v("log_tag", "Width: " + this.getWidth());
        Log.v("log_tag", "Height: " + this.getHeight());
        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(mBitmap);
        try {
            FileOutputStream mFileOutStream = new FileOutputStream(mypath);

            this.draw(canvas);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
            mFileOutStream.flush();
            mFileOutStream.close();
            String url = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    mBitmap, "title", null);
            Log.v("log_tag", "url: " + url);

        } catch (Exception e) {
            Log.v("log_tag", e.toString());
        }
        return mBitmap;
    }

    public void clear() {
        path.reset();
        invalidate();
        this.clean = true;
        if(onStarDrawing!=null)onStarDrawing.startedDrawingListener(this.clean);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        //mGetSign.setEnabled(true);
        this.clean = false;
        if(onStarDrawing!=null)onStarDrawing.startedDrawingListener(this.clean);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                lastTouchX = eventX;
                lastTouchY = eventY;
                return true;

            case MotionEvent.ACTION_MOVE:

            case MotionEvent.ACTION_UP:

                resetDirtyRect(eventX, eventY);
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    expandDirtyRect(historicalX, historicalY);
                    path.lineTo(historicalX, historicalY);
                }
                path.lineTo(eventX, eventY);
                break;

            default:
                debug("Ignored touch event: " + event.toString());
                return false;
        }

        invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

        lastTouchX = eventX;
        lastTouchY = eventY;

        return true;
    }

    private void debug(String string) {
    }

    private void expandDirtyRect(float historicalX, float historicalY) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX;
        } else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX;
        }

        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY;
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY;
        }
    }

    private void resetDirtyRect(float eventX, float eventY) {
        dirtyRect.left = Math.min(lastTouchX, eventX);
        dirtyRect.right = Math.max(lastTouchX, eventX);
        dirtyRect.top = Math.min(lastTouchY, eventY);
        dirtyRect.bottom = Math.max(lastTouchY, eventY);
    }

    public DrawableView.onStarDrawing getOnStarDrawing() {
        return onStarDrawing;
    }

    public void setOnStarDrawing(DrawableView.onStarDrawing onStarDrawing) {
        this.onStarDrawing = onStarDrawing;
    }

    public interface  onStarDrawing{
        void startedDrawingListener(boolean isClean);
    }
}

