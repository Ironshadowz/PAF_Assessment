package vttp2023.batch3.assessment.paf.bookings.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vttp2023.batch3.assessment.paf.bookings.models.Details;
import vttp2023.batch3.assessment.paf.bookings.models.Form;
import vttp2023.batch3.assessment.paf.bookings.models.Result;
import vttp2023.batch3.assessment.paf.bookings.models.Search;
import vttp2023.batch3.assessment.paf.bookings.repositories.ListingsRepository;
import vttp2023.batch3.assessment.paf.bookings.repositories.ReservationRepo;

@Service
public class ListingsService 
{
	@Autowired
	ListingsRepository listRepo;

	@Autowired
	ReservationRepo resRepo;

	public List<String> countryList()
	{
		return listRepo.getCountry();
	}

	public Optional<List<Result>> showListing(Search search)
	{
		Optional<List<Document>> searchResults = listRepo.getListing(search);
		if(searchResults.isEmpty())
		{
			return Optional.empty();
		}
		List<Result> results = new ArrayList<>();
		for(Document d : searchResults.get())
		{
			Result result = new Result();
			result.setAddress(d.getString("address.street"));
			result.setPrice(d.getInteger("price"));
			result.setImage(d.getString("images.picture_url"));
			results.add(result);
		}
		return Optional.of(results);
	}

	public Optional<Details> getDetails(String address)
	{
		Optional<Document> doc = listRepo.getDetails(address);
		if(doc.isEmpty())
			return Optional.empty();
		Details detail = new Details();
		Document d = doc.get();
		detail.setAddress(address);
		detail.setAmenities(d.getString("amenitites"));
		detail.setDescription(d.getString("description"));
		detail.setId(d.getString("_id"));
		detail.setImage(d.getString("images.picture_url"));
		detail.setPrice(d.getString("price"));
		return Optional.of(detail);
	}
	
	public int checkVacancy(String days)
	{
		int vacancy = resRepo.checkVacancy(days);
		return vacancy;
	}

	public String generateID()
	{
		String resId = UUID.randomUUID().toString().substring(0, 8);
		return resId;
	}
	@Transactional
	public boolean book(Form form, String id, String occupancy, int vacancy)
	{
		boolean booked = resRepo.bookBNB(form, id, occupancy);
		//update acc_occupancy
		int newVancancy = vacancy-form.getDays();
		resRepo.updateOccu(newVancancy, id);
		return booked;
	}
	//TODO: Task 4
	

	//TODO: Task 5


}
