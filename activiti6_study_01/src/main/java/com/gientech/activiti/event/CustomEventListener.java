package com.gientech.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 流程event
 *
 */
public class CustomEventListener implements ActivitiEventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomEventListener.class);

	@Override
	public void onEvent(ActivitiEvent event) {

		ActivitiEventType eventType = event.getType();

		if (ActivitiEventType.CUSTOM.equals(eventType)) {
			LOGGER.info("监听到自定义监听,事件类型:{},流程实例ID：{}", eventType, event.getProcessInstanceId());
		}
	}

	@Override
	public boolean isFailOnException() {
		return false;
	}

}
