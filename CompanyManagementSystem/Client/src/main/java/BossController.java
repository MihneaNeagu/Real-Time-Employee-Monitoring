import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BossController implements ObserverInterface {

    public TableView<TaskUndoneDTO> tasksTable;
    public Button removeButton;
    public TextField taskDescriptionTextBox;
    public TableView<Employee> loggedEmployeesTable;
    private Service service;
    private Boss boss;

    private static <E> void initializeTable(TableView<E> tabel, List<String> columnNames) {
        for (String columnName : columnNames) {
            TableColumn<E, String> column = new TableColumn<>(columnName);
            column.setCellValueFactory(new PropertyValueFactory<>(columnName));
            tabel.getColumns().add(column);
        }
    }

    public void initialize(Service service,Boss boss) throws Exception {
        this.service=service;
        this.boss=boss;
        //service.loginBoss(boss,this);
        List<String> tasksColumnNames=Arrays.asList("description","name","employeeId");
        initializeTable(tasksTable,tasksColumnNames);
        List<Task> undoneTasks=service.getUndoneTasks();
        List<TaskUndoneDTO> taskUndoneDTOS=fromTaskToDTO(undoneTasks);
        tasksTable.setItems(FXCollections.observableArrayList(taskUndoneDTOS));

        TableColumn<Employee, String> column = new TableColumn<>("Name");
        column.setCellValueFactory(new PropertyValueFactory<>("name"));
        loggedEmployeesTable.getColumns().add(column);
        loggedEmployeesTable.setItems(FXCollections.observableArrayList(service.getLoggedEmployees()));

    }

    public List<TaskUndoneDTO> fromTaskToDTO(List<Task> undoneTasks){
        List<TaskUndoneDTO> taskUndoneDTOS=new ArrayList<>();
        for (Task task:undoneTasks){
            TaskUndoneDTO taskUndoneDTO=new TaskUndoneDTO(
                    task.getDescription(),service.findEmployee(task.getEid()).getName(), task.getEid());
            taskUndoneDTOS.add(taskUndoneDTO);
        }
        return taskUndoneDTOS;
    }
    public void removeTask(){
        TaskUndoneDTO taskToBeRemovedDTO=tasksTable.getSelectionModel().getSelectedItem();
        service.removeTask(taskToBeRemovedDTO);
        tasksTable.setItems(FXCollections.observableArrayList(
                fromTaskToDTO(service.getUndoneTasks())));
    }

    public void showDescription(){
        String taskDescription=tasksTable.getSelectionModel().getSelectedItem().getDescription();
        taskDescriptionTextBox.setText(taskDescription);
    }

    public void updateTask(){
        String newDescription=taskDescriptionTextBox.getText();
        TaskUndoneDTO taskUndoneDTO=tasksTable.getSelectionModel().getSelectedItem();
        service.updateTask(taskUndoneDTO,newDescription);
        tasksTable.setItems(FXCollections.observableArrayList(
                fromTaskToDTO(service.getUndoneTasks())));
    }

    public void sendTask(){
        String description=taskDescriptionTextBox.getText();
        Employee employee=loggedEmployeesTable.getSelectionModel().getSelectedItem();
        service.sendTask(description,employee);
    }

    public void logout(){
        service.logoutBoss(boss);
        Stage stage = (Stage) tasksTable.getScene().getWindow();
        stage.close();
    }

    @Override
    public void update() throws Exception {
        System.out.println("Received update!");
        Platform.runLater(() -> {
            try {
                System.out.println("UPDATE TABLE NOTIFY ALL");
                tasksTable.setItems(FXCollections.observableArrayList(
                        fromTaskToDTO(service.getUndoneTasks())));
                loggedEmployeesTable.setItems(FXCollections.observableArrayList(service.getLoggedEmployees()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
