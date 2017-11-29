package com.allens.okhttputil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allens.lib_okhttp.OKHttpUtils;

import java.io.IOException;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private ImageView img;
    String urlStr = "http://apis.juhe.cn/mobile/get";

    String UrlGet = "http://apis.juhe.cn/mobile/get?phone=18856907654&key=5778e9d9cf089fc3b093b162036fc0e1";

    String imgUrl = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1263067444,118499721&fm=27&gp=0.jpg";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.img);
        textView = findViewById(R.id.text);
    }

    public void btnGet(View view) {
        OKHttpUtils.create(this)
                .get(urlStr, 1, PhoneBean.class, new OKHttpUtils.OnResponse<PhoneBean>() {
                    @Override
                    public void OnMap(Map<String, String> map) {
                        map.put("phone", "18856907654");
                        map.put("key", "5778e9d9cf089fc3b093b162036fc0e1");
                    }

                    @Override
                    public void onFailure(IOException e) {

                    }

                    @Override
                    public void onResponse(PhoneBean result) {
                        Log.e("TAG", "response---->" + result);
                        textView.setText(result.getResultcode() + " " + result.getResult().getCity());
                    }
                });
    }

    public void btnPost(View view) {
        OKHttpUtils.create(this)
                .post(urlStr, 1, PhoneBean.class, new OKHttpUtils.OnResponse<PhoneBean>() {
                    @Override
                    public void OnMap(Map<String, String> map) {
                        map.put("phone", "18856907654");
                        map.put("key", "5778e9d9cf089fc3b093b162036fc0e1");
                    }

                    @Override
                    public void onFailure(IOException e) {

                    }

                    @Override
                    public void onResponse(PhoneBean result) {
                        Log.e("TAG", "response---->" + result);
                        textView.setText(result.getResultcode() + " " + result.getResult().getCity());
                    }
                });

    }

    public void btnDownLoad(View view) {
        OKHttpUtils.create(this)
                .downLoad("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3565619450,1776366346&fm=27&gp=0.jpg",
                        1, "1111",
                        "test.png");
    }


    public void btnUpLoad(View view) {
        OKHttpUtils.create(this)
                .upLoad("url", 1, "path", "xxx.png", PhoneBean.class, new OKHttpUtils.OnResponse<PhoneBean>() {
                    @Override
                    public void OnMap(Map<String, String> map) {

                    }

                    @Override
                    public void onFailure(IOException e) {

                    }

                    @Override
                    public void onResponse(PhoneBean result) {

                    }
                });
    }
}
