import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaskRepository implements Repository<Integer,Task>{
    public final SessionFactory sessionFactory;

    public TaskRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(Task elem) {
        System.out.println("adding");
    }

    @Override
    public void update(Integer integer, Task elem) {

    }

    @Override
    public Iterable<Task> findAll() {
        return null;
    }

    @Override
    public Task findOne(Integer id) {
        return null;
    }

    public List<Task> getUndoneTasks() {
        System.out.println("getUndoneTasks() Tasks Hibernate Repository called");
        List<Task> tasks=new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session.createNativeQuery("select * from tasks where done=0");
            List<Objects[]> usersResult = query.list();
            for (Object[] a : usersResult) {
                Integer id=Integer.parseInt(a[0].toString());
                String description=a[1].toString();
                Integer eid=Integer.parseInt(a[2].toString());
                Integer done=Integer.parseInt(a[3].toString());

                Task task=new Task(id,description,eid,done);
                tasks.add(task);
            }
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            System.err.println("Error find All " + ex);
        }
        System.out.println("Return: "+tasks);
        return tasks;
    }

    public void removeTask(TaskUndoneDTO taskToBeRemovedDTO) {
        System.out.println("removeTask() Task Hibernate Repository called with data:" + taskToBeRemovedDTO);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session.createNativeQuery("delete from Tasks where eid=? and description=?");
            query.setParameter(1, taskToBeRemovedDTO.getEmployeeId());
            query.setParameter(2, taskToBeRemovedDTO.getDescription());
            int result=query.executeUpdate();
            if(result>=0)
                System.out.println("Delete Successful!");
            else System.out.println("Delete Unsuccessful!");
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            System.err.println("Error find All " + ex);
        }


    }

    public void update(TaskUndoneDTO taskUndoneDTO, String newDescription) {
        System.out.println("update() Task Hibernate Repository called with data:" + taskUndoneDTO + newDescription);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session.createNativeQuery(
                    "update Tasks set description=? where eid=? and description=?");
            query.setParameter(1, newDescription);
            query.setParameter(2, taskUndoneDTO.getEmployeeId());
            query.setParameter(3, taskUndoneDTO.getDescription());
            int result=query.executeUpdate();
            if(result>=0)
                System.out.println("Update Successful!");
            else System.out.println("Update Unsuccessful!");
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            System.err.println("Error find All " + ex);
        }
    }

    public List<Task> getUndoneTasks(Employee employee) {
        System.out.println("getUndoneTasks() Tasks Hibernate Repository called");
        List<Task> tasks=new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session.createNativeQuery("select * from tasks where done=0 and eid=?");
            query.setParameter(1,employee.getId());
            List<Objects[]> usersResult = query.list();
            for (Object[] a : usersResult) {
                Integer id=Integer.parseInt(a[0].toString());
                String description=a[1].toString();
                Integer eid=Integer.parseInt(a[2].toString());
                Integer done=Integer.parseInt(a[3].toString());

                Task task=new Task(id,description,eid,done);
                tasks.add(task);
            }
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            System.err.println("Error find All " + ex);
        }
        System.out.println("Return: "+tasks);
        return tasks;
    }

    public void doTask(Task task) {
        System.out.println("update() Task Hibernate Repository called with data:" + task);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session.createNativeQuery(
                    "update Tasks set done=1 where id=?");
            query.setParameter(1, task.getId());
            int result=query.executeUpdate();
            if(result>=0)
                System.out.println("Update Successful!");
            else System.out.println("Update Unsuccessful!");
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            System.err.println("Error find All " + ex);
        }
    }

    public void add(String description, Employee employee) {
        System.out.println("add() Task Hibernate Repository called with data:" + description+employee);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session.createNativeQuery(
                    "insert into Tasks(description,eid,done) values (?,?,0)");
            query.setParameter(1, description);
            query.setParameter(2, employee.getId());
            int result=query.executeUpdate();
            if(result>=0)
                System.out.println("Add Successful!");
            else System.out.println("Add Unsuccessful!");
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            System.err.println("Error find All " + ex);
        }
    }
}
