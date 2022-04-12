package socialnetwork.repository.database;

import socialnetwork.domain.Grup;
import socialnetwork.domain.GrupDTO;
import socialnetwork.domain.Message;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupDataBase {
    private final String url;
    private final String username;
    private final String password;

    public GroupDataBase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * 1. toate grupurile din care face parte un user X
     * 2. join group X
     * 3. toti userii dintr-un grup (?)
     * 4. toate grupurile existente X
     * 5. exista un user in grupul dat
     */

    /**
     * Returneaza toate grupurile
     * @return
     */
    public Iterable<Grup> findAll(){
        String sql = "select * from grup ";
        Set<Grup> gr = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int groupId = resultSet.getInt("group_id");
                String groupName = resultSet.getString("group_name");

                Grup grup = new Grup(groupId, groupName);

                gr.add(grup);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gr;
    }

    /**
     * Returneaza toate grupurile din care face parte un user
     * @param user
     * @return
     */
    public Iterable<GrupDTO> findGroupsUser(String user){
        String sql = "select * from grup_user gru inner join grup gr on gru.group_id = gr.group_id where gru.email = (?)";
        Set<GrupDTO> gr = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, user);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int groupId = resultSet.getInt("group_id");
                String groupName = resultSet.getString("group_name");

                GrupDTO grup = new GrupDTO(groupName, groupId);

                gr.add(grup);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gr;
    }

    /**
     * Creeaza un grup nou
     * @param entity
     * @return
     */
    public int createGroup(Grup entity) {

        String sql = "insert into grup (group_name) values (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setString(1,entity.getGroupName());

            if(statement.executeUpdate() == 0) {
                ResultSet rs = statement.getGeneratedKeys();
                System.out.println(rs.getInt(1));
                return rs.getInt(1);
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Adauga un user intr-un grup
     * @param email
     * @param groupId
     * @return
     */
    public String addToGroup(String email, int groupId){
        String sql = "insert into grup_user (email,group_id) values (?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, email);
            statement.setInt(2, groupId);

            if(statement.executeUpdate() == 0) {
                return email;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Verifica daca un user este intr-un grup
     * @param user
     * @param group
     * @return
     */
    public boolean isInGroup(String user, int group){
        String sql = "select * from grup_user where email = (?) and group_id = (?)";

        boolean exists = false;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, user);
            statement.setInt(2, group);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                exists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
}
