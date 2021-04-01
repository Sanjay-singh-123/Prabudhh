package distinct.digitalsolutions.prabudhh.Interfaces;

import java.util.List;

import distinct.digitalsolutions.prabudhh.Model.UserDeatilsModelClass;

public interface UserDatailsFetchingInterface {

    void onSuccess(UserDeatilsModelClass userDeatilsModelClass);
    void onFailure(String error);
}
