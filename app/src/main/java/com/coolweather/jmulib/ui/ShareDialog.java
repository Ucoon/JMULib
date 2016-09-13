package com.coolweather.jmulib.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;


import com.coolweather.jmulib.R;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by ZongJie on 2016/7/20.
 */
public class ShareDialog extends Dialog implements View.OnClickListener {



    private Platform.ShareParams shareParams;

    private PlatformActionListener platformActionListener;
    public void setPlatformActionListener(
            PlatformActionListener platformActionListener) {
        this.platformActionListener = platformActionListener;
    }
    //增加一个回调函数,用以从外部接收返回值
    public interface ICustomDialogEventListener {
        public void customDialogEvent();
    }
    private ICustomDialogEventListener mCustomDialogEventListener;
    private Context mContext;
    public ShareDialog(Context context) {
        super(context);
        mContext = context;
    }
    public ShareDialog(Context context, ICustomDialogEventListener listener, int theme) {
        super(context, theme);
        mContext = context;
        mCustomDialogEventListener = listener;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_share, null);
        ImageView qq_btn= (ImageView) view.findViewById(R.id.qq_btn);
        ImageView friend_btn= (ImageView) view.findViewById(R.id.f_btn);
        ImageView wx_btn= (ImageView) view.findViewById(R.id.wx_btn);
        qq_btn.setOnClickListener(this);
        friend_btn.setOnClickListener(this);
        wx_btn.setOnClickListener(this);

        Window window=this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0,0,0,0);
        //获得window窗口的属性
        WindowManager.LayoutParams layoutParams=window.getAttributes();
        layoutParams.width=WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height=WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(layoutParams);
        this.setContentView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qq_btn:
                qq();
                break;
            case R.id.wx_btn:
                wx();
                break;
            case R.id.f_btn:
                friend();
                break;
            default:
                break;
        }
    }
    /**
     * 初始化分享参数
     *
     * @param shareModel
     */
    public void initShareParams(ShareModel shareModel) {
        if (shareModel != null) {
            Platform.ShareParams sp = new Platform.ShareParams();
            sp.setShareType(Platform.SHARE_TEXT);
            sp.setShareType(Platform.SHARE_WEBPAGE);

            sp.setTitle(shareModel.getText());
            sp.setText(shareModel.getText());
            sp.setUrl(shareModel.getUrl());
            sp.setImageUrl(shareModel.getImageUrl());
            sp.setImagePath(shareModel.getImageFilepath());
            sp.setComment("发现一本好书");
            shareParams = sp;
        }
    }
    private void qq() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(shareParams.getTitle());
        sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
        sp.setText(shareParams.getText());
        sp.setImageUrl(shareParams.getImageUrl());
        sp.setComment("我对此分享内容的评论");
        sp.setSite(shareParams.getTitle());
        sp.setSiteUrl(shareParams.getUrl());
        sp.setImagePath(shareParams.getImagePath());
        Platform qq = ShareSDK.getPlatform(getContext(), "QQ");
        qq.setPlatformActionListener(platformActionListener);
        qq.share(sp);


    }


    private void wx() {
        Platform plat = null;
        plat = ShareSDK.getPlatform(getContext(), "Wechat");
        if (platformActionListener != null) {
            plat.setPlatformActionListener(platformActionListener);
        }

        plat.share(shareParams);

//        WXImageObject imgObj=new WXImageObject(FragmentStatu.bmp);
//        WXMediaMessage msg=new WXMediaMessage();
//        msg.mediaObject=imgObj;
//        Bitmap thumbBmp=Bitmap.createScaledBitmap(FragmentStatu.bmp,100,100,true);
//        FragmentStatu.bmp.recycle();
//        msg.thumbData= com.qudao.watchapp.Utils.Util.bmpToByteArray(thumbBmp,true);
//        SendMessageToWX.Req req=new SendMessageToWX.Req();
//        req.transaction=buildTransaction("img");
//        req.message=msg;
//        req.scene=SendMessageToWX.Req.WXSceneTimeline;
//        IWXAPI mWxapi = WXAPIFactory.createWXAPI(getContext(), "wx2bb7c365aa8e4dec", false);
//        mWxapi.sendReq(req);
    }
    private void friend(){

        Platform plat = null;
        plat = ShareSDK.getPlatform(getContext(), "WechatMoments");
        if (platformActionListener != null) {
            plat.setPlatformActionListener(platformActionListener);
        }

        plat.share(shareParams);
        /*WXTextObject textObject=new WXTextObject();
        textObject.text="测试测试";
        WXMediaMessage wxMediaMessage=new WXMediaMessage();
        wxMediaMessage.mediaObject=textObject;
        wxMediaMessage.description="测试测试";
        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.transaction=buildTransaction("text");
        req.message=wxMediaMessage;
        //分享微信
        req.scene=SendMessageToWX.Req.WXSceneTimeline;
        NewLoginActivity.iwxapi.sendReq(req);*/
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
