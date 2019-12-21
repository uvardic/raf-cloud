package njp.raf.cloud.machine.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import njp.raf.cloud.user.domain.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name = "machine")
public class Machine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String uuid;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MachineStatus status;

    @JsonIdentityReference(alwaysAsId = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "owner_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User owner;

    @Temporal(TemporalType.DATE)
    @Column(updatable = false, nullable = false)
    private Date dateFrom;

    @Temporal(TemporalType.DATE)
    private Date dateTo;

    private boolean active;

    @JsonIgnore
    private transient final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public String getDateFrom() {
        if (dateFrom == null)
            return dateFormat.format(new Date());

        return dateFormat.format(dateFrom);
    }

    public void setDateFrom(String dateFrom) {
        try {
            this.dateFrom = dateFormat.parse(dateFrom);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setDateFromToCurrentDate() {
        this.dateFrom = new Date();
    }

    public String getDateTo() {
        if (dateTo == null)
            return "00-00-0000";

        return dateFormat.format(dateTo);
    }

    public void setDateTo(String dateTo) {
        try {
            this.dateTo = dateFormat.parse(dateTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setDateToToCurrentDate() {
        this.dateTo = new Date();
    }

}
