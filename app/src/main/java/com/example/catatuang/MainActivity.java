package com.example.catatuang;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catatuang.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView tvIncome, tvExpanses, tvBalance, tvEmail, tvUser;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    Button btnadd;
    Cursor cursor;
    private SessionManagement sessionManagement;
    RecyclerView rv;
    private ArrayList<String> dataId,dataTanggal, dataKeterangan, dataJumlah;
    DatabaseHelper dbcenter;
    String id="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        dbcenter = new DatabaseHelper(this);
        setSupportActionBar(toolbar);
        sessionManagement = new SessionManagement(MainActivity.this);
        tvIncome = findViewById(R.id.income);
        tvExpanses = findViewById(R.id.expenses);
        tvBalance=findViewById(R.id.balance);
        tvEmail = findViewById(R.id.txtemail);
        tvUser = findViewById(R.id.txtusername);

        dataId = new ArrayList<>();
        dataTanggal = new ArrayList<>();
        dataKeterangan=new ArrayList<>();
        dataJumlah = new ArrayList<>();


        if(sessionManagement.isLoggedIn()) {
            final HashMap<String, String> user = sessionManagement.getUserInformation();
            SQLiteDatabase db = dbcenter.getReadableDatabase();
            int i = 0;
            int e = 0;
            //mengambil jumlah income user dari sqlite
            cursor = db.rawQuery("SELECT SUM(jumlah) FROM transaksi WHERE id_user = '" + user.get(sessionManagement.KEY_ID_USER) + "' and jenis = 'Income'", null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) //jika hasil query tidak kosong
            {
                cursor.moveToPosition(0);
                if (cursor.isNull(0)) {
                    tvIncome.setText("0"); //jika jumlah nya null isi text view dengan angka 0
                    i = 0;
                } else { //jika tidak null isi sesuai databasw
                    tvIncome.setText(cursor.getString(0).toString());
                    i = Integer.parseInt(cursor.getString(0).toString());
                }

            }
            //mengambil jumlah expenses user dari sqlite
            cursor = db.rawQuery("SELECT SUM(jumlah) FROM transaksi WHERE id_user = '" + user.get(sessionManagement.KEY_ID_USER) + "' and jenis = 'Expenses'", null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) //jika hasil query tidak kosong
            {
                cursor.moveToPosition(0);
                if (cursor.isNull(0)) { //jika jumlah nya null isi text view dengan angka 0
                    tvExpanses.setText("0");
                    e = 0;
                } else{
                    tvExpanses.setText(cursor.getString(0).toString());
                    e = Integer.parseInt(cursor.getString(0).toString());
                }
            }
            int total = i-e; //total diperoleh dari income - expenses
            tvBalance.setText(Integer.toString(total));
            id=user.get(sessionManagement.KEY_ID_USER);
        }

        initDataset();
        rv = (RecyclerView) findViewById(R.id.rv_main);
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        adapter = new MyAdapter(dataId,dataTanggal,dataKeterangan,dataJumlah);
        rv.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent m = new Intent(getApplicationContext(),tambahCatatan.class);
                startActivity(m);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void initDataset() {

        SQLiteDatabase db = dbcenter.getReadableDatabase();
        //mengambil data dari database untuk isi dari recycler view
        cursor = db.rawQuery("SELECT * FROM transaksi where id_user = "+ id,null);
        cursor.moveToFirst();
        for (int cc=0; cc < cursor.getCount(); cc++){
            cursor.moveToPosition(cc);
            //masukkan data kedalam array
            dataId.add(cursor.getString(0).toString());
            dataTanggal.add(cursor.getString(1).toString());
            dataKeterangan.add(cursor.getString(6).toString());
            dataJumlah.add(cursor.getString(5).toString());
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_incomegraf) {
            Intent i = new Intent(getApplicationContext(),Graph.class);
            startActivity(i);
        } else if (id == R.id.nav_expgaf) {
            Intent m = new Intent(getApplicationContext(),GraphExpenses.class);
            startActivity(m);
        } else if (id == R.id.nav_home) {
            Intent m = new Intent(this,MainActivity.class);
            startActivity(m);
        } else if (id == R.id.nav_logout) {
            sessionManagement.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}