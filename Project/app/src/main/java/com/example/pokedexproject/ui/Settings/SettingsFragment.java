package com.example.pokedexproject.ui.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pokedexproject.databinding.FragmentSettingsBinding;
import com.example.pokedexproject.ui.Settings.SettingsViewModel;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SettingsViewModel settingsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);

        binding.buttonDownload.setOnClickListener(v -> {
            settingsViewModel.downloadDataToFirestore();
            Toast.makeText(getContext(), "Pokémon data downloaded to Firestore", Toast.LENGTH_SHORT).show();
        });

        binding.buttonDelete.setOnClickListener(v -> {
            settingsViewModel.deleteDataFromFirestore();
            Toast.makeText(getContext(), "Pokémon data deleted from Firestore", Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
