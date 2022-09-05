package com.gientech.activiti.context;

import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;



public class ConfigContextTest {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigContextTest.class);
	
	@Rule
	@Autowired
	public ActivitiRule activitiRule;

	
	@Test
	public void testConfigContext() {
		
	}
}
