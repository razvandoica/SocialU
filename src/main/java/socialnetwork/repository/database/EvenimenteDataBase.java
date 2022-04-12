package socialnetwork.repository.database;

import socialnetwork.domain.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EvenimenteDataBase {
    private final String url;
    private final String username;
    private final String password;

    public EvenimenteDataBase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Returneaza toate evenimentele
     * @return
     */
    public Iterable<Eveniment> findAll(){
        String sql = "select * from eveniment order by data asc";
        Collection<Eveniment> events = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("nume");
                String locatie = resultSet.getString("locatie");
                LocalDate data = resultSet.getDate("data").toLocalDate();

                Eveniment ev = new Eveniment(id, name, locatie, data, 1);

                events.add(ev);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public Iterable<Eveniment> getUpcomingEvents(String user){
        String sql = "select * from eveniment_user evu inner join eveniment ev on evu.id_event = ev.id where now() >= ev.data - interval '7 day' and ev.data > now() and evu.user_id = (?) and evu.notifs = 1 order by ev.data asc ";
        Collection<Eveniment> events = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, user);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("nume");
                String locatie = resultSet.getString("locatie");
                LocalDate data = resultSet.getDate("data").toLocalDate();
                int notif = resultSet.getInt("notifs");

                Eveniment ev = new Eveniment(id, name, locatie, data, notif);

                events.add(ev);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    /**
     * Cauta un user care participa la un eveniment (folosit pentru verificarea apartenentei)
     * @param user
     * @param id
     * @return
     */
    public Eveniment findOne(String user, int id) {
        String sql = "select * from eveniment ev inner join eveniment_user evu on ev.id = evu.id_event where evu.user_id = (?) and ev.id = (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, user);
            statement.setInt(2, id);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                String nume = resultSet.getString("nume");
                String locatie = resultSet.getString("locatie");
                LocalDate date = resultSet.getDate("data").toLocalDate();
                int notif = resultSet.getInt("notifs");
                Eveniment ev = new Eveniment(id, nume, locatie, date, notif);

                return ev;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creeaza un eveniment nou
     * @param entity
     * @return
     */
    public Eveniment createEvent(Eveniment entity) {
        String sql = "insert into eveniment (nume, locatie, data) values (?,?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1,entity.getNume());
            statement.setString(2,entity.getLocatie());
            statement.setDate(3, Date.valueOf(entity.getData()));

            if(statement.executeUpdate() == 0) {
                return entity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Aboneaza un user la un eveniment
     * @param user
     * @param id
     * @return
     */
    public String addToEvent(String user, int id){
        String sql = "insert into eveniment_user (user_id,id_event, notifs) values (?,?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, user);
            statement.setInt(2, id);
            statement.setInt(3, 1);

            if(statement.executeUpdate() == 0) {
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Dezaboneaza un user de la un eveniment
     * @param user
     * @param id
     * @return
     */
    public String leaveEvent(String user, int id){
        String sql = "delete from eveniment_user where user_id = (?) and id_event = (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1,user);
            statement.setInt(2,id);

            if(statement.executeUpdate() == 0) {
                return "ok";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gaseste toate evenimentele la care participa un user
     * @param user
     * @return
     */
    public Iterable<Eveniment> findEventsUser(String user){
        String sql = "select * from eveniment ev inner join eveniment_user evu on ev.id = evu.id_event where evu.user_id = (?) order by ev.data asc";
        Collection<Eveniment> events = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, user);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("nume");
                String locatie = resultSet.getString("locatie");
                LocalDate data = resultSet.getDate("data").toLocalDate();
                int notif = resultSet.getInt("notifs");

                Eveniment ev = new Eveniment(id, name, locatie, data, notif);

                events.add(ev);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public int deleteOldEvents() {
        String sql = "delete from eveniment where now() > eveniment.data";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            if(ps.executeUpdate() == 0){
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;

    }

    public int changeNotif(String user, int notif, int ide){
        String sql = "update eveniment_user set notifs = (?) where user_id = (?) and id_event = (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setInt(1, notif);
            statement.setString(2, user);
            statement.setInt(3, ide);

            if(statement.executeUpdate() == 0) {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public int getParticipantCount(int idEvent){
        String sql = "Select count(*) as nrParticipanti from eveniment_user where id_event = (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setInt(1, idEvent);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int count = resultSet.getInt("nrParticipanti");

                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
