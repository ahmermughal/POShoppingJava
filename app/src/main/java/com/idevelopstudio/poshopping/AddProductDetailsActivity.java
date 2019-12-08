package com.idevelopstudio.poshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;
import com.idevelopstudio.poshopping.Database.AppDatabase;
import com.idevelopstudio.poshopping.Database.Product;
import com.idevelopstudio.poshopping.Extra.AppExecutors;

public class AddProductDetailsActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CAMERA = 200;
    private EditText categoryEditText;
    private FloatingActionButton barcodeFab;
    private View mainLayout;
    private EditText idEdiText;
    private AppDatabase db;
    private EditText nameEditText;
    private EditText priceEditText;
    private EditText stockEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_details);
        Intent intent = getIntent();
        db = AppDatabase.getInstance(getApplicationContext());
        categoryEditText = findViewById(R.id.et_category);
        nameEditText = findViewById(R.id.et_name);
        priceEditText = findViewById(R.id.et_price);
        idEdiText = findViewById(R.id.et_id);
        stockEditText = findViewById(R.id.et_total_stock);
        saveButton = findViewById(R.id.btn_save);
        categoryEditText.setText(intent.getStringExtra(getString(R.string.CATEGORY_NAME)));
        barcodeFab = findViewById(R.id.fab_camera);
        mainLayout = findViewById(R.id.main_layout);
        barcodeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
            }
        });
        saveButton.setOnClickListener(view ->{

            if(idEdiText.getText().toString().trim().equals("")){
                idEdiText.setError("Enter or Scan Barcode Number!");
                return;
            }
            if(nameEditText.getText().toString().trim().equals("")){
                nameEditText.setError("Enter Product Name!");
                return;
            }
            if(priceEditText.getText().toString().equals("")){
                priceEditText.setError("Enter Product Price!");
                return;
            }
            if(stockEditText.getText().toString().equals("")){
                stockEditText.setError("Enter Product Stock");
                return;
            }

            Long id = Long.valueOf(idEdiText.getText().toString());
            String name = nameEditText.getText().toString().trim();
            float price = Float.valueOf(priceEditText.getText().toString().trim());
            String category = categoryEditText.getText().toString();
            int stock = Integer.valueOf(stockEditText.getText().toString());
            Product product = new Product(id, name, category, price, stock);
            AppExecutors.getInstance().diskIO().execute(() -> db.productDao().insertProduct(product));
            finish();
        });

    }

    private void checkCameraPermission() {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            openCameraActivity();
        } else {
            requestCameraPermission();
        }
    }

    private void requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            // Display a SnackBar with cda button to request the missing permission.
            Snackbar.make(mainLayout, "Permission required to scan barcode.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(getParent(),
                            new String[]{Manifest.permission.CAMERA},
                            PERMISSION_REQUEST_CAMERA);
                }
            }).show();
        }else{
            Snackbar.make(mainLayout, "Storage Permission is not given", Snackbar.LENGTH_LONG).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CAMERA){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Permission has been granted. Activate the package
                Snackbar.make(mainLayout, "Permission Granted", Snackbar.LENGTH_SHORT).show();
                openCameraActivity();
            }else{
                Snackbar.make(mainLayout, "Permission Denied",Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void openCameraActivity(){
        showCustomDialog();
    }


    private void showCustomDialog(){
        final Dialog dialog;
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_barcode_reader_simple);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final CodeScanner mCodeScanner;
        CodeScannerView scannerView = dialog.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setScanMode(ScanMode.CONTINUOUS);
        mCodeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        mCodeScanner.setAutoFocusEnabled(true);
        mCodeScanner.startPreview();
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
                        idEdiText.setText(result.getText());
                        mCodeScanner.releaseResources();
                        dialog.dismiss();
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}
