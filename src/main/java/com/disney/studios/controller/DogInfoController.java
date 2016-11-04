package com.disney.studios.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.disney.studios.error.NotFoundException;
import com.disney.studios.jsonview.DogInfoGroupByView;
import com.disney.studios.model.DogInfo;
import com.disney.studios.pojo.Response;
import com.disney.studios.repository.DogInfoRepository;
import com.disney.studios.service.VoteManagement;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class DogInfoController {

	@Autowired
	private DogInfoRepository dogInfoRepository;
	
	@Autowired
	private VoteManagement voteManagement;
	
	/*
	 * Get All Dogs with their Details
	 */
	
	@RequestMapping("/doginfo")
	public @ResponseBody Iterable<DogInfo> getAllDogInfo(){
		return dogInfoRepository.findAll();
		
	}
	
	/*
	 * Return Details of the Dog with Id
	 */
	
	@RequestMapping("/doginfo/{id}")
	public @ResponseBody DogInfo getDogInfo(@PathVariable Integer id){
		return dogInfoRepository.findOne(id);	
	}
	
	@RequestMapping("/doginfo/voteup/{dogInfoId}/clientId/{clientId}")
	public @ResponseBody Response voteup(@PathVariable Integer dogInfoId, @PathVariable String clientId){
		
		boolean result = voteManagement.voteWithClientIdAndDogInfoId(clientId, dogInfoId, true);
	
		if (result)
			return new Response (Response.SUCCESSFUL,""); 
		
	    return new Response (Response.FAIL,"unable to vote with id: "+ dogInfoId); 
	}
	
	@RequestMapping("/doginfo/votedown/{dogInfoId}/clientId/{clientId}")
	public @ResponseBody Response votedown(@PathVariable Integer dogInfoId, @PathVariable String clientId){
		
		boolean result = voteManagement.voteWithClientIdAndDogInfoId(clientId, dogInfoId, false);
		
		if (result)
			return new Response (Response.SUCCESSFUL,""); 
		
	    return new Response (Response.FAIL,"unable to vote with id: "+ dogInfoId); 
	}
		
	/*
	 * Return images URL in groupBy
	 * 
	 * sample response: {"pug": ["http://url1","http://url2",...]}
	 * 
	 */
	
	@RequestMapping("/group/doginfo")
	@JsonView(DogInfoGroupByView.ImageURLView.class)
	public @ResponseBody Map<String, List<DogInfo>> getGroupBy() throws NotFoundException{
		
		Iterable<DogInfo> allInfo = dogInfoRepository.findAll();
		Map<String, List<DogInfo>> breedGroup = new HashMap<String, List<DogInfo>>();
		if (allInfo != null){
			
			
			
			allInfo.forEach(dogInfo->{	
				String breedName = dogInfo.getBreed();
				if (breedGroup.get(breedName) == null)
					breedGroup.put(breedName, new ArrayList<DogInfo>());
				breedGroup.get(breedName).add(dogInfo);
			});
				
			return breedGroup;
				
		}
		// throw not found exception if nothing is found
		if (breedGroup.isEmpty())
			throw new NotFoundException("No DogInfo data found!");
		return breedGroup;
	}
	
	@RequestMapping("/group/doginfo/{breed}")
	@JsonView(DogInfoGroupByView.ImageURLView.class)
	public @ResponseBody Map<String,List<DogInfo>> getGroupByBreedName(@PathVariable String breed) throws NotFoundException{
		
		Iterable<DogInfo> allInfo = dogInfoRepository.findByBreedIgnoreCase(breed);
		Map<String, List<DogInfo>> breedGroup = new HashMap<String, List<DogInfo>>();
		if (allInfo != null){
			
			
			allInfo.forEach(dogInfo->{	
				String breedName = dogInfo.getBreed();
				if (breedGroup.get(breedName) == null)
					breedGroup.put(breedName, new ArrayList<DogInfo>());
				breedGroup.get(breedName).add(dogInfo);
			});
				
	
				
		}
		// throw not found exception if nothing is found
		if (breedGroup.isEmpty())
			throw new NotFoundException("No DogInfo data found!");
		return breedGroup;
		
	}
	


		
	
	
	
	
}
