DROP VIEW BrowserTrend;
CREATE VIEW BrowserTrend AS 
SELECT RunId, strftime('%d-%m-%Y', RunOn) as ExecutedOn, Browser, ROUND(100*SUM(Success)/SUM(Total),0) as Value 
FROM 
( 
SELECT A.RunId, A.RunOn, C.Browser, B.StoryId,B.RunId,1 as Success,0 as Total 
FROM Run A 
INNER JOIN RunEnvironmentStoryScenarioMetaStep B ON A.RunId = B.RunId 
INNER JOIN Environment C ON B.EnvironmentId = C.EnvironmentId 
GROUP BY 1,2,3,4 
HAVING MAX(B.ResultId) = 2 
UNION 
SELECT A.RunId, A.RunOn, C.Browser, B.StoryId,B.RunId,0,1 
FROM Run A 
INNER JOIN RunEnvironmentStoryScenarioMetaStep B ON A.RunId = B.RunId 
INNER JOIN Environment C ON B.EnvironmentId = C.EnvironmentId 
GROUP BY 1,2,3,4 
) 
GROUP BY RunOn,Browser;