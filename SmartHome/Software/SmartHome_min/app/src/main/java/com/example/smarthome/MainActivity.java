package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;
import android.media.SoundPool;
import android.media.AudioManager;
import android.media.MediaPlayer;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class MainActivity extends AppCompatActivity {

    private BluetoothSPP bt;
    private TextToSpeech tts;
    SoundPool mPool;
    int warningWma;
    AudioManager mAm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = new BluetoothSPP(this); //Initializing
        mPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        warningWma = mPool.load(this, R.raw.warning, 1);
        mAm = (AudioManager)getSystemService(AUDIO_SERVICE);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getApplicationContext()
                    , "블루투스가 사용이 불가합니다." + "\n" + "블루투스를 확인하여 주세요."
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
            TextView state = findViewById(R.id.state);
            TextView temp = findViewById(R.id.temp);
            TextView humd = findViewById(R.id.humd);
            MediaPlayer player;

            public void onDataReceived(byte[] data, String message) {
                if (message.equals("0") == false) {

                    String[] array = message.split(",");
                    String str1 = new String("Warning");
                    String str2 = array[0];

                    if (str1.equals(str2) == true) {
                        Toast.makeText(getApplicationContext()
                                , "Warning!!"
                                , Toast.LENGTH_SHORT).show();
                        tts.setPitch(1.5f);
                        tts.setSpeechRate(1.0f);
                        mPool.play(warningWma, 1, 1, -1, 0, 1);

                        for (int i = 0; i < 2; i++) {
                            tts.speak("침입경보발생", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    state.setText(array[0]);
                    temp.setText(array[1].concat("C"));
                    humd.setText(array[2].concat("%"));
                    timeData("Time");
                }
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "이름 : " + name + "\n" + "주소 : " + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "연결이 해제 되었습니다." + "\n" +"다시 시도하여 주십시오.", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "연결을 실패하였습니다." + "\n" + "다시 시도하여 주십시오.", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnConnect = findViewById(R.id.연결); //연결시도
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });
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
        Button btnSendledon = findViewById(R.id.ledon); //데이터 전송
        Button btnSendledoff = findViewById(R.id.ledoff);
        Button btnSendopencurtains = findViewById(R.id.opencurtains);
        Button btnSendclosecurtains = findViewById(R.id.closecurtains);
        Button btnSendcurtainsauto = findViewById(R.id.curtainsauto);
        Button btnSendcpca = findViewById(R.id.cpca);
        Button btnSendcpcm = findViewById(R.id.cpcm);

        btnSendledon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timeData("ledhigh");
            }
        });

        btnSendledoff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timeData("ledlow");
            }
        });

        btnSendopencurtains.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timeData("opencurtains");
            }
        });

        btnSendclosecurtains.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timeData("closecurtains");
            }
        });

        btnSendcurtainsauto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timeData("curtainsauto");
            }
        });

        btnSendcpca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timeData("cpca");
            }
        });

        btnSendcpcm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timeData("cpcm");
            }
        });
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

    void timeData(String sendMSG){
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = simpleDate.format(mDate);
        bt.send("(" + getTime + ")" + sendMSG, true);
    }
}