package Framework.Core;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.Keywords;
import org.jbehave.core.model.ExamplesTableFactory;
import org.jbehave.core.model.Story;

public class InjectorStoryParser  extends org.jbehave.core.parsers.RegexStoryParser {
    private Map<String,KeywordInjector> keywordInjectorMappings = null;

    public InjectorStoryParser() 
    {
        super();
    }

    public InjectorStoryParser(Keywords keywords) 
    {
    	super(keywords);
    }

    public InjectorStoryParser(ExamplesTableFactory tableFactory) 
    {
    	super(tableFactory);
    }

    public InjectorStoryParser(Keywords keywords, ExamplesTableFactory tableFactory) 
    {
    	super(keywords, tableFactory);
    }

    public InjectorStoryParser(Configuration configuration) 
    {
    	super(configuration);
    }
    
    public void registerKeywordInjector(String keyword, KeywordInjector object)
    {
    	if(keywordInjectorMappings == null)
    		keywordInjectorMappings = new HashMap<String,KeywordInjector>();
    	keywordInjectorMappings.put(keyword, object); 
    }
    
    public Story parseStory(String storyAsText) {
        return super.parseStory(storyAsText);
    }

    public Story parseStory(String storyAsText, String storyPath) 
    {
    	for(Entry<String, KeywordInjector> keywordInjector : keywordInjectorMappings.entrySet())
    	{
    		storyAsText = injectKeyword(storyAsText, keywordInjector.getKey(), keywordInjector.getValue());
    	}
    	return super.parseStory(storyAsText, storyPath);
    }
    
    private String injectKeyword(String storyAsText, String injectKeyword, KeywordInjector keywordInjector)
    {
    	String injectedStoryText = storyAsText;
    	Boolean firstRun = true;
    	for(String beforeScenario :storyAsText.split(injectKeyword))
    	{
    		// Skip first element
    		if(firstRun)
    		{
    			firstRun = false;
    			continue;
    		}
			String beforeScenarioFullPath = beforeScenario.substring(0, beforeScenario.indexOf("\n"));
			injectedStoryText = injectedStoryText.replace(injectKeyword +" "+ beforeScenarioFullPath.trim(), keywordInjector.injectWith(beforeScenarioFullPath.trim()));			
    	}
    	return injectedStoryText;
    }
}
