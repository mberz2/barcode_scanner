package com.example.barcode_scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode_text);
        initialiseDetectorsAndSources();
    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(),
                "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission
                            (MainActivity.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(),
                        "To prevent memory leaks barcode scanner has been stopped",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {

                    System.out.println("****** IN DETECTOR");

                    barcodeText.post(new Runnable() {

                        @Override
                        public void run() {

                            /*
                            Toast.makeText(getApplicationContext(), ("Value: "+barcodes.valueAt(0).displayValue
                                            +"\nType: "+getType(barcodes.valueAt (0) .valueFormat)),
                                    Toast.LENGTH_SHORT).show();
                             */
                            //barcodeData = barcodes.valueAt(0).displayValue;

                            barcodeData=("Value: "+barcodes.valueAt(0).displayValue
                                    +"\nType: "+getType(barcodes.valueAt (0) .valueFormat));
                            barcodeText.setText(barcodeData);
                            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                        }
                    });
                }
            }

            public String getType(int x){
                switch(x){
                    case 1:
                        return "CODE 128";
                    case 2:
                        return "CODE 93";
                    case 3:
                        return "ISBN";
                    case 4:
                        return "Contact Info";
                    case 5:
                        return "Product";
                    case 6:
                        return "SMS";
                    case 7:
                        return "TEXT";
                    case 8:
                        return "URL";
                    case 9:
                        return "WIFI";
                    case 10:
                        return "GEO";
                    case 12:
                        return "Driver's License";
                    case 16:
                        return "Data Matrix";
                    case 32:
                        return "EAN 13";
                    case 64:
                        return "EAN 8";
                    case 128:
                        return "ITF";
                    case 256:
                        return "QR Code";
                    case 512:
                        return "UPC A (Grocery)";
                    case 1024:
                        return "UPC E (Grocery)";
                    case 2048:
                        return "PDF417";
                }
                return "INVALID";
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        getSupportActionBar().hide();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().hide();
        initialiseDetectorsAndSources();
    }

}