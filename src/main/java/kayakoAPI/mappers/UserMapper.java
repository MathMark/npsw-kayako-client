package kayakoAPI.mappers;

import kayakoAPI.pojos.User;
import org.json.JSONArray;
import org.json.JSONObject;

@Deprecated
public class UserMapper {

    public User mapUser(JSONObject userJson){
        User user = new User();

        String name = userJson.getJSONObject("data").getString("full_name");
        if(name.contains(" ")){
            String[] splittedName = name.split(" ");
           // user.setFirstName(splittedName[0]);
           // user.setLastName(splittedName[1]);
        }else{
           // user.setFirstName(name);
        }
        String profileLink = fetchAcpLink(userJson.getJSONObject("data").getJSONArray("custom_fields"));
        user.setAcpLink(profileLink);

        if((profileLink != null)){
            String userId = getUserId(profileLink);
            user.setAcpId(userId);
        }else{
            user.setAcpId("noId");
        }

        return user;
    }

    private String fetchAcpLink(JSONArray customFields){
        String acpLink = null;

        for(int i = 0; i < customFields.length(); i++){
            JSONObject field = (JSONObject)customFields.get(i);
            acpLink = field.getString("value");
            if(acpLink.contains("netpeaksoftware.com")){
                return acpLink;
            }
        }
        return acpLink;
    }

    private String getUserId(String acpLink){
        return  acpLink.substring(acpLink.lastIndexOf('/') + 1);
    }
}
