package com.aptech.bruno.tom_cua_ca;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    custom_gridview_banco adapter;
    Integer[] hinh = {R.drawable.bau, R.drawable.ca, R.drawable.cua, R.drawable.ga, R.drawable.huou, R.drawable.tom};
    //
    Button btn;

    //
    AnimationDrawable cdxingau1, cdxingau2, cdxingau3;
    ImageView img1, img2, img3;
    //
    Random random;
    //
    int gtriXiNgau1, gtriXiNgau2, gtriXiNgau3;
    //
    Timer timer = new Timer();

    TextView txttien,txttime;
    SharedPreferences luTru;
    int tongtiencu,tongtienmoi,id_AmThanh;
    Handler handler;
    SoundPool amThanhXiNgau=new SoundPool(1, AudioManager.STREAM_MUSIC,0);
    android.os.Handler.Callback myCallback = new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            RandomXiNgau1();
            RandomXiNgau2();
            RandomXiNgau3();
            //xu li dat cuoc
            for (int i = 0; i <gtDatCuoc.length ; i++) {
                if (gtDatCuoc[i]!=0) {
                    if (i==gtriXiNgau1){
                        tienThuong+=gtDatCuoc[i];
                    }
                    if (i==gtriXiNgau2){
                        tienThuong+=gtDatCuoc[i];
                    }
                    if (i==gtriXiNgau3){
                        tienThuong+=gtDatCuoc[i];
                    }
                    if (i!=gtriXiNgau1&& i!=gtriXiNgau2&&i!=gtriXiNgau3){
                        tienThuong-=gtDatCuoc[i];
                    }
                }
            }
            if (tienThuong>0){
                Toast.makeText(getApplicationContext(),"Chúc mừng bạn trúng được : "+tienThuong,Toast.LENGTH_SHORT).show();
            }else if (tienThuong==0){
                Toast.makeText(getApplicationContext(),"Hên quá hòa vốn",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(),"Đen quá bị "+tienThuong+" rồi !",Toast.LENGTH_SHORT).show();
            }


            LuuDuLieuNguoiDung(tienThuong);
            txttien.setText(String.valueOf(tongtienmoi));

            return false;
        }
    };

    //tao mang luu gia tri dat cuoc
    public static Integer[] gtDatCuoc = new Integer[6];
    int tienThuong,kiemTra;
    CountDownTimer demThoiGian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.gridBanCo);

        img1 = (ImageView) findViewById(R.id.xingau1);
        img2 = (ImageView) findViewById(R.id.xingau2);
        img3 = (ImageView) findViewById(R.id.xingau3);

        adapter = new custom_gridview_banco(this, R.layout.custom_layout, hinh);
        gridView.setAdapter(adapter);
        txttien=(TextView)findViewById(R.id.txtTien);
        txttime=(TextView)findViewById(R.id.txtTime);

        luTru=getSharedPreferences("luutruthongtin", Context.MODE_PRIVATE);
        tongtiencu=luTru.getInt("TongTien",5000);
        txttien.setText(String.valueOf(tongtiencu));


        btn = (Button) findViewById(R.id.btnLac);

        //tu dong tang tien theo thoi gian thuc

        demThoiGian=new CountDownTimer(180000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long milis=millisUntilFinished;
                long gio= TimeUnit.MILLISECONDS.toHours(milis);
                long phut=TimeUnit.MILLISECONDS.toMinutes(milis)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milis));
                long giay=TimeUnit.MILLISECONDS.toSeconds(milis)- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milis));

                String gioPhutGiay=String.format("%02d:%02d:%02d",gio,phut,giay);
                txttime.setText(gioPhutGiay);

            }
            @Override
            public void onFinish() {
                SharedPreferences.Editor edit=luTru.edit();
                tongtiencu=luTru.getInt("TongTien",5000);
                tongtienmoi=tongtiencu+5000;
                edit.putInt("TongTien",tongtienmoi);
                edit.commit();
                txttien.setText(String.valueOf(tongtienmoi));
                demThoiGian.cancel();
                demThoiGian.start();
            }
        };
        demThoiGian.start();
        //
        handler = new Handler(myCallback);
        id_AmThanh=amThanhXiNgau.load(MainActivity.this,R.raw.audio,1);
        //
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img1.setImageResource(R.drawable.hinhdong);
                img3.setImageResource(R.drawable.hinhdong);
                img2.setImageResource(R.drawable.hinhdong);

                cdxingau1 = (AnimationDrawable) img1.getDrawable();

                cdxingau2 = (AnimationDrawable) img2.getDrawable();

                cdxingau3 = (AnimationDrawable) img3.getDrawable();

                kiemTra=0;
                for (int i = 0; i <gtDatCuoc.length; i++) {
                    kiemTra+=gtDatCuoc[i];
                }
                if (kiemTra==0){
                    Toast.makeText(getApplicationContext(),"Bạn vui lòng đặt cược",Toast.LENGTH_SHORT).show();
                }else {
                    if (kiemTra>tongtiencu){
                        Toast.makeText(getApplicationContext(),"Bạn không đủ tiền để đặt cược",Toast.LENGTH_SHORT).show();
                    }else {
                        amThanhXiNgau.play(id_AmThanh,1.0f,1.0f,1,0,1.0f);
                        cdxingau1.start();
                        cdxingau2.start();
                        cdxingau3.start();
                        tienThuong=0;
                        timer.schedule(new LacXiNgau(), 1000);
                    }
                }
            }
        });
    }
    private void LuuDuLieuNguoiDung(int tienthuong){
        SharedPreferences.Editor edit=luTru.edit();
        tongtiencu=luTru.getInt("TongTien",5000);
        tongtienmoi=tongtiencu+tienthuong;
        edit.putInt("TongTien",tongtienmoi);
        edit.commit();

    }

    class LacXiNgau extends TimerTask {
        @Override
        public void run() {

            handler.sendEmptyMessage(0);

        }
    }

    private void RandomXiNgau1() {
        random = new Random();
        int rd = random.nextInt(6);
        switch (rd) {
            case 0:
                img1.setImageResource(hinh[0]);
                gtriXiNgau1 = rd;
                break;
            case 1:
                img1.setImageResource(hinh[1]);
                gtriXiNgau1 = rd;
                break;
            case 2:
                img1.setImageResource(hinh[2]);
                gtriXiNgau1 = rd;
                break;
            case 3:
                img1.setImageResource(hinh[3]);
                gtriXiNgau1 = rd;
                break;
            case 4:
                img1.setImageResource(hinh[4]);
                gtriXiNgau1 = rd;
                break;
            case 5:
                img1.setImageResource(hinh[5]);
                gtriXiNgau1 = rd;
                break;
        }
    }

    private void RandomXiNgau2() {
        random = new Random();
        int rd = random.nextInt(6);
        switch (rd) {
            case 0:
                img2.setImageResource(hinh[0]);
                gtriXiNgau2 = rd;
                break;
            case 1:
                img2.setImageResource(hinh[1]);
                gtriXiNgau2 = rd;
                break;
            case 2:
                img2.setImageResource(hinh[2]);
                gtriXiNgau2 = rd;
                break;
            case 3:
                img2.setImageResource(hinh[3]);
                gtriXiNgau2 = rd;
                break;
            case 4:
                img2.setImageResource(hinh[4]);
                gtriXiNgau2 = rd;
                break;
            case 5:
                img2.setImageResource(hinh[5]);
                gtriXiNgau2 = rd;
                break;
        }
    }

    private void RandomXiNgau3() {
        random = new Random();
        int rd = random.nextInt(6);
        switch (rd) {
            case 0:
                img3.setImageResource(hinh[0]);
                gtriXiNgau3 = rd;
                break;
            case 1:
                img3.setImageResource(hinh[1]);
                gtriXiNgau3 = rd;
                break;
            case 2:
                img3.setImageResource(hinh[2]);
                gtriXiNgau3 = rd;
                break;
            case 3:
                img3.setImageResource(hinh[3]);
                gtriXiNgau3 = rd;
                break;
            case 4:
                img3.setImageResource(hinh[4]);
                gtriXiNgau3 = rd;
                break;
            case 5:
                img3.setImageResource(hinh[5]);
                gtriXiNgau3 = rd;
                break;
        }
    }
}
