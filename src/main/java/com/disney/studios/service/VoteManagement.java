package com.disney.studios.service;



public interface VoteManagement {

		public boolean voteWithClientIdAndDogInfoId(String clientId, int dogInfoId, boolean voteUp);
}
