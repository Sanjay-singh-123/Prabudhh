package distinct.digitalsolutions.prabudhh.Model;

import java.util.Collection;

public class CategoryViewModelClass implements Comparable<CategoryViewModelClass> {

    public CategoryViewModelClass(){}

    private String audio_url;
    private String description;
    private String id;
    private String img_url;
    private String paid_content;
    private String pdf_url;
    private String recommended;
    private String song_id;
    private String title;

    public String getRecommended() {
        return recommended;
    }

    public void setRecommended(String recommended) {
        this.recommended = recommended;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public CategoryViewModelClass(String audio_url, String description, String id, String img_url, String paid_content,
                                  String pdf_url,String recommended ,String song_id, String title) {
        this.audio_url = audio_url;
        this.description = description;
        this.id = id;
        this.img_url = img_url;
        this.paid_content = paid_content;
        this.pdf_url = pdf_url;
        this.recommended = recommended;
        this.song_id = song_id;
        this.title = title;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPaid_content() {
        return paid_content;
    }

    public void setPaid_content(String paid_content) {
        this.paid_content = paid_content;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int compareTo(CategoryViewModelClass o) {

        return title.compareTo(o.title);

    }
}
