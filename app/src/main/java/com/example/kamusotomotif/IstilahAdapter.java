package com.example.kamusotomotif;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class IstilahAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> namaList;
    private List<String> deskripsiList;
    private List<String> gambarList;
    private List<Integer> idList; // Untuk mode bookmark
    private boolean isBookmarkMode = false;
    private DBHelper dbHelper;

    public IstilahAdapter(@NonNull Context context,
                          @NonNull List<String> nama,
                          @NonNull List<String> deskripsi,
                          @NonNull List<String> gambar) {
        super(context, R.layout.list_item_custom, nama);
        this.context = context;
        this.namaList = nama;
        this.deskripsiList = deskripsi;
        this.gambarList = gambar;
        this.dbHelper = new DBHelper(context);
    }

    // Konstruktor tambahan untuk mode bookmark (dengan id dan hapus)
    public IstilahAdapter(@NonNull Context context,
                          @NonNull List<String> nama,
                          @NonNull List<String> deskripsi,
                          @NonNull List<String> gambar,
                          @NonNull List<Integer> idList,
                          boolean isBookmarkMode) {
        this(context, nama, deskripsi, gambar);
        this.idList = idList;
        this.isBookmarkMode = isBookmarkMode;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_custom, parent, false);
            holder = new ViewHolder();
            holder.tvNama = convertView.findViewById(R.id.tv_nama);
            holder.tvDeskripsi = convertView.findViewById(R.id.tv_deskripsi);
            holder.imgIcon = convertView.findViewById(R.id.img_icon);
            holder.btnDelete = convertView.findViewById(R.id.btn_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvNama.setText(namaList.get(position));
        holder.tvDeskripsi.setText("Lihat selengkapnya");
        holder.tvDeskripsi.setTextColor(context.getResources().getColor(R.color.primary));

        int resId = context.getResources().getIdentifier(gambarList.get(position), "drawable", context.getPackageName());
        holder.imgIcon.setImageResource(resId != 0 ? resId : R.drawable.placeholder);

        // Tombol hapus hanya tampil di mode bookmark
        if (isBookmarkMode) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(v -> {
                String namaIstilah = namaList.get(position);
                int id = idList.get(position);

                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle("Hapus Bookmark")
                        .setMessage("Yakin ingin menghapus \"" + namaIstilah + "\" dari bookmark?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            dbHelper.unbookmarkIstilah(id); // Hapus dari DB

                            namaList.remove(position);
                            deskripsiList.remove(position);
                            gambarList.remove(position);
                            idList.remove(position);
                            notifyDataSetChanged();
                        })
                        .setNegativeButton("Batal", null)
                        .create();

                alertDialog.setOnShowListener(dialogInterface -> {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setTextColor(context.getResources().getColor(android.R.color.white));
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                            .setTextColor(context.getResources().getColor(android.R.color.white));
                });

                alertDialog.show();
            });
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tvNama, tvDeskripsi;
        ImageView imgIcon, btnDelete;
    }
}
