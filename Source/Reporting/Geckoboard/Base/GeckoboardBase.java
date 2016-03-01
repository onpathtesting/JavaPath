package Reporting.Geckoboard.Base;

import java.io.IOException;

import Framework.Core.Helper;
import Framework.Database.Base.ViewBase;
import nl.pvanassen.geckoboard.api.Push;
import nl.pvanassen.geckoboard.api.SoSGeckoboard;

public abstract class GeckoboardBase  extends ViewBase {
	private SoSGeckoboard geckoBoard;
	public GeckoboardBase(String viewName) {
		super(viewName);
		geckoBoard = new SoSGeckoboard(Helper.get("GeckoAPIKey"));
	}
	
	public void Push(Push pushWidget) throws IOException
	{
		geckoBoard.push(pushWidget);
	}
}
