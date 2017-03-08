package com.hlk.myutilstest;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.AlarmClock;
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

//    private Activity mActivity;
//
//    public IntentUtils(Activity activity) {
//        this.mActivity = activity;
//    }

    /**
     * 浏览手机相册
     */
    public static Intent createAlbumIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        return Intent.createChooser(intent, null);
    }

    /**
     * 拍照
     */
    public static Intent createShotIntent(File tempFile) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Uri uri = Uri.fromFile(tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    /**
     * 拍摄照片
     * onActivityResult中返回Bitmap
     */
    public static void capturePhoto(Activity activity,Uri mLocationForPhotos, String targetFilename, Integer requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.withAppendedPath(mLocationForPhotos, targetFilename));
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 以静态图像模式启动相机应用
     * onActivityResult中返回
     */
    public static void capturePhoto_static_Image(Activity activity, Integer requestCode) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 以视频模式启动相机应用
     * onActivityResult中返回
     */
    public static void capturePhoto_Video(Activity activity ,int requestCode) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 选择联系人
     * onActivityResult中返回uri
     */
    public static void selectContact(Activity activity,int requesetCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, requesetCode);
        }
    }


    /**
     * 发短信
     */
    public static void sendSMS(Activity activity,String phone, String content) {
        if (TextUtils.isEmpty(phone)) {
            return;
        }
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
                + phone));
        if (activity.getPackageManager().resolveActivity(sendIntent, 0) == null) {
            Toast.makeText(activity, "系统不支持此功能", Toast.LENGTH_SHORT).show();
            return;
        }
        sendIntent.putExtra("sms_body", content);
        activity.startActivity(sendIntent);
    }

    /**
     * 发邮件
     */
    public static void sendEmail(Activity activity,String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    /**
     * 打电话
     */
    public static void callPhone(Activity activity,String phone) {
        dialPhone(activity, phone, true);
    }


    private static void dialPhone(Context mContext, String phone, boolean isShow) {
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
    public static boolean isInstall(Activity activity,String packageName) {
        PackageManager pckMan;
        pckMan = activity.getPackageManager();
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
    public static void createAlarm(Activity activity,String message, int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }


    /**
     * 创建定时器
     * 需要SET_ALARM 权限
     */
    public static void startTimer(Activity activity,String message, int seconds) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_LENGTH, seconds)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
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
    public static void playMedia(Activity activity,Uri file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(file);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    /**
     * 执行网页搜索
     */
    public static void searchWeb(Activity activity,String url) {
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, url);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
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
    public static void openWebPage(Activity activity,String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }


}
