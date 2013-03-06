/*
 * Author: Tim O'Shea
 * 
 * © 2013 All rights reserved
 */

package edu.pitt.cs1635.tdo7.prog2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

// MAIN
public class InnerDrawingView extends View {

	private Bitmap myBitmap;
	private Canvas myCanvas;
	private Paint myBitmapPaint;
	private Path myPath;
	private int currentColor;
	protected StringBuilder coordArray;

	// constructor 1
	public InnerDrawingView(Context context) {
		super(context);

		myBitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
		myCanvas = new Canvas(myBitmap);
		myPath = new Path();

		myBitmapPaint = new Paint();
		myBitmapPaint.setAntiAlias(true);
		myBitmapPaint.setDither(false);
		myBitmapPaint.setColor(Color.BLACK);
		myBitmapPaint.setStrokeWidth(5);
		myBitmapPaint.setStyle(Paint.Style.STROKE);
		currentColor = Color.YELLOW;

		coordArray = new StringBuilder("[");
	}

	// constructor 2
	public InnerDrawingView(Context context, AttributeSet atts) {
		super(context, atts);

		myBitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
		myCanvas = new Canvas(myBitmap);
		myPath = new Path();

		myBitmapPaint = new Paint();
		myBitmapPaint.setAntiAlias(true);
		myBitmapPaint.setDither(false);
		myBitmapPaint.setColor(Color.BLACK);
		myBitmapPaint.setStrokeWidth(5);
		myBitmapPaint.setStyle(Paint.Style.STROKE);
		currentColor = Color.YELLOW;

		coordArray = new StringBuilder("[");

	}

	// use new settings
	public void updatePrefs(int newWidth, int newPaintColor, int newBgColor) {
		myBitmapPaint.setStrokeWidth(newWidth);
		myBitmapPaint.setColor(newPaintColor);
		myCanvas.drawColor(newBgColor);
		currentColor = newBgColor;
	} // end of updateprefs

	// clear the canvas
	public void clear() {
		myCanvas.drawColor(currentColor);
		invalidate();
		resetResultString();
	} // end of clear

	// main draw class
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.YELLOW);
		canvas.drawBitmap(myBitmap, 0, 0, myBitmapPaint);
	} // end of ondraw

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 3;

	// start of a stroke
	void touch_start(float x, float y) {
		myPath.reset();
		myPath.moveTo(x, y);
		invalidate();
		mX = x;
		mY = y;
		coordArray
				.append(((x * 254) / 1000) + ", " + ((y * 254) / 1000) + ", ");
	} // end of touch start

	// finger is still drawing
	void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			myPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			myCanvas.drawPath(myPath, myBitmapPaint);
			invalidate();
			mX = x;
			mY = y;
			coordArray.append(((x * 254) / 1000) + ", " + ((y * 254) / 1000)
					+ ", ");
		}
	} // end of touch move

	// connect the start and end of the stroke
	void touch_up() {
		myPath.lineTo(mX, mY);
		// commit the path to our offscreen
		myCanvas.drawPath(myPath, myBitmapPaint);
		invalidate();
		// kill this so we don't double draw
		myPath.reset();
		coordArray.append("255, 0, ");
	} // end of touch up

	// get the coordinate string
	public String getCoordArray() {
		return coordArray.toString();
	}

	// user is done drawing, finish the string array
	public void endOfChar() {
		coordArray.append("255, 255]");
	}

	// back to "["
	public void resetResultString() {
		coordArray.setLength(0);
		coordArray.append("[");
	}
} // end of activity
