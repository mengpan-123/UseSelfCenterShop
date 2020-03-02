package com.example.selfshopcenter.download;

public interface JsDownloadListener {

    void onStartDownload(long length);

    void onProgress(int progress);

    void onFinishDownload();

    void onFail(String errorMessage);
}