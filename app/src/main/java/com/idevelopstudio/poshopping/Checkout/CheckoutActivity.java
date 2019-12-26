package com.idevelopstudio.poshopping.Checkout;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;
import com.idevelopstudio.poshopping.Database.AppDatabase;
import com.idevelopstudio.poshopping.Database.Product;
import com.idevelopstudio.poshopping.Extra.AppExecutors;
import com.idevelopstudio.poshopping.Extra.SendRecieptEmail;
import com.idevelopstudio.poshopping.R;
import com.idevelopstudio.poshopping.tools.SwipeToDeleteCallbackCheckOutAdapter;
import com.idevelopstudio.poshopping.tools.SwipeToDeleteCallbackProductAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CheckoutActivity extends AppCompatActivity implements CheckoutAdapter.AdapterDeleteListener {
    private static final String TAG = CheckoutActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CAMERA = 200;

    private FloatingActionButton fab;
    private View mainLayout;
    private List<Product> products;
    AppDatabase db;
    private RecyclerView recyclerView;
    private Product product;
    private CheckoutAdapter checkoutAdapter;
    private LottieAnimationView emptyStateLottie;
    private View emptyStateLayout;
    private TextView totalPriceTextView;
    private Button sendEmailButton;
    private int maxCounterValue; // used to limit the + button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        products = new ArrayList<>();
        setContentView(R.layout.activity_checkout);
        mainLayout = findViewById(R.id.main_layout);
        db = AppDatabase.getInstance(getApplicationContext());
        fab = findViewById(R.id.fab_camera);
        fab.setOnClickListener(v -> checkCameraPermission());
        recyclerView = findViewById(R.id.rv_checkout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        checkoutAdapter = new CheckoutAdapter(this, this);
        recyclerView.setAdapter(checkoutAdapter);
        emptyStateLottie = findViewById(R.id.lottie_empty_state);
        emptyStateLayout = findViewById(R.id.layout_empty_state);
        totalPriceTextView = findViewById(R.id.tv_total_price);
        sendEmailButton = findViewById(R.id.btn_send_email);
        sendEmailButton.setEnabled(false);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallbackCheckOutAdapter(checkoutAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (products.get(0).getName() != null || products.get(0).getName().equals(""))
                    showEmailDialog();
            }
        });
        showEmptyState();
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
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
        } else {
            Snackbar.make(mainLayout, "Storage Permission is not given", Snackbar.LENGTH_LONG).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Activate the package
                Snackbar.make(mainLayout, "Permission Granted", Snackbar.LENGTH_SHORT).show();
                openCameraActivity();
            } else {
                Snackbar.make(mainLayout, "Permission Denied", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void openCameraActivity() {
        showCustomDialog();
    }


    private void showCustomDialog() {
        final Dialog dialog;
        dialog = new Dialog(this);
        AtomicInteger count = new AtomicInteger(1);
        maxCounterValue = 1;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_barcode_reader);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final CodeScanner mCodeScanner;
        CodeScannerView scannerView = dialog.findViewById(R.id.scanner_view);
        TextView counter = dialog.findViewById(R.id.tv_counter);
        ImageView plus = dialog.findViewById(R.id.iv_add);
        ImageView minus = dialog.findViewById(R.id.iv_subtract);
        ImageView close = dialog.findViewById(R.id.iv_close);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setScanMode(ScanMode.SINGLE);
        mCodeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        mCodeScanner.setAutoFocusEnabled(true);
        mCodeScanner.startPreview();
        View buttonCardLayout = dialog.findViewById(R.id.layout_button_card);
        Button addButton = dialog.findViewById(R.id.btn_add);
        addButton.setEnabled(false);
        plus.setEnabled(false);
        minus.setEnabled(false);
        plus.setImageDrawable(getDrawable(R.drawable.plus_disabled));
        minus.setImageDrawable(getDrawable(R.drawable.minus_disabled));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        plus.setOnClickListener(v -> {
            if (count.get() != maxCounterValue) {
                counter.setText(String.valueOf(Integer.valueOf(counter.getText().toString()) + 1));
                count.getAndIncrement();
            }
        });
        minus.setOnClickListener(v -> {
            if (Integer.valueOf(counter.getText().toString()) != 1) {
                counter.setText(String.valueOf(Integer.valueOf(counter.getText().toString()) - 1));
                count.getAndDecrement();
            }
        });

        buttonCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                product = db.productDao().getProductById(Long.valueOf(result.getText()));
                if(product != null){
                    if (product.getStock() > 0) {
                        maxCounterValue = product.getStock();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mCodeScanner.stopPreview();
                                addButton.setEnabled(true);
                                plus.setEnabled(true);
                                minus.setEnabled(true);
                                plus.setImageDrawable(getDrawable(R.drawable.plus));
                                minus.setImageDrawable(getDrawable(R.drawable.minus));

                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CheckoutActivity.this, "Product not in stock", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CheckoutActivity.this, "Unknown Product", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null) {
                    if (count.get() > 1) {
                        for (int i = 1; i <= count.get(); i++) {
                            products.add(product);
                        }
                        long id = product.getId();
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                db.productDao().updateProductStock(product.getStock() - count.get(), id);
                            }
                        });
                    } else {
                        products.add(product);
                        long id = product.getId();
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                db.productDao().updateProductStock(product.getStock() - 1, id);
                            }
                        });
                    }
                    addProductToList();
                    count.set(1);
                    counter.setText(String.valueOf(1));
                    maxCounterValue = 1;
                    product = null;
                    mCodeScanner.startPreview();
                    addButton.setEnabled(false);
                    plus.setEnabled(false);
                    minus.setEnabled(false);
                    plus.setImageDrawable(getDrawable(R.drawable.plus_disabled));
                    minus.setImageDrawable(getDrawable(R.drawable.minus_disabled));
                    sendEmailButton.setEnabled(true);
                }

            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
                addButton.setEnabled(false);
                product = null;
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mCodeScanner.stopPreview();
                if (products != null) {
                    checkoutAdapter.setProducts(products);
                    hideEmptyState();
                }
            }
        });
    }

    private void showEmailDialog() {
        Dialog dialog;
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edittext);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        EditText editText = dialog.findViewById(R.id.et_email);
        Button sendEmailButton = dialog.findViewById(R.id.btn_send_email);
        sendEmailButton.setOnClickListener(v -> {
            String email = editText.getText().toString().trim();
            if (email.isEmpty()) {
                editText.setError("Email Required!");
                editText.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editText.setError("Valid Email Required!");
                editText.requestFocus();
                return;
            }

            if (products.get(0).getName() != null || products.get(0).getName().equals("")) {
                SendRecieptEmail.sendEmail(products, Float.valueOf(totalPriceTextView.getText().toString()), email);
                Toast.makeText(this, "Email Sent!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                onBackPressed();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showEmptyState() {
        emptyStateLayout.setVisibility(View.VISIBLE);
        emptyStateLottie.playAnimation();
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void hideEmptyState() {
        emptyStateLayout.setVisibility(View.INVISIBLE);
        emptyStateLottie.pauseAnimation();
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void addProductToList() {
        checkoutAdapter.setProducts(products);
        float sum = 0;
        for (Product product : products) {
            sum += product.getPrice();
        }

        totalPriceTextView.setText(String.valueOf(sum));

    }

    @Override
    public void onAdapterDelete(Product product) {
        float sum = Float.parseFloat(totalPriceTextView.getText().toString());
        sum -= product.getPrice();
        totalPriceTextView.setText(String.valueOf(sum));
    }
}
