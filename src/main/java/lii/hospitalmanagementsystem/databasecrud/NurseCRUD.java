package lii.hospitalmanagementsystem.databasecrud;


import lii.hospitalmanagementsystem.model.Nurse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NurseCRUD {

    public boolean insertNurse(Nurse nurse) {
        String sql = "INSERT INTO nurse (employee_id, rotation, salary, department_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, nurse.getEmployeeId());
                statement.setString(2, nurse.getRotation());
                statement.setDouble(3, nurse.getSalary());
                statement.setLong(4, nurse.getDepartmentId());
                boolean result = statement.executeUpdate() > 0;
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error inserting nurse", e);

            }
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Nurse> getAllNurses() {
        List<Nurse> nurses = new ArrayList<>();
        String sql = "SELECT * FROM nurse";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Nurse nurse = new Nurse(
                        resultSet.getLong("employee_id"),
                        resultSet.getString("rotation"),
                        resultSet.getDouble("salary"),
                        resultSet.getLong("department_id")
                );
                nurses.add(nurse);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching nurses", e);
        }
        return nurses;
    }

    public boolean updateNurse(Nurse nurse) {
        String sql = "UPDATE nurse SET rotation = ?, salary = ?, department_id = ? WHERE employee_id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, nurse.getRotation());
                statement.setDouble(2, nurse.getSalary());
                statement.setLong(3, nurse.getDepartmentId());
                statement.setLong(4, nurse.getEmployeeId());
                boolean result = statement.executeUpdate() > 0;
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error updating nurse", e);

            }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteNurse(long employeeId) throws SQLException {
        String checkSql = "SELECT ward_id FROM ward WHERE supervisor_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setLong(1, employeeId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                throw new SQLException("Cannot delete nurse: Still assigned as supervisor to ward " + rs.getLong("ward_id"));
            }

            String deleteSql = "DELETE FROM nurse WHERE employee_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setLong(1, employeeId);
                return deleteStmt.executeUpdate() > 0;
            }
        }
    }
}