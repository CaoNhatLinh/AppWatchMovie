package com.appxemphim.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appxemphim.Api.ApiType;
import com.appxemphim.R;
import com.appxemphim.Utils.ItemClickSupport;
import com.appxemphim.Utils.Utils;
import com.appxemphim.activities.ChiTietPhimActivity;
import com.appxemphim.activities.SearchActivity;
import com.appxemphim.adapters.ListPhimAdapter;
import com.appxemphim.dao.PhimDAO;
import com.appxemphim.data.Phim;

import java.util.ArrayList;
import java.util.List;


public class ListPhimFragment extends Fragment {

    private RecyclerView recyclerView;
    private PhimDAO phimDAO;
    private ListPhimAdapter phimAdapter;
    public ListPhimFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_phim, container, false);
        phimDAO = new PhimDAO();
        recyclerView = view.findViewById(R.id.recyclerViewListPhim);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // 3 columns
        phimAdapter = new ListPhimAdapter(getContext(),new ArrayList<>());
        recyclerView.setAdapter(phimAdapter);
        Utils.setupRecyclerViewClickListener(getActivity(), recyclerView, phimAdapter, ChiTietPhimActivity.class);
        EditText etTimKiem = view.findViewById(R.id.etTimKiem);
        etTimKiem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        loadPhim(ApiType.ALL_PHIM, phimAdapter);
    }
    private void loadPhim(ApiType apiType, final ListPhimAdapter adapter) {
        phimDAO.fetchPhimList(apiType, new PhimDAO.PhimCallback() {
            @Override
            public void onSuccess(List<Phim> phimList) {
                adapter.updatePhimList(phimList);
            }

            @Override
            public void onFailure(String message) {
                Log.e("MainActivity", "Failed to fetch data: " + message);
            }
        });
    }
}