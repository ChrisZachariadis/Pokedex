package com.example.pokedexproject.ui.pokemonDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pokedexproject.R;
import com.example.pokedexproject.models.Pokemon;
import com.squareup.picasso.Picasso;

public class PokemonDetailsFragment extends Fragment {

    private PokemonDetailsViewModel pokemonDetailsViewModel;

    private TextView nameTextView, typeTextView, weightTextView, heightTextView, baseExperienceTextView;
    private ImageView frontImageView, backImageView;

    public static PokemonDetailsFragment newInstance() {
        return new PokemonDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pokemon_details, container, false);

        // Initialize UI components
        nameTextView = root.findViewById(R.id.textViewPokemonName);
        typeTextView = root.findViewById(R.id.textViewPokemonType);
        weightTextView = root.findViewById(R.id.textViewPokemonWeight);
        heightTextView = root.findViewById(R.id.textViewPokemonHeight);
        baseExperienceTextView = root.findViewById(R.id.textViewPokemonBaseExperience);
        frontImageView = root.findViewById(R.id.imageViewPokemonFront);
        backImageView = root.findViewById(R.id.imageViewPokemonBack);

        // Initialize ViewModel
        pokemonDetailsViewModel = new ViewModelProvider(this).get(PokemonDetailsViewModel.class);

        // Observe LiveData in ViewModel to update UI when Pokémon data is available
        pokemonDetailsViewModel.getPokemonLiveData().observe(getViewLifecycleOwner(), this::displayPokemonDetails);

        // Retrieve Pokémon data from arguments and set it in the ViewModel
        if (getArguments() != null) {
            Pokemon pokemon = (Pokemon) getArguments().getSerializable("pokemon");
            if (pokemon != null) {
                pokemonDetailsViewModel.setPokemon(pokemon);
            }
        }

        return root;
    }

    private void displayPokemonDetails(Pokemon pokemon) {
        if (pokemon == null) return;

        nameTextView.setText(pokemon.getName());
        typeTextView.setText("Type: " + pokemon.getType());
        weightTextView.setText("Weight: " + pokemon.getWeight());
        heightTextView.setText("Height: " + pokemon.getHeight());
        baseExperienceTextView.setText("Base Experience: " + pokemon.getBaseExperience());

        // Load images using Picasso
        if (pokemon.getImageFront() != null && !pokemon.getImageFront().isEmpty()) {
            Picasso.get().load(pokemon.getImageFront()).into(frontImageView);
        }
        if (pokemon.getImageBack() != null && !pokemon.getImageBack().isEmpty()) {
            Picasso.get().load(pokemon.getImageBack()).into(backImageView);
        }
    }
}
