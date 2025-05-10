package lii.hospitalmanagementsystem.databasecrud;


import lii.hospitalmanagementsystem.model.PatientTreatment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientTreatmentCRUD {

    public boolean insertPatientTreatment(PatientTreatment treatment) {
        String sql = "INSERT INTO patienttreatment (patient_id, doctor_id, treatment_date, remarks, patientadmission_id) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, treatment.getPatientId());
                statement.setLong(2, treatment.getDoctorId());
                statement.setDate(3, Date.valueOf(treatment.getTreatmentDate()));
                statement.setString(4, treatment.getRemarks());
                statement.setLong(5, treatment.getPatientAdmissionId());

                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    treatment.setId(rs.getLong(1));
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


    public List<PatientTreatment> getAllPatientTreatments() {
        List<PatientTreatment> treatments = new ArrayList<>();
        String sql = "SELECT * FROM patienttreatment";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                PatientTreatment treatment = new PatientTreatment(
                        resultSet.getLong("id"),
                        resultSet.getLong("patient_id"),
                        resultSet.getLong("doctor_id"),
                        resultSet.getDate("treatment_date").toLocalDate(),  // convert to LocalDate
                        resultSet.getString("remarks"),
                        resultSet.getLong("patientadmission_id")
                );
                treatments.add(treatment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return treatments;
    }

    public boolean updatePatientTreatment(PatientTreatment treatment) {
        String sql = "UPDATE patienttreatment SET patient_id = ?, doctor_id = ?, treatment_date = ?, remarks = ?, patientadmission_id = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, treatment.getPatientId());
                statement.setLong(2, treatment.getDoctorId());
                statement.setDate(3, Date.valueOf(treatment.getTreatmentDate()));
                statement.setString(4, treatment.getRemarks());
                statement.setLong(5, treatment.getPatientAdmissionId());
                statement.setLong(6, treatment.getId());
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

    public boolean deletePatientTreatment(long id) {
        String sql = "DELETE FROM patienttreatment WHERE id = ?";
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


    public List<PatientTreatment> getAllTreatments() throws Exception {
        List<PatientTreatment> treatments = new ArrayList<>();
        String sql = "SELECT * FROM patienttreatment";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                treatments.add(new PatientTreatment(
                        rs.getLong("id"),
                        rs.getLong("patient_id"),
                        rs.getLong("doctor_id"),
                        rs.getDate("treatment_date").toLocalDate(),
                        rs.getString("remarks"),
                        rs.getLong("patientadmission_id")
                ));
            }
        }
        return treatments;
    }



    public List<PatientTreatment> getTreatmentsByPatient(Long patientId) throws Exception {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }

        final String sql = "SELECT * FROM patienttreatment WHERE patient_id = ?";
        List<PatientTreatment> treatments = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, patientId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    PatientTreatment treatment = new PatientTreatment(
                            resultSet.getLong("id"),
                            resultSet.getLong("patient_id"),
                            resultSet.getLong("doctor_id"),
                            resultSet.getDate("treatment_date").toLocalDate(),
                            resultSet.getString("remarks"),
                            resultSet.getLong("patientadmission_id"));
                    treatments.add(treatment);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error retrieving treatments for patient ID: " + patientId, e);
        }

        return treatments;
    }

}