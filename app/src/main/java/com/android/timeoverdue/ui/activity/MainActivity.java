package com.android.timeoverdue.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.widget.EditText;

import com.android.timeoverdue.R;
import com.android.timeoverdue.app.Contents;
import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.bean.BmobGoods;
import com.android.timeoverdue.databinding.ActivityMainBinding;
import com.android.timeoverdue.ui.adapter.GoodsAdapter;
import com.android.timeoverdue.utils.JsonParser;
import com.android.timeoverdue.utils.ZSPTool;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private GoodsAdapter goodsAdapter;
    private List<BmobGoods> data;
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人
    private String voicer = "xiaoyan";
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();
    private StringBuffer buffer = new StringBuffer();
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private String resultType = "json";
    private int ret = 0; // 函数调用返回值
    private StringBuffer resultBuffer;
    private Boolean isSpeaking = false;

    private GoodsAdapter.OnItemClickListener listener = new GoodsAdapter.OnItemClickListener() {
        @Override
        public void onClick(int position) {
            Intent intent = new Intent(MainActivity.this, GoodsActivity.class);
            intent.putExtra("mObjectId", data.get(position).getObjectId());
            startActivity(intent);
        }
    };

    @Override
    protected void initData() {
        viewBinding.smart.setOnRefreshListener(refreshLayout -> {
            getGoodsList();
        });
        viewBinding.smart.setEnableLoadMore(false);
        getGoodsList();
    }

    @Override
    protected void initView() {
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(MainActivity.this, mInitListener);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(MainActivity.this, mInitListener);
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(MainActivity.this, mTtsInitListener);
        // 设置参数
        setParamYuYi();
    }

    @Override
    protected void onClick() {
        //设置按钮点击事件
        viewBinding.ivSet.setOnClickListener(v -> {
            jumpToActivity(SettingActivity.class);
        });
        //添加物品按钮点击事件
        viewBinding.ivAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddGoodsActivity.class);
            intent.putExtra("editStr", "add");
            startActivity(intent);
        });
        //语音
        viewBinding.ivVoice.setOnClickListener(v -> {
            if (null == mIat) {
                // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
                showToast("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
                return;
            }
            //申请麦克风权限
            XXPermissions.with(this)
                    .permission(Permission.RECORD_AUDIO)
                    .request(new OnPermissionCallback() {
                        @Override
                        public void onGranted(List<String> permissions, boolean all) {
                            if (!all) {
                                showToast("权限未授予，部分功能不可使用！");
                            } else {
                                buffer.setLength(0);
                                mIatResults.clear();
                                // 设置参数
                                setParam();
                                mIatDialog.setListener(mRecognizerDialogListener);
                                mIatDialog.show();
                                showToast("请开始说话...");
                                // 不显示听写对话框
//                                ret = mIat.startListening(mRecognizerListener);
//                                if (ret != ErrorCode.SUCCESS) {
//                                    showToast("听写失败,错误码：" + ret + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
//                                } else {
//                                    showToast("请开始说话...");
//                                }
                            }
                        }
                    });

        });
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        // 返回结果
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
        }

        // 识别回调错误
        public void onError(SpeechError error) {
            showToast(error.getPlainDescription(true));
        }

    };

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showToast("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            Log.d(TAG, "onError " + error.getPlainDescription(true));
            showToast(error.getPlainDescription(true));

        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showToast("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            if (isLast) {
                Log.d(TAG, "onResult 结束");
            }
            if (resultType.equals("json")) {
                printResult(results);
                return;
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showToast("当前正在说话，音量大小 = " + volume + " 返回音频数据 = " + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    /**
     * 显示结果
     */
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        judgeSpeaking(resultBuffer.toString());
        Log.e(TAG, resultBuffer.toString());
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showToast("初始化失败，错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    };

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showToast("初始化失败,错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, resultType);
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        Log.e(TAG, "last language:" + mIat.getParameter(SpeechConstant.LANGUAGE));

        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav.
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
                getExternalFilesDir("msc").getAbsolutePath() + "/iat.wav");
    }

    /**
     * 参数设置
     *
     * @return
     */
    private void setParamYuYi() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 支持实时音频返回，仅在 synthesizeToUri 条件下支持
            mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            //	mTts.setParameter(SpeechConstant.TTS_BUFFER_TIME,"1");

            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "50");
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");

        }

        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,
                getExternalFilesDir("msc").getAbsolutePath() + "/tts.pcm");
    }

    private void getGoodsList() {
        BmobQuery<BmobGoods> query = new BmobQuery<>();
        query.order("-createAt");
        query.findObjects(new FindListener<BmobGoods>() {
            @Override
            public void done(List<BmobGoods> list, BmobException e) {
                viewBinding.smart.finishRefresh();
                if (e == null) {
                    data = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getUserId().equals(ZSPTool.getString(Contents.USER_ID))) {
                            if (GoodsAdapter.dateDiff("-1", list.get(i).getExpirationTime()) > 0) {
                                data.add(list.get(i));
                            }
                        }
                    }
                    if (goodsAdapter == null) {
                        goodsAdapter = new GoodsAdapter(mContext, data);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        goodsAdapter.setOnItemClickListener(listener);
                        viewBinding.recyclerView.setLayoutManager(linearLayoutManager);
                        viewBinding.recyclerView.setAdapter(goodsAdapter);
                    } else {
                        goodsAdapter.setData(data);
                    }
                    if (!isSpeaking) {
                        judgeSpeaking("三天以内过期的物品有哪些");
                    }
                } else {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGoodsList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIat != null) {
            mIat.cancel();
            mIat.destroy();
        }
    }

    private void judgeSpeaking(String str) {
        String speakingStr = "";
        int day = 0;
        if (str.contains("最近") && str.contains("过期")) {
            speakingStr = "最近过期的物品有";
            for (int i = 0; i < data.size(); i++) {
                if (GoodsAdapter.dateDiff("-1", data.get(i).getExpirationTime()) < 7) {
                    speakingStr += data.get(i).getName();
                }
            }
            speakingStr += "。";
        } else if (str.contains("可乐") && str.contains("多久过期")) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getName().equals("可乐")){
                    if (day == 0) {
                        day = GoodsAdapter.dateDiff("-1", data.get(i).getExpirationTime());
                    }else {
                        if (day > GoodsAdapter.dateDiff("-1", data.get(i).getExpirationTime())) {
                            day = GoodsAdapter.dateDiff("-1", data.get(i).getExpirationTime());
                        }
                    }
                }
            }
            if (day == 0) {
                speakingStr = "没有找到可乐，请添加后再尝试。";
            }else {
                speakingStr = "可乐还有" + day + "天就要过期了，请尽快饮用。";
            }
        } else if (str.equals("三天以内过期的物品有哪些")) {
            for (int i = 0; i < data.size(); i++) {
                if (GoodsAdapter.dateDiff("-1", data.get(i).getExpirationTime()) < 7) {
                    speakingStr += data.get(i).getName() + "还有" + GoodsAdapter.dateDiff("-1", data.get(i).getExpirationTime())+ "天过期，";
                }
            }
            speakingStr += "请尽快使用";
            isSpeaking = true;
        }else{
            speakingStr = "对不起，我听不清你在说什么";
        }
        int code = mTts.startSpeaking(speakingStr, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            showToast("语音合成失败,错误码: " + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
        }
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
//            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
//            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
//            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            Log.e("MscSpeechLog_", "percent =" + percent);
//            mPercentForBuffering = percent;
//            showTip(String.format(getString(R.string.tts_toast_format), mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            Log.e("MscSpeechLog_", "percent =" + percent);
//            mPercentForPlaying = percent;
//            showTip(String.format(getString(R.string.tts_toast_format), mPercentForBuffering, mPercentForPlaying));

//            SpannableStringBuilder style = new SpannableStringBuilder(texts);
//            Log.e(TAG, "beginPos = " + beginPos + "  endPos = " + endPos);
//            style.setSpan(new BackgroundColorSpan(Color.RED), beginPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ((EditText) findViewById(R.id.tts_text)).setText(style);
        }

        @Override
        public void onCompleted(SpeechError error) {
//            showTip("播放完成");
            if (error != null) {
                showToast(error.getPlainDescription(true));
                return;
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            //	 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	 若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                Log.d(TAG, "session id =" + sid);
            }
            // 当设置 SpeechConstant.TTS_DATA_NOTIFY 为1时，抛出buf数据
            if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
                byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
                Log.e(TAG, "EVENT_TTS_BUFFER = " + buf.length);
                // 保存文件
//                appendFile(pcmFile, buf);
            }

        }
    };
}