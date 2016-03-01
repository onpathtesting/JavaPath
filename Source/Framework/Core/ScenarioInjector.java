package Framework.Core;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScenarioInjector implements KeywordInjector{

	@Override
	public String injectWith(String keywordValue) {
		String scenarioPath = keywordValue.split("#")[0].trim(); 
		String scenarioId = keywordValue.split("#")[1];
		String injectText = null;
		try {
			injectText = readScenarioStepsFromId(scenarioPath, scenarioId);
		} catch (IOException e) {
		}
		
		return injectText;
	}
	
    private String readScenarioStepsFromId(String storyPath, String scenarioId) throws IOException
    {
    	String scenarioText = ""; 
    	Path path = Paths.get(storyPath.trim());
    	try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8))
    	{
    		 String line;
    		 Boolean scenarioFound = false;
    		 while ((line = reader.readLine()) != null) 
    		 {
    			 if(line.equals("@id " + scenarioId.trim()))
    			 {
    				 scenarioFound = true;
    				 continue;
    			 }
    			 if(scenarioFound)
    			 {
    				 if(line.startsWith("Given") || line.startsWith("When") || line.startsWith("Then") || line.startsWith("And"))
    				 {
    					 scenarioText += (scenarioText.isEmpty()) ?  line : "\n" + line;
    				 }
    				 else
    					 break;
    			 }
    		 }      
    	}    	
    	return scenarioText;
    }
    
}
