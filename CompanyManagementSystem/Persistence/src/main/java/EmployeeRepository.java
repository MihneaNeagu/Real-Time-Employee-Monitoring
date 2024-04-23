import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Objects;

public class EmployeeRepository implements Repository<Integer,Employee>{
    public final SessionFactory sessionFactory;

    public EmployeeRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(Employee elem) {

    }

    @Override
    public void update(Integer integer, Employee elem) {

    }

    @Override
    public Iterable<Employee> findAll() {
        return null;
    }

    @Override
    public Employee findOne(Integer id) {
        Employee employee = null;
        System.out.println("findOne() Employee Hibernate Repository called");
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session.createNativeQuery("select * from employees where id=?");
            query.setParameter(1, id);
            List<Objects[]> usersResult = query.list();
            for (Object[] a : usersResult) {
                String name=a[1].toString();
                String username=a[2].toString();
                String password=a[3].toString();
                employee = new Employee(id,username,password,name);
            }
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            System.err.println("Error find All " + ex);
        }
        System.out.println("Return: "+employee);
        return employee;
    }

    public Employee getAccount(String username, String password) {
        Employee employee = null;
        System.out.println("getAccount() Boss Hibernate Repository called");
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session.createNativeQuery("select * from employees where username=? and password=?");
            query.setParameter(1, username);
            query.setParameter(2, password);
            List<Objects[]> usersResult = query.list();
            for (Object[] a : usersResult) {
                String name=a[1].toString();
                Integer Eid=Integer.parseInt(a[0].toString());
                employee = new Employee(Eid,username,password,name);
            }
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            System.err.println("Error find All " + ex);
        }
        System.out.println("Return: "+employee);
        return employee;
    }
}
