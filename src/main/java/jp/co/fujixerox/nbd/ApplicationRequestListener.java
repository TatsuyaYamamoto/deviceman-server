package jp.co.fujixerox.nbd;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class ApplicationRequestListener implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent request) {
        // リクエスト開始時の処理を行う。
        // (実装は省略)
    }
    @Override
    public void requestDestroyed(ServletRequestEvent response) {
        // リクエスト終了時の処理を行う。
        // (実装は省略)
    }

}