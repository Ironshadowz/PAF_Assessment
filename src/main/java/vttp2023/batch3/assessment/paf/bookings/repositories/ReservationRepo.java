package vttp2023.batch3.assessment.paf.bookings.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp2023.batch3.assessment.paf.bookings.models.Form;

@Repository
public class ReservationRepo 
{
    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String checkVacancy = "select vacancy from acc_occupancy where acc_id = ?";
    private final String bookReservation ="insert into reservations values(?, ?, ?, ?, ?, ?)";
    private final String updateOccupancy = "update acc_occupancy set vacancy = ? where acc_id = ?";
 
    public int checkVacancy(String id)
    {
        List<Integer> day = jdbcTemplate.query(checkVacancy, BeanPropertyRowMapper.newInstance(Integer.class), id);
        return day.get(0);
    }

    public boolean bookBNB(Form form, String id, String occupancy)
    {
        int num = jdbcTemplate.update(bookReservation, id, form.getName(), form.getEmail(), occupancy, form.getDate(), form.getDays());
        return num>0 ? true:false;
    }

    public boolean updateOccu(int vacancy, String id)
    {
        int updated = jdbcTemplate.update(updateOccupancy, vacancy, id);
        return updated>0 ? true:false;
    }
}
