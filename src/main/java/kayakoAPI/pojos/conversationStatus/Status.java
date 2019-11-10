package kayakoAPI.pojos.conversationStatus;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum Status {
    NEW(1),
    OPEN(2),
    PENDING(3),
    COMPLETED(4),
    CLOSED(5);

    private int number;

    Status(int number){
        this.number = number;
    }

    private final static Map<Integer,Status> statusMap;

    static {
        Map<Integer,Status> temp = new HashMap<>();
        for(Status status : Status.values()){
            temp.put(status.getNumber(),status);
        }
        statusMap = Collections.unmodifiableMap(temp);
    }

    public static Status fromInt(int key){
        return statusMap.get(key);
    }
}
