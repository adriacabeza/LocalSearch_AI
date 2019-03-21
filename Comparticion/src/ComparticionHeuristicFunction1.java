/**
 * Created by bejar on 17/01/17.
 */

import aima.search.framework.HeuristicFunction;

public class ComparticionHeuristicFunction1 implements HeuristicFunction {

    public double getHeuristicValue(Object n){
        ComparticionState state = (ComparticionState) n;
        int count = 0;
        for(Integer dist : state.getdistances()){
            if(dist > 30) count += 10*(dist-30);
        }
        return count;
    }
}
