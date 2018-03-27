package com.upmc.tomo.tp_alt_mozaigram.mozaik_process;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.Image;
import android.util.Log;

/**
 * Created by Tomo on 25/03/2018.
 */

public class MozaikGenerator {

    final static String TAG = MozaikGenerator.class.getSimpleName();

    public static Bitmap generate(Bitmap image, String originalFileName) {





        return image;
    }


    /**
     * Algorithme de Generation de Mosaique, mais opération couteuse en temps
     *
     * @param bmp Image à traiter
     * @param percent Grain（0-1）
     *                300*300 precent=1  time=57ms
     * @return
     */
    public static Bitmap getMosaicsBitmap(Bitmap bmp, double percent) {
        long start = System.currentTimeMillis();
        int bmpW = bmp.getWidth();
        int bmpH = bmp.getHeight();



        Bitmap resultBmp = Bitmap.createBitmap(bmpW, bmpH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBmp);
        Paint paint = new Paint();
        double unit;
        if (percent == 0) {
            unit = bmpW;
        } else {
            unit = 1 / percent;
        }
        double resultBmpW = bmpW / unit;
        double resultBmpH = bmpH / unit;
        for (int i = 0; i < resultBmpH; i++) {
            for (int j = 0; j < resultBmpW; j++) {
                int pickPointX = (int) (unit * (j + 0.5));
                int pickPointY = (int) (unit * (i + 0.5));
                int color;
                if (pickPointX >= bmpW || pickPointY >= bmpH) {
                    color = bmp.getPixel(bmpW / 2, bmpH / 2);
                } else {
                    color = bmp.getPixel(pickPointX, pickPointY);
                }
                paint.setColor(color);
                canvas.drawRect((int) (unit * j), (int) (unit * i), (int) (unit * (j + 1)), (int) (unit * (i + 1)), paint);
            }
        }
        canvas.setBitmap(null);
        long end = System.currentTimeMillis();
        Log.v(TAG, "getMosaicsBitmap() => non optimal algorithm - DrawTime : " + (end - start));
        return resultBmp;
    }

    /**
     * Algorithme de Generation de Mosaique, avec une meilleure efficacite par rapport au premier algorithme
     *
     * @param bmp Image à traiter
     * @param percent Grain
     * @return
     */
    public static Bitmap getMosaicsBitmaps(Bitmap bmp, double percent) {
        long start = System.currentTimeMillis();
        int bmpW = bmp.getWidth();
        int bmpH = bmp.getHeight();

        Log.e(TAG, " [" + bmpW + "," + bmpH + "]");

        int[] pixels = new int[bmpH * bmpW];
        bmp.getPixels(pixels, 0, bmpW, 0, 0, bmpW, bmpH);
        int raw = (int) (bmpW * percent);
        Log.e(TAG, "raw : " + raw);
        int unit;
        if (raw == 0) {
            unit = bmpW;
        } else {
            // Unité originale * Les pixels de l'unité sont combinés en un seul, en utilisant la valeur d'origine en haut à gauche
            unit = bmpW / raw;
        }
        if (unit >= bmpW || unit >= bmpH) {
            return getMosaicsBitmap(bmp, percent);
        }
        for (int i = 0; i < bmpH; ) {
            for (int j = 0; j < bmpW; ) {
                int leftTopPoint = i * bmpW + j;
                for (int k = 0; k < unit; k++) {
                    for (int m = 0; m < unit; m++) {
                        int point = (i + k) * bmpW + (j + m);
                        if (point < pixels.length) {
                            pixels[point] = pixels[leftTopPoint];
                        }
                    }
                }
                j += unit;
            }
            i += unit;
        }
        long end = System.currentTimeMillis();
        Log.v(TAG, "getMosaicsBitmap() => optimal algorithm - DrawTime : " + (end - start));
        return Bitmap.createBitmap(pixels, bmpW, bmpH, Bitmap.Config.ARGB_8888);
    }

}
