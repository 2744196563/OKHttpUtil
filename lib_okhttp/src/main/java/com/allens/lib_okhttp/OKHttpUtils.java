package com.allens.lib_okhttp;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by allens on 2017/11/28.
 */

public class OKHttpUtils {

    private OkHttpClient mOkHttpClient;

    private Handler mHandler = new Handler();


    //1.构造方法私有化
    private OKHttpUtils(Context context) {
        //做一些配置的信息
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//      int cacheSize = 10 * 1024 * 1024; //10Mb
        int cacheSize = 10 << 20; //10Mb
        mOkHttpClient = builder.cache(new Cache(context.getCacheDir(), cacheSize))//设置缓存目录和大小
                .connectTimeout(15, TimeUnit.SECONDS) //连接时间 15s
                .readTimeout(20, TimeUnit.SECONDS) //读取时间 20s
                .writeTimeout(20, TimeUnit.SECONDS) //写入的时间20s
                .build();
    }

    //2.暴露出一个方法，返回当前类的对象
    private static OKHttpUtils mInstance;

    public static OKHttpUtils create(Context context) {
        if (mInstance == null) {
            //实例化对象
            //加上一个同步锁，只能有一个执行路径进入
            synchronized (OKHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OKHttpUtils(context);
                }
            }
        }
        return mInstance;
    }

    private static String prepareParam(Map<String, String> paramMap) {
        StringBuilder sb = new StringBuilder();
        if (paramMap.isEmpty()) {
            return "";
        } else {
            for (String key : paramMap.keySet()) {
                String value = paramMap.get(key);
                if (sb.length() < 1) {
                    sb.append(key).append("=").append(value);
                } else {
                    sb.append("&").append(key).append("=").append(value);
                }
            }
            return sb.toString();
        }
    }


    public <T> void get(String url, Object tag, final Class<T> tClass, final OnResponse<T> listener) {

        HashMap<String, String> map = new HashMap<>();
        listener.OnMap(map);
        String param = prepareParam(map);
        if (param.trim().length() >= 1) {
            url += "?" + param;
        }
        Log.e("OKHttp", "urlResult---->" + url);

        Request.Builder builder = new Request.Builder();
        if (tag != null) {
            builder.tag(tag);
        }
        final Request request = builder
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handleFailure(e, listener);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String result = response.body().string();
                Log.e("OKHttp", "result Json---->" + result);
                Gson gson = new Gson();
                T t = gson.fromJson(result, tClass);
                handleSuccess(t, listener);
            }
        });
    }


    public <T> void post(String url, Object tag, final Class<T> tClass, final OnResponse<T> listener) {
        HashMap<String, String> map = new HashMap<>();
        listener.OnMap(map);

        FormBody.Builder builder = new FormBody.Builder();
        if (map.size() != 0) {
            //添加键值对
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        RequestBody requestBody = builder.build();
        Request.Builder builder1 = new Request.Builder();
        if (tag != null) {
            builder1.tag(tag);
        }
        Request request = builder1
                .url(url)
                .post(requestBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handleFailure(e, listener);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String result = response.body().string();
                Log.e("OKHttp", "result Json---->" + result);
                Gson gson = new Gson();
                T t = gson.fromJson(result, tClass);
                handleSuccess(t, listener);
            }
        });
    }


    public void downLoad(String url, Object tag, final String path, final String FileName) {
        Request.Builder builder = new Request.Builder();
        if (tag != null) {
            builder.tag(tag);
        }
        final Request request = builder
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                Log.e("OKHttp", "result Json---->" + inputStream);
                String newPath = Environment.getExternalStorageDirectory().getPath() + File.separator + path + File.separator;
                File file = new File(newPath);
                if (!file.exists()) {
                    file.mkdir();
                }
                FileOutputStream fos = new FileOutputStream(newPath + FileName);
                int n = 0;
                byte[] buf = new byte[1024];
                while ((n = inputStream.read(buf)) != -1) {
                    fos.write(buf, 0, n);
                }
                fos.close();
                inputStream.close();
            }
        });

    }


    public <T> void upLoad(String url, Object tag, final String path, String FileName, final Class<T> tClass, final OnResponse<T> listener) {
        HashMap<String, String> map = new HashMap<>();
        listener.OnMap(map);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (map.size() != 0) {
            //添加键值对
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }

        //添加文件信息
        MultipartBody multipartBody = builder
                .addFormDataPart("file", FileName, RequestBody.create(MediaType.parse("file/*"), new File(path)))//添加文件
                .build();
        Request.Builder builder1 = new Request.Builder();
        if (tag != null) {
            builder1.tag(tag);
        }

        final Request request = builder1
                .url(url)
                .post(multipartBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handleFailure(e, listener);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String result = response.body().string();
                Log.e("OKHttp", "result Json---->" + result);
                Gson gson = new Gson();
                T t = gson.fromJson(result, tClass);
                handleSuccess(t, listener);
            }
        });
    }


    // 取消tag
    public void cancel(Object tag) {
        //准备的队列
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        //正在运行的消息队列
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }


    //处理失败的请求
    private void handleFailure(final IOException e, final OnResponse listener) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //主线程中。。。
                listener.onFailure(e);
            }
        });
    }

    //处理成功的请求
    private <T> void handleSuccess(final T t, final OnResponse listener) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //主线程。。。。。。。可以更新ui等
                //只要是在这里执行的方法都是在主线程中。。。
                listener.onResponse(t);
            }
        });
    }


    public interface OnResponse<T> {

        void OnMap(Map<String, String> map);

        void onFailure(IOException e);

        void onResponse(T result);
    }

}
