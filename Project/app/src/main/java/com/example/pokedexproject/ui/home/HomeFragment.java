package com.example.pokedexproject.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pokedexproject.R;
import com.example.pokedexproject.databinding.FragmentHomeBinding;
import com.example.pokedexproject.models.Pokemon;
import com.example.pokedexproject.ui.shared.SharedViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements PokemonAdapter.OnPokemonClickListener {

    private FragmentHomeBinding binding;
    private PokemonAdapter pokemonAdapter;
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        setupRecyclerView();

        // Observe the shared Pokémon list
        sharedViewModel.getPokemonListLiveData().observe(getViewLifecycleOwner(), pokemons -> {
            pokemonAdapter.updatePokemonList(pokemons);
        });

        // Fetch Pokémon data if the list is empty
        if (sharedViewModel.getPokemonListLiveData().getValue().isEmpty()) {
            fetchPokemons();
        }

        return root;
    }

    private void setupRecyclerView() {
        pokemonAdapter = new PokemonAdapter(new ArrayList<>(), this);
        binding.recyclerViewPokemons.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewPokemons.setAdapter(pokemonAdapter);
    }

    private void fetchPokemons() {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // Pass the fetched Pokémon list to SharedViewModel
        homeViewModel.fetchPokemons(pokemons -> sharedViewModel.setPokemonList(pokemons));
    }

    @Override
    public void onPokemonClick(Pokemon pokemon) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("pokemon", pokemon);

        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_pokemonFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
