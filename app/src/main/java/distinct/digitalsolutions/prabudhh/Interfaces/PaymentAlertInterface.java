package distinct.digitalsolutions.prabudhh.Interfaces;

import java.util.List;

import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;

public interface PaymentAlertInterface {

    void showAlertDialog(CategoryViewModelClass categoryViewModelClass, String categoryName, List<CategoryViewModelClass> categoryViewModelClasses
                         //String categoryId
    );

}
