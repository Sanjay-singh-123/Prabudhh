package distinct.digitalsolutions.prabudhh.Interfaces;

public interface SongPostFirebaseInterface {

    void onSuccess(String success,String date,String expiry_date);
    void onFailure(String error);

}
