package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
    private static final int defaultPort = 55555;

    private static SessionFactory sessionFactory;

    @Override
    public void start(Stage stage) throws IOException {
        Properties serverProps = new Properties();
        try {
            serverProps.load(HelloApplication.class.getResourceAsStream("/application.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch(IOException e){
            System.err.println("Cannot find application.properties " + e);
            return;
        }
        initialize();
        BossRepository bossRepository=new BossRepository(sessionFactory);
        for (Boss boss : bossRepository.findAll()) {
            System.out.println(boss);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
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
            throw e;
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