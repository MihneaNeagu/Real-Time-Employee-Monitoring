import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class HelloApplication extends Application {
    private static SessionFactory sessionFactory;

    @Override
    public void start(Stage stage) throws IOException {
        initialize();
        BossRepository bossRepository=new BossRepository(sessionFactory);
        TaskRepository taskRepository=new TaskRepository(sessionFactory);
        EmployeeRepository employeeRepository=new EmployeeRepository(sessionFactory);
        Service service=new Service(taskRepository,employeeRepository,bossRepository);
        for (Boss boss : bossRepository.findAll()) {
            System.out.println(boss);
        }
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/login-view.fxml"));
        Parent root=fxmlLoader.load();
        LoginController loginController=fxmlLoader.getController();
        loginController.setService(service);


        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }


    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exceptie "+e);

            System.err.println(Arrays.toString(e.getStackTrace()));
            StandardServiceRegistryBuilder.destroy( registry );
            //throw e;
        }
    }
    static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    public static void main(String[] args) {
        Application.launch();
    }
}