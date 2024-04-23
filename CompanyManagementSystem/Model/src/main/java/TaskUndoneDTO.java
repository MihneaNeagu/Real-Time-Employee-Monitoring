public class TaskUndoneDTO {
    private String description;
    private String name;
    private Integer employeeId;

    public TaskUndoneDTO(String description, String name, Integer employeeId) {
        this.description = description;
        this.name = name;
        this.employeeId = employeeId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TaskUndoneDTO{" +
                "description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", employeeId=" + employeeId +
                '}';
    }
}
