package com.example.catatuang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.view.PieChartView;


public class GraphExpenses extends AppCompatActivity {
    Cursor cursor;
    PieChartView pieChartView;
    DatabaseHelper dbcenter;
    SessionManagement sessionManagement;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_expenses);

        back = findViewById(R.id.back);
        PieChart pieChart = findViewById(R.id.pieChart);
        dbcenter = new DatabaseHelper(this);

        back.setOnClickListener(new View.OnClickListener() {//memberikan perintah jika button back di klik maka akan berpindah ke halaman menu
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class); //berpindah ke halaman menu
                startActivity(i);
            }
        });

        ArrayList<PieEntry> visitors = new ArrayList<>();
        sessionManagement = new SessionManagement(GraphExpenses.this);
        if (sessionManagement.isLoggedIn()) {
            final HashMap<String, String> user = sessionManagement.getUserInformation();

            SQLiteDatabase db = dbcenter.getReadableDatabase();
            cursor = db.rawQuery("SELECT SUM(jumlah) as total, kategori FROM transaksi WHERE id_user = '" + user.get(sessionManagement.KEY_ID_USER) + "' and jenis = 'Expenses' group by kategori", null);
            cursor.moveToFirst();

            for (int cc = 0; cc < cursor.getCount(); cc++) {
                cursor.moveToPosition(cc);
                int jumlah = Integer.parseInt(cursor.getString(0).toString());
                String kategori = cursor.getString(1).toString();
                //agar warna berbeda , buat if else sehingga memiliki warna beda tiap indeks cursor
                if(cc == 0){
                    visitors.add(new PieEntry(jumlah, kategori));
                }else if (cc == 1){
                    visitors.add(new PieEntry(jumlah, kategori));
                }else if(cc == 2){
                    visitors.add(new PieEntry(jumlah, kategori));
                }else if(cc == 3){
                    visitors.add(new PieEntry(jumlah, kategori));
                }else if(cc == 4){
                    visitors.add(new PieEntry(jumlah, kategori));
                }
            }
        }

        PieDataSet pieDataSet = new PieDataSet(visitors, "EXPENSES GRAPHIC");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("EXPENSES GRAPHIC BAR");
        pieChart.animate();
    }
}