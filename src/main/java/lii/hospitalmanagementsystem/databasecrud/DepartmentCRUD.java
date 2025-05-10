package lii.hospitalmanagementsystem.databasecrud;


import lii.hospitaltrial.model.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentCRUD {

    public boolean insertDepartment(Department department) {
        String sql = "INSERT INTO department (department_name, building, director_id) " +
                "VALUES (?, ?, ?) RETURNING department_code";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, department.getDepartmentName());
                statement.setString(2, department.getBuilding());    // Changed from location to building
                statement.setLong(3, department.getDirectorId());

                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        department.setDepartmentCode(rs.getLong(1));
                        connection.commit();
                        return true;
                    }
                }
                connection.rollback();
                return false;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error inserting department", e);

            }
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM department";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Department department = new Department(
                        resultSet.getLong("department_code"),
                        resultSet.getString("department_name"),
                        resultSet.getString("building"),
                        resultSet.getLong("director_id")
                );
                departments.add(department);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching departments", e);
        }
        return departments;
    }

    public boolean updateDepartment(Department department) {
        String sql = "UPDATE department SET department_name = ?, building = ?, director_id = ? WHERE department_code = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, department.getDepartmentName());
                statement.setString(2, department.getBuilding());
                statement.setLong(3, department.getDirectorId());
                statement.setLong(4, department.getDepartmentCode());
                boolean result = statement.executeUpdate() > 0;
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error updating department", e);

            }
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    public boolean deleteDepartment(long departmentCode) {
        String sql = "DELETE FROM department WHERE department_code = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, departmentCode);
                boolean result = statement.executeUpdate() > 0;
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error deleting department", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    public Department getDepartmentById(long departmentId) {
        String sql = "SELECT * FROM department WHERE department_code = ?";
        Department department = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, departmentId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                department = new  Department(
                        resultSet.getLong("department_code"),
                        resultSet.getString("department_name"),
                        resultSet.getString("building"),
                        resultSet.getLong("director_id")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving department with ID: " + departmentId, e);
        }
        return department;
    }
}