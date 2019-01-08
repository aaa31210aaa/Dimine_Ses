package bean;

public class TzSbBean {
    private String sfid;// 生产设备设施ID
    private String facilitiesname;// 设备名称
    private String facilitiesno;// 规格型号
    private String manufacturer;// 制造厂商
    private String installsite;// 安装位置
    private String installcode;// 场内编号
    private String facilitiestypename;//设备状态
    private String functioncase;//运行情况
    private String functiondate;//投用时间
    private String checktimename;//检验周期
    private String checkdate;//检查时间
    private String checkcase;// 检验情况
    private String memo;//备注
    private String filename;//附件名称
    //过期标识
    private String isexpire;

    public String getSfid() {
        return sfid;
    }

    public void setSfid(String sfid) {
        this.sfid = sfid;
    }

    public String getFacilitiesname() {
        return facilitiesname;
    }

    public void setFacilitiesname(String facilitiesname) {
        this.facilitiesname = facilitiesname;
    }

    public String getFacilitiesno() {
        return facilitiesno;
    }

    public void setFacilitiesno(String facilitiesno) {
        this.facilitiesno = facilitiesno;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getInstallsite() {
        return installsite;
    }

    public void setInstallsite(String installsite) {
        this.installsite = installsite;
    }

    public String getInstallcode() {
        return installcode;
    }

    public void setInstallcode(String installcode) {
        this.installcode = installcode;
    }

    public String getFacilitiestypename() {
        return facilitiestypename;
    }

    public void setFacilitiestypename(String facilitiestypename) {
        this.facilitiestypename = facilitiestypename;
    }

    public String getFunctioncase() {
        return functioncase;
    }

    public void setFunctioncase(String functioncase) {
        this.functioncase = functioncase;
    }

    public String getFunctiondate() {
        return functiondate;
    }

    public void setFunctiondate(String functiondate) {
        this.functiondate = functiondate;
    }

    public String getChecktimename() {
        return checktimename;
    }

    public void setChecktimename(String checktimename) {
        this.checktimename = checktimename;
    }

    public String getCheckdate() {
        return checkdate;
    }

    public void setCheckdate(String checkdate) {
        this.checkdate = checkdate;
    }

    public String getCheckcase() {
        return checkcase;
    }

    public void setCheckcase(String checkcase) {
        this.checkcase = checkcase;
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

    public String getIsexpire() {
        return isexpire;
    }

    public void setIsexpire(String isexpire) {
        this.isexpire = isexpire;
    }
}
