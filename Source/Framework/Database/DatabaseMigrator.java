package Framework.Database;

import java.sql.Connection;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.callback.FlywayCallback;

import Framework.Core.Helper;

public class DatabaseMigrator implements FlywayCallback 
{

	 Flyway flyway;
	 
	 public DatabaseMigrator()
	 {
		 flyway = new Flyway();
		 flyway.setDataSource("jdbc:sqlite:/" + Helper.get("OutputFolder") + "/QaAutomation.sqlite", null, null);
	     flyway.setLocations("filesystem:" + Helper.get("ConfigFolder"));
	     flyway.setBaselineOnMigrate(true);
	     flyway.setCallbacks(this);

	 }
	
	 public void doMigration()
	 {
		 flyway.migrate();
	 }
	 
	public void afterBaseline(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

	public void afterClean(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

	public void afterEachMigrate(Connection arg0, MigrationInfo arg1) {
		// TODO Auto-generated method stub
		
	}

	public void afterInfo(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

	public void afterInit(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

	public void afterMigrate(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

	public void afterRepair(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

	public void afterValidate(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

	public void beforeBaseline(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

	public void beforeClean(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

	public void beforeEachMigrate(Connection arg0, MigrationInfo arg1) {
		// TODO Auto-generated method stub
		
	}

	public void beforeInfo(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

	public void beforeInit(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

	public void beforeMigrate(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

	public void beforeRepair(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

	public void beforeValidate(Connection arg0) {
		// TODO Auto-generated method stub
		
	}

}
