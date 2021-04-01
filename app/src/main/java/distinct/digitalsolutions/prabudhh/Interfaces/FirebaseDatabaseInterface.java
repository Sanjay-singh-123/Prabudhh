package distinct.digitalsolutions.prabudhh.Interfaces;

import java.util.List;

import distinct.digitalsolutions.prabudhh.Model.HomeModelClass;

public interface FirebaseDatabaseInterface {

    void onSuccess(List<HomeModelClass> homeModelClasses);
    void onFailure(String error);

}
