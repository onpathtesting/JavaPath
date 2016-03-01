package Framework.Core;

import java.lang.reflect.Type;

import Framework.Core.Types.Facebook;
import Framework.Core.Types.YopMail;

public class ParameterConverter implements org.jbehave.core.steps.ParameterConverters.ParameterConverter {

	public boolean accept(Type type) {
		if(type.equals(YopMail.class))
			return true;
		if(type.equals(Facebook.class))
			return true;
		return false;
	}

	public Object convertValue(String value, Type type) {
		if(type.equals(YopMail.class))
		{
			return new YopMail();
		}
		if(type.equals(Facebook.class))
		{
			return new Facebook();
		}
		return null;
	}

}
