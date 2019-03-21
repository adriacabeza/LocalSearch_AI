import aima.search.framework.GoalTest;


public class ComparticionGoalTest implements GoalTest {

    public boolean isGoalState(Object n){
        boolean valid = true;
        ComparticionState state = (ComparticionState) n;
        for(int i = 0; i< state.getassignments().size(); ++i) {
            int dist = state.getdistances().get(i);
            if (dist > 30){
                valid = false;
                System.out.println("WARNING: CAR "+i+ " drives " + dist +" km.");
            }
            int maxP = state.getMaxSimultaneous(state.getassignments().get(i));
            if(maxP > 2){
                valid = false;
                System.out.println("WARNING: CAR "+ i + " carries "+ maxP +" people at once.");
            }
        }
        System.out.println("Finally we have " + state.getassignments().size() + " cars.");
        return valid;
    }

}
