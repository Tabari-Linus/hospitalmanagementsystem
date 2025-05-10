package lii.hospitalmanagementsystem.databasecrud;

import lii.hospitaltrial.model.UserAccount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAccountCRUD {

    public boolean insertUserAccount(UserAccount account) {
        String sql = "INSERT INTO useraccount (username, password, is_admin, employee_id) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, account.getUsername());
                statement.setString(2, account.getPassword());
                statement.setBoolean(3, account.isAdmin());
                statement.setObject(4, account.getEmployeeId(), Types.BIGINT);

                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    account.setUserId(rs.getLong(1));
                    connection.commit();
                    return true;
                }
                connection.rollback();
                return false;
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<UserAccount> getAllUserAccounts() {
        List<UserAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM useraccount";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                UserAccount account = new UserAccount(
                        resultSet.getLong("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("is_admin"),
                        resultSet.getObject("employee_id", Long.class)
                );
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public boolean updateUserAccount(UserAccount account) {
        String sql = "UPDATE useraccount SET username = ?, password = ?, is_admin = ?, employee_id = ? WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, account.getUsername());
                statement.setString(2, account.getPassword());
                statement.setBoolean(3, account.isAdmin());
                statement.setObject(4, account.getEmployeeId(), Types.BIGINT);
                statement.setLong(5, account.getUserId());
                boolean result = statement.executeUpdate() > 0;
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUserAccount(long userId) {
        String sql = "DELETE FROM useraccount WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, userId);
                boolean result = statement.executeUpdate() > 0;
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
