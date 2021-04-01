package distinct.digitalsolutions.prabudhh.Model;

public class HomeModelClass {

    private String category_id;
    private String category_name;
    private String thumbnail;

    public HomeModelClass(){}

    public HomeModelClass(String category_id, String category_name, String thumbnail) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.thumbnail = thumbnail;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
