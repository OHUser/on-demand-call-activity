package org.camunda.bpm.extension.bpmn.callactivity.ondemand.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.jobexecutor.JobHandler;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;

/**
 * A {@link ProcessEnginePlugin} that replaces the {@link ActivityBehavior} of
 * BPMN Call Activities with {@link OnDemandCallActivityBehavior}.
 *
 * @author Falko Menge (Camunda)
 */
public class OnDemandCallActivityProcessEnginePlugin implements ProcessEnginePlugin {

  @Override
  public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
    List<BpmnParseListener> preParseListeners = processEngineConfiguration.getCustomPreBPMNParseListeners();
    if (preParseListeners == null) {
      preParseListeners = new ArrayList<BpmnParseListener>();
      processEngineConfiguration.setCustomPreBPMNParseListeners(preParseListeners);
    }
    preParseListeners.add(new OnDemandCallActivityParseListener());
  }

  @Override
  public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
    Map<String, JobHandler> jobHandlers = processEngineConfiguration.getJobHandlers();
    if (jobHandlers == null) {
      jobHandlers = new HashMap<String, JobHandler>();
      processEngineConfiguration.setJobHandlers(jobHandlers);
    }
    jobHandlers.put(ScopelessAsyncContinuationJobHandler.TYPE, new ScopelessAsyncContinuationJobHandler());
  }

  @Override
  public void postProcessEngineBuild(ProcessEngine processEngine) {

  }

}
