package com.gientech.activiti.helloword;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivitiDemoMain {
	
	private static final Logger logger =   LoggerFactory.getLogger(ActivitiDemoMain.class);
	
	public static void main(String[] args) throws ParseException {
		
		logger.info("启动程序---!");
		
		//1.创建流程引擎
		ProcessEngine processEngine = getProcessEngine();
		
		//2.部署流程
		String deploymentProcessId = deploymentProcess(processEngine);
		
		// 3.启动流程
		ProcessInstance processInstance = getProcessInstance(processEngine, deploymentProcessId);
		
		// 4.处理任务
		completeTask(processEngine, processInstance);
		
		
		logger.info("结束程序---!");
	}

	private static void completeTask(ProcessEngine processEngine, ProcessInstance processInstance)
			throws ParseException {
		
		Scanner scanner = new Scanner(System.in);
		while(processInstance!= null && !processInstance.isEnded()) {
			TaskService taskService = processEngine.getTaskService();
			List<Task> list = taskService.createTaskQuery().list();
			logger.info("待处理的任务数量:{}", list.size());
			
			for (Task task : list) {
				logger.info("待处理的任务:{}",task.getName());
				
				FormService formService = processEngine.getFormService();
				TaskFormData taskFormData = formService.getTaskFormData(task.getId());
				List<FormProperty> formProperties = taskFormData.getFormProperties();
				Map<String,Object> variblesMap = new HashMap<>();
				
				getVariblesMap(scanner, formProperties, variblesMap);
				taskService.complete(task.getId(), variblesMap);
				
				processInstance = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
			}
		}
	}

	private static void getVariblesMap(Scanner scanner, List<FormProperty> formProperties,
			Map<String, Object> variblesMap) throws ParseException {
		
		for (FormProperty property : formProperties) {
			String nextLine = null;
			if(StringFormType.class.isInstance(property.getType())) {
				logger.info("请输入:{} ?", property.getName());
				nextLine = scanner.nextLine();
				variblesMap.put(property.getId(), nextLine);
			}else if(DateFormType.class.isInstance(property.getType())) {
				logger.info("请输入:{} ? 格式：[yyyy-MM-dd]", property.getName());
				nextLine = scanner.nextLine();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date parse = simpleDateFormat.parse(nextLine);
				variblesMap.put(property.getId(), parse);
			} else {
				logger.info("类型[{}]暂时不支持", property.getType());
			}
			logger.info("您输入的内容是:{}" , nextLine);
		}
	}

	private static ProcessInstance getProcessInstance(ProcessEngine processEngine, String deploymentProcessId) {
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(deploymentProcessId);
		
		logger.info("启动流程:{},流程ID：{}", processInstance.getProcessDefinitionKey(), processInstance.getProcessInstanceId());
		return processInstance;
	}

	private static String deploymentProcess(ProcessEngine processEngine) {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder createDeployment = repositoryService.createDeployment();
		createDeployment.addClasspathResource("com/gientech/activiti/bpmn/SecondaryApprovalProcess.bpmn");
		Deployment deploy = createDeployment.deploy();
		String id = deploy.getId();
		
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(id).singleResult();
		
		logger.info("流程文件:{},流程ID：{}",processDefinition.getName(),processDefinition.getId());
		
		return processDefinition.getId();
	}

	private static ProcessEngine getProcessEngine() {
		ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		ProcessEngine processEngine = cfg.buildProcessEngine();
		
		logger.info("创建流程引擎：名称：{},版本：{}",processEngine.getName(),processEngine.VERSION);
		return processEngine;
	}

}
