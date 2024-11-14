package com.example.pokedexproject.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pokedexproject.R;
import com.example.pokedexproject.models.Pokemon;
import com.squareup.picasso.Picasso;

public class PokemonFragment extends Fragment {

    private TextView nameTextView, typeTextView, weightTextView, heightTextView, baseExperienceTextView;
    private ImageView frontImageView, backImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pokemon, container, false);

        // Initialize UI components
        nameTextView = root.findViewById(R.id.textViewPokemonName);
        typeTextView = root.findViewById(R.id.textViewPokemonType);
        weightTextView = root.findViewById(R.id.textViewPokemonWeight);
        heightTextView = root.findViewById(R.id.textViewPokemonHeight);
        baseExperienceTextView = root.findViewById(R.id.textViewPokemonBaseExperience);
        frontImageView = root.findViewById(R.id.imageViewPokemonFront);
        backImageView = root.findViewById(R.id.imageViewPokemonBack);

        // Retrieve and display Pokémon details from arguments
        if (getArguments() != null) {
            Pokemon pokemon = (Pokemon) getArguments().getSerializable("pokemon");
            if (pokemon != null) {
                displayPokemonDetails(pokemon);
            }
        }

        return root;
    }

    private void displayPokemonDetails(Pokemon pokemon) {
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
