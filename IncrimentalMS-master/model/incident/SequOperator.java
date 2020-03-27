package model.incident;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import evaluation.CostModel;
import model.incidentree.OpNode;
import model.log.Activity;
import model.log.LogRecord;
import model.log.ProbModel;

public class SequOperator extends Operator {
	public  Map<Integer, List<Occurrence>> execute(
			Map<Integer, List<Occurrence>> occs1, Map<Integer, List<Occurrence>> occs2, OpNode op){
		
		Map<Integer, List<Occurrence>> res = op.occs;//new HashMap<Integer, List<Occurrence>>();
		Map<Integer, List<Occurrence>> verify = new HashMap<Integer, List<Occurrence>>();
		
		ArrayList<Occurrence> left_buffer = op.left.buffer;
		ArrayList<Occurrence> right_buffer = op.right.buffer;
		
		//if left buffer not empty and right occs2 is empty, no need to progress.
		if(!left_buffer.isEmpty() && occs2.size() == 0) {
			left_buffer.clear();
		}
		
		//if right buffer not empty, and left occs contains nothing, then no need to progress
		if(!right_buffer.isEmpty() && occs1.size() == 0) {
			right_buffer.clear();
		}
		
		//Actually no need to keep this check. remove this line maybe?
		//if(occs1.size() == 0 || occs2.size() == 0 || op.changeFlag == 0){
		//	return res;
		//}
		
		if(right_buffer.isEmpty() && left_buffer.isEmpty()) {
			return res;
		}
		
		//if right still not empty, process it!
		if(!right_buffer.isEmpty()) {
			Occurrence r = right_buffer.get(0);
			int key = (int) r.wid; 
			
			//For debugging purpose
			verify.put(key, new ArrayList<Occurrence>());
			
			//right matches left! Only perform action if left contains the key!
			if(occs1.containsKey(key)) {
				for(Occurrence occ1: occs1.get(key)){
					if(occ1.end < r.start){
						if(!res.containsKey(key)){
							res.put(key, new ArrayList<Occurrence>());
						}
						res.get(key).add(merge(occ1, r));
						
						//For debugging purpose
						verify.get(key).add(merge(occ1, r));
					}
				}
			}
			right_buffer.clear();
		} else if(!left_buffer.isEmpty()) {
			Occurrence l = left_buffer.get(0);
			int key = (int) l.wid; 
			
			//For debugging purpose
			verify.put(key, new ArrayList<Occurrence>());
			
			//right matches left! Only perform action if left contains the key!
			if(occs1.containsKey(key)) {
				for(Occurrence occ2: occs2.get(key)){
					if(l.end < occ2.start){
						if(!res.containsKey(key)){
							res.put(key, new ArrayList<Occurrence>());
						}
						res.get(key).add(merge(occ2, l));
						verify.get(key).add(merge(occ2, l));
					}
				}
			}
			left_buffer.clear();
		}
		
		
		
		//if(left_buffer.isEmpty() || right_buffer.isEmpty()) {
		//feel like we dont care about left at all!
		//if(right_buffer.isEmpty()) {
		//	return op.occs;
		//}
		
		/*for(int key: occs1.keySet()){
			if(!occs2.containsKey(key))
				continue;
			List<Occurrence> li1 = occs1.get(key);
			List<Occurrence> li2 = occs2.get(key);
			for(Occurrence occ1: li1){
				for(Occurrence occ2: li2){
					if(occ1.end < occ2.start){
						if(!res.containsKey(key)){
							res.put(key, new ArrayList<Occurrence>());
						}
						res.get(key).add(merge(occ1, occ2));
					}
				}
			}
		
//		for(int key: occs1.keySet()){
//			if(!occs2.containsKey(key))
//				continue;
//			List<Occurrence> li1 = occs1.get(key);
//			List<Occurrence> li2 = occs2.get(key);
//			for(Occurrence occ1: li1){
//				if(occ1.end < li2.get(li2.size()-1).start){
//					if(!res.containsKey(key)){
//						res.put(key, new ArrayList<Occurrence>());
//					}
//					res.get(key).add(merge(occ1, li2.get(li2.size()-1)));
//					//System.out.println("sequ res" + res);
//				}
//			}
////			System.out.println("hit");
////			System.out.println("before op.occs: "+ op.occs);
////			System.out.println("before res: "+ res);
//			if(op.occs.get(key) != null) {
//				for(int i = 0; i < op.occs.get(key).size(); i++) {
//					if(res.get(key).contains(op.occs.get(key).get(i))) {
//						continue;
//					}
//					res.get(key).add(op.occs.get(key).get(i));
//				}
//			}
//			System.out.println("hello");
//			System.out.println("after op.occs: "+ op.occs);
//			System.out.println("after res: "+ res);
			//Map<Integer, List<Occurrence>> res
			//HashMap<Integer, List<Occurrence>> occs;
//			System.out.println("op.occs" + op.occs.get(key));
			//TODO: Remove the duplicates

					
//			for(Occurrence occ1: li1){
//				for(Occurrence occ2: li2){
//					if(occ1.end < occ2.start){
//						if(!res.containsKey(key)){
//							res.put(key, new ArrayList<Occurrence>());
//						}
//						res.get(key).add(merge(occ1, occ2));
//						System.out.println("sequ res" + res);
//					}
//				}
			
		}
		*/
		System.out.println("Executed Block OCCS: " + verify);
		System.out.println("Final reutrned OCCS: " + res + "\n");
		return res;
	}

	private Occurrence merge(Occurrence occ1, Occurrence occ2) {
/*		Occurrence occ = new Occurrence(occ1.wid);
		occ.seq.addAll(occ1.seq);
		occ.seq.addAll(occ2.seq);
		occ.setTimeInterval(occ1.start, occ2.end);
		
		//Merge the effects on attributes
//		occ.preMap.putAll(occ1.preMap);
//		occ.postMap.putAll(occ1.postMap);
//		occ.postMap.putAll(occ2.postMap);
		
		occ.setPreMap(occ1.preMap);
		occ.setPostMap(occ2.postMap);*/
		return new Occurrence(occ1, occ2);
	}

	@Override
	public long getResultSize1(long c1, long c2) {
		return Math.min(c1, c2);
	}

	@Override
	public CostModel getResultSize2(CostModel a1, CostModel a2) {
		if(a1.count == 0 || a2.count == 0 || a1.numStart == 0 || a2.numStart == 0){
			return new CostModel(a1.name);
		}
		
		long ts1 = a1.aveStart, td1 = a1.aveInterval;
		long ts2 = a2.aveStart, td2 = a2.aveInterval;
		CostModel cur = new CostModel(a1.name);
		cur.aveStart = Math.min(a1.aveStart, a2.aveStart);
		cur.aveInterval = a1.aveInterval;
		long lastIndex = (a2.count*td2/a2.numStart + ts2 - ts1)/td1;
		long countPerInst = a2.count*lastIndex/a2.numStart - (ts1 - ts2)*lastIndex/td2 - td1*lastIndex*(lastIndex+1)/2/td2;
		cur.count = countPerInst * a1.numStart;
		cur.numStart = a1.numStart;
		return cur;
	}

	@Override
	public ProbModel estimate(ProbModel incidentHist1, ProbModel incidentHist2) {
		// TODO Auto-generated method stub
		ProbModel curHist = new ProbModel();
		
		// If one operand is empty, no results are generated
		if(incidentHist1.numActi == 0 || incidentHist2.numActi == 0)
			return curHist;
		
		// constraint on wid_1 == wid_2
		//double factor = 1.0/QueryEngine.queryEngine.log.numWorkflow;
		//double factor = 1.0/Math.min(incidentHist1.numInst, incidentHist2.numInst);
		double factor = 0.0;
		//factor method 2
		/*for(int x: incidentHist1.instDict.keySet()){
			if(incidentHist2.instDict.containsKey(x)){
				double prob_inst = incidentHist1.instDict.get(x)*incidentHist2.instDict.get(x);
				curHist.instDict.put(x, prob_inst);
				factor += incidentHist1.instDict.get(x)*incidentHist2.instDict.get(x);
			}
		}
		*/
		//factor method 1
		factor = 1.0/Math.min(incidentHist1.numInst, incidentHist2.numInst);

		//experimenting with numbers
		/*for(int s: incidentHist1.startHist.keySet()){
			double probStart1 = incidentHist1.startHist.get(s);
			double probStart2 = 0;
			for(int d: incidentHist1.durHist.keySet()){
				if(!incidentHist2.startHist.containsKey(s+d)){
					continue;
				}
				double prob_d = 1.0*incidentHist1.durHist.get(d)/incidentHist1.numActi*incidentHist2.startHist.get(s+d);
				probStart2 += prob_d;
				for(int d2: incidentHist2.durHist.keySet()){
					int probDur = (int) (1.0*probStart1*prob_d*incidentHist2.durHist.get(d2)/incidentHist2.numActi*factor);
					if(curHist.durHist.containsKey(d+d2)){
						curHist.durHist.put(d+d2, curHist.durHist.get(d+d2)+probDur);
					}else{
						curHist.durHist.put(d+d2, probDur);
					}
				}
			}
			
			int curprobStart = (int) (probStart1*probStart2*factor);
			curHist.startHist.put(s, curprobStart);
			curHist.numActi += curprobStart;
		}*/
		
		//update startHist/durHist
		double totalprobStart = 0;
		double totalprobDur = 0;
		for(int s: incidentHist1.startHist.keySet()){
			double probStart1 = incidentHist1.startHist.get(s);
			double probStart2 = 0;
			for(int d: incidentHist1.durHist.keySet()){
				for(int s2 = s+d; s2<incidentHist2.maxStart; s2++){
					if(!incidentHist2.startHist.containsKey(s2)){
						continue;
					}
					double prob_d = incidentHist1.durHist.get(d)*incidentHist2.startHist.get(s2);
					probStart2 += prob_d;
					for(int d2: incidentHist2.durHist.keySet()){
					 	double probDur = probStart1*prob_d*incidentHist2.durHist.get(d2)*factor;
						if(curHist.durHist.containsKey(s2-s+d2)){
							curHist.durHist.put(s2-s+d2, curHist.durHist.get(s2-s+d2)+probDur);
						}else{
							curHist.durHist.put(s2-s+d2, probDur);
						}
						curHist.minDur = Math.min(d+d2, curHist.minDur);
						curHist.maxDur = Math.min(d+d2, curHist.maxDur);
						totalprobDur += probDur;
					}
				}
			}
			
			double curprobStart = probStart1*probStart2*factor;
			if(Math.abs(curprobStart) > 1e-7){
				curHist.startHist.put(s, curprobStart);
				totalprobStart += curprobStart;
				curHist.minStart = Math.min(curHist.minStart, s);
				curHist.maxStart = Math.max(curHist.maxStart, s);
			}
			
		}
		
		//update numActi
		curHist.numActi = (int)Math.round(totalprobStart*incidentHist1.numActi*incidentHist2.numActi);
				
		//normalize
		for(int s: curHist.startHist.keySet()){
			curHist.startHist.put(s, curHist.startHist.get(s)/totalprobStart);
		}
		
		for(int s: curHist.durHist.keySet()){
			curHist.durHist.put(s, curHist.durHist.get(s)/totalprobDur);
		}
		
		//factor update 2
		//for(int x: curHist.instDict.keySet()){
		//	curHist.instDict.put(x, curHist.instDict.get(x)/factor);
		//}
		
		//factor update 1
		curHist.numInst = Math.min(incidentHist1.numInst, incidentHist2.numInst);
		
		return curHist;
	}

}
