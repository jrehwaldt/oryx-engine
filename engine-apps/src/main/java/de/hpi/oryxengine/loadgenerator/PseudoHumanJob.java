package de.hpi.oryxengine.loadgenerator;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.factories.process.HumanTaskProcessDeployer;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemState;

/**
 * The PseudoHumanJob, which is a quartz scheduler Job and gets called whenever it is time that one worker finished its
 * work.
 */
public class PseudoHumanJob implements Job {

    /**
     * Executes our PseudoHuman Job which has the task to complete its current item and then claim a new one.
     * {@inheritDoc}
     */
    @Override
    public void execute(JobExecutionContext context)
    throws JobExecutionException {

        // get the data we need from the Jobcontext and Participant/WorklistService
        JobDataMap data = context.getJobDetail().getJobDataMap();
        Participant participant = (Participant) data.get(HumanTaskProcessDeployer.PARTICIPANT_KEY);
        List<AbstractWorklistItem> itemsInWork = participant.getWorklistItemsCurrentlyInWork();
        WorklistService worklistService = ServiceFactory.getWorklistService();

        // if we currently have items in Work, complete the first one
        if (!itemsInWork.isEmpty()) {
            AbstractWorklistItem worklistItem = itemsInWork.get(0);
            worklistService.completeWorklistItemBy(worklistItem, participant);
        }
        
        // If there are still items left we can work on we claim them and then start working on them
        List<AbstractWorklistItem> itemsToWorkOn = worklistService.getWorklistItems(participant);
        if (!itemsToWorkOn.isEmpty()) {
            AbstractWorklistItem item = itemsToWorkOn.get(0);

            // Worklistitems can be directly allocated to us, so they might not have the status offered and we can't
            // claim them
            if (item.getStatus() == WorklistItemState.OFFERED) {
                worklistService.claimWorklistItemBy(item, participant);
            }
            worklistService.beginWorklistItemBy(item, participant);
        }

    }

}
