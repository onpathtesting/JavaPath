DROP VIEW FailureReport;
CREATE VIEW FailureReport AS SELECT A.Description as Story, B.Description as Scenario, C.Description as Step, D.Description as Meta, E.Browser, E.BrowserVersion, E.OperatingSystem, E.OperatingSystemVersion,  F.Description as Result
FROM  RunEnvironmentStoryScenarioMetaStep X 
INNER JOIN Story A ON A.StoryId = X.StoryID
INNER JOIN Scenario B ON B.ScenarioId = X.ScenarioId
INNER JOIN Step C ON C.StepId = X.StepId
INNER JOIN Meta D ON D.MetaId = X.MetaId
INNER JOIN Environment E ON E.EnvironmentId = X.EnvironmentId
INNER JOIN Result F ON F.ResultId = X.ResultId
WHERE X.RunId = (SELECT MAX(RunId) FROM Run) AND X.ResultId <> 2
GROUP BY 1,2,3,4,5,6,7,8
HAVING X.ResultId = MAX(X.ResultId)
ORDER BY X.EnvironmentId, X.MetaId, X.StoryId, X.ScenarioId, X.StepId;