package bean;

public class AccidentBean {
    private String AccidentName; //事故名称
    private String procid; //事故id
    private String deptname; //事故单位
    private String aldate;//事故日期
    private String aitypename;//事故分类
    private String ailevelname;//事故等级
    private String swnum;//死亡人数
    private String zsnum;//重伤人数
    private String qsnum;//轻伤人数
    private String sgqksm;//事故详情
    private String Publisher;  //发布人
    private String ReleaseTime; //发布时间

    public String getAccidentName() {
        return AccidentName;
    }

    public void setAccidentName(String accidentName) {
        AccidentName = accidentName;
    }

    public String getProcid() {
        return procid;
    }

    public void setProcid(String procid) {
        this.procid = procid;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getAldate() {
        return aldate;
    }

    public void setAldate(String aldate) {
        this.aldate = aldate;
    }

    public String getAitypename() {
        return aitypename;
    }

    public void setAitypename(String aitypename) {
        this.aitypename = aitypename;
    }

    public String getAilevelname() {
        return ailevelname;
    }

    public void setAilevelname(String ailevelname) {
        this.ailevelname = ailevelname;
    }

    public String getSwnum() {
        return swnum;
    }

    public void setSwnum(String swnum) {
        this.swnum = swnum;
    }

    public String getZsnum() {
        return zsnum;
    }

    public void setZsnum(String zsnum) {
        this.zsnum = zsnum;
    }

    public String getQsnum() {
        return qsnum;
    }

    public void setQsnum(String qsnum) {
        this.qsnum = qsnum;
    }

    public String getSgqksm() {
        return sgqksm;
    }

    public void setSgqksm(String sgqksm) {
        this.sgqksm = sgqksm;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public String getReleaseTime() {
        return ReleaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        ReleaseTime = releaseTime;
    }
}
