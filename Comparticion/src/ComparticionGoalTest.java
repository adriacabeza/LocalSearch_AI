import aima.search.framework.GoalTest;

/**
 * Created by bejar on 17/01/17.
 */
public class ComparticionGoalTest implements GoalTest {

    public boolean isGoalState(Object state){

        return((ComparticionBoard) state).is_goal();
    }
}
