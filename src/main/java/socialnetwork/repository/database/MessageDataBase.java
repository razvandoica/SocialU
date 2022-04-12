package socialnetwork.repository.database;

import socialnetwork.domain.CererePrietenie;
import socialnetwork.domain.Message;
import socialnetwork.domain.Prietenie;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MessageDataBase {
        private final String url;
        private final String username;
        private final String password;

        public MessageDataBase(String url, String username, String password) {
                this.url = url;
                this.username = username;
                this.password = password;
        }

        /**
         * 1. Toate mesajele dintre 2 useri X
         * 2. Toate mesajele dintr-un grup X
         * 3. Save X
         */

        /**
         * Sterge chat-ul dintre 2 utilizatori ( folosit cand se da unfriend )
         * @param user1
         * @param user2
         * @return
         */
        public String deleteChat (String user1, String user2){
                String sql = "delete from mesaje where ((from_user = (?) and to_user = (?)) or (from_user = (?) and to_user = (?)))";
                try (Connection connection = DriverManager.getConnection(url, username, password);
                     PreparedStatement statement = connection.prepareStatement(sql);
                ) {
                        statement.setString(1,user1);
                        statement.setString(2,user2);

                        statement.setString(3,user2);
                        statement.setString(4,user1);

                        if(statement.executeUpdate() == 0) {
                                return "ok";
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                }
                return null;
        }

        /**
         * Returneaza mesajele dintre 2 utilizatori
         * @param user1
         * @param user2
         * @return
         */
        public Iterable<Message> findUsersChat(String user1, String user2){
                String sql = "select * from mesaje where ((from_user = (?) and to_user = (?)) or (from_user = (?) and to_user = (?))) order by data ASC";
                Collection<Message> msgs = new ArrayList<>();
                try (Connection connection = DriverManager.getConnection(url, username, password);
                     PreparedStatement statement = connection.prepareStatement(sql);
                ) {
                        statement.setString(1, user1);
                        statement.setString(2, user2);
                        statement.setString(3, user2);
                        statement.setString(4, user1);

                        ResultSet resultSet = statement.executeQuery();

                        while (resultSet.next()) {

                                String fromUser = resultSet.getString("from_user");
                                String toUser = resultSet.getString("to_user");
                                String message = resultSet.getString("sent_message");
                                LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();

                                Message msg = new Message(fromUser,toUser,message,-1);
                                msg.setData(data);
                                msgs.add(msg);
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                }
                return msgs;
        }

        /**
         * Returneaza mesajele dintr-un grup
         * @param group
         * @return
         */
        public Iterable<Message> findGroupChat(int group){
                String sql = "select * from mesaje m inner join grup g on m.group_id = g.group_id where m.group_id = (?) order by m.data asc ";
                Collection<Message> msgs = new ArrayList<>();
                try (Connection connection = DriverManager.getConnection(url, username, password);
                     PreparedStatement statement = connection.prepareStatement(sql);
                ) {
                        statement.setInt(1, group);

                        ResultSet resultSet = statement.executeQuery();

                        while (resultSet.next()) {
                                String fromUser = resultSet.getString("from_user");
                                String message = resultSet.getString("sent_message");
                                LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();

                                Message msg = new Message(fromUser,null,message, group);
                                msg.setData(data);
                                msgs.add(msg);
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                }
                return msgs;
        }

        /**
         * Salveaza un mesaj
         * @param entity
         * @return
         */
        public Message save(Message entity) {

                String sql = "insert into mesaje  (from_user, to_user, sent_message, data, group_id) values (?,?,?,?,?)";
                try (Connection connection = DriverManager.getConnection(url, username, password);
                     PreparedStatement statement = connection.prepareStatement(sql);
                ) {
                        statement.setString(1,entity.getFrom());
                        statement.setString(2,entity.getTo());
                        statement.setString(3,entity.getText());
                        statement.setTimestamp(4,Timestamp.valueOf(entity.getData()));
                        statement.setInt(5, entity.getGroup());

                        if(statement.executeUpdate() == 0) {
                                return entity;
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                }
                return null;
        }

        public LocalDateTime getLastMessageDate(String user1, String user2){
                String sql = "Select * from mesaje where (from_user = (?) and to_user = (?)) or (from_user = (?) and to_user = (?)) order by data desc limit 1";
                LocalDateTime data = LocalDateTime.now().minusYears(2);
                try (Connection connection = DriverManager.getConnection(url, username, password);
                     PreparedStatement statement = connection.prepareStatement(sql);
                ) {
                        statement.setString(1,user1);
                        statement.setString(2,user2);
                        statement.setString(3,user2);
                        statement.setString(4,user1);

                        ResultSet resultSet = statement.executeQuery();

                        if (resultSet.next()) {
                                data = resultSet.getTimestamp("data").toLocalDateTime();
                                return data;
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                }
                return data;
        }

        public Iterable<Message> findReceivedMsg(String user, LocalDate start, LocalDate end){
                String sql = "select * from mesaje where to_user = (?) and data >= (?) and data <=(?) order by data asc";
                Collection<Message> msgs = new ArrayList<>();
                try (Connection connection = DriverManager.getConnection(url, username, password);
                     PreparedStatement statement = connection.prepareStatement(sql);
                ) {
                        statement.setString(1, user);
                        statement.setDate(2, Date.valueOf(start));
                        statement.setDate(3, Date.valueOf(end));


                        ResultSet resultSet = statement.executeQuery();

                        while (resultSet.next()) {

                                String fromUser = resultSet.getString("from_user");
                                String toUser = resultSet.getString("to_user");
                                String message = resultSet.getString("sent_message");
                                LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();

                                Message msg = new Message(fromUser,toUser,message,-1);
                                msg.setData(data);
                                msgs.add(msg);
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                }
                return msgs;
        }

        public Iterable<Message> findReceivedMsgFromUser(String user, String from, LocalDate start, LocalDate end){
                String sql = "select * from mesaje where to_user = (?) and from_user=(?) and data >= (?) and data <=(?) order by data asc";
                Collection<Message> msgs = new ArrayList<>();
                try (Connection connection = DriverManager.getConnection(url, username, password);
                     PreparedStatement statement = connection.prepareStatement(sql);
                ) {
                        statement.setString(1, user);
                        statement.setString(2, from);
                        statement.setDate(3, Date.valueOf(start));
                        statement.setDate(4, Date.valueOf(end));


                        ResultSet resultSet = statement.executeQuery();

                        while (resultSet.next()) {

                                String fromUser = resultSet.getString("from_user");
                                String toUser = resultSet.getString("to_user");
                                String message = resultSet.getString("sent_message");
                                LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();

                                Message msg = new Message(fromUser,toUser,message,-1);
                                msg.setData(data);
                                msgs.add(msg);
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                }
                return msgs;
        }


}
