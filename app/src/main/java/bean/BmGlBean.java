package bean;


public class BmGlBean {
    private String deptid;// 部门ID
    private String parentdeptname;//单位名称
    private String deptname;//部门名称
    private String deptcode;// 部门编号

    private String tel; //联系电话
    private String fax;// 传真
    private String address;// 地址
    private String memo; //备注
    private String filename;// 附件名称

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getParentdeptname() {
        return parentdeptname;
    }

    public void setParentdeptname(String parentdeptname) {
        this.parentdeptname = parentdeptname;
    }

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
