package com.migu.schedule.info;

import java.util.List;

public class NodeInfo 
{
	// 服务节点id
	private int nodeId;
	
	private List<TaskInfo> taskinfolist;
	
	public NodeInfo(int nodeid) 
	{
		this.nodeId = nodeid;
	}

	public int getNodeId() 
	{
		return nodeId;
	}

	public void setNodeId(int nodeId) 
	{
		this.nodeId = nodeId;
	}
	
	public List<TaskInfo> getTaskinfolist() 
	{
		return taskinfolist;
	}

	public void setTaskinfolist(List<TaskInfo> taskinfolist) 
	{
		this.taskinfolist = taskinfolist;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NodeInfo [nodeId=");
		builder.append(nodeId);
		builder.append(", taskinfolist=");
		builder.append(taskinfolist);
		builder.append("]");
		return builder.toString();
	}

}
