package socialnetwork.domain;

import java.time.LocalDateTime;

public class PrietenDTO implements Comparable<PrietenDTO>{
    private String email;
    private String firstName;
    private String lastName;
    private String selected;
    private LocalDateTime data;

    public PrietenDTO(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.selected = "";
        data = LocalDateTime.now();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public int compareTo(PrietenDTO o) {
        return this.getData().compareTo(o.getData());
    }
}
