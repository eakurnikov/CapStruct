package com.private_void.utils.notifiers;

public class ProgressProvider {
    private final Object progressLock = new Object();
    private ProgressListener progressListener;

    private ProgressProvider() {}

    public static ProgressProvider getInstance() {
        return Holder.progressProvider;
    }

    public void setProgress(double progress) {
        synchronized (progressLock) {
            progressListener.onProgressUpdated(progress);
        }
    }

    public ProgressProvider setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
        return this;
    }

    private static class Holder {
        private static ProgressProvider progressProvider = new ProgressProvider();
    }

    public interface ProgressListener {
        void onProgressUpdated(double progress);
    }
}