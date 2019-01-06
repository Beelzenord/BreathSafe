package com.breathsafe.kth.breathsafe;

import com.breathsafe.kth.breathsafe.IO.Network.NetworkTask;

/**
 * Interface to receive callbacks from network tasks.
 */
public interface AsyncTaskCallback {
    void onDownloadComplete(NetworkTask.Result result);
}
