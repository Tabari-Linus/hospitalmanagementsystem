package lii.hospitalmanagementsystem.databasecrud;

import lii.hospitalmanagementsystem.model.Ward;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WardCRUD {

    public boolean insertWard(Ward ward) {
        String sql = "INSERT INTO ward (department_id, ward_number, supervisor_id, bed_count) " +
                "VALUES (?, ?, ?, ?) RETURNING ward_id";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, ward.getDepartmentId());
                statement.setInt(2, ward.getWardNumber());
                statement.setLong(3, ward.getSupervisorId());
                statement.setInt(4, ward.getBedCount());

                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        ward.setWardId(rs.getLong(1));
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

    public List<Ward> getAllWards() {
        List<Ward> wards = new ArrayList<>();
        String sql = "SELECT * FROM ward";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Ward ward = new Ward(
                        resultSet.getLong("ward_id"),
                        resultSet.getLong("department_id"),
                        resultSet.getInt("ward_number"),
                        resultSet.getLong("supervisor_id"),
                        resultSet.getInt("bed_count")
                );
                wards.add(ward);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wards;
    }

    public boolean updateWard(Ward ward) {
        String sql = "UPDATE ward SET department_id = ?, ward_number = ?, supervisor_id = ?, bed_count = ? WHERE ward_id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, ward.getDepartmentId());
                statement.setInt(2, ward.getWardNumber());
                statement.setLong(3, ward.getSupervisorId());
                statement.setInt(4, ward.getBedCount());
                statement.setLong(5, ward.getWardId());
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

    public boolean deleteWard(long wardId) {
        String sql = "DELETE FROM ward WHERE ward_id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, wardId);
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

    public Ward getWardById(Long wardId) throws Exception {
        String sql = "SELECT * FROM ward WHERE ward_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, wardId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Ward(
                        rs.getLong("ward_id"),
                        rs.getLong("department_id"),
                        rs.getInt("ward_number"),
                        rs.getLong("supervisor_id"),
                        rs.getInt("bed_count")
                );
            }
        }
        return null;
    }
}
