package com.hlk.myutilstest;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * Intent 开启的工具类
 * Created by hlk on 2017/3/5.
 * https://developer.android.google.cn/guide/components/intents-common.html
 */

public class IntentUtils {

    private Activity mActivity;

    public IntentUtils(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 浏览手机相册
     */
    public Intent createAlbumIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        return Intent.createChooser(intent, null);
    }

    /**
     * 拍照
     */
    public Intent createShotIntent(File tempFile) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Uri uri = Uri.fromFile(tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    /**
     * 拍摄照片
     * onActivityResult中返回Bitmap
     */
    public void capturePhoto(Uri mLocationForPhotos, String targetFilename, Integer requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.withAppendedPath(mLocationForPhotos, targetFilename));
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            mActivity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 以静态图像模式启动相机应用
     * onActivityResult中返回
     */
    public void capturePhoto_static_Image(Activity activity, Integer requestCode) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 以视频模式启动相机应用
     * onActivityResult中返回
     */
    public void capturePhoto_Video(int requestCode) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            mActivity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 选择联系人
     * onActivityResult中返回uri
     */
    public void selectContact(int requesetCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            mActivity.startActivityForResult(intent, requesetCode);
        }
    }


    /**
     * 发短信
     */
    public void sendSMS(String phone, String content) {
        if (TextUtils.isEmpty(phone)) {
            return;
        }
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
                + phone));
        if (mActivity.getPackageManager().resolveActivity(sendIntent, 0) == null) {
            Toast.makeText(mActivity, "系统不支持此功能", Toast.LENGTH_SHORT).show();
            return;
        }
        sendIntent.putExtra("sms_body", content);
        mActivity.startActivity(sendIntent);
    }

    /**
     * 发邮件
     */
    public void sendEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            mActivity.startActivity(intent);
        }
    }

    /**
     * 打电话
     */
    public void callPhone(String phone) {
        dialPhone(mActivity, phone, true);
    }


    private void dialPhone(Context mContext, String phone, boolean isShow) {
        String action = Intent.ACTION_CALL;// 在应用中启动一次呼叫有缺陷,不能用在紧急呼叫上
        if (isShow) {
            action = Intent.ACTION_DIAL;// 显示拨号界面
        }
        if (TextUtils.isEmpty(phone)) {
            return;
        }
        Intent intent = new Intent(action, Uri.parse("tel:" + phone));
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(intent);
        }
    }

    /**
     * 是否安装了客户端
     */
    public boolean isInstall(String packageName) {
        PackageManager pckMan;
        pckMan = mActivity.getPackageManager();
        List<PackageInfo> packs = pckMan.getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if (packageName.equals(p.packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建闹钟
     * 需要 SET_ALARM 权限
     */
    public void createAlarm(String message, int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            mActivity.startActivity(intent);
        }
    }


    /**
     * 创建定时器
     * 需要SET_ALARM 权限
     */
    public void startTimer(String message, int seconds) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_LENGTH, seconds)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            mActivity.startActivity(intent);
        }
    }


//    /**
//     * 添加日历事件
//     */
//    public void addEvent(String title, String location, Calendar begin, Calendar end) {
//        Intent intent = new Intent(Intent.ACTION_INSERT)
//                .setData(CalendarContract.Events.CONTENT_URI)
//                .putExtra(CalendarContract.Events.TITLE, title)
//                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
//                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
//                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
//        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
//            mActivity.startActivity(intent);
//        }
//    }

    /**
     * 播放媒体文件
     */
    public void playMedia(Uri file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(file);
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            mActivity.startActivity(intent);
        }
    }

    /**
     * 执行网页搜索
     */
    public void searchWeb(String query) {
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            mActivity.startActivity(intent);
        }
    }

//    public void openWifiSettings() {
//        Intent intent = new Intent(Intent.ACTION_ACCESSIBILITY_SETTINGS);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
//    }

    /**
     * 网络浏览器
     */
    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            mActivity.startActivity(intent);
        }
    }


}
