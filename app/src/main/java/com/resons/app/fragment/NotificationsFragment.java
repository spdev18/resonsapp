package com.resons.app.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.resons.app.PrefManager;
import com.resons.app.R;
import com.resons.app.adapter.NotificationsAdapter;
import com.resons.app.databinding.FragmentNotificationsBinding;
import com.resons.app.model.request.NotificationSeenRequest;
import com.resons.app.model.request.response.NotificationsModel;
import com.resons.app.viewmodel.NotificationSeenViewModel;
import com.resons.app.viewmodel.NotificationsViewModel;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment implements NotificationsAdapter.itemClick {

    private FragmentNotificationsBinding binding;
    private NotificationsViewModel viewModel;

    private NotificationSeenViewModel notificationSeenViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false);
        initUI();
        addListner();
        return binding.getRoot();
    }

    private void initUI() {
        viewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        notificationSeenViewModel=new ViewModelProvider(this).get(NotificationSeenViewModel.class);
        viewModel.postData("Bearer " + new PrefManager(requireContext()).getvalue("token"));

        observeLiveData();
    }

    private void addListner() {

    }

    @SuppressLint("SetTextI18n")
    private void observeLiveData() {
        viewModel.getResponseLiveData().observe(requireActivity(), response -> {
            ArrayList<NotificationsModel> notificationsModel = new Gson().fromJson(response.toString(),
                    new TypeToken<ArrayList<NotificationsModel>>() {
                    }.getType());

            if (notificationsModel.size() > 0) {
                binding.rvNotifications.setVisibility(View.VISIBLE);
                binding.tvNoDataFound.setVisibility(View.GONE);
                binding.tvNotificationsCount.setVisibility(View.VISIBLE);

                int unseencount = 0;

                for (int i = 0; i <= notificationsModel.size() - 1; i++) {
                    if (!notificationsModel.get(i).isIs_read()) {
                        unseencount = unseencount + i;
                    }

                }

                binding.tvNotificationsCount.setText("Total: " + notificationsModel.size() + " | " + "Unseen: " + unseencount);

                setAdapter(notificationsModel);

            } else {
                binding.tvNotificationsCount.setVisibility(View.GONE);
                binding.rvNotifications.setVisibility(View.GONE);
                binding.tvNoDataFound.setVisibility(View.VISIBLE);
            }

        });


        notificationSeenViewModel.getResponseLiveData().observe(requireActivity(), response -> {
            if (response.getStatus()==200){
                viewModel.postData("Bearer "+new PrefManager(requireContext()).getvalue("token"));
            }else {
                Toast.makeText(requireContext(),response.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


        notificationSeenViewModel.getErrorLiveData().observe(requireActivity(), error -> Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show());

        notificationSeenViewModel.getLoadingLiveData().observe(requireActivity(), isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
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

    private void setAdapter(ArrayList<NotificationsModel> notificationsModel) {
        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvNotifications.setHasFixedSize(true);
        NotificationsAdapter notificationsAdapter = new NotificationsAdapter(requireActivity(), notificationsModel,
                this);
        binding.rvNotifications.setAdapter(notificationsAdapter);
        notificationsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(int pos, NotificationsModel notificationsModel) {
        if (!notificationsModel.isIs_read()){
            notificationSeenViewModel.postData("Bearer "+new PrefManager(requireContext())
                    .getvalue("token"), new NotificationSeenRequest(notificationsModel.getId()));
        }

    }
}