package Framework.Core.Random;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

public class RandomUser {
	public String nationality;
	public String seed;
	public String Version;
	public List<Results> results = new ArrayList<Results>();
	
	private static RandomUser Instance;
	
	static
	{
		try {
			Instance = refreshUser();
		} catch (IOException e) {
		}
	}
	
	public static RandomUser randomUser()
	{
		return Instance;
	}
	
	public static RandomUser refreshUser() throws IOException
	{
		String requestURL = "http://api.randomuser.me/?nationality=FR";
		URL wikiRequest = new URL(requestURL);	
		Scanner scanner = new Scanner(wikiRequest.openStream());
		String response = scanner.useDelimiter("\\Z").next();
		scanner.close();
		
		Gson gson = new Gson();

		RandomUser randomUser = gson.fromJson(response, RandomUser.class);
		
		return randomUser;
	}
}

