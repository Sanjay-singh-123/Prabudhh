package distinct.digitalsolutions.prabudhh.Interfaces;

import java.util.List;

import distinct.digitalsolutions.prabudhh.Model.MostlyPlayed;

public interface MostPlayedSongInterface {

    void onSuccess(List<MostlyPlayed> mostlyPlayeds);
    void onFailure(String failed);
}
