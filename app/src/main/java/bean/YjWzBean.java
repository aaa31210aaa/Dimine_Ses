package bean;

public class YjWzBean {
    private String emid;// 应急物资ID
    private String modifydate;//  更新时间
    private String materialsname;//  应急物资名称
    private String materialssum;// 应急物资数量
    private String ohaddres;//  物资存放位置
    private String sites;//  物资更新周期
    private String remark;//  说明
    private String memo;// 备注
    private String filename;//  附件名称

    public String getEmid() {
        return emid;
    }

    public void setEmid(String emid) {
        this.emid = emid;
    }

    public String getModifydate() {
        return modifydate;
    }

    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
    }

    public String getMaterialsname() {
        return materialsname;
    }

    public void setMaterialsname(String materialsname) {
        this.materialsname = materialsname;
    }

    public String getMaterialssum() {
        return materialssum;
    }

    public void setMaterialssum(String materialssum) {
        this.materialssum = materialssum;
    }

    public String getOhaddres() {
        return ohaddres;
    }

    public void setOhaddres(String ohaddres) {
        this.ohaddres = ohaddres;
    }

    public String getSites() {
        return sites;
    }

    public void setSites(String sites) {
        this.sites = sites;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
