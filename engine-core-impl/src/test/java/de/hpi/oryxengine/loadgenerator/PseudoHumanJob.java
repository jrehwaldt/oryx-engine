package de.hpi.oryxengine.loadgenerator;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.resource.AutomatedParticipant;
import de.hpi.oryxengine.resource.worklist.WorklistItem;

/**
 * The PseudoHumanJob, which is a quartz scheduler Job and gets called whenever it is time that one worker finished its
 * work.
 */
public class PseudoHumanJob implements Job {

    @Override
    public void execute(JobExecutionContext context)
    throws JobExecutionException {

        JobDataMap data = context.getJobDetail().getJobDataMap();
        // TODO change keys? (definitely!)
        AutomatedParticipant participant = (AutomatedParticipant) data.get("Participant");
        WorklistItem worklistItem = (WorklistItem) data.get("WorklistItem");
        WorklistService worklistService = ServiceFactory.getWorklistService();

        worklistService.completeWorklistItemBy(worklistItem, participant);
        WorklistItem item = worklistService.getWorklistItems(participant)[0];
        worklistService.claimWorklistItemBy(worklistItem, resource)

    }

}
