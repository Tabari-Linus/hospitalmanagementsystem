package lii.hospitalmanagementsystem.databasecrud;


import lii.hospitalmanagementsystem.model.Speciality;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpecialityCRUD {

    public boolean insertSpeciality(Speciality speciality) {
        String sql = "INSERT INTO speciality (name) VALUES (?) RETURNING speciality_id";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, speciality.getName());
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        speciality.setSpecialityId(rs.getLong(1));
                        connection.commit();
                        return true;
                    }
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

    public List<Speciality> getAllSpecialities() {
        List<Speciality> specialities = new ArrayList<>();
        String sql = "SELECT speciality_id, name FROM speciality";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Speciality speciality = new Speciality(
                        resultSet.getLong("speciality_id"),  // Changed from "id" to "speciality_id"
                        resultSet.getString("name")
                );
                specialities.add(speciality);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specialities;
    }

    public boolean updateSpeciality(Speciality speciality) {
        String sql = "UPDATE speciality SET name = ? WHERE speciality_id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, speciality.getName());
                statement.setLong(2, speciality.getSpecialityId());
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

    public boolean deleteSpeciality(long id) {
        String sql = "DELETE FROM speciality WHERE speciality_id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
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
    public Speciality getSpecialityById(long id) {
        String sql = "SELECT speciality_id, name FROM speciality WHERE speciality_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Speciality(
                            resultSet.getLong("speciality_id"),
                            resultSet.getString("name")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
