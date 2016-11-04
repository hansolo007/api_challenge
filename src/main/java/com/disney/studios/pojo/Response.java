package com.disney.studios.pojo;



public class Response {
	public static final String SUCCESSFUL = "Successul";
	public static final String FAIL = "Fail";
	public static final String NOTFOUND = "Not_Found";
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private String status;
	private String message;
	
	public Response (String status, String message){
		this.status = status;
		this.message = message;
	}


}

