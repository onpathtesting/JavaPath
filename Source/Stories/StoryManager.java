package Stories;

import java.util.ArrayList;
import java.util.List;

import Framework.Core.Base.StoryBase;

public class StoryManager {
	private static List<Class<? extends StoryBase>> _stories = new ArrayList<Class<? extends StoryBase>>(); 

	static
	{
//		_stories.add(VideographyDemandStory.class);
	}
	
	public static List<Class<? extends StoryBase>> getStories()
	{
		return _stories;
	}
}
