package Framework.Core;

import org.jbehave.core.model.ExamplesTable;

public class StoryTable extends org.jbehave.core.model.ExamplesTableFactory{

	  public ExamplesTable createExamplesTable(String input) {
		  input = input.replace(".table", "-" + Helper.get("Language") + ".table");
		  return super.createExamplesTable(input);
	    }
}
