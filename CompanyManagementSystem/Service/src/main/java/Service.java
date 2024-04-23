import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements ServiceInterface{
    private TaskRepository taskRepository;
    private EmployeeRepository employeeRepository;
    private BossRepository bossRepository;

    private Map<Integer,ObserverInterface> loggedEmployees;
    private Map<Integer,ObserverInterface> loggedBosses;

    public Service(TaskRepository taskRepository, EmployeeRepository employeeRepository, BossRepository bossRepository) {
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
        this.bossRepository = bossRepository;
        loggedBosses=new ConcurrentHashMap<>();
        loggedEmployees=new ConcurrentHashMap<>();
    }


    @Override
    public Boss getBossAccount(String username, String password) {
        return bossRepository.getAccount(username,password);
    }

    @Override
    public List<Task> getUndoneTasks() {
        return taskRepository.getUndoneTasks();
    }

    @Override
    public Employee findEmployee(Integer eid) {
        return employeeRepository.findOne(eid);
    }

    @Override
    public void updateTask(TaskUndoneDTO taskUndoneDTO, String newDescription) {
        taskRepository.update(taskUndoneDTO,newDescription);
        notifyClients();
    }

    @Override
    public void removeTask(TaskUndoneDTO taskToBeRemovedDTO) {
        taskRepository.removeTask(taskToBeRemovedDTO);
        notifyClients();
    }

    @Override
    public Employee getEmployeeAccount(String username, String password) {
        return employeeRepository.getAccount(username,password);
    }

    @Override
    public List<Task> getUndoneTasks(Employee employee) {
        return taskRepository.getUndoneTasks(employee);
    }

    @Override
    public void doTask(Task task) {
        taskRepository.doTask(task);
        notifyClients();
    }

    @Override
    public List<Employee> getLoggedEmployees() {
        List<Employee> loggedEmployees= new ArrayList<>();
        for(Integer key:this.loggedEmployees.keySet()){
            loggedEmployees.add(employeeRepository.findOne(key));
        }
        return loggedEmployees;
    }

    @Override
    public void logoutEmployee(Employee employee) {
        loggedEmployees.remove(employee.getId());
        notifyClients();
    }

    @Override
    public void logoutBoss(Boss boss) {
        loggedBosses.remove(boss.getId());
    }

    @Override
    public void sendTask(String description, Employee employee) {
        taskRepository.add(description,employee);
        notifyClients();
    }

    public synchronized Boss loginBoss(Boss boss, ObserverInterface client) throws Exception {
        Boss boss1 = bossRepository.getAccount(boss.getUsername(), boss.getPassword());
        if (boss1 != null) {
            if (loggedBosses.get(boss1.getId()) != null)
                throw new Exception("Boss already logged in.");
            loggedBosses.put(boss1.getId(), client);
            System.out.println("Logged bosses: " + loggedBosses);
        } else
            throw new Exception("Authentication failed.");
        return boss1;
    }

    public synchronized Employee loginEmployee(Employee employee, ObserverInterface client) throws Exception {
        Employee employee1 = employeeRepository.getAccount(employee.getUsername(), employee.getPassword());
        if (employee1 != null) {
            if (loggedEmployees.get(employee1.getId()) != null)
                throw new Exception("Employee already logged in.");
            loggedEmployees.put(employee1.getId(), client);
            System.out.println("Logged bosses: " + loggedEmployees);
            notifyClients();
        } else
            throw new Exception("Authentication failed.");
        return employee1;
    }
    private synchronized void notifyClients() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for(Integer key : loggedBosses.keySet()) {
            ObserverInterface clientT = loggedBosses.get(key);
            executor.execute(() -> {
                try {
                    clientT.update();
                } catch (Exception e) {
                    System.err.println("Error notifying client " + e);
                }
            });
        }
        for(Integer key : loggedEmployees.keySet()) {
            ObserverInterface clientT = loggedEmployees.get(key);
            executor.execute(() -> {
                try {
                    clientT.update();
                } catch (Exception e) {
                    System.err.println("Error notifying client " + e);
                }
            });
        }
    }


}
