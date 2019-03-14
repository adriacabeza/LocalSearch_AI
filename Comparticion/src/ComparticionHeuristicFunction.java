/**
 * Created by bejar on 17/01/17.
 */

import aima.search.framework.HeuristicFunction;

public class ComparticionHeuristicFunction implements HeuristicFunction {

    public double getHeuristicValue(Object n){
        ComparticionState state = (ComparticionState) n;
        int count = 0;
        for(Integer dist : state.getdistances()){
            count += dist;
        }
        return (double) count;
    }
}
