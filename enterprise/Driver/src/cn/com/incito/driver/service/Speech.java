
package cn.com.incito.driver.service;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * 科大讯飞语音合成服务
 * 
 * @description 科大讯飞语音合成服务
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class Speech extends Service {

    private static String TAG = Speech.class.getSimpleName();

    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 默认发音人
    private String voicer = "xiaoyan";

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private static String BAIDU_PUSH_PUBLIC_MSG_TITLE = "mTitle";

    private static String BAIDU_PUSH_PUBLIC_MSG_Description = "mDescription";

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);

    }

    /**
     * 初期化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code == ErrorCode.SUCCESS) {
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTts != null) {

            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (null != intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                // String publicMsg = intent.getExtras()
                // .getParcelable("public_msg").toString();
                String publicMsg = bundle.getString("public_msg");
                StringBuffer speechText = new StringBuffer();
                String[] msg = publicMsg.split("\r\n");

                for (int i = 0; i < msg.length; i++) {
                    String[] msgContent = msg[i].split(" = ");
                    if (msgContent.length > 0) {
                        if (msgContent[0].replaceAll(" ", "").equals(BAIDU_PUSH_PUBLIC_MSG_TITLE)) {
                            speechText.append(msgContent[1]);
                        } else if (msgContent[0].replaceAll(" ", "").equals(BAIDU_PUSH_PUBLIC_MSG_Description)) {
                            speechText.append(msgContent[1]);
                        }
                    }

                }
                // 设置参数
                setParam();
                int code = mTts.startSpeaking(speechText.toString(), mTtsListener);
                if (code != ErrorCode.SUCCESS) {
                    if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                        // 未安装则跳转到提示安装页面
                    } else {
                    }
                }
            }

        }

    }

    /**
     * 参数设置
     * 
     * @param param
     * @return
     */
    private void setParam() {

        // 设置合成
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        }

        // 设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);

        // 设置语速
        mTts.setParameter(SpeechConstant.SPEED, "50");

        // 设置音调
        mTts.setParameter(SpeechConstant.PITCH, "50");

        // 设置音量
        mTts.setParameter(SpeechConstant.VOLUME, "80");

        // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
        }

        @Override
        public void onSpeakPaused() {
        }

        @Override
        public void onSpeakResumed() {
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
            } else if (error != null) {
            }
        }
    };
}
