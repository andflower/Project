package com.example.Radar_cup_cat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity {

    private BluetoothSPP bt;

    private View mContentView;

    boolean bluetoothState = false;

    private TextView serialPrint;
    private TextView connetion_text;

    public static String dataString = "";

    public static float step = 0;
    public static int distance = 0;
    public static int temp = 0;
    public static int flux = 0;
    public static String tfLunaVer = "";
    public static String serialNumber = "";
    public static int ori_tfTime = 0;
    public static int angle = 0;
    public static int tfTime = 0;
    public static int cDistance = 0;

    public static String t_angle = "";
    public static String t_distance = "";
    public static String t_temp = "";
    public static String t_flux = "";
    public static String t_tfTime = "";
    public static String fluxString = "";

    public static int firstRun = 0;
    public static int[] newDistance = new int[181];
    public static int[] oldDistance = new int[181];

    data_print dataPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mContentView = findViewById(R.id.radar_distance);
        mContentView = findViewById(R.id.radar_motion);
        mContentView = findViewById(R.id.radar_signal);
        mContentView = findViewById(R.id.radar_temp);
        mContentView = findViewById(R.id.blank);
        mContentView = findViewById(R.id.connetion_button);
        mContentView = findViewById(R.id.serial);
        mContentView = findViewById(R.id.connetion_button);
        mContentView = findViewById(R.id.dataPrint);
        mContentView = findViewById(R.id.radar_text);

        serialPrint = (TextView) findViewById(R.id.serial);
        connetion_text = (TextView) findViewById(R.id.connetion_button_text);

        dataPrint = (data_print) findViewById(R.id.dataPrint);

        bt = new BluetoothSPP(this); //Initializing
        serialPrint.append("The device initializing.\n");

        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            serialPrint.append("Connetion faled!\nPlease check the device.\n");
            finish();
        }

        //데이터 수신
        bt.setOnDataReceivedListener((data, message) -> {
            if (message.equals("0") == false) {
                String[] getStringArr = message.split(",");

                step = Float.parseFloat(getStringArr[0]);
                distance = Integer.parseInt(getStringArr[1]);
                temp = Integer.parseInt(getStringArr[2]);
                flux = Integer.parseInt(getStringArr[3]);
                tfLunaVer = getStringArr[4];
                serialNumber = getStringArr[5];
                ori_tfTime = Integer.parseInt(getStringArr[6]);

                angle = (int)((step * 0.9) + (float)0.5);
                tfTime = (int) ((float)Math.round(ori_tfTime / 100) / 10);

                flux_String();

                t_angle = String.valueOf(angle);
                t_distance = String.valueOf(distance);
                t_temp = String.valueOf(temp);
                t_flux = String.valueOf(flux);
                t_tfTime = String.valueOf(tfTime);

                dataString = "Angle : " + t_angle + "˚\n";
                dataString += "Distance : " + t_distance + "cm\n";
                dataString += "Temp : " + t_temp + "˚C\n";
                dataString += "Flux : " + t_flux + "\n";
                dataString += "TF-LUNA Ver : " + tfLunaVer + "(" + serialNumber + ")\n";
                dataString += "Sensor Time : " + t_tfTime + "s\n";

                serialPrint.setText(dataString);

                if (distance > 200) {
                    cDistance = 200;
                }

                else if (distance <= 20){
                    cDistance = 20;
                }
                else {
                    cDistance = distance;
                }

                oldDistance[angle] = newDistance[angle];


                if (flux > 100) {
                    newDistance[angle] = cDistance;
                }

                else {
                    newDistance[angle] = oldDistance[angle];
                }

                firstRun++;

                if (firstRun > 360) {
                    firstRun = 360; // keep the value at 360
                }
                dataPrint.invalidate();
            }
        });

        if (bluetoothState = true) {
            serialPrint.append("Connetion success!\n");
        }

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Name : " + name + "\nAddress : " + address
                        , Toast.LENGTH_SHORT).show();
                connetion_text.setText("DISCONNET");
            }

            public void onDeviceDisconnected() { //연결해제
                serialPrint.append("Disconnected\nPlease retry it.\n");
                connetion_text.setText("CONNET");
            }

            public void onDeviceConnectionFailed() { //연결실패
                serialPrint.append("Connetion faled!\nPlease retry it.\n");
            }
        });

        View draw = findViewById(R.id.connetion_button); //연결시도
        draw.setOnClickListener(v -> {
            if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                bt.disconnect();
                serialPrint.append("Trying to connect.\n");
            } else {
                Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                serialPrint.append("Requesting a connetion.\n");
            }
        });
    }

    public void flux_String() {
        if (flux <= 100) {
            temp /= 100;
            fluxString = "Weak(Less than 100)";
        }

        else {
            fluxString = "Strong";
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void onDestroy() {

        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
                setup();
            }
        }
    }

    public void setup() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "블루투스가 활성화 되지 않았습니다." + "\n" + "블루투스를 확인 하여주세요."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}