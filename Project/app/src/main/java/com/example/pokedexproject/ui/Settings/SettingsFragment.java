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
import com.example.pokedexproject.models.Pokemon;
import com.example.pokedexproject.ui.shared.SharedViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SettingsViewModel settingsViewModel;
    private SharedViewModel sharedViewModel;
    private static final String IMAGE_URL = "https://img.pokemondb.net/artwork/large/snorlax.jpg";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize ViewModels
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Load image into ImageView using Picasso
        Picasso.get()
                .load(IMAGE_URL)
                .into(binding.imagePlaceholder);

        // Set up Download button
        binding.buttonDownload.setOnClickListener(v -> {
            List<Pokemon> pokemonList = sharedViewModel.getPokemonListLiveData().getValue();
            if (pokemonList != null && !pokemonList.isEmpty()) {
                settingsViewModel.downloadDataToFirestore(pokemonList);
                Toast.makeText(getContext(), "Pokémon data downloaded to Firestore", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "No Pokémon data to download", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up Delete button
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
