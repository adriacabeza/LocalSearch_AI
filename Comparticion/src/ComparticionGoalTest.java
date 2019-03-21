import aima.search.framework.GoalTest;

/**
 * Created by bejar on 17/01/17.
 */
public class ComparticionGoalTest implements GoalTest {

    public boolean isGoalState(Object n){

        ComparticionState state = (ComparticionState) n;
        for(Integer dist : state.getdistances()){
            if (dist > 30) return false;
        }
        return true;

    }
}
