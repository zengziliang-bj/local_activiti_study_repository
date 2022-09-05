package com.gientech.activiti.helloword;

import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gientech.activiti.event.CustomEventListener;

public class ConfigEventListenerTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigHistoryLevelTest.class);
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti.eventlistener.cfg.xml");
	
	@Test
	@Deployment(resources = "com/gientech/activiti/bpmn/PracticeTestProcess.bpmn")
	public void testConfigEventListener() {
		
		// 启动流程
		ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("practiceTestProcess");
		String processInstanceId = processInstance.getProcessInstanceId();
		String processDefinitionKey = processInstance.getProcessDefinitionKey();
		String processDefinitionId = processInstance.getProcessDefinitionId();
		
		LOGGER.info("请假审批流程已启动，流程实例ID:{},自定义key:{},自定义ID:{}",processInstanceId,processDefinitionKey,processDefinitionId);
		
		Task task = activitiRule.getTaskService().createTaskQuery().processInstanceId(processInstanceId).singleResult();
		
		activitiRule.getTaskService().complete(task.getId());
		
		LOGGER.info("任务:{} 已处理提交", task.getId());
		
		// 第三种配置事件监听的方式
		activitiRule.getRuntimeService().addEventListener(new CustomEventListener());
		
		// 发布自定义监听
		activitiRule.getRuntimeService().dispatchEvent(new ActivitiEventImpl(ActivitiEventType.CUSTOM));
		
	}

}
