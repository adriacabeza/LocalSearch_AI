import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by bejar on 17/01/17
 * Modified on 14/3/18
 */
public class ComparticionSuccesorFunction2 implements SuccessorFunction{



    public static int randInt(int min, int max){
        Random rand = new Random();
        int randomNum = rand.nextInt(max-min)+min;
        return randomNum;
    }

    public List getSuccessors(Object n){
        ComparticionState state = (ComparticionState) n;
        ArrayList<Successor> retval = new ArrayList<>();
        int o = randInt(0,3);
        // if 1 swap inside
        if (o == 0) {
            int i = randInt(0,state.getassignments().size()-1);
            if(state.getassignments().get(i).size() > 2) {
                int j = randInt(1, state.getassignments().get(i).size() - 2);
                int k = randInt(j + 1, state.getassignments().get(i).size() - 1);
                if (state.getassignments().get(i).get(j) != state.getassignments().get(i).get(k)) {
                    ComparticionState temp_state = new ComparticionState(state);
                    temp_state.swapInside(i, j, k);
                    StringBuffer s = new StringBuffer();
                    s.append("swap inside: Car " + i + " changing user " + j + " to position " + k); //+"\n"+ state.toString();
                    retval.add(new Successor(s.toString(), temp_state));
                    //    System.out.println(s.toString());
                    //  System.out.println(count);
                }
            }
        }
        // else swap outside
        else if(o == 1) {
            int i = randInt(0,state.getassignments().size()-1); //cotxe 1
            int j = randInt(i+1,state.getassignments().size());  //cotxe 2
            if(state.getassignments().get(i).size() > 2) {
                int m = randInt(1, state.getassignments().get(i).size() - 1);
                int k = 1;
                int l = 2;
                if(state.getassignments().get(j).size()>2){
                    k = randInt(1, state.getassignments().get(j).size() - 2); //on el deixem recollida
                    l = randInt(k + 1, state.getassignments().get(j).size() - 1); //on el deixem deixada
                }
                ComparticionState temp_state = new ComparticionState(state);
                temp_state.swapOutside2(i, j, state.getassignments().get(i).get(m), k, l);
                StringBuffer s = new StringBuffer();
                s.append("swap outside:  Car " + i + " to Car " + j + " changing user " + m + " from " + k + " to " + l); //+"\n"+ state.toString();
                retval.add(new Successor(s.toString(), temp_state));
                // System.out.println(s.toString());
                //System.out.println(count);
            }
        }

        else {
            int i = randInt(0,state.getassignments().size()); //cotxe 1
                if (state.getassignments().get(i).size() == 2) {
                    int j = randInt(i+1, state.getassignments().size()); //cotxe 2
                    int k = 1;
                    int l = 2;
                    if(state.getassignments().get(j).size()>2) {
                        k = randInt(1, state.getassignments().get(j).size() - 2); //on el deixem recollida
                        l = randInt(k + 1, state.getassignments().get(j).size() - 1); //on el deixem deixada
                    }
                    ComparticionState temp_state = new ComparticionState(state);
                    temp_state.deleteCar(i, j, k, l);
                    StringBuffer s = new StringBuffer();
                    s.append("delete : Deleting car " + i + " and adding to Car " + j + " user from " + k + " to " + l); //+"\n"+ state.toString();
                    retval.add(new Successor(s.toString(), temp_state));
                }

        }
        return retval;

    }

}
