package com.rmicro.printersdk.util;

/**
 * CPCL命令封装
 */


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.rmicro.printersdk.BluetoothSdkManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class PrinterHelper implements Serializable {
    private static final String TAG = PrinterHelper.class.getSimpleName();
    public static String LanguageEncode = "gb2312";
    public static int BetweenWriteAndReadDelay = 1500;
    private static boolean m = false;
    private static int A = 2;
    public static String TEXT = "T";
    public static String TEXT_Anti_White = "TR";
    public static String TEXT90 = "T90";
    public static String TEXT180 = "T180";
    public static String TEXT270 = "T270";
    public static String CONCAT = "CONCAT";
    public static String VCONCAT = "VCONCAT";
    public static String CENTER = "CENTER";
    public static String LEFT = "LEFT";
    public static String RIGHT = "RIGHT";
    //条码方向
    public static String BARCODE = "BARCODE";
    public static String VBARCODE = "VBARCODE";
    //条码类型
    public static String UPCA = "UPCA";
    public static String UPCA2 = "UPCA2";
    public static String UPCA5 = "UPCA5";
    public static String UPCE = "UPCE";
    public static String UPCE2 = "UPCE2";
    public static String UPCE5 = "UPCE5";
    public static String EAN13 = "EAN13";
    public static String EAN132 = "EAN132";
    public static String EAN135 = "EAN135";
    public static String EAN8 = "EAN8";
    public static String EAN82 = "EAN82";
    public static String EAN85 = "EAN85";
    public static String code39 = "39";
    public static String code39C = "39C";
    public static String F39 = "F39";
    public static String F39C = "F39C";
    public static String code93 = "93";
    public static String I2OF5 = "I2OF5";
    public static String I2OF5C = "I2OF5C";
    public static String I2OF5G = "I2OF5G";
    public static String code128 = "128";
    public static String UCCEAN128 = "UCCEAN128";
    public static String CODABAR = "CODABAR";
    public static String CODABAR16 = "CODABAR16";
    public static String MSI = "MSI";
    public static String MSI10 = "MSI10";
    public static String MSI1010 = "MSI1010";
    public static String MSI1110 = "MSI1110";
    public static String POSTNET = "POSTNET";
    public static String FIM = "FIM";
    //制图命令
    public static String EXPANDED_GRAPHICS = "EG";
    public static String VEXPANDED_GRAPHICS = "VEG";
    public static String COMPRESSED_GRAPHICS = "CG";
    public static String VCOMPRESSED_GRAPHICS = "VCG";
    public static String ENDSTATUS = "CC";

    //###########################YP20/E100 新打印机 控制指令######################//
    /*
    * 注：app与标签机的数据流才用软件流控方式，即：app收到0x11 表示打印机空闲可以发送数据，app收到0x13表示打印机忙不可以发送数据
    * */
    //状态查询指令
    public static final byte[] GET_PRINTER_STATUS = getByteArray(0x1B, 0x06);
    //打印参数查询
    public static final byte[] GET_PRINTER_PARAM = getByteArray(0x1B, 0x17);
    //打印型号查询
    public static final byte[] GET_PRINTER_MODEL = getByteArray(0x1B, 0x18);
    //设置打印浓度 命令头  1C 21 n    0<n<16；
    public static final byte[] SET_PRINTER_CHROMA = getByteArray(0x1C, 0x21);
    //设置打印纸张类型 命令头  1C 25 n    n=0:连续纸  n=1:定位孔  n=2:间隙纸  n=3: 黑标纸  n=4：线缆标签
    public static final byte[] SET_PRINTER_PAGE_TYPE = getByteArray(0x1C, 0x25);
    //设置打印规格  HEX:		1B  29  L0 L1 wL wH  hL  hH  m
    public static final byte[] SET_PRINTER_CONFIG = getByteArray(0x1B, 0x29);
    //图形打印命令1  十六进制码	1B 72 0 0 x 0 yL yH d1...dk
    public static final byte[] SET_PRINTER_IMAGE = getByteArray(0x1B, 0x72, 0x00, 0x00);
    //图形打印命令2  十六进制码	1B 19 m n k d1...dk
    public static final byte[] SET_PRINTER_IMAGE2 = getByteArray(0x1B, 0x19);
    //走纸命令  1B 4A m  m表示走纸点行数   0 ≤ m ≤ 255
    public static final byte[] SET_PRINTER_PAGE_RUN = getByteArray(0x1B, 0x4A);
    //走纸命令  1B 34 m  n 垂直移动 m=0 表示打印区域上移  m=1 表示打印区域下移
    public static final byte[] SET_PRINTER_OFFSET_V = getByteArray(0x1B, 0x34);
    //走纸命令  1B 35 m  n 水平移动 m=0 表示打印区域左移  m=1 表示打印区域右移
    public static final byte[] SET_PRINTER_OFFSET_H = getByteArray(0x1B, 0x35);
    //走纸命令  走到下个标签  十六进制码	1B 0C
    public static final byte[] SET_PRINTER_PAGE_RUN_NEXT = getByteArray(0x1B, 0x69);
    //打印字体／图片行间距 命令头
    public static final byte[] PRINT_LINESPACE = getByteArray(0x1B, 0x31);
    public static final byte[] SET_PRINTER_FOR_ANDROID = getByteArray(0x1B, 0x33);

    // 打印并回车
    public static final byte[] ENTER_PRINT = getByteArray(0x0D);
    // 打印并换行
    public static final byte[] WRAP_PRINT = getByteArray(0x0A);
    // 打印图片m  1,8点单倍宽
    public static final byte[] IMAGECMD = getByteArray(0x00, 0x1B, 0x2A, 1);
    // 打印图片m
    public static final byte[] IMAGECMD24 = getByteArray(0x00, 0x1B, 0x2A, 0x21);

    public static final int BLACK = 0xff000000;
    public static final int WITER = 0xffffffff;


    public static final byte[] getByteArray(int... array) {
        byte[] bytes = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bytes[i] = (byte) array[i];
        }
        return bytes;
    }
    public static void setPrinterForAndroid() throws Exception {
        WriteData(SET_PRINTER_FOR_ANDROID);
    }

    //打印字体／图片行间距设置 0x00～0xFF  打印图片设置为0x0  打印文字参考设置为0x5
    public static void setPrinterSpace(int val) throws Exception {
        byte[] PRINT_LINESPACE_SET = new byte[3];
        System.arraycopy(PRINT_LINESPACE, 0, PRINT_LINESPACE_SET, 0, PRINT_LINESPACE.length);
        PRINT_LINESPACE_SET[2] = (byte)val;
        WriteData(PRINT_LINESPACE_SET);
    }

    //打印字体／图片 深度以及清晰度设置0x0~0x0F  <0--16>
    public static void setPrinterChroma(int val) throws Exception {
        byte[] PRINT_CHROMA_SET = new byte[3];
        System.arraycopy(SET_PRINTER_CHROMA, 0, PRINT_CHROMA_SET, 0, SET_PRINTER_CHROMA.length);
        PRINT_CHROMA_SET[2] = (byte)val;
        WriteData(PRINT_CHROMA_SET);
    }

    //设置打印纸张类型 命令头  1C 25 n    n=0:连续纸  n=1:定位孔  n=2:间隙纸  n=3: 黑标纸  n=4：线缆标签
    public static void setPrinterPageType(int val) throws Exception {
        byte[] PRINT_PAGE_TYPE_SET = new byte[3];
        System.arraycopy(SET_PRINTER_PAGE_TYPE, 0, PRINT_PAGE_TYPE_SET, 0, SET_PRINTER_PAGE_TYPE.length);
        PRINT_PAGE_TYPE_SET[2] = (byte)val;
        WriteData(PRINT_PAGE_TYPE_SET);
    }

    /*
    * HEX:		1B  29  L0 L1 wL wH  hL  hH  m
        打印点行数：w=( L0 + L1 × 256)，表示当前打印标签数据总共点行数
        纸张宽度：w=( wL + wH × 256)
        纸张高度或长度：h=( hL + hH × 256)
        如果h为0表示当前的纸张不限定打印长度
        m表示纸张间缝隙或打孔纸孔直径（预切割标签纸，打孔纸独有），其他类型纸张默认为0。
        高度和宽度设置的单位为mm。
        精度为0.1mm
    * */
    //public static void setPrinterLabelParam(int l0,int l1,int wL,int wH,int hL, int hH,int m) throws Exception {
    /*
    * HEX:		1B  29  L0 L1 wL wH  hL  hH  m
    * width = 标签实际宽度/长度
    * height = 标签实际高度
      高度和宽度设置的单位为mm。
      精度为0.1mm
    * */
    public static void setPrinterLabelParam(int width,int height,int m) throws Exception {
        byte[] PRINT_LABEL_PARAM_SET = new byte[9];
        int l0;
        int l1;
        int wL;
        int wH;
        int hL;
        int hH;

        if(width  >= 256) {
            wL = (width) % 256;
            wH = (width) / 256;
        }else{
            wL = (width);
            wH = 0;
        }

        if(height >= 256) {
            hL = (height) % 256;
            hH = (height) / 256;
        }else{
            hL = (height);
            hH = 0;
        }

        if((width * height)/10 >= 256){
            l0 = ((width * height)/10) % 256;
            l1 = ((width * height)/10) / 256;
        }else{
            l0 = ((width * height)/10);
            l1 = 0;
        }

        System.arraycopy(SET_PRINTER_CONFIG, 0, PRINT_LABEL_PARAM_SET, 0, SET_PRINTER_CONFIG.length);
        PRINT_LABEL_PARAM_SET[2] = (byte)l0;
        PRINT_LABEL_PARAM_SET[3] = (byte)l1;
        PRINT_LABEL_PARAM_SET[4] = (byte)wL;
        PRINT_LABEL_PARAM_SET[5] = (byte)wH;
        PRINT_LABEL_PARAM_SET[6] = (byte)hL;
        PRINT_LABEL_PARAM_SET[7] = (byte)hH;
        PRINT_LABEL_PARAM_SET[8] = (byte)m;
        WriteData(PRINT_LABEL_PARAM_SET);
    }

    // 打印机走纸设置 ： 0 ≤ m ≤ 255
    public static void setPrinterPageRun(int val) throws Exception {
        byte[] PRINT_PAGE_RUN_SET = new byte[3];
        System.arraycopy(SET_PRINTER_PAGE_RUN, 0, PRINT_PAGE_RUN_SET, 0, SET_PRINTER_PAGE_RUN.length);
        PRINT_PAGE_RUN_SET[2] = (byte)val;
        WriteData(PRINT_PAGE_RUN_SET);
    }
    //走纸命令  1B 34 m  n 垂直移动 m=0 表示打印区域上移  m=1 表示打印区域下移
    public static void setSetPrinterOffsetV(int direction,int val) throws Exception {
        byte[] PRINTER_OFFSET_V_SET = new byte[4];
        System.arraycopy(SET_PRINTER_OFFSET_V, 0, PRINTER_OFFSET_V_SET, 0, SET_PRINTER_OFFSET_V.length);
        PRINTER_OFFSET_V_SET[2] = (byte)direction;
        PRINTER_OFFSET_V_SET[3] = (byte)val;
        WriteData(PRINTER_OFFSET_V_SET);
    }

    //走纸命令  1B 35 m  n 水平移动 m=0 表示打印区域左移  m=1 表示打印区域右移
    public static void setSetPrinterOffsetH(int direction,int val) throws Exception {
        byte[] PRINTER_OFFSET_H_SET = new byte[4];
        System.arraycopy(SET_PRINTER_OFFSET_H, 0, PRINTER_OFFSET_H_SET, 0, SET_PRINTER_OFFSET_H.length);
        PRINTER_OFFSET_H_SET[2] = (byte)direction;
        PRINTER_OFFSET_H_SET[3] = (byte)val;
        WriteData(PRINTER_OFFSET_H_SET);
    }

    // 打印机走纸-->走到下一个标签纸
    public static void setPrinterPageRunNext() throws Exception {
        WriteData(SET_PRINTER_PAGE_RUN_NEXT);
    }

    /**
     * 获取打印机状态
     * 0：打印机有纸。 1：打印机缺纸。 2：打印机上盖关闭。 3：打印机上盖开启。
     * 4：打印机切刀正常。5：打印机切刀异常。6：打印机切刀抬起。7：打印机切刀按下。
     * 8: 纸张类型正常。 9：纸张类型错误。10：运行正常。11：运行异常。其他：保留
     */
    public static int getRDPrinterStatus() throws Exception {
        int var0 = -1;
        int status = -1;
        byte[] var1 = new byte[4];
        WriteData(GET_PRINTER_STATUS);
        var1 = ReadData(3);
        Log.d(TAG, "var1 ：" + bytesToHexString(var1));
        if (var1 != null ){

            if(var1[0]== 0x11 && var1[1]== 0x1B && var1[2] == 0x06) {
                var0 = var1[3] & 255;
                if ((int) (var0 & 0x01) == 0)
                    status = 0;
                if ((int) (var0 & 0x01) == 1)
                    status = 1;
                if ((int) (var0 & 0x02) == 0)
                    status = 2;
                if ((int) (var0 & 0x02) == 2)
                    status = 3;
                if ((int) (var0 & 0x04) == 0)
                    status = 4;
                if ((int) (var0 & 0x04) == 4)
                    status = 5;
                if ((int) (var0 & 0x08) == 0)
                    status = 6;
                if ((int) (var0 & 0x08) == 8)
                    status = 7;
                if ((int) (var0 & 0x10) == 0)
                    status = 8;
                if ((int) (var0 & 0x10) == 16)
                    status = 9;
                if ((int) (var0 & 0x20) == 0)
                    status = 10;
                if ((int) (var0 & 0x20) == 32)
                    status = 11;

            }else if(var1[0]== 0x1B && var1[1] == 0x06) {
                var0 = var1[2] & 255;
                if ((int) (var0 & 0x01) == 0)
                    status = 0;
                if ((int) (var0 & 0x01) == 1)
                    status = 1;
                if ((int) (var0 & 0x02) == 0)
                    status = 2;
                if ((int) (var0 & 0x02) == 2)
                    status = 3;
                if ((int) (var0 & 0x04) == 0)
                    status = 4;
                if ((int) (var0 & 0x04) == 4)
                    status = 5;
                if ((int) (var0 & 0x08) == 0)
                    status = 6;
                if ((int) (var0 & 0x08) == 8)
                    status = 7;
                if ((int) (var0 & 0x10) == 0)
                    status = 8;
                if ((int) (var0 & 0x10) == 16)
                    status = 9;
                if ((int) (var0 & 0x20) == 0)
                    status = 10;
                if ((int) (var0 & 0x20) == 32)
                    status = 11;
            }

            Log.d(TAG, "获取打印机状态：" + status);
            return status;
        } else {
            Log.d(TAG, "获取打印机状态失败");
            return -1;
        }
    }

    /*
    * 获取打印机型号
    * */
    public static String getRDPrinterModel() throws Exception {
        String model = null;
        int length = -1;
        byte[] var1 = new byte[32];
        WriteData(GET_PRINTER_MODEL);
        var1 = ReadData(3);
        Log.d(TAG, "var1 ：" + bytesToHexString(var1));
        if (var1 != null && var1[0]== 0x1B && var1[1] == 0x18) {
            length = var1[2]& 255;
            if(length > 0) {
                byte[] version_data = new byte[length];
                System.arraycopy(var1, 3, version_data, 0, length);
                model = Byte2Ascii(version_data);
            }
        }
        return model;
    }

    private static String Byte2Ascii(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length);
        for (int i = 0; i < data.length; ++ i) {
            if (data[i] < 0) throw new IllegalArgumentException();
            sb.append((char) data[i]);
        }
        return sb.toString();
    }

    //###################################################################//

    /**
     * 37 自检页
     *
     * @throws Exception
     */
    public static void setSelf() throws Exception {
        byte[] var0 = new byte[]{29, 40, 65, 2, 0, 0, 2};
        WriteData(var0);
    }

    /**
     * 36 设置打印机纸张类型(只适用于 HM-A300 机型)
     *
     * @param page 0：连续纸。 1：标签纸。 2：后黑标。 3：前黑标。 4：三寸黑标。 5：两寸黑标
     * @throws Exception
     */
    public static void papertype_CPCL(int page) throws Exception {
        byte[] var1 = new byte[]{27, 99, 48, (byte) page};
        WriteData(var1);
    }

    /**
     * 1 页标签开始指令
     *
     * @param offset     此值使所有字段将水平偏移指定的单位数量。
     * @param horizontal 水平方向的 dpi。
     * @param vertical   垂直方向的 dpi
     * @param height     整个标签的高度
     * @param qty        打印的次数
     * @return
     * @throws Exception
     */
    public static int printAreaSize(String offset, String horizontal, String vertical, String height, String qty) throws Exception {
        byte[] var5 = ("! " + offset + " " + horizontal + " " + vertical + " " + height + " " + qty + "\r\n").getBytes(LanguageEncode);
        return WriteData(var5);
    }

    /**
     * 2 页标签结束指令
     * 注意：行模式不能用
     *
     * @return
     * @throws Exception
     */
    public static int Print() throws Exception {
        byte[] var0 = "PRINT\r\n".getBytes(LanguageEncode);
        return WriteData(var0);
    }

    /**
     * 38 旋转 180 度打印
     * 注意：它不能与 Print()同时使用。
     *
     * @return
     * @throws Exception
     */
    public static int PoPrint() throws Exception {
        byte[] var0 = "POPRINT\r\n".getBytes(LanguageEncode);
        return WriteData(var0);
    }

    /**
     * 3 编码指令
     *
     * @param code 编码
     * @return
     * @throws Exception
     */
    public static int Encoding(String code) throws Exception {
        byte[] var1 = ("ENCODING " + code + "\r\n").getBytes(LanguageEncode);
        return WriteData(var1);
    }


    public static int BackFeed(String var0) throws Exception {
        byte[] var1 = ("BACKFEED " + var0 + "\r\n").getBytes(LanguageEncode);
        return WriteData(var1);
    }

    /**
     * 4 走纸到下一张标签
     *
     * @return
     * @throws Exception
     */
    public static int Form() throws Exception {
        byte[] var0 = "FORM\r\n".getBytes(LanguageEncode);
        return WriteData(var0);
    }

    /**
     * 5 注释
     *
     * @param note 注释的内容
     * @return
     * @throws Exception
     */
    public static int Note(String note) throws Exception {
        byte[] var1 = ("; " + note + "\r\n").getBytes(LanguageEncode);
        return WriteData(var1);
    }

    /**
     * 6 终止指令
     *
     * @return
     * @throws Exception
     */
    public static int Abort() throws Exception {
        byte[] var0 = "END\r\n".getBytes(LanguageEncode);
        return WriteData(var0);
    }

    /**
     * 7 文本打印
     *
     * @param command 文字的方向，总的有四种:1.PrinterHelper.TEXT：水平。 2.PrinterHelper.TEXT90：逆时针旋转 90 度。 3.PrinterHelper.TEXT180：逆时针旋转 180 度。 4.PrinterHelper.TEXT270：逆时针旋转 270 度
     * @param font    字体点阵大小
     * @param size    字体大小。（该功能被屏蔽统一参数传 0）
     * @param x       起始点的横坐标
     * @param y       起始点的纵坐标
     * @param data    文本数据
     * @return
     * @throws Exception
     */
    public static int Text(String command, String font, String size, String x, String y, String data) throws Exception {
        byte[] var6 = (command + " " + font + " " + size + " " + x + " " + y + " " + data + "\r\n").getBytes(LanguageEncode);
        return WriteData(var6);
    }

    public static int Concat(String var0, String var1, String var2, String[] var3) throws Exception {
        var0 = var0 + " " + var1 + " " + var2 + "\r\n";

        for (int i = 0; i < var3.length; ++i) {
            var0 = var0 + var3[i] + " ";
            if ((i + 1) % 4 == 0) {
                var0 = var0 + "\r\n";
            }
        }

        byte[] var5 = (var0 + "ENDCONCAT\r\n").getBytes(LanguageEncode);
        return WriteData(var5);
    }

    /**
     * 多行打印指令
     *
     * @param var0
     * @return
     * @throws Exception
     */
    public static int ML(String var0) throws Exception {
        byte[] var1 = ("ML " + var0 + "\r\n").getBytes(LanguageEncode);
        return WriteData(var1);
    }

    /**
     * 结束多行打印指令
     *
     * @return
     * @throws Exception
     */
    public static int ENDML() throws Exception {
        byte[] var0 = "ENDML\r\n".getBytes(LanguageEncode);
        return WriteData(var0);
    }

    /**
     * 8 计数
     *
     * @param ml 下次加减的数值
     * @return
     * @throws Exception
     */
    public static int Count(String ml) throws Exception {
        byte[] var1 = ("COUNT " + ml + "\r\n").getBytes(LanguageEncode);
        return WriteData(var1);
    }

    /**
     * 9 设置字符宽高放大倍数
     *
     * @param width  宽的放大倍数
     * @param height 高的放大倍数
     * @return
     * @throws Exception
     */
    public static int SetMag(String width, String height) throws Exception {
        byte[] var2 = ("SETMAG " + width + " " + height + "\r\n").getBytes(LanguageEncode);
        return WriteData(var2);
    }

    /**
     * 10 对齐方式
     *
     * @param align 对齐方式总的有三种:1.PrinterHelper.CENTER：居中 2.PrinterHelper.LEFT：左对齐 3.PrinterHelper.RIGHT：右对齐
     * @return
     * @throws Exception
     */
    public static int Align(String align) throws Exception {
        byte[] var1 = (align + "\r\n").getBytes(LanguageEncode);
        return WriteData(var1);
    }

    /**
     * 11 条码
     *
     * @param command   条码方向:1.PrinterHelper.BARCODE：水平方向 2.PrinterHelper.VBARCODE：垂直方向
     * @param type      条码类型
     * @param width     窄条的单位宽度
     * @param ratio     宽条窄条的比例
     * @param height    条码高度
     * @param x         条码的起始横坐标
     * @param y         条码的起始纵坐标
     * @param undertext 条码下方的数据是否可见。ture：可见，false：不可见
     * @param number    字体的类型
     * @param size      字体的大小
     * @param offset    条码与文字间的距离
     * @param data      条码数据
     * @return
     * @throws Exception
     */
    public static int Barcode(String command, String type, String width, String ratio, String height, String x, String y, boolean undertext, String number, String size, String offset, String data) throws Exception {
        command = command + " " + type + " " + width + " " + ratio + " " + height + " " + x + " " + y + " " + data + "\r\n";
        if (undertext) {
            command = "BARCODE-TEXT " + number + " " + size + " " + offset + "\r\n" + command + "BARCODE-TEXT OFF\r\n";
        }

        byte[] var12 = command.getBytes(LanguageEncode);
        return WriteData(var12);
    }

    /**
     * 12 打印二维码
     *
     * @param command 打印方向：1.PrinterHelper.BARCODE：水平方向  2.PrinterHelper.VBARCODE：垂直方向
     * @param x       二维码的起始横坐标
     * @param y       二维码的起始纵坐标
     * @param m       QR 的类型：类型 1 和类型 2；类型 2 是增加了个别的符号，提供了而外的功能
     * @param u       单位宽度/模块的单元高度。范围是 1 到 32 默认为 6
     * @param data    二维码的数据
     * @return
     * @throws Exception
     */
    public static int PrintQR(String command, String x, String y, String m, String u, String data) throws Exception {
        byte[] var6 = (command + " QR " + x + " " + y + " M " + m + " U " + u + "\r\nMA," + data + "\r\nENDQR\r\n").getBytes(LanguageEncode);
        return WriteData(var6);
    }

    /**
     * 13 打印 PDF417 码
     *
     * @param command 打印方向：1.PrinterHelper.BARCODE：水平方向 2.PrinterHelper.VBARCODE：垂直方向
     * @param x       二维码的起始横坐标
     * @param y       二维码的起始纵坐标
     * @param xd      最窄元素的单位宽度。范围是 1 到 32，默认为 2
     * @param yd      最窄元素的单位高度。范围是 1 到 32，默认值是 6
     * @param c       使用的列数。数据列不包括启动/停止字符和左/右指标。范围为 1 到 30;默认值是 3
     * @param s       安全级别表示要检测到的错误的最大金额和/或校正。范围为 0 到 8;默认值是 1
     * @param data    PDF417 码的数据
     * @return
     * @throws Exception
     */
    public static int PrintPDF417(String command, String x, String y, String xd, String yd, String c, String s, String data) throws Exception {
        byte[] var8 = (command + " PDF-417 " + x + " " + y + " XD " + xd + " YD " + yd + " C " + c + " S " + s + "\r\n" + data + "\r\nENDPDF\r\n").getBytes(LanguageEncode);
        return WriteData(var8);
    }

    /**
     * 14 画矩形框
     *
     * @param x0    左上角的 X 坐标
     * @param y0    左上角的 Y 坐标
     * @param x1    右下角的 X 坐标
     * @param y1    右下角的 Y 坐标
     * @param width 线条的单位宽度
     * @return
     * @throws Exception
     */
    public static int Box(String x0, String y0, String x1, String y1, String width) throws Exception {
        byte[] var5 = ("BOX " + x0 + " " + y0 + " " + x1 + " " + y1 + " " + width + "\r\n").getBytes(LanguageEncode);
        return WriteData(var5);
    }

    /**
     * 15 画直线
     *
     * @param x0    起始的 X 坐标
     * @param y0    起始的 Y 坐标
     * @param x1    结尾的 X 坐标
     * @param y1    结尾的 Y 坐标
     * @param width 线条的单位宽度
     * @return
     * @throws Exception
     */
    public static int Line(String x0, String y0, String x1, String y1, String width) throws Exception {
        byte[] var5 = ("LINE " + x0 + " " + y0 + " " + x1 + " " + y1 + " " + width + "\r\n").getBytes(LanguageEncode);
        return WriteData(var5);
    }

    /**
     * 16 反白框
     *
     * @param x0    起始的 X 坐标
     * @param y0    起始的 Y 坐标
     * @param x1    结尾的 X 坐标
     * @param y1    结尾的 Y 坐标
     * @param width 反白框的宽度
     * @return
     * @throws Exception
     */
    public static int InverseLine(String x0, String y0, String x1, String y1, String width) throws Exception {
        byte[] var5 = ("INVERSE-LINE " + x0 + " " + y0 + " " + x1 + " " + y1 + " " + width + "\r\n").getBytes(LanguageEncode);
        return WriteData(var5);
    }

    /**
     * 17 打印图片
     *
     * @param x   图片起始的 x 坐标
     * @param y   图片起始的 y 坐标
     * @param url 图片的路径
     * @return
     * @throws Exception
     */
    public static int Expanded(String x, String y, String url) throws Exception {
        Bitmap var3;
        int var4;
        if ((var3 = BitmapFactory.decodeFile(url)).getWidth() % 8 == 0) {
            var4 = var3.getWidth() / 8;
        } else {
            var4 = var3.getWidth() / 8 + 1;
        }

        int var9 = var3.getHeight();
        int var5;
        if (var4 > 999 | var9 > '\uffff') {
            var5 = -1;
        } else {
            Bitmap var7;
            (var7 = BitmapFactory.decodeFile(url)).getWidth();
            var7.getHeight();
            byte[] var8 = a(var7, (byte) 0);
            x = "CG " + var4 + " " + var9 + " " + x + " " + y + " ";
            y = "\r\n";
            byte[] var6 = x.getBytes(LanguageEncode);
            WriteData(var6);
            WriteData(var8);
            var5 = WriteData(y.getBytes(LanguageEncode));
        }

        return var5;
    }

    /**
     * 17 打印图片
     *
     * @param x    图片起始的 x 坐标
     * @param y    图片起始的 y 坐标
     * @param bmap 图片的 Bitmap 的对象
     * @param type 图片算法。0：二值算法；1：半色调算法
     * @return
     * @throws Exception
     */
    public static int Expanded(String x, String y, Bitmap bmap, int type) throws Exception {
        int var4;
        if (bmap.getWidth() % 8 == 0) {
            var4 = bmap.getWidth() / 8;
        } else {
            var4 = bmap.getWidth() / 8 + 1;
        }

        int var5 = bmap.getHeight();
        int var6;
        if (var4 > 999 | var5 > '\uffff') {
            var6 = -1;
        } else {
            byte[] var8 = a(bmap, (byte) type);
            x = "CG " + var4 + " " + var5 + " " + x + " " + y + " ";
            y = "\r\n";
            byte[] var7 = x.getBytes(LanguageEncode);
            WriteData(var7);
            WriteData(var8);
            var6 = WriteData(y.getBytes(LanguageEncode));
        }

        return var6;
    }

    /**
     * 18 打印浓度
     *
     * @param contrast 浓度类型，总的有四种： 1.默认 =0  2.中 =1 3.黑暗 =2 4.非常深 =3
     * @return
     * @throws Exception
     */
    public static int Contrast(String contrast) throws Exception {
        byte[] var1 = ("CONTRAST " + contrast + "\r\n").getBytes(LanguageEncode);
        return WriteData(var1);
    }

    /**
     * 19 打印速度
     *
     * @param speed 速度类型，总的有 5 种：从 0 到 5 越来越快；5 是理想状态的最快速度。
     * @return
     * @throws Exception
     */
    public static int Speed(String speed) throws Exception {
        byte[] var1 = ("SPEED " + speed + "\r\n").getBytes(LanguageEncode);
        return WriteData(var1);
    }

    /**
     * 20 设置字间距
     *
     * @param setsp 间距
     * @return
     * @throws Exception
     */
    public static int SetSp(String setsp) throws Exception {
        byte[] var1 = ("SETSP " + setsp + "\r\n").getBytes(LanguageEncode);
        return WriteData(var1);
    }

    /**
     * 21 走纸再打印
     *
     * @param prefeed 走纸的距离，单位：dot-lines
     * @return
     * @throws Exception
     */
    public static int Prefeed(String prefeed) throws Exception {
        byte[] var1 = ("PREFEED " + prefeed + "\r\n").getBytes(LanguageEncode);
        return WriteData(var1);
    }

    /**
     * 22 打印后走纸一定距离
     *
     * @param posfeed 走纸的距离，单位：dot-lines
     * @return
     * @throws Exception
     */
    public static int Postfeed(String posfeed) throws Exception {
        byte[] var1 = ("POSFEED " + posfeed + "\r\n").getBytes(LanguageEncode);
        return WriteData(var1);
    }

    /**
     * 23 编码设定
     *
     * @param country Country：国家名称
     * @return
     * @throws Exception
     */
    public static int Country(String country) throws Exception {
        byte[] var1 = ("COUNTRY " + country + "\r\n").getBytes(LanguageEncode);
        return WriteData(var1);
    }

    /**
     * 24 设置蜂鸣器鸣叫时间
     *
     * @param beep 蜂鸣声的持续时间，（1/8）秒为单位指定
     * @return
     * @throws Exception
     */
    public static int Beep(String beep) throws Exception {
        byte[] var1 = ("BEEP " + beep + "\r\n").getBytes(LanguageEncode);
        return WriteData(var1);
    }

    /**
     * 25 下划线
     *
     * @param ul UL ：true：添加下划线，false：取消下划线
     * @return
     * @throws Exception
     */
    public static int Underline(boolean ul) throws Exception {
        String var1;
        if (ul) {
            var1 = "UNDERLINE ON\r\n";
        } else {
            var1 = "UNDERLINE OFF\r\n";
        }

        byte[] var2 = var1.getBytes(LanguageEncode);
        return WriteData(var2);
    }

    /**
     * 26 打印一页标签后延时
     *
     * @param wait Wait：延时的单位是：1/8 秒
     * @return
     * @throws Exception
     */
    public static int Wait(String wait) throws Exception {
        wait = "WAIT " + wait + "\r\n";
        return WriteData(wait.getBytes(LanguageEncode));
    }

    /**
     * 27 打印宽度
     *
     * @param pw 指定页面宽度
     * @return
     * @throws Exception
     */
    public static int PageWidth(String pw) throws Exception {
        byte[] var1 = ("PW " + pw + "\r\n").getBytes(LanguageEncode);
        return WriteData(var1);
    }

    /**
     * 28 行模式下设置行间距
     *
     * @param sf 间距
     *           注意：与 SETLP 效果相同，若 2 个同时存在，则以最后设置的参数值为准
     * @return
     * @throws Exception
     */
    public static int Setlf(String sf) throws Exception {
        byte[] var1 = ("! U! SETLP " + sf + "\r\n").getBytes(LanguageEncode);
        return WriteData(var1);
    }

    /**
     * 29 设置字体与字符大小及行间距
     *
     * @param font    字体
     * @param size    大小
     * @param spacing 字体的高度
     * @return
     * @throws Exception
     */
    public static int Setlp(String font, String size, String spacing) throws Exception {
        byte[] var3 = ("! U1 SETLP " + font + " " + size + " " + spacing + "\r\n").getBytes(LanguageEncode);
        return WriteData(var3);
    }

    /**
     * 3 行打印模式字体加粗
     *
     * @param bold 加粗倍数
     * @return
     * @throws Exception
     */
    public static int RowSetBold(String bold) throws Exception {
        byte[] var1 = ("! U1 SETBOLD " + bold + "\r\n").getBytes(LanguageEncode);
        return WriteData(var1);
    }

    /**
     * 4 设置行模式的 X 坐标
     * 注意：必须放在 Setlp 函数之前
     *
     * @param x x 坐标
     * @return
     * @throws Exception
     */
    public static int RowSetX(String x) throws Exception {
        byte[] var1 = ("! U1 LMARGIN " + x + "\r\n").getBytes(LanguageEncode);
        return WriteData(var1);
    }

    /**
     * 打印CPCL文件内容字符串,参看申通运单实现过程
     *
     * @param strFileContent
     * @return
     * @throws Exception
     */
    public static int printText(String strFileContent) throws Exception {
        return WriteData(strFileContent.getBytes(LanguageEncode));
    }

    /**
     * 通过CPCL文件路径和占位符HashMap实现打印
     *
     * @param path    CPCL文件路径,一变量多用,for中用于保存CPCL文件内的命令字符串,完成替换
     * @param hashMap 变量占位符HashMap
     * @return
     * @throws Exception
     */
    public static int printSingleInterface(String path, HashMap<String, String> hashMap) throws Exception {
        if (path == null && hashMap == null) {
            return 1;
        } else {
            Iterator var2 = hashMap.keySet().iterator();
            File var4 = new File(path);
            FileInputStream var5 = new FileInputStream(var4);

            String var3;
            for (path = new String(getByteFromInputStream(var5), "utf-8"); var2.hasNext(); path = path.replace(var3, hashMap.get(var3))) {
                var3 = (String) var2.next();
            }

            return WriteData(path.getBytes("GBK"));
        }
    }

    /**
     * 通过CPCL文件流内置占位符实现打印功能
     *
     * @param inputStream CPCL文件流
     * @param hashMap     CPCL文件内变量占位符的HashMap
     * @return
     * @throws Exception
     */
    public static int printSingleInterface(InputStream inputStream, HashMap<String, String> hashMap) throws Exception {
        if (inputStream != null && hashMap != null) {
            Iterator var2 = hashMap.keySet().iterator();

            String var3;
            String var4;
            for (var4 = new String(getByteFromInputStream(inputStream), "utf-8"); var2.hasNext(); var4 = var4.replace(var3, hashMap.get(var3))) {
                var3 = (String) var2.next();
            }

            return PrintData(var4);
        } else {
            return 1;
        }
    }

    private static byte[] getByteFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int var2;
        while ((var2 = inputStream.read()) != -1) {
            byteArrayOutputStream.write(var2);
        }

        byte[] var3 = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return var3;
    }

    private static byte[] a(Bitmap bitmap, byte var1) throws Exception {
        PrinterDataCore var2;
        (var2 = new PrinterDataCore()).HalftoneMode = var1;
        var2.ScaleMode = 0;
        return var2.PrintDataFormat(bitmap);
    }

    /**
     * 2 行打印模式打印文本
     *
     * @param str 文本内容（以\r\n 结尾）
     * @return
     * @throws Exception
     */
    public static int PrintData(String str) throws Exception {
        return WriteData(str.getBytes(LanguageEncode));
    }

    /**
     * 32 字体加粗
     *
     * @param bold 加粗系数（范围：1-5）
     *             注意：使用完后记得关闭加粗。
     * @return
     * @throws Exception
     */
    public static int SetBold(String bold) throws Exception {
        return WriteData(("SETBOLD " + bold + "\r\n").getBytes(LanguageEncode));
    }

    /**
     * 34 文字自动换行
     *
     * @param x        文字的起始的 x 坐标
     * @param y        文字的起始的 y 坐标
     * @param width    一行打印的宽度（单位：8=1mm）
     * @param size     字体大小
     * @param isbole   加粗
     * @param isdouble 放大两倍字体
     * @param str      要打印的文本
     * @return
     * @throws Exception
     */
    public static int AutLine(String x, String y, int width, int size, boolean isbole, boolean isdouble, String str) throws Exception {
        byte var7 = 24;
        String var8 = "";
        switch (size) {
            case 1:
                var7 = 9;
                break;
            case 2:
                var7 = 8;
                break;
            case 3:
                var7 = 16;
                break;
            case 4:
                var7 = 32;
                break;
            case 8:
                var7 = 24;
                break;
            case 55:
                var7 = 16;
        }

        if (width < var7) {
            return 0;
        } else {
            if (isdouble) {
                width = width / (var7 * 2) * 2;
            } else {
                width = width / var7 * 2;
            }

            String[] var9 = new String[str.length()];
            ArrayList var10;
            (var10 = new ArrayList()).clear();
            int var11 = 0;
            String var12 = "";

            int var13;
            for (var13 = 0; var13 < str.length(); ++var13) {
                var9[var13] = "" + str.charAt(var13);
                var11 += var9[var13].getBytes("GBK").length;
                var12 = var12 + var9[var13];
                if (var11 > width) {
                    var10.add(var12);
                    var12 = "";
                    var11 = 0;
                }
            }

            var10.add(var12);

            for (var13 = 0; var13 < var10.size(); ++var13) {
                var8 = var8 + "TEXT " + size + " 0 " + x + " " + (Integer.valueOf(y).intValue() + var13 * var7) + " " + var10.get(var13) + "\r\n";
            }

            if (isdouble) {
                SetMag("2", "2");
            }

            if (isbole) {
                SetBold("1");
            }

            var13 = WriteData(var8.getBytes(LanguageEncode));
            if (isdouble) {
                SetMag("1", "1");
            }

            if (isbole) {
                SetBold("0");
            }

            return var13;
        }
    }

    /**
     * 35 文字在文本框内居中显示
     *
     * @param command 文字的方向，总的有两种：1.PrinterHelper.TEXT：水平 2.PrinterHelper.TEXT270：垂直
     * @param x       文本框起始的 x 坐标
     * @param y       文本框起始的 y 坐标
     * @param width   文本框的宽度（单位：8=1mm）
     * @param size    字体大小
     * @param str     要打印的文本
     * @return
     * @throws Exception
     */
    public static int AutCenter(String command, String x, String y, int width, int size, String str) throws Exception {
        byte var6 = 24;
        switch (size) {
            case 1:
                var6 = 3;
                break;
            case 2:
                var6 = 24;
                break;
            case 3:
                var6 = 16;
                break;
            case 4:
                var6 = 32;
                break;
            case 8:
                var6 = 24;
                break;
            case 55:
                var6 = 16;
        }

        if (command.equals(TEXT)) {
            width = (int) (((float) width - (float) str.getBytes("GBK").length * (float) var6 / 2.0F) / 2.0F + (float) Integer.valueOf(x).intValue());
            command = "TEXT " + size + " 0 " + width + " " + y + " " + str + "\r\n";
        } else {
            width = (int) (((float) width - (float) str.getBytes("GBK").length * (float) var6 / 2.0F) / 2.0F + (float) Integer.valueOf(y).intValue());
            command = command + " " + size + " 0 " + x + " " + width + " " + str + "\r\n";
        }

        return WriteData(command.getBytes(LanguageEncode));
    }

    /**
     * 7 文本打印
     *
     * @param command  文字的方向，总的有两种：1.PrinterHelper.TEXT：水平 2.PrinterHelper.TEXT270：垂直
     * @param font     字体点阵大小
     * @param x        起始点的横坐标
     * @param y        起始点的纵坐标
     * @param data     文本数据
     * @param n        字体的特效
     * @param Iscenter 是否居中
     * @param width    要居中的范围。（Iscenter=true 时才生效）单位：8=1mm
     * @return
     * @throws Exception
     */
    public static int PrintTextCPCL(String command, int font, String x, String y, String data, int n, boolean Iscenter, int width) throws Exception {
        if (font == 1) {
            font = 24;
        }

        boolean var9 = false;
        boolean var10 = false;
        boolean var11 = false;
        boolean var12 = false;
        if ((n & 1) == 1) {
            var9 = true;
        }

        if ((n & 2) == 2) {
            var10 = true;
        }

        if ((n & 4) == 4) {
            var11 = true;
        }

        if ((n & 8) == 8) {
            var12 = true;
        }

        if (var9) {
            SetBold("1");
        }

        if (var12) {
            SetMag("1", "2");
        }

        if (var11) {
            SetMag("2", "1");
        }

        if (var11 & var12) {
            SetMag("2", "2");
        }

        String var15 = "55";
        switch (font) {
            case 1:
                var15 = "1";
                break;
            case 16:
                var15 = "55";
                break;
            case 24:
                var15 = "8";
                break;
            case 32:
                var15 = "4";
        }

        if (Iscenter) {
            AutCenter(command, x, y, width, Integer.valueOf(var15).intValue(), data);
        } else {
            Text(command, var15, "0", x, y, data);
        }

        if (var12 | var11) {
            SetMag("1", "1");
        }

        if (var10) {
            float var13;
            if (command.equals(TEXT)) {
                if (var11) {
                    var13 = (float) Integer.valueOf(x).intValue() + (float) data.getBytes("GBK").length * (float) font;
                } else {
                    var13 = (float) Integer.valueOf(x).intValue() + (float) data.getBytes("GBK").length * (float) font / 2.0F;
                }

                if (var12) {
                    if (Iscenter) {
                        InverseLine(x, y, "" + width, y, "" + font * 2);
                    } else {
                        InverseLine(x, y, "" + var13, y, "" + font * 2);
                    }
                } else if (Iscenter) {
                    InverseLine(x, y, "" + width, y, "" + font);
                } else {
                    InverseLine(x, y, "" + var13, y, "" + font);
                }
            } else {
                if (var11) {
                    var13 = (float) Integer.valueOf(y).intValue() + (float) data.getBytes("GBK").length * (float) font;
                } else {
                    var13 = (float) Integer.valueOf(y).intValue() + (float) data.getBytes("GBK").length * (float) font / 2.0F;
                }

                int var14;
                if (var12) {
                    if (Iscenter) {
                        var14 = Integer.valueOf(x).intValue() - font * 2;
                        InverseLine("" + var14, y, "" + var14, "" + (width + Integer.valueOf(y).intValue()), "" + font * 2);
                    } else {
                        var14 = Integer.valueOf(x).intValue() - font * 2;
                        InverseLine("" + var14, y, "" + var14, "" + var13, "" + font * 2);
                    }
                } else if (Iscenter) {
                    var14 = Integer.valueOf(x).intValue() - font;
                    InverseLine("" + var14, y, "" + var14, "" + (width + Integer.valueOf(y).intValue()), "" + font);
                } else {
                    var14 = Integer.valueOf(x).intValue() - font;
                    InverseLine("" + var14, y, "" + var14, "" + var13, "" + font);
                }
            }
        }

        if (var9) {
            SetBold("0");
        }

        return 0;
    }

    /**
     * 30 发数据函数
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static int WriteData(byte[] bytes) throws Exception {
        BluetoothSdkManager.INSTANCE.print(bytes);
        return 0;
    }

    /**
     * 31 读数据函数
     *
     * @param time 超时时间
     * @return
     * @throws Exception
     */
    public static byte[] ReadData(int time) throws Exception {
        int count = 0;
        byte[] bytes = null;
        while (count < time * 10) {
            //如果bytes取到值,执行一次直接返回
            if ((bytes = BluetoothSdkManager.INSTANCE.ReadData()) != null) {
                count = time * 10 + 1;
            } else {
                //如果bytes取不到值,每隔100毫秒执行一次,共执行time秒
                Thread.sleep(100L);
                ++count;
            }
        }
        return bytes;
    }

    /**
     * 向bt中写入文件中的字节数组
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static boolean PrintBinaryFile(String path) throws Exception {
        byte[] bytes = getByteFromFile(path);
        WriteData(bytes);
        return true;
    }

    private static byte[] getByteFromFile(String path) {
        File file;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (file.exists() && file.isFile() && file.canRead()) {
            byte[] bytes = null;

            try {
                FileInputStream inputStream = new FileInputStream(file);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] var4 = new byte[1024];

                int len;
                while ((len = inputStream.read(var4)) > 0) {
                    outputStream.write(var4, 0, len);
                }

                bytes = outputStream.toByteArray();
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            } catch (Exception var6) {
                var6.printStackTrace();
            }

            return bytes;
        } else {
            return null;
        }
    }

    /**
     * 39 获取打印完成时状态开关
     * 注意:它需要跟 getEndStatus()配合使用，使用完记得关闭
     *
     * @param isopen
     * @throws Exception
     */
    public static void openEndStatic(boolean isopen) throws Exception {
        if (isopen) {
            WriteData(new byte[]{27, 27, 49, 1});
        } else {
            WriteData(new byte[]{27, 27, 49, 0});
        }
    }

    /**
     * 40 获取打印完成时状态
     *
     * @return
     * @throws Exception
     */
    public static int getEndStatus() throws Exception {
        int var1 = -1;
        byte[] var2 = null;
        var2 = ReadData(3);
        if (var2 != null) {
            String var3 = bytetohex(var2);
            Log.d(TAG, "打印完成时状态：" + var3);
            var1 = var3.lastIndexOf(ENDSTATUS);
            Log.d(TAG, "lastIndexOf：" + var1);
            return var2[var1 / 3 + 1];
        } else {
            Log.d(TAG, "获取打印完成时状态失败");
            return -1;
        }
    }

    /**
     * 33 获取打印机状态
     * 0：打印机准备就绪。 1：打印机打印中。 2：打印机缺纸。 6：打印机开盖。 其他：出错
     *
     * @return
     * @throws Exception
     */
    public static int getStatus() throws Exception {
        int var0 = -1;
        byte[] var1 = new byte[]{27, 104};
        int var2;
        WriteData(var1);
        var1 = ReadData(3);
        if (var1 != null) {
            var2 = (var1).length;
            var0 = var1[var2 - 1] & 255;
            Log.d(TAG, "获取打印机状态：" + var0);
            return var0;
        } else {
            Log.d(TAG, "获取打印机状态失败");
            return -1;
        }
    }

    /**
     * 获取打印机名称
     *
     * @return
     * @throws Exception
     */
    public static String getPrintName() throws Exception {
        String var0 = "";
        int var1 = -1;
        byte[] var2 = null;
        WriteData(new byte[]{29, 73, 67});
        var2 = ReadData(3);
        if (var2 != null) {
            String var3 = bytetohex(var2);
            Log.d(TAG, "获取打印机名称：" + var3);
            var1 = var3.lastIndexOf("5F");
            return (var0 = new String(var2)).substring(var1 + 1, var0.length() - 1);
        } else {
            Log.d(TAG, "获取打印机名称失败");
            return "";
        }
    }

    /**
     * 获取打印机版本
     *
     * @return
     * @throws Exception
     */
    public static String getPrintVersion() throws Exception {
        String var0 = "";
        int var1 = -1;
        WriteData(new byte[]{29, 73, 65});
        byte[] var2 = ReadData(3);
        if (var2 != null) {
            String var3 = bytetohex(var2);
            Log.d(TAG, "获取打印机版本:" + var3);
            var1 = var3.lastIndexOf("5F");
            return (var0 = new String(var2)).substring(var1 + 1, var0.length() - 1);
        } else {
            Log.d(TAG, "获取打印机版本失败");
            return "";
        }
    }

    /**
     * @return
     * @throws Exception
     */
    public static byte getPrintStatus() throws Exception {
        byte rc = (byte)0xff;
        WriteData(new byte[]{0x1B,0x06});//状态查询指令
        byte[] var3 = ReadData(3);
        if (var3 != null) {
            Log.d(TAG, "获取打印机状态：" + bytesToHexString(var3));
            byte status = var3[2];
            return status;
        } else {
            Log.d(TAG, "获取打印机状态失败");
            return rc;
        }
    }

    /**
     * @return
     * @throws Exception
     */
    public static String getPrintParam() throws Exception {
        int var1 = -1;
        String var2 = "";
        WriteData(new byte[]{0x1B,0x18});
        byte[] var3 = ReadData(3);
        if (var3 != null) {
            var2 = bytetohex(var3);
            Log.d(TAG, "获取打印机ID：" + var2);
            var1 = var2.lastIndexOf("22");
            return var2.substring(var1 + 3, var2.length() - 4);
        } else {
            Log.d(TAG, "获取打印机ID失败");
            return "";
        }
    }

    /**
     * @return
     * @throws Exception
     */
    public static String getPrintID() throws Exception {
        int var1 = -1;
        String var2 = "";
        WriteData(new byte[]{0x1B,0x18});
        byte[] var3 = ReadData(3);
        if (var3 != null) {
            var2 = bytetohex(var3);
            Log.d(TAG, "获取打印机ID：" + var2);
            var1 = var2.lastIndexOf("22");
            return var2.substring(var1 + 3, var2.length() - 4);
        } else {
            Log.d(TAG, "获取打印机ID失败");
            return "";
        }
    }

    public static String bytetoString(byte[] var0) {
        String var1 = "";
        if (var0 != null) {
            int var2 = var0.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                var1 = var1 + var0[var3] + " ";
            }
        }

        return var1;
    }

    public static String bytetohex(byte[] var0) {
        StringBuilder var1 = new StringBuilder(var0.length);
        byte[] var4 = var0;
        int var3 = var0.length;

        for (int var2 = 0; var2 < var3; ++var2) {
            byte var5 = var4[var2];
            var1.append(String.format("%02X ", new Object[]{Byte.valueOf(var5)}));
        }

        return var1.toString();
    }

    public static void printBitmap(int x, int y, Bitmap bitmap, boolean var3) throws Exception {
        int var4 = bitmap.getHeight() / 50;
        int var5 = 0;

        for (int var6 = 0; var6 < var4; ++var6) {
            printAreaSize("0", "200", "200", "50", "1");
            Bitmap var7 = Tobitmap(bitmap, bitmap.getWidth(), 50, 0, var5);
            if (var6 == 0) {
                Expanded("" + x, "" + y, var7, 0);
            } else {
                Expanded("" + x, "0", var7, 0);
            }

            if (var3 & bitmap.getHeight() % 50 == 0 & var6 == var4 - 1) {
                Form();
            }

            Print();
            var5 += 50;
        }

        if (bitmap.getHeight() % 50 != 0) {
            printAreaSize("0", "200", "200", "" + (bitmap.getHeight() - 50 * var4), "1");
            Bitmap var8 = Tobitmap(bitmap, bitmap.getWidth(), bitmap.getHeight() - 50 * var4, 0, var5);
            Expanded("" + x, "0", var8, 0);
            if (var3) {
                Form();
            }

            Print();
        }

    }


    public static Bitmap Tobitmap(Bitmap source, int width, int height, int x, int y) {
        return Bitmap.createBitmap(source, x, y, width, height);
    }

    public static String intToString(int[] src) {
        StringBuffer stringBuilder = new StringBuffer("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            stringBuilder = stringBuilder.append(src[i]).append(" ");;
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static String intToHexString(int[] src) {
        StringBuffer stringBuilder = new StringBuffer("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            String hv = Integer.toHexString(src[i]);
            stringBuilder = stringBuilder.append(hv).append(" ");;
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static int byte2Int(byte b){
        return (int)(b & 0xff);
    }

    //byte[]转十六进制字符串
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv).append(" ");
        }
        return stringBuilder.toString().toUpperCase();
    }

    //获取位图数据
    public static int[] getBitmapData(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        byte[] bytes = new byte[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        Log.d(TAG,"getBitmapData ori pixels  : " + intToHexString(pixels));
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] == WITER) {
                pixels[i] = 0;
            } else {
                pixels[i] = 1;
            }
            bytes[i] = (byte)pixels[i];
        }
        Log.d(TAG,"getBitmapData pixels  : " + intToString(pixels));
        Log.d(TAG,"getBitmapData pixels_bytes  : " + bytesToHexString(bytes));
        return get8BitData(pixels, width, height);
    }

    // 二进制转十进制
    public static int binaryToDecimal(int[] src) {
        int result = 0;
        for (int i = 0; i < src.length; i++) {
            result += src[i] * Math.pow(2, src.length - i - 1);
        }
        return result;
    }

    //获得8bit数据
    public static int[] get8BitData(int[] source, int width, int height) {
        int[] targData = new int[width * height / 8];
        byte[] bytes = new byte[width * height /8 ];
        // 组织数据
        for (int i = 0; i < height / 8; i++) {
            for (int j = 0; j < width; j++) {
                int[] temp = new int[8];
                for (int k = 0; k < 8; k++) {
                    temp[k] = source[(k + i * 8) * width + j];
                }
                targData[i * width + j] = binaryToDecimal(temp);
                bytes[i * width + j] = (byte)targData[i * width + j];
            }
        }
        Log.d(TAG,"get8BitData targData  : " + intToString(targData));
        Log.d(TAG,"get8BitData targData_bytes  : " + bytesToHexString(bytes));
        return targData;
    }

    //获得24bit图像数据
    public static int[] getBitmapData24(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i++) {

            if (pixels[i] == WITER) {
                pixels[i] = 0;
            } else {
                pixels[i] = 1;
            }
        }
        return get24BitData(pixels, width, height);
    }

    /**
     * 把原始数据转换为24位像素数据
     *
     * @param source
     *            初始数据
     * @param width
     *            图片宽度
     * @return
     */
    public static int[] get24BitData(int[] source, int width, int height) {
        int[] target = new int[width * height / 8];
        for (int i = 0; i < height / 24; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < 3; k++) {
                    int[] temp = new int[8];
                    for (int m = 0; m < 8; m++) {
                        temp[m] = source[(i * 24 + k * 8 + m) * width + j];
                    }
                    target[3 * (i * width + j) + k] = binaryToDecimal(temp);
                }
            }
        }
        return target;

    }

    public static byte[] getImageCmd(byte[] CMD, int width) {
        String[] result = new String[2];
        String str = Integer.toHexString(width).toUpperCase();
        StringBuffer sbuffer = new StringBuffer();
        int olen = 4 - str.length();
        for (int i = 0; i < olen; i++) {
            sbuffer.append("0");
        }
        sbuffer.append(str);
        result[0] = sbuffer.toString().substring(2, 4);
        result[1] = sbuffer.toString().substring(0, 2);
        int[] end = new int[2];
        end[0] = Integer.parseInt(result[0], 16);
        end[1] = Integer.parseInt(result[1], 16);
        return concat(CMD, getByteArray(end));
    }

    public static byte[] getImageCmd2(byte[] CMD, int width) {
        byte[] end = new byte[3];
        end[0] = 0x01;
        end[1] = (byte)width;
        end[2] = 0x00;
        return concat(CMD, end);
    }

    /**
     * @param first
     *            第一个数据
     * @param second
     *            第一个数据
     * @return 两个数据合并
     */
    public static byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * @return
     */
    public static int[] getCacheData(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // Log.v(TAG, "width = " + width + " ,height = " + height);
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] == WITER) {
                pixels[i] = 0;
            } else {
                pixels[i] = 1;
            }
        }
        int[] target = new int[width * height / 8];
        for (int i = 0; i < height / 8; i++) {
            for (int j = 0; j < width; j++) {
                int[] temp = new int[8];
                for (int m = 0; m < 8; m++) {
                    temp[m] = pixels[(i * 8 + m) * width + j];
                }
                target[j * height / 8 + i] = binaryToDecimal(temp);
            }
        }
        return target;
    }
}
