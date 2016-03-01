package Framework.Core.Types;


import Framework.Core.Helper;
import Framework.Core.Facebook.FacebookTestUserAccount;
import Framework.Core.Facebook.FacebookTestUserStore;
import Framework.Core.Facebook.HttpClientFacebookTestUserStore;

public class Facebook {
	
	FacebookTestUserStore _facebookStore;
	FacebookTestUserAccount _facebookAccount;
	
	public Facebook()
	{
		_facebookStore = new HttpClientFacebookTestUserStore(Helper.get("FacebookAppId"), Helper.get("FacebookAppSecret"));
	}
	
	public void createTestUser()
	{
		_facebookAccount = _facebookStore.createTestUser(true, "email,read_stream");
	}
	
	public String getEmail()
	{
		return _facebookAccount.email();
	}

	public String getUserId()
	{
		return _facebookAccount.id();
	}
	
	public String getPassword()
	{
		return _facebookAccount.getPassword();
	}
}
