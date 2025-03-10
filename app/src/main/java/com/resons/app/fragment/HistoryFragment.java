package com.resons.app.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.resons.app.PrefManager;
import com.resons.app.R;
import com.resons.app.adapter.HistoryAdapter;
import com.resons.app.adapter.NotificationsAdapter;
import com.resons.app.databinding.FragmentHistoryBinding;
import com.resons.app.databinding.FragmentHomeBinding;
import com.resons.app.model.request.response.HistoryResponse;
import com.resons.app.model.request.response.NotificationsModel;
import com.resons.app.viewmodel.HistoryViewModel;
import com.resons.app.viewmodel.NotificationsViewModel;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private HistoryViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history,
                container, false);
        initUI();
        addListner();
        return binding.getRoot();
    }

    public void initUI() {
        viewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        viewModel.postData("Bearer "+new PrefManager(requireContext())
                .getvalue("token"));

        observeLiveData();
    }

    public void addListner() {

    }

    @SuppressLint("SetTextI18n")
    private void observeLiveData() {
        viewModel.getResponseLiveData().observe(requireActivity(), response -> {
            ArrayList<HistoryResponse> historyResponses = new Gson().fromJson(response.toString(),
                    new TypeToken<ArrayList<HistoryResponse>>() {
                    }.getType());

            if (historyResponses.size() > 0) {
                binding.rvHistory.setVisibility(View.VISIBLE);
                binding.tvNoDataFound.setVisibility(View.GONE);




                setAdapter(historyResponses);

            } else {
                binding.rvHistory.setVisibility(View.GONE);
                binding.tvNoDataFound.setVisibility(View.VISIBLE);
            }

        });

        viewModel.getErrorLiveData().observe(requireActivity(), error -> Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show());

        viewModel.getLoadingLiveData().observe(requireActivity(), isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setAdapter(ArrayList<HistoryResponse> historyResponses) {
        Collections.reverse(historyResponses);

        binding.rvHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvHistory.setHasFixedSize(true);
        HistoryAdapter historyAdapter = new HistoryAdapter(requireActivity(), historyResponses);
        binding.rvHistory.setAdapter(historyAdapter);
        historyAdapter.notifyDataSetChanged();
    }
}