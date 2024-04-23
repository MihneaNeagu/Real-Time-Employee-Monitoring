import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class BossRepository implements Repository<Integer, Boss>{
    public final SessionFactory sessionFactory;

    public BossRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(Boss elem) {

    }

    @Override
    public void update(Integer integer, Boss elem) {

    }

    @Override
    public Iterable<Boss> findAll() {
        System.out.println("findAll() Boss Hibernate Repository called");
        List<Boss> bosses=new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session.createNativeQuery("select * from bosses");
            List<Objects[]> usersResult = query.list();
            for (Object[] a : usersResult) {
                String name=a[0].toString();
                String username2=a[1].toString();
                String password2=a[2].toString();
                Integer Bid=Integer.parseInt(a[3].toString());

                Boss boss = new Boss(Bid,username2,password2,name);
                bosses.add(boss);
            }
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            System.err.println("Error find All " + ex);
        }
        System.out.println("Return: "+bosses);
        return bosses;
    }

    @Override
    public Boss findOne(Integer integer) {
        return null;
    }

    public Boss getAccount(String username, String password) {
        Boss boss = null;
        System.out.println("getAccount() Boss Hibernate Repository called");
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query = session.createNativeQuery("select * from bosses where username=? and password=?");
            query.setParameter(1, username);
            query.setParameter(2, password);
            List<Objects[]> usersResult = query.list();
            for (Object[] a : usersResult) {
                String name=a[0].toString();
                String username2=a[1].toString();
                String password2=a[2].toString();
                Integer Bid=Integer.parseInt(a[3].toString());
                boss = new Boss(Bid,username2,password2,name);
            }
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            System.err.println("Error find All " + ex);
        }
        System.out.println("Return: "+boss);
        return boss;
    }


}
