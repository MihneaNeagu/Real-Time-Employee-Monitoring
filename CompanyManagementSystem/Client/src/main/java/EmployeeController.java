import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class EmployeeController implements ObserverInterface{
    public TableView<Task> tasksTable;
    private Service service;
    private Employee employee;
    @Override
    public void update() throws Exception {
        System.out.println("Received update!");
        Platform.runLater(() -> {
            try {
                System.out.println("UPDATE TABLE NOTIFY ALL");
                tasksTable.setItems(FXCollections.observableArrayList(service.getUndoneTasks(employee)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void initialize(Service service, Employee employee) {
        this.service=service;
        this.employee=employee;
        TableColumn<Task, String> column = new TableColumn<>("Description");
        column.setCellValueFactory(new PropertyValueFactory<>("description"));
        tasksTable.getColumns().add(column);
        tasksTable.setItems(FXCollections.observableArrayList(service.getUndoneTasks(employee)));
    }
    public void doTask(){
        Task task=tasksTable.getSelectionModel().getSelectedItem();
        service.doTask(task);
        tasksTable.setItems(FXCollections.observableArrayList(service.getUndoneTasks(employee)));

    }

    public void logout(){
        service.logoutEmployee(employee);
        Stage stage = (Stage) tasksTable.getScene().getWindow();
        stage.close();
    }
}
