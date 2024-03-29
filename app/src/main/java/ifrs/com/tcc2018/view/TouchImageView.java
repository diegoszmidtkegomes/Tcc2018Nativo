package ifrs.com.tcc2018.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Created by diego.gomes on 23/02/2017.
 */


public class TouchImageView extends android.support.v7.widget.AppCompatImageView {

    /**
     * The constant NONE.
     */
// We can be in one of these 3 states
    static final int NONE = 0;
    /**
     * The Drag.
     */
    static final int DRAG = 1;
    /**
     * The Zoom.
     */
    static final int ZOOM = 2;
    /**
     * The Click.
     */
    static final int CLICK = 3;
    /**
     * The Orig width.
     */
    protected float origWidth, /**
     * The Orig height.
     */
    origHeight;
    /**
     * The Matrix.
     */
    Matrix matrix;
    /**
     * The Mode.
     */
    int mode = NONE;
    /**
     * The Last.
     */
// Remember some things for zooming
    PointF last = new PointF();
    /**
     * The Start.
     */
    PointF start = new PointF();
    /**
     * The Min scale.
     */
    float minScale = 1f;
    /**
     * The Max scale.
     */
    float maxScale = 3f;
    /**
     * The M.
     */
    float[] m;
    /**
     * The View width.
     */
    int viewWidth, /**
     * The View height.
     */
    viewHeight;
    /**
     * The Save scale.
     */
    float saveScale = 1f;
    /**
     * The Old measured width.
     */
    int oldMeasuredWidth, /**
     * The Old measured height.
     */
    oldMeasuredHeight;

    /**
     * The M scale detector.
     */
    ScaleGestureDetector mScaleDetector;

    /**
     * The Context.
     */
    Context context;

    /**
     * Instantiates a new Touch image view.
     *
     * @param context the context
     */
    public TouchImageView(Context context) {
        super(context);
        sharedConstructing(context);
    }

    /**
     * Instantiates a new Touch image view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructing(context);
    }

    private void sharedConstructing(Context context) {

        super.setClickable(true);

        this.context = context;

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        matrix = new Matrix();

        m = new float[9];

        setImageMatrix(matrix);

        setScaleType(ScaleType.MATRIX);

        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mScaleDetector.onTouchEvent(event);

                PointF curr = new PointF(event.getX(), event.getY());

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        last.set(curr);

                        start.set(last);

                        mode = DRAG;

                        break;

                    case MotionEvent.ACTION_MOVE:

                        if (mode == DRAG) {

                            float deltaX = curr.x - last.x;

                            float deltaY = curr.y - last.y;

                            float fixTransX = getFixDragTrans(deltaX, viewWidth, origWidth * saveScale);

                            float fixTransY = getFixDragTrans(deltaY, viewHeight, origHeight * saveScale);

                            matrix.postTranslate(fixTransX, fixTransY);

                            fixTrans();

                            last.set(curr.x, curr.y);

                        }

                        break;

                    case MotionEvent.ACTION_UP:

                        mode = NONE;

                        int xDiff = (int) Math.abs(curr.x - start.x);

                        int yDiff = (int) Math.abs(curr.y - start.y);

                        if (xDiff < CLICK && yDiff < CLICK)

                            performClick();

                        break;

                    case MotionEvent.ACTION_POINTER_UP:

                        mode = NONE;

                        break;

                }

                setImageMatrix(matrix);

                invalidate();

                return true; // indicate event was handled

            }

        });
    }

    /**
     * Sets max zoom.
     *
     * @param x the x
     */
    public void setMaxZoom(float x) {

        maxScale = x;

    }

    /**
     * Fix trans.
     */
    void fixTrans() {

        matrix.getValues(m);

        float transX = m[Matrix.MTRANS_X];

        float transY = m[Matrix.MTRANS_Y];

        float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);

        float fixTransY = getFixTrans(transY, viewHeight, origHeight * saveScale);

        if (fixTransX != 0 || fixTransY != 0)

            matrix.postTranslate(fixTransX, fixTransY);

    }

    /**
     * Gets fix trans.
     *
     * @param trans       the trans
     * @param viewSize    the view size
     * @param contentSize the content size
     * @return the fix trans
     */
    float getFixTrans(float trans, float viewSize, float contentSize) {

        float minTrans, maxTrans;

        if (contentSize <= viewSize) {

            minTrans = 0;

            maxTrans = viewSize - contentSize;

        } else {

            minTrans = viewSize - contentSize;

            maxTrans = 0;

        }

        if (trans < minTrans)

            return -trans + minTrans;

        if (trans > maxTrans)

            return -trans + maxTrans;

        return 0;

    }

    /**
     * Gets fix drag trans.
     *
     * @param delta       the delta
     * @param viewSize    the view size
     * @param contentSize the content size
     * @return the fix drag trans
     */
    float getFixDragTrans(float delta, float viewSize, float contentSize) {

        if (contentSize <= viewSize) {

            return 0;

        }

        return delta;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = MeasureSpec.getSize(widthMeasureSpec);

        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        //
        // Rescales image on rotation
        //
        if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight

                || viewWidth == 0 || viewHeight == 0)

            return;

        oldMeasuredHeight = viewHeight;

        oldMeasuredWidth = viewWidth;

        if (saveScale == 1) {

            //Fit to screen.

            float scale;

            Drawable drawable = getDrawable();

            if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0)

                return;

            int bmWidth = drawable.getIntrinsicWidth();

            int bmHeight = drawable.getIntrinsicHeight();

            Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);

            float scaleX = (float) viewWidth / (float) bmWidth;

            float scaleY = (float) viewHeight / (float) bmHeight;

            scale = Math.min(scaleX, scaleY);

            matrix.setScale(scale, scale);

            // Center the image

            float redundantYSpace = (float) viewHeight - (scale * (float) bmHeight);

            float redundantXSpace = (float) viewWidth - (scale * (float) bmWidth);

            redundantYSpace /= (float) 2;

            redundantXSpace /= (float) 2;

            matrix.postTranslate(redundantXSpace, redundantYSpace);

            origWidth = viewWidth - 2 * redundantXSpace;

            origHeight = viewHeight - 2 * redundantYSpace;

            setImageMatrix(matrix);

        }

        fixTrans();

    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {

            mode = ZOOM;

            return true;

        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float mScaleFactor = detector.getScaleFactor();

            float origScale = saveScale;

            saveScale *= mScaleFactor;

            if (saveScale > maxScale) {

                saveScale = maxScale;

                mScaleFactor = maxScale / origScale;

            } else if (saveScale < minScale) {

                saveScale = minScale;

                mScaleFactor = minScale / origScale;

            }

            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight)

                matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2, viewHeight / 2);

            else

                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());

            fixTrans();

            return true;

        }

    }
}

