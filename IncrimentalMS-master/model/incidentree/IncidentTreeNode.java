package model.incidentree;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import model.incident.Occurrence;
import model.log.Log;
import model.log.LogRecord;

public abstract class IncidentTreeNode implements Runnable, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum NodeType {
		ACTI, OP, COND
	};
	 
	public NodeType type;
	public String name;
	public IncidentTreeNode left, right;
	public Map<Integer, List<Occurrence>> occs;
	public ArrayList<Occurrence> buffer;
	public long size;
	public int deltaFlag;
	public int changeFlag;
	
	public IncidentTreeNode(String str){
		name = str;
		left = null;
		right = null;
		occs = new HashMap<Integer, List<Occurrence>>();
		size = 0;
		deltaFlag = -1;
		changeFlag = -1;
		buffer = new ArrayList<Occurrence>();
	}
	
	public NodeType getType() {
		return type;
	}
	
	
	/**
	 * This function performs recursion search to check if any node contain non-empty Buffer!
	 * @param node
	 * @return
	 */
	public boolean nodeBufferExist(IncidentTreeNode node) {
		if (node == null) {
			return false;
		}
		
		if (!node.buffer.isEmpty()) {
			return true; 
		}
		
		boolean res1 = nodeBufferExist(node.left);
		if (res1) return true; 
		
		boolean res2 = nodeBufferExist(node.right);	
		return res2;
	}
	
	/**
	 * Recursion function which will be used to find which branch contains non-empty buffer.
	 * 
	 * @param node
	 * @return 0 --> left, 1 --> right, -1 --> no data found.
	 */
	public int bufferLeftOrRight(IncidentTreeNode node) {
		boolean left_exist = this.nodeBufferExist(node.left); 
		if(left_exist) return 0; 
		
		boolean right_exist = this.nodeBufferExist(node.right); 
		if(right_exist) return 1; 
		
		return -1;
	}
	
	
	/*
	 * TODO: move all delta flag related~
	 */
	public boolean nodeDeltaFlageExist(IncidentTreeNode node) {
		if (node == null) {
			return false;
		}
		
		if (node.deltaFlag == 1) {
			return true; 
		}
		
		boolean res1 = nodeDeltaFlageExist(node.left);
		if (res1) return true; 
		
		boolean res2 = nodeDeltaFlageExist(node.right);	
		return res2;
	}
	
	/**
	 * Recursion function which will be used to find which branch contains DeltaFlag.
	 * 
	 * @param node
	 * @return 0 --> left, 1 --> right, -1 --> no data found.
	 */
	public int findDeltaFlag(IncidentTreeNode node) {
		boolean left_exist = this.nodeDeltaFlageExist(node.left); 
		if(left_exist) return 0; 
		
		boolean right_exist = this.nodeDeltaFlageExist(node.right); 
		if(right_exist) return 1; 
		
		return -1;
	}
	
}
