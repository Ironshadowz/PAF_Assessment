package vttp2023.batch3.assessment.paf.bookings.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp2023.batch3.assessment.paf.bookings.models.Search;

@Repository
public class ListingsRepository 
{
	@Autowired
	MongoTemplate template;

	private static final String C_LISTING = "listings";
	/*
	 * db.listings.distinct
		(
			'address.country'
		)
	 */
	public List<String> getCountry()
	{
		List<String> country = template.findDistinct(new Query(), "address.counrty", C_LISTING, String.class);
		return country;
	}

	/*
	 * db.listings.find
		(
			{
				$and: [{"address.country": "Spain"}, {accommodates":5},
				{"price":{$gte:22}}, {price:{$lte:100000}}]
			},{_id:0, name: 1,"address.street":1, "images.picture_url":1, price:1}
		).sort({"price":-1})
	 */
	public Optional<List<Document>> getListing(Search search)
	{
		Query query = Query.query
			(
				Criteria.where("address.country").is(search.getCountry())
				.and("accomodates").is(search.getPeople())
				.and("price").gte(search.getMin()).lte(search.getMax())
			).with(Sort.by(Sort.Direction.ASC, "price"));
		
		query.fields()
			.exclude("_id")
			.include("name", "address.street", "images.picture_url", "price");
		
		List<Document> results = template.find(query, Document.class, C_LISTING);
		if(results.isEmpty())
			return Optional.empty();
		return Optional.of(results);
	}
	/*
	 * db.listings.find
		(
			{"address.street":"Freshwater, NSW, Australia"},
			{_id:1, description:1, "address.street":1, "images.picture_url":1, price:1, amenities:1}
		)
	 */
	public Optional<Document> getDetails(String address)
	{
		Query query = Query.query
			(
				Criteria.where("address.street").is(address)
			);
		query.fields()
			.include("_id", "description", "address.street", "image.picture_url", "price", "amenities");
		List<Document> doc = template.find(query, Document.class, C_LISTING);
		if(doc.isEmpty())
			return Optional.empty();
		return Optional.of(doc.get(0));
	}
	//TODO: Task 3


	//TODO: Task 4
	

	//TODO: Task 5


}
