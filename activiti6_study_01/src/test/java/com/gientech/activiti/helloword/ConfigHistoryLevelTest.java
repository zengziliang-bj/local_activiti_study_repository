package com.gientech.activiti.helloword;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConfigHistoryLevelTest {

	private static final Logger logger = LoggerFactory.getLogger(ConfigHistoryLevelTest.class);
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg.xml");
	
	@Test
	@Deployment(resources = "com/gientech/activiti/bpmn/TestProcess.bpmn")
	public void testHisroty() {
		
		
		
		// 启动流程
		Map<String,Object> verableMap = new HashMap<>();
		verableMap.put("key1", "启动变量key1");
		verableMap.put("key2", "启动变量key2");
		verableMap.put("key3", "启动变量key3");
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("TestProcess", verableMap);
//		List<Task> list = activitiRule.getTaskService().createTaskQuery().list();
		
		logger.info("启动的流程:{}, 流程ID:{}", processInstance.getName(), processInstance.getId());
		
		// 查询任务
		List<Task> list = activitiRule.getTaskService().createTaskQuery().list();
		for (Task task : list) {
			logger.info("任务ID：{}, 任务名称:{}", task.getId(), task.getName());
			
			// 提交表单
			Map<String, String> properties = new HashMap<>();
			properties.put("key1", "表单提交key1");
			properties.put("key2", "表单提交key2");
			properties.put("key3", "表单提交key3");
			FormService formService = activitiRule.getFormService();
			formService.submitTaskFormData(task.getId(), properties);
		}
		
		// 查询历史任务
		List<HistoricTaskInstance> historicTaskInstances = activitiRule.getHistoryService().createHistoricTaskInstanceQuery().listPage(0, 100);
		logger.info("历史任务的数量:{}",historicTaskInstances.size());
		for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
			logger.info("历史任务：historicTaskInstance={}", historicTaskInstance);
		}
		
		
		// 查询历史活动
		List<HistoricActivityInstance> historicActivityInstances = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().listPage(0, 100);
		logger.info("历史活动数量:{}", historicActivityInstances.size());
		for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
			logger.info("历史活动:historicActivityInstance = {}", historicActivityInstance);
		}
		// 查询历史变量
		List<HistoricVariableInstance> historicVariableInstances = activitiRule.getHistoryService().createHistoricVariableInstanceQuery().listPage(0, 100);
		logger.info("历史变量数量:{}",historicVariableInstances.size());
		for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
			logger.info("历史变量:historicVariableInstance ={}", historicVariableInstance);
		}
		
		// 查询流程实例
		List<HistoricProcessInstance> historicProcessInstances = activitiRule.getHistoryService().createHistoricProcessInstanceQuery().listPage(0, 100);
		logger.info("历史流程实例数量:{}", historicProcessInstances.size());
		for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
			logger.info("历史流程实例:historicProcessInstances = {}", historicProcessInstance);
		}
		
		// 查询历史详情
		List<HistoricDetail> historicDetails = activitiRule.getHistoryService().createHistoricDetailQuery().listPage(0, 100);
		logger.info("历史详情数量:{}", historicDetails.size());
		for (HistoricDetail historicDetail : historicDetails) {
			logger.info("historicDetail = {}", historicDetail);
		}
		
	}
	
}
