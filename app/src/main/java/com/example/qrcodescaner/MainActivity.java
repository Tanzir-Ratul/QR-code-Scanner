package com.example.qrcodescaner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewDebug;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

//import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class MainActivity extends AppCompatActivity {

    //private String[] appPermission = {Manifest.permission.CAMERA};
    private final static int REQUEST_CODE_CAMERA = 1;
    //ZXingScannerView scannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
        intentIntegrator.setPrompt("For flush use volume key");
        //set beep
        intentIntegrator.setBeepEnabled(true);
        //locked orientation
        intentIntegrator.setOrientationLocked(true);
        //set capture Activity
        intentIntegrator.setCaptureActivity(Capture.class);
        //initiate scan
        intentIntegrator.initiateScan();



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(intentResult.getContents()!=null){
            AlertDialog.Builder  builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Result");
            builder.setMessage(intentResult.getContents());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //dialog.dismiss();
                    determineCode(intentResult.getContents());
                }
            });
            builder.show();
        }
        else
            Toast.makeText(this, "You didn't Scan", Toast.LENGTH_SHORT).show();
    }

    private void checkPermission() {
        Dexter.withContext(MainActivity.this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(MainActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    void showText(String title,String msg){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //scannerView.resumeCameraPreview(MainActivity.this);
            }
        }).create().show();
    }
    private void determineCode(String code){
       if(code.contains("http"))
       {
           startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(code)));
           //Toast.makeText(getApplicationContext(),"It is a website",Toast.LENGTH_SHORT).show();
       }
    }
}