package com.adamswiatkowski.smp3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawingSurface extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mPojemnik;

    private Thread mWatekRysujacy;

    private boolean mWatekPracuje = false;

    private Object mBlokada = new Object();

    private Bitmap mBitmapa = null;
    private Canvas mKanwa = null;

    Path mSciezka = null;

    Paint mFarba;

    public DrawingSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPojemnik = getHolder();
        mPojemnik.addCallback(this);
    }

    public void wznowRysowanie(){
        mWatekRysujacy = new Thread(this);
        mWatekPracuje = true;
        mWatekRysujacy.start();
    }

    public void pauzujRysowanie(){

        mWatekPracuje = false;
    }

    public void czyscPowierzchnie() {
        mKanwa.drawARGB(255,255,255,255);
        mFarba.setColor(Color.WHITE);
    }

    public void ustawKolor(int color) {

        mFarba.setColor(color);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mBitmapa = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mKanwa = new Canvas(mBitmapa);
        mKanwa.drawARGB(255,255,255,255);

        mFarba = new Paint();
        mFarba.setColor(Color.WHITE);
        mFarba.setStrokeWidth(5);
        mFarba.setStyle(Paint.Style.STROKE);
        mFarba.setStyle(Paint.Style.FILL);

        wznowRysowanie();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mWatekPracuje = false;
    }

    @Override
    public void run() {
        while(mWatekPracuje){
            Canvas kanwa = null;
            try{
                synchronized (mPojemnik){
                    if(!mPojemnik.getSurface().isValid()) continue;

                    kanwa = mPojemnik.lockCanvas(null);

                    synchronized (mBlokada){
                        if(mWatekPracuje){
                            kanwa.drawBitmap(mBitmapa, 0, 0 ,null);
                        }
                    }
                }
            } finally {
                if(kanwa != null){
                    mPojemnik.unlockCanvasAndPost(kanwa);
                }
            }
        } try{
            Thread.sleep(1000/25);
        } catch (InterruptedException e){

        }
    }

    public boolean performClick(){

        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        performClick();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mSciezka = new Path();
                mSciezka.moveTo(event.getX(), event.getY());
                mFarba.setStyle(Paint.Style.FILL);
                mKanwa.drawCircle(event.getX(), event.getY(), 8, mFarba);

                break;

            case MotionEvent.ACTION_MOVE:
                mFarba.setStyle(Paint.Style.STROKE);
                mSciezka.lineTo(event.getX(), event.getY());
                mKanwa.drawPath(mSciezka, mFarba);

                break;

            case MotionEvent.ACTION_UP:
                mFarba.setStyle(Paint.Style.FILL);
                mKanwa.drawCircle(event.getX(), event.getY(), 8, mFarba);

                break;
        }

        synchronized (mBlokada){

        }


        return true;
    }

}