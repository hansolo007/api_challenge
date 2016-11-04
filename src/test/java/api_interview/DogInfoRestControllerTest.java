package api_interview;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.disney.studios.Application;
import com.disney.studios.model.DogInfo;
import com.disney.studios.repository.DogInfoRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=8888"})
 public class DogInfoRestControllerTest {
	
	//Required to Generate JSON content from Java objects
	  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	  //Required to delete the data added for tests.
	  //Directly invoke the APIs interacting with the DB
	  @Autowired
	  private DogInfoRepository dogInfoRepository;

	  //Test RestTemplate to invoke the APIs.
	  private RestTemplate restTemplate = new TestRestTemplate();
	  
	  @Test
	  public void testGetAllInfoApi() throws JsonParseException, JsonMappingException, IOException{
		  
		  ObjectMapper mapper = new ObjectMapper();
		  String apiResponse = restTemplate.getForObject("http://localhost:8888/doginfo", String.class);
		  System.out.println(apiResponse);
		  DogInfo[] allDogInfo = mapper.readValue(apiResponse, DogInfo[].class);
		  for(DogInfo dogInfo : allDogInfo ){
			  assertTrue(dogInfo.getId() > 0 );
			  assertTrue(dogInfo.getBreed() != null );
			  assertTrue(dogInfo.getImageUrl() != null);
		  }
			 
		 
	  }
	  
	  @Test
	  public void testGetDogInfoById() throws JsonParseException, JsonMappingException, IOException{
		  
		  DogInfo dogInfo = restTemplate.getForObject("http://localhost:8888/doginfo/23", DogInfo.class);
		  System.out.println(dogInfo);
		  assertTrue(dogInfo.getId() == 23);
		  assertTrue(dogInfo.getBreed() != null );
		  assertTrue(dogInfo.getImageUrl() != null);	
		  assertTrue(dogInfo.getVotedown() == 0);
		  assertTrue(dogInfo.getVoteup() == 0);
		 
	  }
	  
	  
	  
	  
	  
	  @Test
	  public void testGroupByBreed() throws JsonProcessingException, IOException{
		  
		  ObjectMapper mapper = new ObjectMapper();
		  String apiResponse = restTemplate.getForObject("http://localhost:8888/group/doginfo", String.class);
		  //System.out.println(apiResponse);
		  JsonNode jsonObj = mapper.readTree(apiResponse);
		  Iterator<String> it = jsonObj.fieldNames();
		  
		  Set<String> allBreadNames = new HashSet<String>();
		  
		  while(it.hasNext()){
			  String breedName = it.next();
			  //make sure name is one of the breed
			  assertTrue (breedName.equals("Pug") || breedName.equals("Labrador") || breedName.equals("Retriever") || breedName.equals("Yorkie"));
			  //make sure the group by structure
			  assertTrue(allBreadNames.add(breedName));
			  
			  JsonNode dogInfoUrl = jsonObj.get(breedName);
			  assertTrue(dogInfoUrl.isArray());
			  for (JsonNode urlCheck : dogInfoUrl){
				  assertTrue(urlCheck.get("imageUrl") != null);
				  assertTrue(urlCheck.get("id") != null);
			  }
			  
		  }
		  
		  
	  }
	  
	  @Test
	  public void testGroupByBreedName() throws JsonProcessingException, IOException{
		  
		  ObjectMapper mapper = new ObjectMapper();
		  String apiResponse = restTemplate.getForObject("http://localhost:8888/group/doginfo/pug", String.class);
		  //System.out.println(apiResponse);
		  JsonNode jsonObj = mapper.readTree(apiResponse);
		  Iterator<String> it = jsonObj.fieldNames();
		  
		  Set<String> allBreadNames = new HashSet<String>();
		  
		  while(it.hasNext()){
			  String breedName = it.next();
			  //make sure name is one of the breed
			  assertTrue (breedName.equals("Pug"));
			  //make sure the group by structure
			  assertTrue(allBreadNames.add(breedName));
			  
			  JsonNode dogInfoUrl = jsonObj.get(breedName);
			  assertTrue(dogInfoUrl.isArray());
			  for (JsonNode urlCheck : dogInfoUrl){
				  assertTrue(urlCheck.get("imageUrl") != null);
				  assertTrue(urlCheck.get("id") != null);
			  }
			  
		  }
		  
		  
	  }
	  
	  @Test
	  public void testVote() throws JsonParseException, JsonMappingException, IOException{
		  
		  String clientIdA = "a100";
		  String clientIdB = "b100";
		  String clientIdC = "c100";
		  
		  int dogInfoId = 4;
		  ObjectMapper mapper = new ObjectMapper();
		  
		  //check inital count , and they all should 0
		  DogInfo dogInfo = restTemplate.getForObject("http://localhost:8888/doginfo/"+dogInfoId, DogInfo.class);  
		  assertTrue(dogInfo.getVotedown() == 0);
		  assertTrue(dogInfo.getVoteup() ==0);
		  
		  // vote with clientA
		  String vote = restTemplate.getForObject("http://localhost:8888/doginfo/voteup/"+dogInfoId+"/clientId/"+clientIdA, String.class);  
		  assertTrue (vote.indexOf("Successul") !=-1);
		  dogInfo = restTemplate.getForObject("http://localhost:8888/doginfo/"+dogInfoId, DogInfo.class);  
		  assertTrue(dogInfo.getVotedown() == 0);
		  assertTrue(dogInfo.getVoteup() == 1);
		  
		  
		  //vote with ClientA again, and it shuldn't work
		  vote = restTemplate.getForObject("http://localhost:8888/doginfo/votedown/"+dogInfoId+"/clientId/"+clientIdA, String.class);  
		  assertTrue (vote.indexOf("Fail") !=-1);
		  dogInfo = restTemplate.getForObject("http://localhost:8888/doginfo/"+dogInfoId, DogInfo.class);  
		  assertTrue(dogInfo.getVotedown() == 0);
		  assertTrue(dogInfo.getVoteup() == 1);
		  
		  vote = restTemplate.getForObject("http://localhost:8888/doginfo/voteup/"+dogInfoId+"/clientId/"+clientIdA, String.class);  
		  assertTrue (vote.indexOf("Fail") !=-1);
		  dogInfo = restTemplate.getForObject("http://localhost:8888/doginfo/"+dogInfoId, DogInfo.class);  
		  assertTrue(dogInfo.getVotedown() == 0);
		  assertTrue(dogInfo.getVoteup() == 1);
		  
		  
		  // vote with clientB 
		  vote = restTemplate.getForObject("http://localhost:8888/doginfo/votedown/"+dogInfoId+"/clientId/"+clientIdB, String.class);  
		  assertTrue (vote.indexOf("Successul") !=-1);
		  dogInfo = restTemplate.getForObject("http://localhost:8888/doginfo/"+dogInfoId, DogInfo.class);  
		  assertTrue(dogInfo.getVotedown() == 1);
		  assertTrue(dogInfo.getVoteup() == 1);
		  
		  // vote with clientC
		  
		  vote = restTemplate.getForObject("http://localhost:8888/doginfo/voteup/"+dogInfoId+"/clientId/"+clientIdC, String.class);  
		  assertTrue (vote.indexOf("Successul") !=-1);
		  dogInfo = restTemplate.getForObject("http://localhost:8888/doginfo/"+dogInfoId, DogInfo.class);  
		  assertTrue(dogInfo.getVotedown() == 1);
		  assertTrue(dogInfo.getVoteup() == 2); 
		  
		  
		  
	  }
	  
	  
	  

}
