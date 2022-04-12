package socialnetwork.domain;

public class GrupDTO {
    private String selected;
    private String groupName;
    private int groupId;

    public GrupDTO(String groupName, int groupId) {
        this.selected = "";
        this.groupName = groupName;
        this.groupId = groupId;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
