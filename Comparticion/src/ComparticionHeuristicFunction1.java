
import aima.search.framework.HeuristicFunction;

public class ComparticionHeuristicFunction1 implements HeuristicFunction {

    public double getHeuristicValue(Object n){
        ComparticionState state = (ComparticionState) n;
        int count = 0;
        for(int i = 0; i< state.getassignments().size(); ++i){
            int distExtra = Math.max(0, state.getdistances().get(i)-300);
            count += (distExtra);
            int maxPExtra = Math.max(state.getMaxSimultaneous(state.getassignments().get(i)) -2,0);
            count += 50*(maxPExtra);
        }
        return count;
    }
}
