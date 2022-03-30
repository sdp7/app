package com.example.fred;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private float centerX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;
    private JoystickListener joystickCallback;

    private void setupDimensions(){
        centerX = getWidth()/2;
        centerY = getHeight()/2;
        baseRadius = Math.min(getWidth(),getHeight())/3;
        hatRadius = Math.min(getWidth(),getHeight())/6;
    }

    public JoystickView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener){
            joystickCallback = (JoystickListener) context;
        }
    }

    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener){
            joystickCallback = (JoystickListener) context;
        }
    }

    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener){
            joystickCallback = (JoystickListener) context;
        }
    }

    private void drawJoystick(float newX, float newY){
        if(getHolder().getSurface().isValid()){
            Canvas myCanvas = this.getHolder().lockCanvas();
            Paint colors = new Paint();
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            colors.setARGB(50,0,0,0);
            myCanvas.drawCircle(centerX,centerY,baseRadius,colors);
            colors.setARGB(150,255,255,255);
            myCanvas.drawCircle(centerX,centerY,baseRadius-4,colors);
            colors.setARGB(255,0,0,0);
            myCanvas.drawCircle(newX,newY, hatRadius,colors);
            colors.setARGB(255,255,255,255);
            myCanvas.drawCircle(newX,newY, hatRadius-4,colors);
            getHolder().unlockCanvasAndPost(myCanvas);
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        setZOrderOnTop(true);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        setupDimensions();
        drawJoystick(centerX,centerY);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if(v.equals(this)){
            if(e.getAction() != e.ACTION_UP){
                float displacement = (float) Math.sqrt((Math.pow(e.getX() - centerX, 2)) + Math.pow(e.getY() - centerY, 2));
                if(displacement < baseRadius){
                    drawJoystick(e.getX(),e.getY());
                    joystickCallback.onJoystickMoved((e.getX() - centerX)/baseRadius,-(e.getY() - centerY)/baseRadius,getId());
                }else{
                    float ratio = baseRadius / displacement;
                    float constrainedX = centerX + (e.getX() - centerX) * ratio;
                    float constrainedY = centerY + (e.getY() - centerY) * ratio;
                    drawJoystick(constrainedX,constrainedY);
                    joystickCallback.onJoystickMoved((constrainedX - centerX)/baseRadius,-(constrainedY - centerY)/baseRadius,getId());
                }
            }else{
                drawJoystick(centerX,centerY);
                joystickCallback.onJoystickMoved(0,0, getId());
            }
        }
        return true;
    }

    public interface JoystickListener{
        void onJoystickMoved(float xPercent, float yPercent, int id);
    }
}
