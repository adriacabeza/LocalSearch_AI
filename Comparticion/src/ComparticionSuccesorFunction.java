import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bejar on 17/01/17
 * Modified on 14/3/18
 */
public class ComparticionSuccesorFunction implements SuccessorFunction{

    public List getSuccessors(Object n){
        ComparticionState state = (ComparticionState) n;
        ArrayList<Successor> retval = new ArrayList<>();
        int count = 0;
        for(int i = 0 ; i < state.getassignments().size(); ++i) { //cotxes
            for (int j = 1; j < state.getassignments().get(i).size() - 1; ++j) { //primera persona
                for (int k = j + 1; k < state.getassignments().get(i).size() - 1; ++k) {
                    if(state.getassignments().get(i).get(j) != state.getassignments().get(i).get(k)){
                        ComparticionState temp_state = new ComparticionState(state);
                        temp_state.swapInside(i,j,k);
                        StringBuffer s = new StringBuffer();
                        s.append("swap inside "+i+" "+j+" "+k);
                        retval.add(new Successor(s.toString(),temp_state));
                    //    System.out.println(s.toString());
                      //  System.out.println(count);
                        ++count;
                    }
                }
            }
        }
        for(int i = 0; i < state.getassignments().size(); ++i){ //cotxe 1
            for(int j = i+1; j < state.getassignments().size(); ++j) { //cotxe 2
                for (int m = 1; m < state.getassignments().get(i).size() - 1; ++m) {
                    //TODO: puta bida els repetits
                    for (int k = 1; k < state.getassignments().get(j).size() - 2; ++k) { //on el deixem recollida
                        for (int l = k+1; l < state.getassignments().get(j).size() - 1; ++l) { //on el deixem deixada
                            ComparticionState temp_state = new ComparticionState(state);
                            temp_state.swapOutside2(i, j,state.getassignments().get(i).get(m), k, l);
                            StringBuffer s = new StringBuffer();
                            s.append("swap outside " + i + " " + j + " " +m +" "+ k + " " + l);
                            retval.add(new Successor(s.toString(), temp_state));
                           // System.out.println(s.toString());
                            //System.out.println(count);
                            ++count;
                        }
                    }
                }
            }
        }

        return retval;

    }

}
