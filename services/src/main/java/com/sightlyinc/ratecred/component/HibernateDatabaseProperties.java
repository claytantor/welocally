package com.sightlyinc.ratecred.component;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <property name="creationMode">
			<value>${rating-services.HibernateDatabaseProperties.creationMode}</value>
		</property>	
		<property name="dialect">
			<value>${rating-services.HibernateDatabaseProperties.dialect}</value>
		</property>	
		<property name="showSql">
			<value>${rating-services.HibernateDatabaseProperties.showSql}</value>
		</property>
		<property name="cacheProvider">
			<value>${rating-services.HibernateDatabaseProperties.cacheProvider}</value>
		</property>	
 * @author claygraham
 *
 */

@Component("hibernateDatabaseProperties")
public class HibernateDatabaseProperties extends
		com.noi.utility.hibernate.HibernateDatabaseProperties {

	@Override
	public String getCreationMode() {
		
		return super.getCreationMode();
	}

	@Override
	public String getDialect() {
		
		return super.getDialect();
	}

	@Override
	public String getDriverClass() {
		
		return super.getDriverClass();
	}

	@Override
	public String getPassword() {
		
		return super.getPassword();
	}

	@Override
	public Properties getProperties() {
		
		return super.getProperties();
	}

	@Override
	public String getUrl() {
		
		return super.getUrl();
	}

	@Override
	public String getUsername() {
		
		return super.getUsername();
	}

	@Override
	public void setAquireIncrement(int increment) {
		
		super.setAquireIncrement(increment);
	}

	@Override
	@Value("${rating-services.HibernateDatabaseProperties.cacheProvider}")
	public void setCacheProvider(String cacheProvider) {
		
		super.setCacheProvider(cacheProvider);
	}

	@Override
	public void setConnectionProperties(String connectionProperties) {
		
		super.setConnectionProperties(connectionProperties);
	}

	@Override
	public void setConnectionProvider(String connectionProvider) {
		
		super.setConnectionProvider(connectionProvider);
	}

	@Override
	@Value("${rating-services.HibernateDatabaseProperties.creationMode}")
	public void setCreationMode(String creationMode) {
		
		super.setCreationMode(creationMode);
	}

	@Override
	@Value("${rating-services.HibernateDatabaseProperties.dialect}")
	public void setDialect(String dialect) {
		
		super.setDialect(dialect);
	}

	@Override
	public void setDriverClass(String driverClass) {
		
		super.setDriverClass(driverClass);
	}

	@Override
	public void setIdleTestPeriod(int idleTestPeriod) {
		
		super.setIdleTestPeriod(idleTestPeriod);
	}

	@Override
	public void setJdbcBatchSize(int jdbcBatchSize) {
		
		super.setJdbcBatchSize(jdbcBatchSize);
	}

	@Override
	public void setMaxStatements(int maxStatements) {
		
		super.setMaxStatements(maxStatements);
	}

	@Override
	public void setPassword(String password) {
		
		super.setPassword(password);
	}

	@Override
	public void setPoolSizeMax(int max) {
		
		super.setPoolSizeMax(max);
	}

	@Override
	public void setPoolSizeMin(int min) {
		
		super.setPoolSizeMin(min);
	}

	@Override
	public void setQuerySubstitutions(String querySubstitutions) {
		
		super.setQuerySubstitutions(querySubstitutions);
	}

	@Override
	@Value("${rating-services.HibernateDatabaseProperties.showSql}")
	public void setShowSql(boolean showSql) {
		
		super.setShowSql(showSql);
	}

	@Override
	public void setTimeout(int timeout) {
		
		super.setTimeout(timeout);
	}

	@Override
	public void setUrl(String url) {
		
		super.setUrl(url);
	}

	@Override
	public void setUseQueryCache(boolean useQueryCache) {
		
		super.setUseQueryCache(useQueryCache);
	}

	@Override
	public void setUsername(String userName) {
		
		super.setUsername(userName);
	}

	@Override
	public void setUseSecondLevelCache(boolean useSecondLevelCache) {
		
		super.setUseSecondLevelCache(useSecondLevelCache);
	}

	@Override
	public void setValidate(boolean validate) {
		
		super.setValidate(validate);
	}
	

}
