package distinct.digitalsolutions.prabudhh.Interfaces;

import java.util.List;

import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;
import distinct.digitalsolutions.prabudhh.Model.WhichAreModelClass;

public interface WhichAreInterface {

    void onSuccess(List<WhichAreModelClass> categoryViewModelClasses);
    void onFailure(String error);

}
