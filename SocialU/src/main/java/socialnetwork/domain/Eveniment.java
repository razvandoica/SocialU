package socialnetwork.domain;

import java.time.LocalDate;

public class Eveniment {
    private int id;
    private String nume;
    private String locatie;
    private LocalDate data;
    private int notif;

    public Eveniment(int id, String nume, String locatie, LocalDate data, int notif) {
        this.id = id;
        this.nume = nume;
        this.locatie = locatie;
        this.data = data;
        this.notif = notif;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getNotif() {
        return notif;
    }

    public void setNotif(int notif) {
        this.notif = notif;
    }
}
