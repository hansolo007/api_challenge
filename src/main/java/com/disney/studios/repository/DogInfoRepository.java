package com.disney.studios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.disney.studios.model.DogInfo;

public interface DogInfoRepository extends CrudRepository<DogInfo, Integer>{
	
	public List<DogInfo> findByBreedIgnoreCase(String breed);
	
	
	//make sure the update value get persists every time.
	@Modifying(clearAutomatically = true)
	@Transactional 
	@Query("Update DogInfo dog SET dog.voteup = dog.voteup + 1 WHERE dog.id=:id")
	public int voteup(@Param("id") Integer id);
	
	@Modifying(clearAutomatically = true)
	@Transactional 
	@Query("Update DogInfo dog SET dog.votedown = dog.votedown + 1 WHERE dog.id=:id")
	public int votedown(@Param("id") Integer id);
	

}
