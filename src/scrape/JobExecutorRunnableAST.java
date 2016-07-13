package scrape;

import java.util.HashMap;
import java.util.List;

public class JobExecutorRunnableAST implements Runnable {

	   private List jobs;
	   private int jobsIDStart;
	   
	   
	   JobExecutorRunnableAST(List jobs,int jobsIDStart){
	       this.jobs = jobs;
	       this.jobsIDStart = jobsIDStart;
	   }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(int i =0; i<jobs.size();i++){
			JobExecutorAST.executeJobUsingDataset(jobsIDStart+i+1, (HashMap<String, String>) jobs.get(i));
			System.out.println(jobs.get(i)+" executed");
		}
	}
	

}
