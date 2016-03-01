package Framework.Core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jbehave.core.Embeddable;
import org.jbehave.core.io.UnderscoredCamelCaseResolver;

import Framework.Core.Base.StoryBase;

public class StoryResolver extends UnderscoredCamelCaseResolver{
    public StoryResolver() {
        super("");
    }

    protected String resolveName(Class<? extends Embeddable> embeddableClass)
    {
    	String simpleName = embeddableClass.getSimpleName();
    	simpleName = simpleName.replace("Story", "");
        simpleName = simpleName.concat(".story");
		return (simpleName);
    }
    
    public String getStoryTitle(Class<? extends StoryBase> storyBaseClass)
    {
    	String simpleName = storyBaseClass.getResource(storyBaseClass.getSimpleName() + ".class").getPath();
    	simpleName = simpleName.replace("Story.class", ".story");
        BufferedReader br;
        String storyTitle = null;
        try {
			br = new BufferedReader(new FileReader(simpleName));
            storyTitle = br.readLine();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return storyTitle.replace("Description: ", "");
    }
}
