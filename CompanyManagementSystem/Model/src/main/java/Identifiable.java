import java.io.Serializable;

public interface Identifiable<ID> extends Serializable {
    static final long serialVersionUID = 7331115341259248461L;

    void setId(ID id);
    ID getId();
}
