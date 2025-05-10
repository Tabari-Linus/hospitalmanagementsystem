package lii.hospitalmanagementsystem.databasecrud;


import lii.hospitalmanagementsystem.model.PatientAdmission;
import lii.hospitalmanagementsystem.databasecrud.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PatientAdmissionCRUD {

    public boolean insertPatientAdmission(PatientAdmission admission) {
        String sql = "INSERT INTO patientadmission (patient_id, ward_id, bed_number, diagnosis, date_admitted, date_discharged) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, admission.getPatientId());
                statement.setLong(2, admission.getWardId());
                statement.setInt(3, admission.getBedNumber());
                statement.setString(4, admission.getDiagnosis());
                statement.setDate(5, Date.valueOf(admission.getDateAdmitted()));
                statement.setDate(6, admission.getDateDischarged() != null ?
                        Date.valueOf(admission.getDateDischarged()) : null);

                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        admission.setId(rs.getLong(1));
                        connection.commit();
                        return true;
                    }
                }
                connection.rollback();
                return false;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error inserting patient admission", e);

            }
        } catch (SQLException e) {
            return false;
        }
    }

    public List<PatientAdmission> getAllAdmissions() throws Exception {
        List<PatientAdmission> admissions = new ArrayList<>();
        String sql = "SELECT * FROM patientadmission";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                admissions.add(new PatientAdmission(
                        rs.getLong("id"),
                        rs.getLong("patient_id"),
                        rs.getLong("ward_id"),
                        rs.getInt("bed_number"),
                        rs.getString("diagnosis"),
                        rs.getDate("date_admitted").toLocalDate(),
                        rs.getDate("date_discharged") != null ? rs.getDate("date_discharged").toLocalDate() : null
                ));
            }
        }
        return admissions;
    }

    public List<PatientAdmission> getCurrentAdmissionsByWard(Long wardId) throws Exception {
        List<PatientAdmission> admissions = new ArrayList<>();
        String sql = "SELECT * FROM patientadmission WHERE ward_id = ? AND date_discharged IS NULL";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, wardId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    admissions.add(new PatientAdmission(
                            rs.getLong("id"),
                            rs.getLong("patient_id"),
                            rs.getLong("ward_id"),
                            rs.getInt("bed_number"),
                            rs.getString("diagnosis"),
                            rs.getDate("date_admitted").toLocalDate(),
                            null
                    ));
                }
            }
        }
        return admissions;
    }

    public boolean updateAdmission(PatientAdmission admission) {
        String sql = "UPDATE patientadmission SET patient_id = ?, ward_id = ?, bed_number = ?, diagnosis = ?, " +
                "date_admitted = ?, date_discharged = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, admission.getPatientId());
                statement.setLong(2, admission.getWardId());
                statement.setInt(3, admission.getBedNumber());
                statement.setString(4, admission.getDiagnosis());
                statement.setDate(5, Date.valueOf(admission.getDateAdmitted()));
                statement.setDate(6, admission.getDateDischarged() != null ?
                        Date.valueOf(admission.getDateDischarged()) : null);
                statement.setLong(7, admission.getId());

                boolean result = statement.executeUpdate() > 0;
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error updating patient admission", e);
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deletePatientAdmission(long id) {
        String sql = "DELETE FROM patientadmission WHERE id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                boolean result = statement.executeUpdate() > 0;
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error deleting patient admission", e);

            }
        } catch (SQLException e) {
            return false;
        }
    }

    public PatientAdmission getAdmissionById(Long patientId) throws Exception {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }

        String sql = "SELECT * FROM patientadmission WHERE patient_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, patientId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new PatientAdmission(
                            rs.getLong("id"),
                            rs.getLong("patient_id"),
                            rs.getLong("ward_id"),
                            rs.getInt("bed_number"),
                            rs.getString("diagnosis"),
                            rs.getDate("date_admitted").toLocalDate(),
                            rs.getDate("date_discharged") != null ?
                                    rs.getDate("date_discharged").toLocalDate() : null
                    );
                }
                return null;
            }
        }
    }

    public Collection<PatientAdmission> getAdmissionsByWard(long wardId) {
        String sql = "SELECT * FROM patientadmission WHERE ward_id = ?";
        List<PatientAdmission> admissions = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, wardId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    admissions.add(new PatientAdmission(
                            rs.getLong("id"),
                            rs.getLong("patient_id"),
                            rs.getLong("ward_id"),
                            rs.getInt("bed_number"),
                            rs.getString("diagnosis"),
                            rs.getDate("date_admitted").toLocalDate(),
                            rs.getDate("date_discharged") != null ?
                                    rs.getDate("date_discharged").toLocalDate() : null
                    ));
                }
            }
        } catch (SQLException e) {
            return Collections.emptyList();
        }
        return admissions;
    }

    public List<PatientAdmission> getAdmissionsByPatient(Long patientId) throws Exception {
        List<PatientAdmission> admissions = new ArrayList<>();
        String sql = "SELECT * FROM patientadmission WHERE patient_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, patientId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                admissions.add(new PatientAdmission(
                        rs.getLong("id"),
                        rs.getLong("patient_id"),
                        rs.getLong("ward_id"),
                        rs.getInt("bed_number"),
                        rs.getString("diagnosis"),
                        rs.getDate("date_admitted").toLocalDate(),
                        rs.getDate("date_discharged") != null ? rs.getDate("date_discharged").toLocalDate() : null
                ));
            }
        }
        return admissions;
    }
}
