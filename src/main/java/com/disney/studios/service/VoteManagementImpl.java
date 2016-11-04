package com.disney.studios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.disney.studios.model.DogInfo;
import com.disney.studios.model.Vote;
import com.disney.studios.repository.DogInfoRepository;
import com.disney.studios.repository.VoteRepository;

@Service
public class VoteManagementImpl implements VoteManagement {
	
	//every client only gets to vote once
	private static final int LIMIT  =1;
	
	@Autowired
	private  DogInfoRepository dogInfoRepository;
	  
	@Autowired
	private  VoteRepository voteRepository;

	@Transactional
	public boolean voteWithClientIdAndDogInfoId(String clientId, int dogInfoId, boolean voteUp) {
	
		Vote vote = voteRepository.findByClientIdAndDogInfoId(clientId, dogInfoId);
		DogInfo dogInfo = dogInfoRepository.findOne(dogInfoId);
		if (vote == null || vote.getCount() < LIMIT){
			if (voteUp)
				dogInfoRepository.voteup(dogInfoId);
			else
				dogInfoRepository.votedown(dogInfoId);
			vote = new Vote();
			vote.setClientId(clientId);
			vote.setDogInfo(dogInfo);
			vote.setCount(1);
			voteRepository.save(vote);
			return true;
		}
		return false;
		
	}
	  
	  
	  

}
