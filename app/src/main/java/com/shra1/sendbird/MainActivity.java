package com.shra1.sendbird;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shra1.sendbird.utils.SharedPreferencesManager;

public class MainActivity extends AppCompatActivity {
    SharedPreferencesManager s;
    Context mCtx;
    private Button bMAGuest;
    private Button bMAAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        s = SharedPreferencesManager.getInstance(this);

        mCtx = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 021
            );
        } else {
            MAIN();

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(mCtx, "Please allow all permissions", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        MAIN();
    }

    private void MAIN() {
        initViews();

        bMAGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, GuestActivity.class);
                startActivity(intent);
            }
        });

        bMAAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, AdminActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        bMAGuest = (Button) findViewById(R.id.bMAGuest);
        bMAAdmin = (Button) findViewById(R.id.bMAAdmin);
    }
}
