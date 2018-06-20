package com.migu.schedule;


import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.migu.schedule.constants.ReturnCodeKeys;
import com.migu.schedule.info.NodeInfo;
import com.migu.schedule.info.TaskInfo;

/*
*类名和方法不能修改
 */
public class Schedule {

	private List<NodeInfo> nodeinfolist;
	
	private ConcurrentLinkedQueue<TaskInfo> taskinfoQueue;
	
    public int init() 
    {
    	// 清空服务节点信息、清空服务节点下的任务信息
    	int status = ReturnCodeKeys.E000;
    	
    	if (null != nodeinfolist && nodeinfolist.size() > 0)
    	{
    		for (NodeInfo nodeinfo : nodeinfolist)
    		{
    			if (null != nodeinfo)
    			{
    				List<TaskInfo> taskInfoList = nodeinfo.getTaskinfolist();
    				// 注销服务节点信息
    				this.unregisterNode(nodeinfo.getNodeId());
    				if (this.unregisterNode(nodeinfo.getNodeId()) != ReturnCodeKeys.E006)
    				{
    					System.out.println("unregister is fail and nodeid = " + nodeinfo.getNodeId());
    					return status;
    				}
    				
    				if (null != taskInfoList && taskInfoList.size() > 0)
    				{
    					for (TaskInfo taskinfo : taskInfoList)
    					{
    						if (null != taskinfo)
    						{
    							// 清空当前节点下任务信息
    							if (this.deleteTask(taskinfo.getTaskId()) != ReturnCodeKeys.E011)
    							{
    								System.out.println("deleteTask is fail and nodeid = " + nodeinfo.getNodeId() + " | taskid = " + taskinfo.getTaskId());
    								return status;
    							}
    						}
    					}
    				}
    				
    			}
    		}
    	}
    	else 
    	{
    		status = ReturnCodeKeys.E001;
    	}
    	
    	status = ReturnCodeKeys.E001;
    	
        return status;
    }

    /**
     * 服务节点注册
     * @param nodeId
     * @return
     */
    public int registerNode(int nodeId) 
    {
    	// 校验服务节点是否符合编号规则
    	if (nodeId <= 0)
    	{
    		return ReturnCodeKeys.E004;
    	}
    	
    	if (null != nodeinfolist && nodeinfolist.size() > 0)
    	{
    		// 核对服务节点是否已被注册
    		for (NodeInfo node : nodeinfolist)
        	{
        		if (null != node && (node.getNodeId() == nodeId))
        		{
        			return ReturnCodeKeys.E005;
        		}
        	}
    		
    		// 将新的服务节点注册到本地
    		NodeInfo nodeinfo = new NodeInfo(nodeId);
        	nodeinfolist.add(nodeinfo);
        	
        	// 服务节点id在服务列表中已存在，判断为注册成功
        	for (NodeInfo node : nodeinfolist)
        	{
        		if (node.getNodeId() == nodeId)
        		{
        			return ReturnCodeKeys.E003;
        		}
        	}
    	}
    	
    	nodeinfolist = new ArrayList<NodeInfo>();
    	// 将新的服务节点注册到本地
		NodeInfo nodeinfo = new NodeInfo(nodeId);
    	nodeinfolist.add(nodeinfo);
    	
        return ReturnCodeKeys.E003;
    }

    public int unregisterNode(int nodeId) 
    {
    	System.out.println("begin nodeinfolist size = " + nodeinfolist.size());
    	
    	// 校验服务节点是否符合编号规则
    	if (nodeId <= 0)
    	{
    		return ReturnCodeKeys.E004;
    	}
    	
    	if (null != nodeinfolist && nodeinfolist.size() > 0)
    	{
    		// 服务节点编号未被注册
    		for (NodeInfo node : nodeinfolist)
    		{
    			if (null != node && (node.getNodeId() == nodeId))
    			{
    				break;
    			}
    			else
    			{
    				System.out.println("current nodeid no register | nodeid = " + nodeId);
    				return ReturnCodeKeys.E007;
    			}
    		}
    		
    		// 注销服务节点
    		for (int i = 0;i < nodeinfolist.size();i++)
    		{
    			if (null != nodeinfolist.get(i) && (nodeinfolist.get(i).getNodeId() == nodeId))
    			{
    				nodeinfolist.remove(i);
    				System.out.println("end nodeinfolist size = " + nodeinfolist.size());
    				return ReturnCodeKeys.E006;
    			}
    		}
    		
    	}
    	
        return ReturnCodeKeys.E006;
    }


    public int addTask(int taskId, int consumption) 
    {
    	// 校验任务编号是否符合编号规则
    	if (taskId <= 0)
    	{
    		return ReturnCodeKeys.E009;
    	}
    	
    	if (null != taskinfoQueue && (taskinfoQueue.size() > 0))
    	{
    		// 相同任务编号任务是否已经被添加到任务队列中
    		for (TaskInfo taskinfo : taskinfoQueue)
    		{
    			if (null != taskinfo && (taskinfo.getTaskId() == taskId))
    			{
    				return ReturnCodeKeys.E010;
    			}
    		}
    		
    		// 添加任务到任务队列中
    		TaskInfo task = new TaskInfo();
    		task.setTaskId(taskId);
    		task.setConsumption(consumption);
    		taskinfoQueue.add(task);
    	}
    	else 
    	{
    		// 添加任务到任务队列中
    		taskinfoQueue = new ConcurrentLinkedQueue<TaskInfo>();
    		TaskInfo task = new TaskInfo();
    		task.setTaskId(taskId);
    		task.setConsumption(consumption);
    		taskinfoQueue.add(task);
    	}
    	
        return ReturnCodeKeys.E008;
    }


    public int deleteTask(int taskId) 
    {
    	// 校验任务编号是否符合编号规则
    	if (taskId <= 0)
    	{
    		return ReturnCodeKeys.E009;
    	}
    	
    	if (null != taskinfoQueue && (taskinfoQueue.size() > 0))
    	{
    		// 指定编号的任务未被添加
    		for (TaskInfo taskinfo : taskinfoQueue)
    		{
    			if (null != taskinfo && (taskinfo.getTaskId() == taskId))
    			{
    				break;
    			}
    			else 
    			{
    				System.out.println("current task not add into queue | taskid = " + taskinfo.getTaskId());
    				return ReturnCodeKeys.E012;
    			}
    		}
    		
    		// 从任务队列中成功删除指定的任务id
    		for (TaskInfo taskinfo : taskinfoQueue)
    		{
    			if (null != taskinfo && (taskinfo.getTaskId() == taskId))
    			{
    				if (taskinfoQueue.remove(taskinfo))
    				{
    					System.out.println("taskinfoQueue size = " + taskinfoQueue.size());
    					return ReturnCodeKeys.E011;
    				}
    			}
    		}
    	}
    	
        return ReturnCodeKeys.E011;
    }


    public int scheduleTask(int threshold) 
    {
    	// 两两服务器之前总消耗率<=threshold
    	// 先将挂起的任务分配至服务器，然后计算两两服务器之前总的消耗率
    	
    	if ((null != nodeinfolist && nodeinfolist.size() > 0) && (null != taskinfoQueue && taskinfoQueue.size() > 0))
    	{
    		// 挂起任务的总消耗率
    		int allConsumption = 0;
    		System.out.println(taskinfoQueue.size());
    		for (TaskInfo task : taskinfoQueue)
    		{
    			if (null != task)
    			{
    				allConsumption += task.getConsumption();
//    				System.out.println(" taskid = " + task.getTaskId() + " | everyone task Consumption = " + task.getConsumption());
    			}
    		}
    		System.out.println("all task and allConsumption = " + allConsumption);
    	}
    	
    	
    	
        return ReturnCodeKeys.E000;
    }


    public int queryTaskStatus(List<TaskInfo> tasks) 
    {
        return ReturnCodeKeys.E000;
    }
    
}
