package com.rmicro.btprinter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rmicro.btprinter.R;
import com.rmicro.printersdk.BluetoothSdkManager;
import com.rmicro.printersdk.BtCmdService;
import com.rmicro.printersdk.constant.ConstantDefine;
import com.rmicro.printersdk.listener.BluetoothConnectListener;
import com.rmicro.printersdk.listener.BluetoothStateListener;
import com.rmicro.printersdk.listener.IReceiveDataListener;
import com.rmicro.printersdk.util.PrinterHelper;

import java.io.InputStream;
import java.util.Arrays;

public class BtPrinterActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = BtPrinterActivity.class.getSimpleName();
    private Context mContext;

    private BluetoothSdkManager manager;

    private TextView tvPrinterTitle;
    private TextView tvPrinterSummary;
    private ImageView imgPrinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);
        mContext = this;

        initViews();

        initListener();
    }

    public void initViews() {
        tvPrinterTitle = findViewById(R.id.txt_printer_setting_title);
        tvPrinterSummary = findViewById(R.id.txt_printer_setting_summary);
        if (!TextUtils.isEmpty(BluetoothSdkManager.btName) && !TextUtils.isEmpty(BluetoothSdkManager.btAddress)) {
            tvPrinterTitle.setText("已绑定蓝牙：" + BluetoothSdkManager.btName);
            tvPrinterSummary.setText(BluetoothSdkManager.btAddress);
        }
        imgPrinter = findViewById(R.id.img_printer_setting_icon);
        manager = BluetoothSdkManager.INSTANCE;
        manager.checkBluetooth();
    }

    public void initListener() {
        findViewById(R.id.ll_printer_setting_change_device).setOnClickListener(this);
        findViewById(R.id.btn_printTest).setOnClickListener(this);
        findViewById(R.id.btn_imgprintTest).setOnClickListener(this);
        findViewById(R.id.btn_getPrintStatus).setOnClickListener(this);
        findViewById(R.id.btn_getPrintParam).setOnClickListener(this);
        findViewById(R.id.btn_getPrintVersion).setOnClickListener(this);
        findViewById(R.id.btn_getPrintID).setOnClickListener(this);
        findViewById(R.id.btn_reSet).setOnClickListener(this);

        //接收蓝牙数据回调
        manager.setReceiveDataListener(new IReceiveDataListener() {
            @Override
            public void onReceiveData(byte[] data) {
                Log.d(TAG, "onReceiveData ==>> " + Arrays.toString(data));
            }
        });

        //连接状态结果回调
        manager.setBlueStateListener(new BluetoothStateListener() {
            @Override
            public void onConnectStateChanged(int state) {
                switch (state) {
                    case ConstantDefine.CONNECT_STATE_NONE:
                        Log.d(TAG, "  -----> none <----");
                        break;
                    case ConstantDefine.CONNECT_STATE_LISTENER:
                        Log.d(TAG, "  -----> listener <----");
                        break;
                    case ConstantDefine.CONNECT_STATE_CONNECTING:
                        Log.d(TAG, "  -----> connecting <----");
                        break;
                    case ConstantDefine.CONNECT_STATE_CONNECTED:
                        Log.d(TAG, "  -----> connected <----");
                        break;
                }
            }
        });

        manager.setBluetoothConnectListener(new BluetoothConnectListener() {
            @Override
            public void onBTDeviceConnected(String address, String name) {
                Toast.makeText(BtPrinterActivity.this, "已连接到名称为" + name + "的设备", Toast.LENGTH_SHORT).show();
                tvPrinterTitle.setText("已绑定蓝牙：" + name);
                tvPrinterSummary.setText(address);
                imgPrinter.setImageResource(R.drawable.ic_bluetooth_device_connected);
                //mBtnPrintTest.setVisibility(View.VISIBLE);
            }

            @Override
            public void onBTDeviceDisconnected() {
                /*tvPrinterTitle.setText("蓝牙连接已断开");
                tvPrinterSummary.setText("");
                imgPrinter.setImageResource(R.drawable.ic_bluetooth_device_connected);
                Toast.makeText(MainActivity.this, "连接已经断开，请重新尝试连接...", Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onBTDeviceConnectFailed() {
                Toast.makeText(BtPrinterActivity.this, "连接失败，请重新连接...", Toast.LENGTH_SHORT).show();
                tvPrinterTitle.setText("蓝牙连接失败");
                tvPrinterSummary.setText("");
                imgPrinter.setImageResource(R.drawable.ic_bluetooth_device_connected);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (manager != null) {
            Log.d(TAG, "onStart");
            manager.setupService();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (manager != null) {
            Log.d(TAG, "onDestroy");
            manager.stopService();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_printer_setting_change_device:
                Intent serverIntent = new Intent(BtPrinterActivity.this, DeviceListActivity.class);
                startActivity(serverIntent);
                break;
            case R.id.btn_printTest:
                startBtCmdService(ConstantDefine.ACTION_PRINT_TEST);
                break;
            case R.id.btn_imgprintTest:
                //startBtCmdService(ConstantDefine.ACTION_IMG_PRINT_TEST);
                //设置纸张类型
                setPrinterPageType(2);//间隙纸张
                //设置标签规格
                setPrinterLabelParam(17,0,144,1,0,2,0);//(44,0,32,0,28,0,0);
                //走纸10点 --> 与标签顶部距离10点
                setPrinterPageRun(10);
                //打印图形
                printImage();
                //走纸一张标签 --> 出纸张一张标签
                setPrinterPageRunNext();
                break;
            case R.id.btn_getPrintStatus:
                //startBtCmdService(ConstantDefine.ACTION_GET_PRINT_STATUS);
                getPrinterStatus();
                break;
            case R.id.btn_getPrintParam:
                startBtCmdService(ConstantDefine.ACTION_GET_PRINT_PARAM);
                break;
            case R.id.btn_getPrintVersion:
                Log.d(TAG, "model: " +  getPrinterModel());
                break;
            case R.id.btn_getPrintID:
                startBtCmdService(ConstantDefine.ACTION_GET_PRINT_ID);
                break;
            case R.id.btn_reSet:
                //setPrinterPageRunNext();
                //setPrinterPageRun(100);
                break;
            default:
                break;
        }
    }

    //打印字体／图片行间距设置 0x00～0xFF <0-255> 打印图片设置为0x0  打印文字参考设置为0x5
    private void setPrinterSpace(int val) {
        int value = val;
        if(value < 0)
            value = 0;
        if(value > 255)
            value = 255;
        try {
            PrinterHelper.setPrinterSpace(value);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //打印字体／图片 浓度设置0x0~0x0F  <0--16>
    private void setPrinterChroma(int val){
        int value = val;
        if(value < 0)
            value = 0;
        if(value > 16)
            value = 16;
        try {
            PrinterHelper.setPrinterChroma(value);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //设置打印纸张类型 命令头  1C 25 n    n=0:连续纸  n=1:定位孔  n=2:间隙纸  n=3: 黑标纸  n=4：线缆标签
    private void setPrinterPageType(int type){
        if(type!=0 && type!=1 && type!=2 && type!=3 && type!=4){
            Log.d(TAG, "纸张类型设置错误，n=0:连续纸  n=1:定位孔  n=2:间隙纸  n=3: 黑标纸  n=4：线缆标签");
        }
        try {
            PrinterHelper.setPrinterChroma(type);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    *打印点行数：w=( L0 + L1 × 256)，表示当前打印标签数据总共点行数 ---> 图片像素
    *纸张宽度：w=( wL + wH × 256)
    *纸张高度或长度：h=( hL + hH × 256)
    *如果h为0表示当前的纸张不限定打印长度
    *m表示纸张间缝隙或打孔纸孔直径（预切割标签纸，打孔纸独有），其他类型纸张默认为0。
    *高度和宽度设置的单位为mm。
    *精度为0.1mm
    */
    private void setPrinterLabelParam(int l0,int l1,int wL,int wH,int hL, int hH,int m){
        try {
            PrinterHelper.setPrinterLabelParam(l0,l1,wL,wH,hL,hH,m);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 打印机走纸设置 ： 0 ≤ m ≤ 255
    private void setPrinterPageRun(int val){
        int value = val;
        if(value < 0)
            value = 0;
        if(value > 255)
            value = 255;
        try {
            PrinterHelper.setPrinterPageRun(value);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 打印机走纸-->直接走到下一个标签纸
    private void setPrinterPageRunNext(){
        try {
            PrinterHelper.setPrinterPageRunNext();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取打印机状态
     * 0：打印机有纸。 1：打印机缺纸。 2：打印机上盖关闭。 3：打印机上盖开启。
     * 4：打印机切刀正常。5：打印机切刀异常。6：打印机切刀抬起。7：打印机切刀按下。
     * 8: 纸张类型正常。 9：纸张类型错误。10：运行正常。11：运行异常。其他：保留
     */
    private int getPrinterStatus(){
        int status = -1;
        try {
            status =  PrinterHelper.getRDPrinterStatus();
        }catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }

    /*
     * 获取打印机型号
     * */
    private String getPrinterModel(){
        String model_no=null;
        try {
            model_no =  PrinterHelper.getRDPrinterModel();
        }catch (Exception e){
            e.printStackTrace();
        }
        return model_no;
    }

    private void printImage() {
        Log.d(TAG, "printTest..");
        try {
            InputStream inbmp = this.getResources().getAssets().open("logo_sto_print1.png");
            Bitmap bitmap = BitmapFactory.decodeStream(inbmp);
            byte[] add = PrinterHelper.getByteArray(0x00);
            int[] sourceData = PrinterHelper.getBitmapData(bitmap);
            byte[] data = PrinterHelper.getByteArray(sourceData);
            int sendLen = bitmap.getWidth();//
            byte[] ImageCMD = PrinterHelper.getImageCmd(PrinterHelper.IMAGECMD, sendLen);

            for (int i = 0; i < data.length / sendLen; i++) {
                byte[] temp = Arrays.copyOfRange(data, i * sendLen, (i + 1)
                        * sendLen);
                byte[] stemp = PrinterHelper.concat(temp, PrinterHelper.WRAP_PRINT);
                byte[] printData = PrinterHelper.concat(ImageCMD, stemp);
                PrinterHelper.WriteData(printData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startBtCmdService(String action) {
        Intent intent = new Intent(getApplicationContext(), BtCmdService.class);
        intent.setAction(action);
        startService(intent);
    }
}
