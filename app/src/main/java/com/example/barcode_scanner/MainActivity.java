package com.example.barcode_scanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import static com.google.zxing.BarcodeFormat.*;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    //Initialize variable
    Button btScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign variable
        btScan = findViewById(R.id.bt_scan);

        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initialize intent integrator
                IntentIntegrator intentIntegrator = new IntentIntegrator(
                        MainActivity.this
                );

                //Set prompt text
                intentIntegrator.setPrompt("For flash use volume up key.");

                //Set beep
                intentIntegrator.setBeepEnabled(true);

                //Locked orientation
                intentIntegrator.setOrientationLocked(true);

                //Set capture activity
                intentIntegrator.setCaptureActivity(Capture.class);

                //Initiate scan
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Initialize intent result
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode,resultCode,data
        );

        //Check condition
        if(intentResult.getContents() != null){
            //When result content is not null
            //Initialize alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MainActivity.this
            );

            //Set Activity type
            String activityType = "Observe";

            //Set title
            builder.setTitle("Scan Result");

            //Set message
            //builder.setMessage(intentResult.getContents());
            builder.setMessage("#: "+intentResult.getContents()
                    +"\nType: "+intentResult.getFormatName()
                    +"Activity: "+activityType);


            //Set positive button
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Dismiss dialog
                    dialogInterface.dismiss();
                }
            });

            //Show alert dialog
            builder.show();
        } else {
            //When result content is null
            //Display toast
            Toast.makeText(getApplicationContext(),
                    "Failed to scan.",Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public static BarcodeFormat getBarCodeFormatWith(final String s) {

        if (s.equals(EAN_8.toString())){
            return EAN_8;
        } else if (s.equals(EAN_13.toString())) {
            return EAN_13;
        } else if (s.equals(UPC_A.toString())) {
            return UPC_A;
        } else if (s.equals(QR_CODE.toString())) {
            return QR_CODE;
        } else if (s.equals(CODE_39.toString())) {
            return CODE_39;
        } else if (s.equals(CODE_128.toString())) {
            return CODE_128;
        } else if (s.equals(ITF.toString())) {
            return ITF;
        } else if (s.equals(PDF_417.toString())) {
            return PDF_417;
        } else if (s.equals(CODABAR.toString())) {
            return CODABAR;
        } else if (s.equals(DATA_MATRIX.toString())) {
            return DATA_MATRIX;
        } else if (s.equals(AZTEC.toString())) {
            return AZTEC;
        } else if (s.equals(PDF_417.toString())) {
            return PDF_417;
        }  else if (s.equals(CODE_93.toString())) {
            return CODE_93;
        }  else if (s.equals(MAXICODE.toString())) {
            return MAXICODE;
        }  else if (s.equals(UPC_E.toString())) {
            return UPC_E;
        }  else if (s.equals(UPC_EAN_EXTENSION.toString())) {
            return UPC_EAN_EXTENSION;
        }  else if (s.equals(RSS_14.toString())) {
            return RSS_14;
        }  else {
            return null;
        }
    }

}