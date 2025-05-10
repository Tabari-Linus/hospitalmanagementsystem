package lii.hospitalmanagementsystem.model;
public class Speciality {
    private Long specialityId;
    private String name;

    public Speciality() {}

    public Speciality(Long specialityId, String name) {
        this.specialityId = specialityId;
        this.name = name;
    }

    public Long getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(Long specialityId) {
        this.specialityId = specialityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}