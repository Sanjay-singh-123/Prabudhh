package distinct.digitalsolutions.prabudhh.Model;

import distinct.digitalsolutions.prabudhh.Interfaces.UserUploadDataInterface;

public class UserDeatilsModelClass {

    private String user_name,email_address,thumbnail,image;

    public UserDeatilsModelClass(){}

    public UserDeatilsModelClass(String user_name, String email_address, String thumbnail, String image) {
        this.user_name = user_name;
        this.email_address = email_address;
        this.thumbnail = thumbnail;
        this.image = image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
