package sujithvakkomfreelance.com.enterprisemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;
import java.util.Objects;

import barcode.BarcodeReaderFragment;
import barcode.ScannerOverlay;
import sujithvakkomfreelance.com.components.AppLiterals;

public class BarcodeScannerActivity extends AppCompatActivity implements BarcodeReaderFragment.BarcodeReaderListener {
    private static final String TAG = BarcodeScannerActivity.class.getSimpleName();

    private BarcodeReaderFragment barcodeReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        // getting barcode instance
        barcodeReader = (BarcodeReaderFragment) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);


        /***
         * Providing beep sound. The sound file has to be placed in
         * `assets` folder
         */
        // barcodeReader.setBeepSoundFile("shutter.mp3");

        /**
         * Pausing / resuming barcode reader. This will be useful when you want to
         * do some foreground user interaction while leaving the barcode
         * reader in background
         * */
        // barcodeReader.pauseScanning();
        // barcodeReader.resumeScanning();
        ScannerOverlay scannerOverlay = findViewById(R.id.scanner_overlay);
    }

    @Override
    public void onScanned(final Barcode barcode) {
        Log.e(TAG, "onScanned: " + barcode.displayValue);
        barcodeReader.playBeep();
        Intent intent = new Intent();
        intent.putExtra(AppLiterals.BARCODE, barcode.displayValue);
        setResult(BarcodeScannerActivity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        Log.e(TAG, "onScannedMultiple: " + barcodes.size());

        String codes = "";
        for (Barcode barcode : barcodes) {
            codes += barcode.displayValue + ", ";
        }

        final String finalCodes = codes;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Barcodes: " + finalCodes, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getApplicationContext(), "Camera permission denied!", Toast.LENGTH_LONG).show();
        finish();
    }
}