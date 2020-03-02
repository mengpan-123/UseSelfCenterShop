package com.example.selfshopcenter.download;

import androidx.annotation.NonNull;

import com.example.selfshopcenter.net.APIService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DownloadUtils {

    private static final String TAG = "DownloadUtils";
    private static final int DEFAULT_TIMEOUT = 15;
    private Retrofit retrofit;
    private JsDownloadListener listener;

    public DownloadUtils(JsDownloadListener listener) {
        this.listener = listener;
        JsDownloadInterceptor mInterceptor = new JsDownloadInterceptor(listener);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://52.81.85.108:8080/")
                .client(httpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    /**
     * 开始下载
     */
    public void download(@NonNull String url, final File file, Subscriber subscriber) {
        retrofit.create(APIService.class)
                .download(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(responseBody -> responseBody.byteStream())
                .observeOn(Schedulers.computation())
                .doOnNext(inputStream -> writeFile(inputStream, file))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 将输入流写入文件
     */
    private void writeFile(InputStream inputString, File file) {
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            byte[] b = new byte[1024];

            int len;
            while ((len = inputString.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            inputString.close();
            fos.close();
            //listener.onFinishDownload();
        } catch (FileNotFoundException e) {
            listener.onFail("FileNotFoundException");
        } catch (IOException e) {
            listener.onFail("IOException");
        }

    }
}