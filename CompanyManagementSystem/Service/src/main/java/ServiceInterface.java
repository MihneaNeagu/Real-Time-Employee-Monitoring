import java.util.List;

public interface ServiceInterface {
    Boss getBossAccount(String username, String password);

    List<Task> getUndoneTasks();

    Employee findEmployee(Integer eid);

    void updateTask(TaskUndoneDTO taskUndoneDTO, String newDescription);

    public void removeTask(TaskUndoneDTO taskToBeRemovedDTO);

    Employee getEmployeeAccount(String username, String password);

    List<Task> getUndoneTasks(Employee employee);

    void doTask(Task task);

    List<Employee> getLoggedEmployees();

    void logoutEmployee(Employee employee);

    void logoutBoss(Boss boss);

    void sendTask(String description, Employee employee);
}
