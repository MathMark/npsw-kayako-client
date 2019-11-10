package kayakoAPI.pojos;

import common.annotaions.CustomField;
import common.annotaions.Excel;
import common.annotaions.JsonField;
import lombok.Data;

@Data
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
