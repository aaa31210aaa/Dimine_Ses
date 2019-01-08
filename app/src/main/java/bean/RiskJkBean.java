package bean;

public class RiskJkBean {
    //企业id
    private String qyid;
    //公司名字
    private String name;
    //公司街道
    private String address;
    //公司电话
    private String tel;
    //公司经度
    private String lng;
    //公司纬度
    private String lat;
    //公司风险级别
    private String wxjb;


    public String getQyid() {
        return qyid;
    }

    public void setQyid(String qyid) {
        this.qyid = qyid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getWxjb() {
        return wxjb;
    }

    public void setWxjb(String wxjb) {
        this.wxjb = wxjb;
    }
}
