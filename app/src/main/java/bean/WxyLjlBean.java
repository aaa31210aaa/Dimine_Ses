package bean;

/**
 * Created by Administrator on 2017-03-28.
 */
public class WxyLjlBean {
    //物质名称
    private String wzmc;
    //危险物质ID
    private String wxwzid;
    //危险源类别编号
    private String lbcode;
    //危险源类别名称
    private String typename;
    //生产场所临界量
    private String sccsljl;
    //存储区临界量
    private String ccqljl;

    public String getWzmc() {
        return wzmc;
    }

    public void setWzmc(String wzmc) {
        this.wzmc = wzmc;
    }

    public String getWxwzid() {
        return wxwzid;
    }

    public void setWxwzid(String wxwzid) {
        this.wxwzid = wxwzid;
    }

    public String getLbcode() {
        return lbcode;
    }

    public void setLbcode(String lbcode) {
        this.lbcode = lbcode;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getSccsljl() {
        return sccsljl;
    }

    public void setSccsljl(String sccsljl) {
        this.sccsljl = sccsljl;
    }

    public String getCcqljl() {
        return ccqljl;
    }

    public void setCcqljl(String ccqljl) {
        this.ccqljl = ccqljl;
    }
}
