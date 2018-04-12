package com.fx.light.model;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.Arrays;


//**FastRGB
// используется для быстрого доступа к пикселям изображения BufferedImage,
// при создании объекта класса все пиксели из image копируются байтовый массив pixels
// и дальнейшая работа происходит через обращения к этому массиву
// не работает с gif форматом!*/
public class FastRGB
{

    private int width;
    private int height;
    private boolean hasAlphaChannel;
    private int pixelLength;
    private byte[] pixels;

    public FastRGB(BufferedImage image){
        pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();    //не работает с gif форматом!
        width = image.getWidth();
        height = image.getHeight();
        hasAlphaChannel = image.getAlphaRaster() != null;
        pixelLength = 3;
        if (hasAlphaChannel){
            pixelLength = 4;
        }
    }

    public int getRGBint(int x, int y) {
        int pos = (y * pixelLength * width) + (x * pixelLength);
        int argb = -16777216; // 255 alpha
        if (hasAlphaChannel)
        {
            argb = (((int) pixels[pos++] & 0xff) << 24); // alpha
        }

        argb += ((int) pixels[pos++] & 0xff); // blue
        argb += (((int) pixels[pos++] & 0xff) << 8); // green
        argb += (((int) pixels[pos] & 0xff) << 16); // red
        return argb;
    }


    public byte[] getRGBbyt(int x, int y){
        int pos = (y * pixelLength * width) + (x * pixelLength);
        byte[] argb = new byte[pixelLength];
        if (hasAlphaChannel) {
            argb[3] = pixels[pos++]; // alpha
        }
        argb[2] = pixels[pos++]; // blue
        argb[1] = pixels[pos++]; // green
        argb[0] = pixels[pos++]; // red
        return argb;
    }

    public byte[] getRGBbyt(int index){
        byte[] argb = new byte[pixelLength];
        if (hasAlphaChannel) {
            argb[3] = pixels[index++]; // alpha
        }
        argb[2] = pixels[index++]; // blue
        argb[1] = pixels[index++]; // green
        argb[0] = pixels[index++]; // red
        return argb;
    }

    public byte[][] getRGBarray(){
        byte[][] argb = new byte[width*height][pixelLength];
        for (int i = 0; i < pixels.length;) {
            if (hasAlphaChannel){
                argb[i][3] = pixels[i];     // alpha
                argb[i][2] = pixels[i+1];   // blue
                argb[i][1] = pixels[i+2];   // green
                argb[i][0] = pixels[i+3];   // red
            }
            else {
                argb[i][2] = pixels[i];     // blue
                argb[i][1] = pixels[i+1];   // green
                argb[i][0] = pixels[i+2];   // red
            }
        }
        return argb;
    }


    //возвращает двумерный байтовый массив с палитрой
    public Byte[][] getRGBpaletteByt(){
        ArrayList<Byte[]> palette = new ArrayList <>();
         for (int y = 0; y < height; y++) {
             for (int x = 0; x < width; x++) {
                 byte[] buf = getRGBbyt(x, y);
                 Byte[] p = new Byte[pixelLength];
                 if (hasAlphaChannel) {
                    p[3] = buf[3];
                    p[2] = buf[2];
                    p[1] = buf[1];
                    p[0] = buf[0];}
                 else {
                    p[2] = buf[2];
                    p[1] = buf[1];
                    p[0] = buf[0];
                 }
                 boolean repit = false;
                 for (Byte[] color: palette) {
                    if  (Arrays.equals(color, p)) {
                        repit = true;
                        break;
                    }
                 }
                 if (!repit)
                 palette.add(p);

        }
    }
        Byte[][] bytes = new Byte[palette.size()][pixelLength];
         bytes =  palette.toArray(bytes);
        return bytes;
}

}
