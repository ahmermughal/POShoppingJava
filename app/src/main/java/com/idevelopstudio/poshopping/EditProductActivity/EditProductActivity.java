package com.idevelopstudio.poshopping.EditProductActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.idevelopstudio.poshopping.Database.AppDatabase;
import com.idevelopstudio.poshopping.R;

public class EditProductActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_edit_product);
    }
}
