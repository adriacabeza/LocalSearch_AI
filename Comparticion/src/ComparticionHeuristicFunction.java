/**
 * Created by bejar on 17/01/17.
 */

import aima.search.framework.HeuristicFunction;

public class ComparticionHeuristicFunction implements HeuristicFunction {

    public double getHeuristicValue(Object n){

        return ((ComparticionBoard) n).heuristic();
    }
}
