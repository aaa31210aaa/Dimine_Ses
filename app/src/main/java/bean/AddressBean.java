package bean;

public class AddressBean {
    //经度
    private Double Longitude;
    //纬度
    private Double Latitude;
    //标题
    private String Title;
    //内容
    private String Snippet;

    public AddressBean(Double longitude, Double latitude, String title, String snippet) {
        Longitude = longitude;
        Latitude = latitude;
        Title = title;
        Snippet = snippet;
    }

    public Double getLongitude() {
        return Longitude;
    }


    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSnippet() {
        return Snippet;
    }

    public void setSnippet(String snippet) {
        Snippet = snippet;
    }
}
