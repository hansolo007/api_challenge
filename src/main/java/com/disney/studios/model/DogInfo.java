package com.disney.studios.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.disney.studios.jsonview.DogInfoGroupByView;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="doginfo")
public class DogInfo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonView(DogInfoGroupByView.ImageURLView.class)
	private int id;
	
	@Column(nullable=false)
	@JsonView(DogInfoGroupByView.ImageURLView.class)
	private String imageUrl;
	
	@Column(nullable=false) 
	private long voteup;
	
	@Column(nullable=false) 
	private long votedown;
	
	@Column(nullable=false) 
	private String breed;
	
	
	
	public DogInfo(String breed, String imageUrl){
		this.breed = breed;
		this.imageUrl = imageUrl;
		this.voteup = 0 ;
		this.votedown =0;
	}
	
	public DogInfo(){}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getBreed() {
		return breed;
	}

	public long getVoteup() {
		return voteup;
	}

	public void setVoteup(long voteup) {
		this.voteup = voteup;
	}

	public long getVotedown() {
		return votedown;
	}

	public void setVotedown(long votedown) {
		this.votedown = votedown;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}
	

	

}
