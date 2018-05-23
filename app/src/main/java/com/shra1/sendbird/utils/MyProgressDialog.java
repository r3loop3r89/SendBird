package com.shra1.sendbird.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by RupeshCHAVAN on 08-01-2018.
 */

public class MyProgressDialog {

    Context mCtx;
    public int id;
    public ProgressDialog progressDialog;
    public static ProgressDialog static_progressDialog;

    public MyProgressDialog(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void showMyProgressDialog(String msg, Boolean cancelable) {
        progressDialog = new ProgressDialog(mCtx);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
    }

    public static void showMyProgressDialog(Context mCtx, String msg, Boolean cancelable) {
        static_progressDialog = new ProgressDialog(mCtx);
        static_progressDialog.setMessage(msg);
        static_progressDialog.setCancelable(cancelable);
        static_progressDialog.show();
    }

    public void showMyProgressDialog(String msg, Boolean cancelable, int id) {
        progressDialog = new ProgressDialog(mCtx);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
        this.id = id;
    }

    public static void dissmisMyProgressDialog() {
        static_progressDialog.dismiss();
    }

    public void dissmisMyProgressDialog(int id) {
        if (this.id == id) {
            progressDialog.dismiss();
        }
    }
}