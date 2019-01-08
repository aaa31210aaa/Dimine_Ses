package bean;

public class ScInfomationBean {
    private String qyname;
    //安全检查项目
    private String checkname;
    //安全信息id
    private String scid;
    //查处问题
    private String problems;
    //是否提交
    private String iscommit;
    //安全检查场所
    private String checkadd;
    //检查人员
    private String checkman;
    //检查时间
    private String scdate;
    //处理方法
    private String handlemethod;
    //备注
    private String memo;

    public String getQyname() {
        return qyname;
    }

    public void setQyname(String qyname) {
        this.qyname = qyname;
    }

    public String getCheckname() {
        return checkname;
    }

    public void setCheckname(String checkname) {
        this.checkname = checkname;
    }

    public String getScid() {
        return scid;
    }

    public void setScid(String scid) {
        this.scid = scid;
    }

    public String getProblems() {
        return problems;
    }

    public void setProblems(String problems) {
        this.problems = problems;
    }

    public String getIscommit() {
        return iscommit;
    }

    public void setIscommit(String iscommit) {
        this.iscommit = iscommit;
    }

    public String getCheckadd() {
        return checkadd;
    }

    public void setCheckadd(String checkadd) {
        this.checkadd = checkadd;
    }

    public String getCheckman() {
        return checkman;
    }

    public void setCheckman(String checkman) {
        this.checkman = checkman;
    }

    public String getScdate() {
        return scdate;
    }

    public void setScdate(String scdate) {
        this.scdate = scdate;
    }

    public String getHandlemethod() {
        return handlemethod;
    }

    public void setHandlemethod(String handlemethod) {
        this.handlemethod = handlemethod;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
