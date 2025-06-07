package com.example.kamusotomotif;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    DBHelper dbHelper;
    int id;
    TextView tvNama, tvDeskripsi;
    ImageView imgGambar;
    Button btnBookmark;
    boolean isBookmarked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvNama = findViewById(R.id.tv_nama);
        tvDeskripsi = findViewById(R.id.tv_deskripsi);
        imgGambar = findViewById(R.id.img_gambar);
        btnBookmark = findViewById(R.id.btn_bookmark);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String deskripsi = intent.getStringExtra("deskripsi");
        String gambar = intent.getStringExtra("gambar");
        id = intent.getIntExtra("id", -1);

        if (nama != null && deskripsi != null && gambar != null) {
            tampilkanData(nama, deskripsi, gambar);
            if (id != -1) {
                cekStatusBookmark(id);
            } else {
                isBookmarked = false;
                updateButtonText();
            }
        } else if (id != -1) {
            loadDataDariDatabase(id);
        } else {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnBookmark.setOnClickListener(v -> {
            try {
                isBookmarked = !isBookmarked;
                if (id != -1) {
                    dbHelper.setBookmark(id, isBookmarked);
                    updateButtonText();

                    if (!isBookmarked) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("deletedId", id);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(this, "Ditambahkan ke Simpan", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal menyimpan bookmark.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tampilkanData(String nama, String deskripsi, String gambar) {
        tvNama.setText(nama);
        tvDeskripsi.setText(deskripsi);
        int resId = getResources().getIdentifier(gambar, "drawable", getPackageName());
        imgGambar.setImageResource(resId != 0 ? resId : R.drawable.placeholder);
    }

    private void loadDataDariDatabase(int id) {
        Cursor cursor = null;
        try {
            cursor = dbHelper.getIstilahById(id);
            if (cursor != null && cursor.moveToFirst()) {
                String nama = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_NAMA));
                String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_DESKRIPSI));
                String gambar = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_GAMBAR));
                tampilkanData(nama, deskripsi, gambar);

                int bookmarkVal = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKMARK));
                isBookmarked = bookmarkVal == 1;
                updateButtonText();
            } else {
                Toast.makeText(this, "Istilah tidak ditemukan", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
            finish();
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    private void cekStatusBookmark(int id) {
        Cursor cursor = null;
        try {
            cursor = dbHelper.getIstilahById(id);
            if (cursor != null && cursor.moveToFirst()) {
                int bookmarkVal = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_BOOKMARK));
                isBookmarked = bookmarkVal == 1;
            } else {
                isBookmarked = false;
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        updateButtonText();
    }

    private void updateButtonText() {
        btnBookmark.setText(isBookmarked ? "Hapus Simpan" : "Simpan");
    }
}
