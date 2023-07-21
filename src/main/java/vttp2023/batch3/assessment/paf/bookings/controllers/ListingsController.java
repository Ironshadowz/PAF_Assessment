package vttp2023.batch3.assessment.paf.bookings.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import vttp2023.batch3.assessment.paf.bookings.models.Details;
import vttp2023.batch3.assessment.paf.bookings.models.Form;
import vttp2023.batch3.assessment.paf.bookings.models.Result;
import vttp2023.batch3.assessment.paf.bookings.models.Search;
import vttp2023.batch3.assessment.paf.bookings.services.ListingsService;

@Controller
@RequestMapping
public class ListingsController 
{
	@Autowired
	ListingsService listService;

	@GetMapping
	public ModelAndView landingPage()
	{
		ModelAndView mav = new ModelAndView();
		mav.setViewName("View1");
		List<String> countries = listService.countryList();
		mav.addObject("countries", countries);
		return mav;
	}

	@GetMapping("/search")
	public ModelAndView getListing(@RequestParam String country,@RequestParam int people, @RequestParam int max, @RequestParam int min, HttpSession session)
	{	
		Search search = new Search();
		if(session.getAttribute("counrty")==null)
		{
			search.setCountry(country);
			search.setMax(max);
			search.setMin(min);
			search.setPeople(people);
		} else
		{
			search.setCountry((String)session.getAttribute("country"));
			search.setMax((int)session.getAttribute("max"));
			search.setMin((int)session.getAttribute("min"));
			search.setPeople((int)session.getAttribute("people"));
		}
		ModelAndView mav = new ModelAndView();
		session.setAttribute("country", country);
		session.setAttribute("people", people);
		session.setAttribute("max", max);
		session.setAttribute("min", min);
		Optional<List<Result>> result = listService.showListing(search);
		if(result.isEmpty())
		{
			mav.setViewName("View1");
			List<String> countries = listService.countryList();
			mav.addObject("countries", countries);
			mav.addObject("SearchError", "No accomodation matches your criterias");
			return mav;
		}
		mav.setViewName("View2");
		mav.addObject("country", search.getCountry());
		mav.addObject("listing", result.get());
		return mav;
	}

	@GetMapping("/details")
	public ModelAndView getDetails(HttpSession session, @RequestParam String address)
	{
		ModelAndView mav = new ModelAndView();
		Optional<Details> detail = listService.getDetails(address);
		if(detail.isEmpty())
		{
			mav.setViewName("Notfound");
			return mav;
		}
		Form form = new Form();
		session.setAttribute("address", address);
		session.setAttribute("id", detail.get().getId());
		mav.setViewName("View3");
		mav.addObject("details", detail.get());
		mav.addObject("form", form);
		return mav;
	}
	@PostMapping("/book")
	public ModelAndView book(@ModelAttribute Form form, HttpSession session)
	{
		int vacancy = listService.checkVacancy((String)session.getAttribute("id"));
		if(form.getDays()>vacancy)
		{
			ModelAndView mav = new ModelAndView("redirect:/details");
			mav.addObject("reservationError", "Accomodation is not available.");
			mav.addObject("address", session.getAttribute("address"));
			return mav;
		}
		ModelAndView mav = new ModelAndView();
		String reservationId = listService.generateID();
		listService.book(form, reservationId, (String)session.getAttribute("id"), vacancy);
		mav.setViewName("booked");
		mav.addObject("reservationID", reservationId);
		return mav;
	}
	@GetMapping("/end")
	public String end(HttpSession session)
	{
		session.invalidate();
		return "redirect:/";
	}
	//TODO: Task 3


	//TODO: Task 4
	

	//TODO: Task 5


}
