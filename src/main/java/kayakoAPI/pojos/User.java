package kayakoAPI.pojos;

import common.annotaions.CustomField;
import common.annotaions.Excel;
import common.annotaions.JsonField;

public class User {

    @JsonField(path = "/data/full_name")
    @Excel(columnName = "Full Name")
    private String fullName;

    @JsonField(path = "/data/custom_fields")
    @CustomField(contains = "netpeaksoftware.com")
    @Excel(columnName = "ACP Link")
    private String acpLink;

    @Excel(columnName = "User Id")
    private String userId;

    private String acpId;

    public User(){

    }

    public User(String fullName, String acpLink){
        this.fullName = fullName;
        this.acpLink = acpLink;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAcpLink() {
        return acpLink;
    }

    public void setAcpLink(String acpLink) {
        this.acpLink = acpLink;
    }

    public String getAcpId() {
        return acpId;
    }

    public void setAcpId(String acpId) {
        this.acpId = acpId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void initializeUserId(){
        if(acpLink != null){
            this.userId = acpLink.substring(acpLink.lastIndexOf('/') + 1);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", acpLink='" + acpLink + '\'' +
                ", acpId='" + acpId + '\'' +
                '}';
    }
}
