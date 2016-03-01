package Framework.Core;

public class StoryReporterBuilder extends org.jbehave.core.reporters.StoryReporterBuilder {

private Log _logger;
private Integer _runId;
private Integer _environmentId;
private StoryReporter _storyReporter;
private Integer _interStepDelay;

public 	StoryReporterBuilder(Log logger, Integer runId, Integer environmentId, Integer interStepDelay)
{
	_logger = logger;
	_runId = runId;
	_environmentId = environmentId;
	_interStepDelay = interStepDelay;
	_storyReporter = new StoryReporter(_logger, _runId, _environmentId, _interStepDelay);
}

@Override
public StoryReporter build(String storyPath) {
    return _storyReporter;
}

@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
}