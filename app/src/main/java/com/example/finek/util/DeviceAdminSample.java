package com.example.finek.util;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.finek.CameraView;

public class DeviceAdminSample extends DeviceAdminReceiver {

static Context ctx;

static SharedPreferences getSamplePreferences(Context context) {

    ctx = context;

    return context.getSharedPreferences(
            DeviceAdminReceiver.class.getName(), 0);
}

@Override
public void onPasswordFailed(Context context, Intent intent) {
    super.onPasswordFailed(context, intent);

    System.out.println("Password Attempt is Failed...");

    Intent i = new Intent(context, CameraView.class);
    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(i);

}

}