package njp.raf.cloud.machine.dto;

import lombok.Data;
import njp.raf.cloud.machine.domain.Machine;
import njp.raf.cloud.machine.domain.MachineStatus;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@Data
public class MachineSearchRequest {

    private Long ownerId;

    private String name;

    private Date date;

    private Collection<@NotNull MachineStatus> statuses;

    public String getDate() {
        if (date == null)
            return null;

        return Machine.dateFormat.format(date);
    }

    public Date getDateAsObject() {
        try {
            // setting dates time to the latest time possible for the comparison to be accurate
            return new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(getDate() + " 23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public void setDate(String date) {
        if (date == null)
            return;

        try {
            this.date = Machine.dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
