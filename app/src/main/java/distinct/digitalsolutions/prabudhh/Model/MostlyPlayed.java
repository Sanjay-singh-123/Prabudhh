package distinct.digitalsolutions.prabudhh.Model;

public class MostlyPlayed implements Comparable<MostlyPlayed>{

    private String songId;
    private String  songCount;

    public MostlyPlayed(String songId, String songCount) {
        this.songId = songId;
        this.songCount = songCount;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getSongCount() {
        return songCount;
    }

    public void setSongCount(String songCount) {
        this.songCount = songCount;
    }

    @Override
    public int compareTo(MostlyPlayed mostlyPlayed) {

        return Integer.compare(Integer.parseInt(mostlyPlayed.songCount), Integer.parseInt(songCount));

    }
}
