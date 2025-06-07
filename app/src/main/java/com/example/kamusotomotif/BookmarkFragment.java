package com.example.kamusotomotif;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment {

    private ListView listView;
    private DBHelper dbHelper;
    private ArrayList<Integer> listId = new ArrayList<>();
    private ArrayList<String> listNama = new ArrayList<>();
    private ArrayList<String> listDeskripsi = new ArrayList<>();
    private ArrayList<String> listGambar = new ArrayList<>();

    private IstilahAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        listView = view.findViewById(R.id.list_view);
        dbHelper = new DBHelper(getContext());

        tampilkanBookmark();

        adapter = new IstilahAdapter(getContext(), listNama, listDeskripsi, listGambar, listId, true);
        listView.setAdapter(adapter);

        // Klik item -> buka DetailActivity
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra("id", listId.get(position));
            intent.putExtra("nama", listNama.get(position));
            intent.putExtra("deskripsi", listDeskripsi.get(position));
            intent.putExtra("gambar", listGambar.get(position));
            startActivityForResult(intent, 101); // penting untuk pembaruan bookmark
        });

        // Klik lama item -> konfirmasi hapus bookmark
        listView.setOnItemLongClickListener((parent, view1, position, id) -> {
            int selectedId = listId.get(position);
            String selectedNama = listNama.get(position);

            new AlertDialog.Builder(getContext())
                    .setTitle("Hapus Bookmark")
                    .setMessage("Apakah Anda yakin ingin menghapus \"" + selectedNama + "\" dari Bookmark?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        dbHelper.setBookmark(selectedId, false); // hapus dari database
                        hapusItemById(selectedId); // hapus dari list lokal
                        Toast.makeText(getContext(), "Bookmark dihapus", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Batal", null)
                    .show();

            return true;
        });

        return view;
    }

    private void tampilkanBookmark() {
        listId.clear();
        listNama.clear();
        listDeskripsi.clear();
        listGambar.clear();

        Cursor cursor = dbHelper.getBookmarkedIstilah();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listId.add(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_ID)));
                listNama.add(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_NAMA)));
                listDeskripsi.add(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_DESKRIPSI)));
                listGambar.add(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_GAMBAR)));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == getActivity().RESULT_OK && data != null) {
            int deletedId = data.getIntExtra("deletedId", -1);
            if (deletedId != -1) {
                hapusItemById(deletedId);
            }
        }
    }

    private void hapusItemById(int deletedId) {
        for (int i = 0; i < listId.size(); i++) {
            if (listId.get(i) == deletedId) {
                listId.remove(i);
                listNama.remove(i);
                listDeskripsi.remove(i);
                listGambar.remove(i);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
