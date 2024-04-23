import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    public TextField usernameTextBox;
    public TextField passwordTextBox;
    private Service service;

    @FXML
    protected void login() throws Exception {
        String username = usernameTextBox.getText();
        String password = passwordTextBox.getText();
        Boss boss = service.getBossAccount(username, password);
        if (boss != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/boss-view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(fxmlLoader.load(), 320, 240);

                System.out.println("boss received : " + boss);
                BossController bossController = fxmlLoader.getController();
                bossController.initialize(service, boss);
                service.loginBoss(boss, bossController);
                stage.setTitle(boss.getUsername());
                stage.setScene(scene);
                stage.setHeight(450);
                stage.setWidth(900);
                stage.setTitle(boss.getName());
                stage.show();

            } catch (Exception e) {
                Alert a=new Alert(Alert.AlertType.ERROR,e.getMessage());
                a.show();
            }
        }else{
            Employee employee=service.getEmployeeAccount(username,password);
            if(employee!=null){
                try{
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/employee-view.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(fxmlLoader.load(), 320, 240);

                    System.out.println("employee received : " + employee);
                    EmployeeController employeeController = fxmlLoader.getController();
                    employeeController.initialize(service, employee);
                    service.loginEmployee(employee, employeeController);
                    stage.setTitle(employee.getUsername());
                    stage.setScene(scene);
                    stage.setHeight(450);
                    stage.setWidth(900);
                    stage.setTitle(employee.getName());
                    stage.show();

                }catch(Exception e){
                    Alert a=new Alert(Alert.AlertType.ERROR,e.getMessage());
                    a.show();

                }
            }else{
                Alert a=new Alert(Alert.AlertType.ERROR,"Wrong Credentials!");
                a.show();
            }
        }
    }


    public void setService(Service service) {
        this.service = service;
    }
}