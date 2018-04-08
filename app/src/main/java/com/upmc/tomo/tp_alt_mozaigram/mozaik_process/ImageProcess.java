package com.upmc.tomo.tp_alt_mozaigram.mozaik_process;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by Tomo on 03/04/2018.
 */

public class ImageProcess {
    public static int[][][] getPixel(Bitmap bitmap) {
        long startTime = System.currentTimeMillis();
        int[][][] pixels = new int[bitmap.getWidth()][bitmap.getHeight()][3];

        for(int x = 0; x < bitmap.getWidth(); x++) {
            for(int y = 0; y < bitmap.getHeight(); y++) {
                int clr = bitmap.getPixel(x, y);
                int red   = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue  =  clr & 0x000000ff;
                pixels[x][y][0] = red;
                pixels[x][y][1] = green;
                pixels[x][y][2] = blue;
                //System.out.println("["+x+"]["+y+"] - R: "+ red + " G: " + green + " B: " + blue);
            }
        }
        return pixels;
    }

    public static Integer[] getAveragePixel(Bitmap bitmap){
        int[][][] pixels = getPixel(bitmap);

        Integer [] averPix= new Integer[3];

        averPix[0]=0;
        averPix[1]=0;
        averPix[2]=0;

        for (int i=0;i<pixels.length;i++){
            for (int j=0;j<pixels[0].length;j++){
                averPix[0]+=pixels[i][j][0];
                averPix[1]+=pixels[i][j][1];
                averPix[2]+=pixels[i][j][2];
            }
        }

        averPix[0]=averPix[0]/(pixels.length*pixels[0].length);
        averPix[1]=averPix[1]/(pixels.length*pixels[0].length);
        averPix[2]=averPix[2]/(pixels.length*pixels[0].length);

        //System.out.println("R: "+averPix[0]+" G: "+averPix[1]+" B: "+averPix[2]);
        return averPix;
    }

    public static int[][][] getWeightedPixels(Bitmap bitmap, int grain) {
        // la matrice RGB de toutes les tuiles de l'image contenue dans component
        int[][][] pixels = getPixel(bitmap);
        int [][][] weighedpixels=new int[pixels.length/grain][pixels[0].length/grain][3];
        for (int i=0;i<weighedpixels.length;i++){
            for (int j=0;j<weighedpixels[0].length;j++){
                weighedpixels[i][j][0]=0;
                weighedpixels[i][j][1]=0;
                weighedpixels[i][j][2]=0;
                for (int k=0;k<grain;k++){
                    for (int l=0;l<grain;l++){
                        weighedpixels[i][j][0]+=pixels[grain*i+k][grain*j+l][0];
                        weighedpixels[i][j][1]+=pixels[grain*i+k][grain*j+l][1];
                        weighedpixels[i][j][2]+=pixels[grain*i+k][grain*j+l][2];
                    }
                }
                weighedpixels[i][j][0]=weighedpixels[i][j][0]/(grain*grain);
                weighedpixels[i][j][1]=weighedpixels[i][j][1]/(grain*grain);
                weighedpixels[i][j][2]=weighedpixels[i][j][2]/(grain*grain);
            }
        }
        return weighedpixels;
    }

    public static Bitmap[][] splitBitmap(Bitmap bitmap, int xCount, int yCount) {
        // Allocate a two dimensional array to hold the individual images.
        Bitmap[][] bitmaps = new Bitmap[xCount][yCount];
        int width, height;
        // Divide the original bitmap width by the desired vertical column count
        width = bitmap.getWidth() / xCount;
        // Divide the original bitmap height by the desired horizontal row count
        height = bitmap.getHeight() / yCount;
        // Loop the array and create bitmaps for each coordinate
        for(int x = 0; x < xCount; ++x) {
            for(int y = 0; y < yCount; ++y) {
                // Create the sliced bitmap
                bitmaps[x][y] = Bitmap.createBitmap(bitmap, x * width, y * height, width, height);
            }
        }
        // Return the array
        return bitmaps;
    }
}
