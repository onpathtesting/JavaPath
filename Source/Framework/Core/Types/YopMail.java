package Framework.Core.Types;

public class YopMail {
	public String newEmailId()
	{
		String yopMail;
		yopMail =  "sos-";
		yopMail += String.valueOf(System.currentTimeMillis());
		yopMail +=  "@yopmail.com";
		
		return yopMail;
	}
}
