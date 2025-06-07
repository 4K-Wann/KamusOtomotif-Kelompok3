package com.example.kamusotomotif;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "kamus.db";
    private static final int DB_VERSION = 8;

    public static final String TABLE_ISTILAH = "istilah";
    public static final String COL_ID = "id";
    public static final String COL_NAMA = "nama";
    public static final String COL_DESKRIPSI = "deskripsi";
    public static final String COL_GAMBAR = "gambar";
    public static final String COL_BOOKMARK = "bookmark";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_ISTILAH + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAMA + " TEXT, " +
                COL_DESKRIPSI + " TEXT, " +
                COL_GAMBAR + " TEXT, " +
                COL_BOOKMARK + " INTEGER DEFAULT 0)";
        db.execSQL(createTable);

        // Data awal istilah
        insertIstilah(db, "ABS", "Sistem pengereman yang mencegah roda terkunci saat pengereman mendadak.", "abs_img");
        insertIstilah(db, "EFI", "Sistem injeksi bahan bakar elektronik yang mengatur jumlah bahan bakar ke mesin.", "efi_img");
        insertIstilah(db, "ECU", "Unit kontrol elektronik yang mengatur sistem mesin secara otomatis.", "ecu_img");
        insertIstilah(db, "RPM", "Jumlah putaran poros engkol mesin dalam satu menit.", "rpm_img");
        insertIstilah(db, "CVT", "Jenis transmisi otomatis yang mengubah rasio gigi secara terus menerus.", "cvt_img");
        insertIstilah(db, "Odometer", "Alat pada kendaraan untuk mengukur jarak tempuh secara keseluruhan.", "odometer_img");
        insertIstilah(db, "Radiator", "Komponen untuk mendinginkan cairan mesin agar tidak overheat.", "radiator_img");
        insertIstilah(db, "Busi", "Komponen yang memercikkan api untuk membakar campuran bahan bakar dan udara.", "busi_img");
        insertIstilah(db, "Kampas Rem", "Bagian dari sistem rem yang bergesekan dengan cakram atau tromol.", "kampas_rem_img");
        insertIstilah(db, "Filter Udara", "Komponen yang menyaring udara sebelum masuk ke ruang bakar mesin.", "filter_udara_img");
        insertIstilah(db, "Suspensi", "Sistem peredam getaran agar kendaraan lebih nyaman dikendarai.", "suspensi_img");
        insertIstilah(db, "Differential", "Komponen yang membagi tenaga ke roda kiri dan kanan pada kendaraan penggerak roda belakang.", "differential_img");
        insertIstilah(db, "Throttle Body", "Komponen yang mengatur jumlah udara yang masuk ke mesin.", "throttle_body_img");
        insertIstilah(db, "Turbocharger", "Perangkat yang meningkatkan tenaga mesin dengan memanfaatkan gas buang.", "turbo_img");
        insertIstilah(db, "Intercooler", "Pendingin udara yang masuk ke mesin setelah melewati turbo.", "intercooler_img");
        insertIstilah(db, "Airbag", "Kantung udara pengaman yang mengembang saat terjadi tabrakan.", "airbag_img");
        insertIstilah(db, "Power Steering", "Sistem yang membantu meringankan kemudi kendaraan.", "power_steering_img");
        insertIstilah(db, "Dinamo Starter", "Komponen yang memutar mesin saat pertama kali dihidupkan.", "starter_img");
        insertIstilah(db, "Alternator", "Komponen yang menghasilkan listrik untuk mengisi aki.", "alternator_img");
        insertIstilah(db, "Katalisator", "Perangkat di knalpot untuk mengurangi emisi gas buang.", "katalisator_img");
        insertIstilah(db, "Drive Shaft", "Poros penggerak yang menyalurkan tenaga dari transmisi ke roda.", "driveshaft_img");
        insertIstilah(db, "Timing Belt", "Sabuk yang menyinkronkan putaran crankshaft dan camshaft.", "timingbelt_img");
        insertIstilah(db, "Fan Belt", "Sabuk yang menggerakkan komponen seperti alternator dan AC.", "fanbelt_img");
        insertIstilah(db, "VVT-i", "Teknologi pengaturan waktu katup agar efisien dan bertenaga.", "vvti_img");
        insertIstilah(db, "Clutch", "Komponen untuk memutus dan menghubungkan tenaga mesin ke transmisi.", "clutch_img");
        insertIstilah(db, "Headlamp", "Lampu utama untuk penerangan saat malam atau kondisi gelap.", "headlamp_img");
        insertIstilah(db, "Knuckle", "Bagian dari sistem kemudi yang menghubungkan roda dengan suspensi.", "knuckle_img");
        insertIstilah(db, "Stabilizer", "Batang penghubung untuk menjaga stabilitas saat menikung.", "stabilizer_img");
        insertIstilah(db, "Speedometer", "Alat untuk menunjukkan kecepatan kendaraan saat berjalan.", "speedometer_img");
        insertIstilah(db, "Ignition Coil", "Komponen yang mengubah tegangan rendah dari aki menjadi tegangan tinggi untuk menghasilkan percikan api di busi.", "ignition_coil_img");
    }

    // Menambahkan istilah baru (khusus digunakan saat create database)
    private void insertIstilah(SQLiteDatabase db, String nama, String deskripsi, String gambar) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NAMA, nama);
        cv.put(COL_DESKRIPSI, deskripsi);
        cv.put(COL_GAMBAR, gambar);
        db.insert(TABLE_ISTILAH, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ISTILAH);
        onCreate(db);
    }

    // Mengambil semua istilah
    public Cursor getAllIstilah() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ISTILAH + " ORDER BY " + COL_NAMA, null);
    }

    // Mengambil semua istilah yang dibookmark
    public Cursor getBookmarkedIstilah() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ISTILAH + " WHERE " + COL_BOOKMARK + "=1 ORDER BY " + COL_NAMA, null);
    }

    // Menandai istilah sebagai bookmark / unbookmark
    public void setBookmark(int id, boolean bookmarked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BOOKMARK, bookmarked ? 1 : 0);
        db.update(TABLE_ISTILAH, values, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ✅ Method untuk menghapus status bookmark (unbookmark)
    public void unbookmarkIstilah(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BOOKMARK, 0);
        db.update(TABLE_ISTILAH, values, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Mencari istilah berdasarkan kata kunci
    public Cursor searchIstilah(String keyword) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ISTILAH +
                        " WHERE " + COL_NAMA + " LIKE ? OR " + COL_DESKRIPSI + " LIKE ? ORDER BY " + COL_NAMA,
                new String[]{"%" + keyword + "%", "%" + keyword + "%"});
    }

    // Mengambil istilah berdasarkan ID
    public Cursor getIstilahById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ISTILAH + " WHERE " + COL_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
