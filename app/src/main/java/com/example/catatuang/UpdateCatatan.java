package com.example.catatuang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class UpdateCatatan extends AppCompatActivity {

    Button addincome, addexp, camera;
    Context context;
    Spinner kategori;
    ImageView imgnote;
    final int sdk = android.os.Build.VERSION.SDK_INT;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText tvDateResult;
    private Button btDatePicker , save;
    EditText tanggal, keterangan, jumlah;
    String imageFilePath;
    String jenis = "Income";
    SessionManagement sessionManagement;
    DatabaseHelper dbcenter;
    Button back;

    //aray kategori dari income dan expenses
    private static final String[] pathIncome = {"Salary", "Bonus", "Allowance", "Petty cash", "Other"};
    private static final String[] pathExpenses = {"Food", "Social Life", "Transportation", "Gift", "Healt", "Other"};
    private final int versi = 1;
    String kat, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_catatan);
        sessionManagement = new SessionManagement(UpdateCatatan.this);
        final HashMap<String, String> user = sessionManagement.getUserInformation();
        dbcenter = new DatabaseHelper(this); //membuat objek dari class datahelper
        addincome = (Button) findViewById(R.id.addincome);
        addexp = (Button) findViewById(R.id.addexp);
        imgnote = (ImageView) findViewById(R.id.imgnote);
        tanggal = (EditText) findViewById(R.id.date);
        keterangan = (EditText) findViewById(R.id.edtnote);
        jumlah = (EditText) findViewById(R.id.edtjumlah);
        kategori = findViewById(R.id.spinnerCategory);
        save = (Button)  findViewById(R.id.btnsave);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        //mengatur isi dari text view sesuai data
        tanggal.setText(getIntent().getStringExtra("tanggal"));
        jumlah.setText(getIntent().getStringExtra("jumlah"));
        keterangan.setText(getIntent().getStringExtra("keterangan"));
        kat = getIntent().getStringExtra("kategori");
        id = getIntent().getStringExtra("idTran");
        jenis = getIntent().getStringExtra("jenis");


        if (jenis.equals("Income")) {
            jenis = "Income";
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {

            } else {

                //mengubah warna tampilan button
                addincome.setBackground(ContextCompat.getDrawable(UpdateCatatan.this, R.color.black));
                addincome.setTextColor(ContextCompat.getColor(UpdateCatatan.this, R.color.white));
                addincome.setTextColor(ContextCompat.getColor(UpdateCatatan.this, R.color.white));
                addexp.setBackground(ContextCompat.getDrawable(UpdateCatatan.this, R.color.white));
                addexp.setTextColor(ContextCompat.getColor(UpdateCatatan.this, R.color.black));

                //mengatur isi dari spinner sesuai kategori yang dipilih
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateCatatan.this,
                        android.R.layout.simple_spinner_item, pathIncome);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                kategori.setAdapter(adapter);
                int select = 0;
                for (int i = 0; i < pathIncome.length; i++) {
                    if (pathIncome[i].equals(kategori)) {
                        select = i;
                        break;
                    }
                }
                kategori.setSelection(select);
            }
        }else{
            jenis = "Expenses";
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {



            } else {
                addexp.setBackground(ContextCompat.getDrawable(UpdateCatatan.this, R.color.black));
                addexp.setTextColor(ContextCompat.getColor(UpdateCatatan.this, R.color.white));
                addincome.setBackground(ContextCompat.getDrawable(UpdateCatatan.this, R.color.white));
                addincome.setTextColor(ContextCompat.getColor(UpdateCatatan.this, R.color.black));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateCatatan.this,
                        android.R.layout.simple_spinner_item, pathExpenses);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                kategori.setAdapter(adapter);
                int select =0;
                for (int i=0 ; i < pathExpenses.length ; i++){
                    if (pathExpenses[i].equals(kategori)){
                        select=i;
                        break;
                    }
                }
                kategori.setSelection(select);

            }
        }

        addincome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                jenis = "Income";
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {

                } else {
                    addincome.setBackground(ContextCompat.getDrawable(UpdateCatatan.this, R.color.black));
                    addincome.setTextColor(ContextCompat.getColor(UpdateCatatan.this, R.color.white));
                    addincome.setTextColor(ContextCompat.getColor(UpdateCatatan.this, R.color.white));
                    addexp.setBackground(ContextCompat.getDrawable(UpdateCatatan.this, R.color.white));
                    addexp.setTextColor(ContextCompat.getColor(UpdateCatatan.this, R.color.black));
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateCatatan.this,
                            android.R.layout.simple_spinner_item, pathIncome);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kategori.setAdapter(adapter);

                }
            }
        });
        addexp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                jenis = "Expenses";
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    addexp.setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.black));
                    addexp.setTextColor(ContextCompat.getColor(context, R.color.white));
                    addincome.setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.white));


                } else {
                    addexp.setBackground(ContextCompat.getDrawable(UpdateCatatan.this, R.color.black));
                    addexp.setTextColor(ContextCompat.getColor(UpdateCatatan.this, R.color.white));
                    addincome.setBackground(ContextCompat.getDrawable(UpdateCatatan.this, R.color.white));
                    addincome.setTextColor(ContextCompat.getColor(UpdateCatatan.this, R.color.black));
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateCatatan.this,
                            android.R.layout.simple_spinner_item, pathExpenses);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kategori.setAdapter(adapter);

                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//menyimpan data hasil update catatan
                dbcenter.updateTransaksi(Integer.parseInt(id),tanggal.getText().toString(), jenis, kategori.getSelectedItem().toString(), Integer.parseInt(user.get(sessionManagement.KEY_ID_USER)),
                        Integer.parseInt(jumlah.getText().toString()), keterangan.getText().toString());
                Intent m = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(m);
            }
        });
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        tvDateResult = (EditText) findViewById(R.id.date);
        btDatePicker = (Button) findViewById(R.id.calendar);
        btDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        camera = (Button) findViewById(R.id.btncamera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //halaman akan berpindah ke kamera
                startActivityForResult(i, versi);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if (requestCode == REQUEST_IMAGE_CAPTURE) {
        //don't compare the data to null, it will always come as  null because we are providing a file URI, so load with the imageFilePath we obtained before opening the cameraIntent

        // If you are using Glide.
        //}
        super.onActivityResult(requestCode, resultCode, data);
        if (this.versi == requestCode && resultCode == RESULT_OK) { //menampilkan hasil foto di imageview
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgnote.setImageBitmap(bitmap);
            imgnote.setMinimumHeight(170);
            imgnote.setMinimumWidth(200);

        }
    }
    private void showDateDialog() {

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                tvDateResult.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }
}