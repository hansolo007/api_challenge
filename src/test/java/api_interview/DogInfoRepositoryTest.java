package api_interview;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.disney.studios.Application;
import com.disney.studios.model.DogInfo;
import com.disney.studios.repository.DogInfoRepository;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class) 
@IntegrationTest("server.port:8000") 
public class DogInfoRepositoryTest {
	
    @Autowired
    private DogInfoRepository dogInfoRepository;
  
    @Value("classpath:data/labrador.txt")
    private Resource labradors;

    @Value("classpath:data/pug.txt")
    private Resource pugs;

    @Value("classpath:data/retriever.txt")
    private Resource retrievers;

    @Value("classpath:data/yorkie.txt")
    private Resource yorkies;
   
    
    
    private int numberOfPugs =0;
    private int numberOfRetrievers =0;
    private int numberOfYorkies =0;
    private int numberofLabs =0;
    
    @Before
    public void setUp() throws IOException {
    	
    	/*
    	 *  get total number for each breed in datasource
    	 */
    	numberofLabs =loadBreed("Labrador", labradors);
    	numberOfPugs = loadBreed("Pug", pugs);
    	numberOfRetrievers =loadBreed("Retriever", retrievers);
    	numberOfYorkies= loadBreed("Yorkie", yorkies);
    	
    
    }

    @Test
    public void checkCountsInDatabase(){
    	
    	Iterable<DogInfo> breed = dogInfoRepository.findByBreedIgnoreCase("pug");
    	assertTrue(numberOfPugs == getIteratorCount(breed.iterator()));

      	breed= dogInfoRepository.findByBreedIgnoreCase("Labrador");
    	assertTrue(numberofLabs == getIteratorCount(breed.iterator()));
    	
      	breed = dogInfoRepository.findByBreedIgnoreCase("Retriever");
    	assertTrue(numberOfRetrievers == getIteratorCount(breed.iterator()));
    	
      	breed = dogInfoRepository.findByBreedIgnoreCase("Yorkie");
    	assertTrue(numberOfYorkies == getIteratorCount(breed.iterator()));
    	
    	System.out.println("Data Looks Good!");
    	
    }
    
    
    
 
    
    
    private int getIteratorCount(Iterator it){
    	int sum = 0;
    	while(it.hasNext()){
    		it.next();
    		sum++;
    	}
    	return sum;
    }
    
    
    private int loadBreed(String breed, Resource source) throws IOException {
    	
    	int count = 0;
        try ( BufferedReader br = new BufferedReader(new InputStreamReader(source.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
            	if (!line.isEmpty()){
            		count++;
            	}
               
            }
        }
        return count;
    }
    

}
