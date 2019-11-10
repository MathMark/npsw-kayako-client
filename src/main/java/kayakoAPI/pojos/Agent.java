package kayakoAPI.pojos;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Agent {
    GRAVELOT(43708,"Alexey Podlipny"),
    MARK(74618,"Mark Martsun"),
    BAGATAR(93408,"David Mirzoian");

    private String fullName;
    private int id;

    Agent(int id, String fullName){
        this.id = id;
        this.fullName = fullName;
    }

    private final static Map<Integer, Agent> agentsMap;

    static {
        Map<Integer, Agent> temp = new HashMap<>();
        for(Agent agent : Agent.values()){
            temp.put(agent.getId(),agent);
        }
        agentsMap = Collections.unmodifiableMap(temp);
    }

    @Override
    public String toString() {
        return fullName;
    }

    public static Agent fromId(int id){
        return agentsMap.get(id);
    }

    public String getFullName() {
        return fullName;
    }

    public int getId() {
        return id;
    }
}
