package com.private_void.app;

public class ProgressProvider {
    private final Object lock = new Object();
    private ProgressListener progressListener;

    private ProgressProvider() {}

    public static ProgressProvider getInstance() {
        return Holder.progressProvider;
    }

    public void setProgress(double progress) {
        synchronized (lock) {
            progressListener.onProgressUpdated(progress);
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
    }
}