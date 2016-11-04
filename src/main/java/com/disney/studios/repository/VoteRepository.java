package com.disney.studios.repository;


import org.springframework.data.repository.CrudRepository;

import com.disney.studios.model.Vote;

public interface VoteRepository extends CrudRepository<Vote, Long> {

	public Vote findByClientIdAndDogInfoId(String clientId, int id);
}
