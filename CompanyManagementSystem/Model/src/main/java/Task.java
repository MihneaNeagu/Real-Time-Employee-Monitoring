import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "Bosses")

public class Task implements Identifiable<Integer>{

    private Integer id;
    private String description;
    private Integer eid;
    private Integer done;

    public Task(Integer id, String description, Integer eid, Integer done) {
        this.id = id;
        this.description = description;
        this.eid = eid;
        this.done = done;
    }

    public Task() {

    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Override
    public Integer getId() {
        return this.id;
    }
    @Override
    public void setId(Integer id) {
        this.id=id;
    }


    @Column(name="description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name="eid")
    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    @Column(name="done")
    public Integer getDone() {
        return done;
    }

    public void setDone(Integer done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", eid=" + eid +
                ", done=" + done +
                '}';
    }
}
