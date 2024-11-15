package com.example.pokedexproject.ui.pokemonDetails;

import android.annotation.SuppressLint;
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

import java.util.List;

public class PokemonDetailsFragment extends Fragment {

    private TextView nameTextView, typeTextView, abilitiesTextView, weightTextView, heightTextView, heldItemsTextView, movesTextView, baseExperienceTextView;
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
        abilitiesTextView = root.findViewById(R.id.textViewPokemonAbilities);
        weightTextView = root.findViewById(R.id.textViewPokemonWeight);
        heightTextView = root.findViewById(R.id.textViewPokemonHeight);
        heldItemsTextView = root.findViewById(R.id.textViewPokemonHeldItems);
        movesTextView = root.findViewById(R.id.textViewPokemonMoves);
        baseExperienceTextView = root.findViewById(R.id.textViewPokemonBaseExperience);
        frontImageView = root.findViewById(R.id.imageViewPokemonFront);
        backImageView = root.findViewById(R.id.imageViewPokemonBack);

        // Initialize ViewModel
        PokemonDetailsViewModel pokemonDetailsViewModel = new ViewModelProvider(this).get(PokemonDetailsViewModel.class);

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

    @SuppressLint("SetTextI18n")
    private void displayPokemonDetails(Pokemon pokemon) {
        if (pokemon == null) return;

        nameTextView.setText(pokemon.getName());
        typeTextView.setText("Type: " + pokemon.getType());

        // Format Abilities
        String abilities = formatList(pokemon.getAbilities());
        abilitiesTextView.setText("Abilities: " + abilities);

        // Display Weight and Height with units
        weightTextView.setText("Weight: " + pokemon.getWeight() + " kg");
        heightTextView.setText("Height: " + pokemon.getHeight() + " m");

        // Format Held Items
        String heldItems = formatList(pokemon.getHeldItems());
        heldItemsTextView.setText("Held Items: " + heldItems);

        // Format Moves, limit to 5 if there are more
        String moves = formatList(pokemon.getMoves(), 5);
        movesTextView.setText("Moves: " + moves);

        baseExperienceTextView.setText("Base Experience: " + pokemon.getBaseExperience());

        // Load images using Picasso
        if (pokemon.getImageFront() != null && !pokemon.getImageFront().isEmpty()) {
            Picasso.get().load(pokemon.getImageFront()).into(frontImageView);
        }
        if (pokemon.getImageBack() != null && !pokemon.getImageBack().isEmpty()) {
            Picasso.get().load(pokemon.getImageBack()).into(backImageView);
        }
    }

    // Helper method to format a list as a comma-separated string without brackets
    private String formatList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "None";
        }
        return String.join(", ", list);
    }

    // Overloaded helper method to format a list with a limit on the number of items displayed
    private String formatList(List<String> list, int limit) {
        if (list == null || list.isEmpty()) {
            return "None";
        }
        if (list.size() > limit) {
            return String.join(", ", list.subList(0, limit)) + ", ...";
        } else {
            return String.join(", ", list);
        }
    }

}
