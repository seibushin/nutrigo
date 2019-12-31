package de.seibushin.nutrigo.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import de.seibushin.nutrigo.model.Profile;

public class ProfileViewModel extends AndroidViewModel {
    private Repo repo;

    private LiveData<Profile> profile;

    public ProfileViewModel(Application application) {
        super(application);
        repo = new Repo(application);
        profile = repo.getProfile();
    }

    public LiveData<Profile> getProfile() {
        return profile;
    }

    public void update(Profile profile) {
        repo.updateProfile(profile);
    }


}
