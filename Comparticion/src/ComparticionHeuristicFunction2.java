/**
 * Created by bejar on 17/01/17.
 */

import aima.search.framework.HeuristicFunction;

public class ComparticionHeuristicFunction2 implements HeuristicFunction {

    public double getHeuristicValue(Object n){
        ComparticionState state = (ComparticionState) n;
        int count = 0;
        for(int i = 0; i< state.getassignments().size(); ++i){
            int distExtra = Math.max(0, state.getdistances().get(i)-300);
            count += 2*(distExtra);
            int maxPExtra = Math.max(state.getMaxSimultaneous(state.getassignments().get(i)) -2,0);
            count += 150*(maxPExtra);
        }
        count += 100*state.getassignments().size();
        return count;
    }
}
