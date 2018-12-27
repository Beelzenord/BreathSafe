package com.breathsafe.kth.breathsafe;

import com.breathsafe.kth.breathsafe.IO.Network.NetworkTask;

public interface AsyncTaskCallback {
    void onDownloadComplete(NetworkTask.Result result);
}
