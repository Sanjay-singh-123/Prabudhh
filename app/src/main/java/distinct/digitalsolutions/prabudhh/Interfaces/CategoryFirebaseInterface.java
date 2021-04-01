package distinct.digitalsolutions.prabudhh.Interfaces;

import java.util.List;

import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;
import distinct.digitalsolutions.prabudhh.Model.HomeModelClass;

public interface CategoryFirebaseInterface {

    void onSuccess(List<CategoryViewModelClass> categoryViewModelClasses,List<CategoryViewModelClass> allSongDetails);
    void onFailure(String error);

}
