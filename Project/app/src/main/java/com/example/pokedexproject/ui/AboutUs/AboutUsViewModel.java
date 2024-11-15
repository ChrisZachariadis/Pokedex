package com.example.pokedexproject.ui.AboutUs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AboutUsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AboutUsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Welcome to the Pokédex Project!\n\n" +
                "Our mission is to celebrate the world of Pokémon by bringing fans a digital Pokédex filled with nostalgia and excitement. " +
                "From the humble beginnings of catching Pokémon on a black-and-white Game Boy screen, to the global sensation it has become today, " +
                "we honor every journey, every battle, and every friendship forged along the way.\n\n" +
                "Whether you're a new Trainer or a seasoned Champion, we hope our app enhances your Pokémon adventure. " +
                "Let's keep the spirit of discovery alive and continue the quest to 'Catch 'em all!' " +
                "Thank you for joining us on this incredible journey through the world of Pokémon.\n\n" +
                "Gotta catch 'em all!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
