package com.example.kamusotomotif;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ListView listView;
    private SearchView searchView;
    private DBHelper dbHelper;

    private ArrayList<Integer> listId = new ArrayList<>();
    private ArrayList<String> listNama = new ArrayList<>();
    private ArrayList<String> listDeskripsi = new ArrayList<>();
    private ArrayList<String> listGambar = new ArrayList<>();

    private IstilahAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = view.findViewById(R.id.list_view);
        searchView = view.findViewById(R.id.search_view);
        dbHelper = new DBHelper(getContext());

        adapter = new IstilahAdapter(getContext(), listNama, listDeskripsi, listGambar);
        listView.setAdapter(adapter);

        tampilkanSemuaData();

        // Atur warna teks dan hint SearchView agar kontras di semua mode
        int searchTextId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        TextView searchText = searchView.findViewById(searchTextId);

        if (searchText != null) {
            searchText.setTextColor(ContextCompat.getColor(requireContext(), R.color.textPrimary));
            searchText.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.textPrimary));
        }

        // Ubah warna ikon kaca pembesar (search icon)
        int searchIconId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_mag_icon", null, null);
        ImageView searchIcon = searchView.findViewById(searchIconId);

        if (searchIcon != null) {
            searchIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.textPrimary));
        }

        // Ubah warna ikon clear (X) saat mengetik
        int closeIconId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeIcon = searchView.findViewById(closeIconId);

        if (closeIcon != null) {
            closeIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.textPrimary));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cariIstilah(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    tampilkanSemuaData();
                } else {
                    cariIstilah(newText);
                }
                return true;
            }
        });

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            if (position >= 0 && position < listId.size()) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("id", listId.get(position));
                intent.putExtra("nama", listNama.get(position));
                intent.putExtra("deskripsi", listDeskripsi.get(position));
                intent.putExtra("gambar", listGambar.get(position));
                startActivity(intent);
            }
        });

        return view;
    }

    private void tampilkanSemuaData() {
        listId.clear();
        listNama.clear();
        listDeskripsi.clear();
        listGambar.clear();

        Cursor cursor = null;
        try {
            cursor = dbHelper.getAllIstilah();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    listId.add(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_ID)));
                    listNama.add(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_NAMA)));
                    listDeskripsi.add(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_DESKRIPSI)));
                    listGambar.add(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_GAMBAR)));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        adapter.notifyDataSetChanged();
    }

    private void cariIstilah(String keyword) {
        listId.clear();
        listNama.clear();
        listDeskripsi.clear();
        listGambar.clear();

        Cursor cursor = null;
        try {
            cursor = dbHelper.searchIstilah(keyword);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    listId.add(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_ID)));
                    listNama.add(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_NAMA)));
                    listDeskripsi.add(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_DESKRIPSI)));
                    listGambar.add(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_GAMBAR)));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        adapter.notifyDataSetChanged();
    }
}
