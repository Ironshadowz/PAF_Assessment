package vttp2023.batch3.assessment.paf.bookings.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Form 
{
    private String name;
    private String email;
    private Date date;
    private int days;
}
