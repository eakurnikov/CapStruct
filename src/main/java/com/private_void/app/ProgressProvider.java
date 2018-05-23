package com.private_void.app;

public class ProgressProvider {
    private final Object progressLock = new Object();
    private final Object messageLock = new Object();
    private volatile ProgressListener progressListener;

    private ProgressProvider() {}

    public static ProgressProvider getInstance() {
        return Holder.progressProvider;
    }

    public void setProgress(double progress) {
        synchronized (progressLock) {
            progressListener.onProgressUpdated(progress);
        }
    }

    public void setProgress(String message) {
        synchronized (messageLock) {
            progressListener.onProgressUpdated(message);
        }
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    private static class Holder {
        private static ProgressProvider progressProvider = new ProgressProvider();
    }

    public interface ProgressListener {
        void onProgressUpdated(double progress);
        void onProgressUpdated(String message);
    }
}