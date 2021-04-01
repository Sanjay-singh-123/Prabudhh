package distinct.digitalsolutions.prabudhh.Model;

public class WhichAreModelClass {

    private String Song,userName,thumbNail,image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public WhichAreModelClass(String song, String userName,
                              String thumbNail, String image) {
        Song = song;
        this.userName = userName;
        this.thumbNail = thumbNail;
        this.image = image;
    }

    public String getSong() {
        return Song;
    }

    public void setSong(String song) {
        Song = song;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }
}
