package Framework.Core;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
	private Logger _logger; 
	private String _logName;

	public Log(String logName) throws SecurityException, IOException
	{
		ConsoleHandler logHandler = new ConsoleHandler();
		_logName = logName;
		logHandler.setLevel(Level.parse(Helper.get("LogLevel")));
		_logger = Logger.getLogger(_logName+".log");
		_logger.setLevel(Level.parse(Helper.get("LogLevel")));

		while(_logger.getHandlers().length > 0)
		{
			_logger.removeHandler(_logger.getHandlers()[0]);
		}
			
			
		_logger.addHandler(logHandler);
		_logger.setUseParentHandlers(false);
		
		File filePath = new File(Helper.get("OutputFolder") + "/Logs");
		filePath.mkdirs();
		
 		FileHandler fh = new FileHandler(Helper.get("OutputFolder") + "/Logs/"+logName+".log");  
		_logger.addHandler(fh);

		LogFormatter formatter = new LogFormatter(logName);  
        fh.setFormatter(formatter); 
        logHandler.setFormatter(formatter);

	}

	public String getLogName()
	{
		return _logName;
	}
	
	public void fine(String logEntry)
	{
		_logger.fine(logEntry);
	}
	
	public void info(String logEntry)
	{
		_logger.info(logEntry);
	}
}

