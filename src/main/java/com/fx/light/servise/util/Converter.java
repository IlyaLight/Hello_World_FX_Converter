package com.fx.light.servise.util;

import com.fx.light.api.Exception.UnableToEncodeException;
import com.fx.light.model.FastRGB;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class Converter {


    private static final String HEAD = "// Don't edit this file!  It's software-generated.\n" +
            "\n" +
            "#define PALETTE1  0\n" +
            "#define PALETTE4  1\n" +
            "#define PALETTE8  2\n" +
            "#define TRUECOLOR 3\n" +
            "\n";
    private static final String FORMAT_DEFINE_NUM_LEDS = "#define NUM_LEDS %d\n\n";
    private static final String FORMAT_FILE_NAME = "// %s ------------------------------------------------------------\n\n";
    private static final String FORMAT_PROGMEM = "{ PALETTE%d,\t\t%4d,\t(const uint8_t *)palette%02d,\tpixels%02d }";
    private static final String FORMAT_PROGMEM_TRUECOLOR = "{ TRUECOLOR,\t%4d,\tNULL, \t\t\t\t\t\tpixels%02d }";


    /**convert*/
    public static String convert (List<File> fileList, int pixelNumber)
         throws UnableToEncodeException,  NullPointerException,  IOException {
        if (pixelNumber == 0) {
                throw new UnableToEncodeException("pixelNumber must be > 0");
        }
        StringBuilder  outData = new StringBuilder ();

        outData.append(HEAD);
        outData.append(String.format(FORMAT_DEFINE_NUM_LEDS, pixelNumber));
        String[] progmem = new String[fileList.size()];

        //перебираем изображения
        ListIterator<File> listIterator = fileList.listIterator();
        for (File file = listIterator.next(); listIterator.hasNext(); file=listIterator.next()) {
            int fileIndex = listIterator.nextIndex()-1;
            BufferedImage image = ImageIO.read(file);
            int height = image.getHeight();
            int width = image.getWidth();
            FastRGB fastRGB = new FastRGB(image);
            Byte[][] palette = fastRGB.getRGBpaletteByt();
            outData.append(String.format(FORMAT_FILE_NAME, file.getName()));

            if (palette.length <= 256){
                if (palette.length <= 2) {          //2 цвета
                    setPalette(outData, palette, fileIndex);
                    progmem[fileIndex] = String.format(FORMAT_PROGMEM, 1, width, fileIndex, fileIndex);
                    _2_ColorsConverter(outData, palette, fastRGB, width, height, fileIndex);
                }
                else if (palette.length <= 16) {    //не больше 16 цветов в палитре, один цвет 2 бита
                    setPalette(outData, palette, fileIndex);
                    progmem[fileIndex] = String.format(FORMAT_PROGMEM, 4, width, fileIndex, fileIndex);
                    _16_ColorsConverter(outData, palette, fastRGB, width, height, fileIndex);
                }
                else if (palette.length <= 255) {   //не больше 255 цветов в палитре, один цвет 1 байт
                    setPalette(outData, palette, fileIndex);
                    progmem[fileIndex] = String.format(FORMAT_PROGMEM, 8, width, fileIndex, fileIndex);
                    _256_ColorsConverter(outData, palette, fastRGB, width, height, fileIndex);
                }
                else {                              //3 байта на цвет
                    progmem[fileIndex] = String.format(FORMAT_PROGMEM_TRUECOLOR, width, fileIndex);
                    trueColorsConverter(outData, fastRGB, width, height, fileIndex);
                }
            }


        }
        outData.append("typedef struct \n" +
                "{\n" +
                "\tuint8_t\t\t\ttype;\t\t// PALETTE[1,4,8] or TRUECOLOR\n" +
                "\tuint16_t\t\tlines;\t\t// Length of image (in scan lines)\n" +
                "\tconst uint8_t\t*palette; \t// -> PROGMEM color table (NULL if truecolor)\n" +
                "\tconst uint8_t\t*pixels;  \t// -> Pixel data in PROGMEM\n" +
                "} Image;\n" +
                "\n" +
                "const Image PROGMEM images[] = {\n");
        for (int i = 0;i<progmem.length;) {
            outData.append("\t" + progmem[i]);
            i++;
            if (i<progmem.length)
                outData.append(",\n");
        }
        outData.append(
                "};\n" +
                "\n" +
                "#define NUM_IMAGES (sizeof(images) / sizeof(images[0])) \n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "#endif /* GRAPHICS_H_ */");


        return  outData.toString();
    }

    private static void trueColorsConverter(StringBuilder outData, FastRGB fastRGB, int width, int height, Integer fileIndex) {
        int resolution = height * width;
        outData.append(String.format("const uint8_t PROGMEM pixels%02d[] = {", fileIndex));
        byte b[];
        int column = 0;
        int pixelNum = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y ++) {
                column++;
                pixelNum++;
                if (column == 4) {
                    outData.append("/n");
                    column = 0;
                }
                b = fastRGB.getRGBbyt(x, y);
                outData.append(String.format(" %3d,", b[0] & 0b11111111))
                .append(String.format(" %3d,", b[1] & 0b11111111))
                .append(String.format(" %3d", b[2] & 0b11111111));
                if (pixelNum < resolution) {
                    outData.append(",");
                }
            }
        }
        outData.append(" };\n\n");
    }

    private static void _256_ColorsConverter(StringBuilder outData, Byte[][] palette, FastRGB fastRGB, int width, int height, Integer fileIndex) {
        int resolution = width * height;
        int pixelNum = 0;
        outData.append(String.format("const uint8_t PROGMEM pixels%02d[] = {\n    ", fileIndex));
        for (int x = 0; x < width; x++) {
            outData.append("\n    ");
            for (int y = 0; y < height; y ++) {
                byte b = (byte) getPosition(palette, fastRGB.getRGBbyt(x, y));
                pixelNum++;
                outData.append(String.format(" %3d", b & 0b11111111));
                if (pixelNum < resolution)
                    outData.append(",");
            }
        }
    }

    private static void _16_ColorsConverter(StringBuilder outData, Byte[][] palette, FastRGB fastRGB, int width, int height, Integer fileIndex) {
        int resolution = width * height;
        int pixelNum = 0;
        outData.append(String.format("const uint8_t PROGMEM pixels%02d[] = {\n    ", fileIndex));
        for (int x = 0; x < width; x++) {
            outData.append("\n    ");
            for (int y = 0; y < height; y += 2) {
                byte b = (byte) getPosition(palette, fastRGB.getRGBbyt(x, y));
                pixelNum++;
                if (y + 1 < height)
                    b = (byte) (b * 16 + getPosition(palette, fastRGB.getRGBbyt(x, y + 1)));
                outData.append(String.format(" %3d", b & 0b11111111));
                if (pixelNum < resolution / 2)
                    outData.append(",");
            }
        }
        outData.append(" };\n\n");
    }

    private static void _2_ColorsConverter(StringBuilder outData, Byte[][] palette, FastRGB fastRGB, int width, int height, Integer fileIndex) {
        int resolution = width * height;
        int pixelNum = 0;
        byte b = 0;
        int bitNumb = 0;
        int column = 0;
        outData.append(String.format("const uint8_t PROGMEM pixels%02d[] = {\n    ", fileIndex));
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y ++){
                int pos = getPosition(palette, fastRGB.getRGBbyt(x,y));
                pos = pos << bitNumb;
                b += pos;
                //b += (byte) (getPosition(palette, fastRGB.getRGBbyt(x,y)) << bitNumb);
                pixelNum++;
                bitNumb++;
                if (bitNumb == 8) {
                    column++;
                    if (column > 8) {
                        outData.append("\n    ");
                        column = 0;
                    }
                    outData.append(String.format(" %3d", b & 0b11111111));
                    b=0;
                    bitNumb = 0;
                    if (pixelNum < resolution)
                        outData.append(",");
                }
            }
        }
        if (bitNumb != 0){
            column++;
            if (column > height)
                outData.append("\n");
            outData.append(String.format(" %3d", b & 0b11111111));
        }
        outData.append(" };\n\n");
    }


    private static void  setPalette(StringBuilder outData, Byte[][] palette, Integer fileIndex) {
        outData.append(String.format("const uint8_t PROGMEM palette%02d[][3] = {", fileIndex));
        for (int i = 0; ; ) {
            byte rgb[] = new byte[]{
                    palette[i][0],
                    palette[i][1],
                    palette[i][2]};
            outData.append("\n\t{");
            outData.append(String.format(" %3d,", rgb[0] & 0b11111111))
                    .append(String.format(" %3d,", rgb[1] & 0b11111111))
                    .append(String.format(" %3d }", rgb[2] & 0b11111111));
            i++;
            if (i >= palette.length)
                break;
            outData.append(",");
        }
        outData.append("};\n\n");

    }

    private static int getPosition (Byte[][] palette, byte[] color){
        int i = 0;
        for (; i < palette.length; i++) {
            if(palette[i][0] == color[0]
                    && palette[i][1] == color[1]
                    && palette[i][2] == color[2])
                break;
        }
        return i;
    }

}
