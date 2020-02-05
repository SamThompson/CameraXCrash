# CameraXCrash
Demonstrates a crash that occurs when using the CameraX `CameraView` in a fragment due to a buffer queue
being abandoned.

## Steps to reproduce
1. Open the app and approve the camera permission
2. Click the "take picture" button to navigate to the next screen
3. Click the back button

Expected: the screen appears as it did in step 1 aft er the camera permission was approved

Actual: the app crashes

## Details of unexpected behavior

I did do some limited debugging into this and it seems like the lifecycle observer in `CameraXModule` isn't getting
an onDestroy callback when the fragment is put on the backstack.

Stacktrace of crash:
```
2020-02-05 12:53:40.316 7399-7444/com.example.cameraxcrash E/BufferQueueProducer: [SurfaceTexture-0-7399-2] query: BufferQueue has been abandoned
2020-02-05 12:53:40.316 7399-7444/com.example.cameraxcrash E/Legacy-CameraDevice-JNI: LegacyCameraDevice_nativeDetectSurfaceDimens: Error while querying surface width No such device (-19).
2020-02-05 12:53:40.323 7399-7444/com.example.cameraxcrash E/AndroidRuntime: FATAL EXCEPTION: CameraX-
    Process: com.example.cameraxcrash, PID: 7399
    java.lang.RuntimeException: java.lang.IllegalArgumentException: Surface was abandoned
        at androidx.camera.camera2.internal.Camera2CameraImpl$15.onFailure(Camera2CameraImpl.java:973)
        at androidx.camera.core.impl.utils.futures.Futures$CallbackListener.run(Futures.java:338)
        at android.os.Handler.handleCallback(Handler.java:873)
        at android.os.Handler.dispatchMessage(Handler.java:99)
        at android.os.Looper.loop(Looper.java:193)
        at android.os.HandlerThread.run(HandlerThread.java:65)
     Caused by: java.lang.IllegalArgumentException: Surface was abandoned
        at android.hardware.camera2.utils.SurfaceUtils.getSurfaceSize(SurfaceUtils.java:84)
        at android.hardware.camera2.params.OutputConfiguration.<init>(OutputConfiguration.java:260)
        at android.hardware.camera2.params.OutputConfiguration.<init>(OutputConfiguration.java:145)
        at androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatApi28Impl.<init>(OutputConfigurationCompatApi28Impl.java:34)
        at androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.<init>(OutputConfigurationCompat.java:51)
        at androidx.camera.camera2.internal.CaptureSession.lambda$openCaptureSession$2$CaptureSession(CaptureSession.java:354)
        at androidx.camera.camera2.internal.-$$Lambda$CaptureSession$bwwGuGuBhJx-fgB4Br9Wswwme0U.attachCompleter(Unknown Source:8)
        at androidx.concurrent.futures.CallbackToFutureAdapter.getFuture(CallbackToFutureAdapter.java:102)
        at androidx.camera.camera2.internal.CaptureSession.openCaptureSession(CaptureSession.java:273)
        at androidx.camera.camera2.internal.CaptureSession.lambda$open$0$CaptureSession(CaptureSession.java:236)
        at androidx.camera.camera2.internal.-$$Lambda$CaptureSession$2IbSQd39wMeo2dJgmFG1rvePLoM.apply(Unknown Source:8)
        at androidx.camera.core.impl.utils.futures.ChainingListenableFuture.run(ChainingListenableFuture.java:201)
        at android.os.Handler.handleCallback(Handler.java:873) 
        at android.os.Handler.dispatchMessage(Handler.java:99) 
        at android.os.Looper.loop(Looper.java:193) 
        at android.os.HandlerThread.run(HandlerThread.java:65) 
     Caused by: android.hardware.camera2.legacy.LegacyExceptionUtils$BufferQueueAbandonedException
        at android.hardware.camera2.legacy.LegacyExceptionUtils.throwOnError(LegacyExceptionUtils.java:73)
        at android.hardware.camera2.legacy.LegacyCameraDevice.getSurfaceSize(LegacyCameraDevice.java:606)
        at android.hardware.camera2.utils.SurfaceUtils.getSurfaceSize(SurfaceUtils.java:82)
        at android.hardware.camera2.params.OutputConfiguration.<init>(OutputConfiguration.java:260) 
        at android.hardware.camera2.params.OutputConfiguration.<init>(OutputConfiguration.java:145) 
        at androidx.camera.camera2.internal.compat.params.OutputConfigurationCompatApi28Impl.<init>(OutputConfigurationCompatApi28Impl.java:34) 
        at androidx.camera.camera2.internal.compat.params.OutputConfigurationCompat.<init>(OutputConfigurationCompat.java:51) 
        at androidx.camera.camera2.internal.CaptureSession.lambda$openCaptureSession$2$CaptureSession(CaptureSession.java:354) 
        at androidx.camera.camera2.internal.-$$Lambda$CaptureSession$bwwGuGuBhJx-fgB4Br9Wswwme0U.attachCompleter(Unknown Source:8) 
        at androidx.concurrent.futures.CallbackToFutureAdapter.getFuture(CallbackToFutureAdapter.java:102) 
        at androidx.camera.camera2.internal.CaptureSession.openCaptureSession(CaptureSession.java:273) 
        at androidx.camera.camera2.internal.CaptureSession.lambda$open$0$CaptureSession(CaptureSession.java:236) 
        at androidx.camera.camera2.internal.-$$Lambda$CaptureSession$2IbSQd39wMeo2dJgmFG1rvePLoM.apply(Unknown Source:8) 
        at androidx.camera.core.impl.utils.futures.ChainingListenableFuture.run(ChainingListenableFuture.java:201) 
        at android.os.Handler.handleCallback(Handler.java:873) 
        at android.os.Handler.dispatchMessage(Handler.java:99) 
        at android.os.Looper.loop(Looper.java:193) 
        at android.os.HandlerThread.run(HandlerThread.java:65)
```
