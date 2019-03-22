import aima.search.framework.GoalTest;


public class ComparticionGoalTest implements GoalTest {

    public boolean isGoalState(Object n){
        boolean valid = true;
        ComparticionState state = (ComparticionState) n;
        int total_dist = 0;
        for(int i = 0; i< state.getassignments().size(); ++i) {
            int dist = state.getdistances().get(i);
            total_dist += dist;
            if (dist > 300){
                valid = false;
                System.out.println("WARNING: CAR "+i+ " drives " + (dist/10f) +" km");
            }
            int maxP = state.getMaxSimultaneous(state.getassignments().get(i));
            if(maxP > 2){
                valid = false;
                System.out.println("WARNING: CAR "+ i + " carries "+ maxP +" people at once.");
            }
        }
        System.out.println("Finally we have " + state.getassignments().size() + " cars.");
        System.out.println("The total distance is " + (total_dist/10f) + " km");
        return valid;
    }

}
