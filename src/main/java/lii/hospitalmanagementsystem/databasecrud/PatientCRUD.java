package lii.hospitalmanagementsystem.databasecrud;

import lii.hospitalmanagementsystem.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientCRUD {

    public boolean insertPatient(Patient patient) {
        String sql = "INSERT INTO patient (first_name, surname, address, telephone_no) " +
                "VALUES (?, ?, ?, ?) RETURNING patient_id";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, patient.getFirstName());
                statement.setString(2, patient.getSurname());
                statement.setString(3, patient.getAddress());
                statement.setLong(4, patient.getTelephoneNo());

                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        patient.setPatientId(rs.getLong(1));
                        connection.commit();
                        return true;
                    }
                }
                connection.rollback();
                return false;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error inserting patient", e);

            }
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patient";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Patient patient = new Patient(
                        resultSet.getLong("patient_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("surname"),
                        resultSet.getString("address"),
                        resultSet.getLong("telephone_no")
                );
                patients.add(patient);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching patients", e);
        }
        return patients;
    }

    public boolean updatePatient(Patient patient) {
        String sql = "UPDATE patient SET first_name = ?, surname = ?, address = ?, telephone_no = ? " +
                "WHERE patient_id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, patient.getFirstName());
                statement.setString(2, patient.getSurname());
                statement.setString(3, patient.getAddress());
                statement.setLong(4, patient.getTelephoneNo());
                statement.setLong(5, patient.getPatientId());

                boolean result = statement.executeUpdate() > 0;
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error updating patient", e);
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deletePatient(long patientId) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                String deleteAdmissionsSql = "DELETE FROM patientadmission WHERE patient_id = ?";
                try (PreparedStatement deleteAdmissionsStmt = connection.prepareStatement(deleteAdmissionsSql)) {
                    deleteAdmissionsStmt.setLong(1, patientId);
                    deleteAdmissionsStmt.executeUpdate();
                }

                String deletePatientSql = "DELETE FROM patient WHERE patient_id = ?";
                try (PreparedStatement deletePatientStmt = connection.prepareStatement(deletePatientSql)) {
                    deletePatientStmt.setLong(1, patientId);
                    boolean result = deletePatientStmt.executeUpdate() > 0;
                    connection.commit();
                    return result;
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new SQLException("Error deleting patient", e);
            }
        }
    }

    public Patient getPatientById(long id) throws SQLException {
        String sql = "SELECT patient_id, first_name, surname, address, telephone_no " +
                "FROM patient WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Patient(
                            rs.getLong("patient_id"),
                            rs.getString("first_name"),
                            rs.getString("surname"),
                            rs.getString("address"),
                            rs.getLong("telephone_no")
                    );
                }
                return null;
            }
        }
    }
}