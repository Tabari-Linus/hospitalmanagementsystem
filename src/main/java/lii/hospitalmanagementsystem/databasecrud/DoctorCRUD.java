package lii.hospitalmanagementsystem.databasecrud;


import lii.hospitalmanagementsystem.model.Doctor;
import lii.hospitalmanagementsystem.model.DoctorDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorCRUD {



    public void insertDoctor(Doctor doctor) throws SQLException {
        String sql = "INSERT INTO doctor (employee_id, speciality_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, doctor.getEmployeeId());
            stmt.setLong(2, doctor.getSpecialityId());

            stmt.executeUpdate();
        }
    }



    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctor";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Doctor doctor = new Doctor(
                        resultSet.getLong("employee_id"),
                        resultSet.getLong("speciality_id")
                );
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching doctors", e);
        }
        return doctors;
    }

    public boolean updateDoctor(Doctor doctor) {
        String sql = "UPDATE doctor SET speciality_id = ? WHERE employee_id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, doctor.getSpecialityId());
                statement.setLong(2, doctor.getEmployeeId());
                boolean result = statement.executeUpdate() > 0;
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error updating doctor", e);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDoctor(long employeeId) {
        String sql = "DELETE FROM doctor WHERE employee_id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, employeeId);
                boolean result = statement.executeUpdate() > 0;
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error deleting doctor", e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<DoctorDTO> getDoctorsWithSpecialities() {
        List<DoctorDTO> doctors = new ArrayList<>();
        String sql = """
        SELECT e.employee_id, e.first_name, e.surname, e.address, e.telephone_no, s.name as speciality_name
        FROM employee e
        INNER JOIN doctor d ON e.employee_id = d.employee_id
        INNER JOIN speciality s ON d.speciality_id = s.speciality_id
        ORDER BY e.employee_id
    """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                DoctorDTO doctor = new DoctorDTO(
                        resultSet.getLong("employee_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("surname"),
                        resultSet.getString("address"),
                        resultSet.getLong("telephone_no"),
                        resultSet.getString("speciality_name")
                );
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching doctors with specialities", e);
        }
        return doctors;
    }

    public List<Doctor> getDoctorsBySpeciality(Long specialityId) throws Exception {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctor WHERE speciality_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, specialityId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                doctors.add(new Doctor(
                        rs.getLong("employee_id"),
                        rs.getLong("speciality_id")
                ));
            }
        }
        return doctors;
    }


}