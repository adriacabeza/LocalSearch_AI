import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;

import java.util.ArrayList;
import java.util.Collections;

public class ComparticionState {

    //attributes
    private ArrayList<ArrayList<Integer>> assignments;
    private ArrayList<Integer> distances;


    // CONSTRUCTORS

    public ComparticionState(){
        this.assignments = new ArrayList<>();
        this.distances= new ArrayList<>();
    }

    public ComparticionState(ArrayList<ArrayList<Integer>> assignments, ArrayList<Integer> distances){
        this.assignments = assignments;
        this.distances = distances;
    }

    //SETTERS AND GETTERS
    public ArrayList<ArrayList<Integer>> getassignments() {
        return assignments;
    }

    public void setassignments(ArrayList<ArrayList<Integer>> assignments) {
        this.assignments = assignments;
    }

    public ArrayList<Integer> getdistances() {
        return distances;
    }

    public void setdistances(ArrayList<Integer> distances) {
        this.distances = distances;
    }

    //OPERATORS

    public void swapInside(int car, int i, int j){
        Collections.swap(assignments.get(car),i,j);
        distances.set(car, distance(assignments.get(car)));
    }

    /**
     * TODO: cuidado amb el primer i l'ultim que és el conductor
     * Aquí lo que es proposa es passar-los junts al principi del cotxe 2 i que el swap inside s'encarregui de separar-los.
     **/
    public void swapOutside(int car1, int car2, int id){
        int b = 0;
        for (int j = 0; j < assignments.get(car1).size() && b != 2; ++j){
            if(assignments.get(car1).get(j) == id){
                ++b;
                assignments.get(car1).remove(j);
            }
        }

        assignments.get(car2).add(0,id);
        assignments.get(car2).add(0,id);

        distances.set(car1, distance(assignments.get(car1)));
        distances.set(car2, distance(assignments.get(car2)));
    }

    //TOTS AMB TOTS

    public void swapOutside2(int car1, int car2, int id, int i, int k){
        int b = 0;
        for (int j = 0; j < assignments.get(car1).size() && b != 2; ++j){
            if(assignments.get(car1).get(j) == id){
                ++b;
                assignments.get(car1).remove(j);
            }
        }

        assignments.get(car2).add(i,id);
        assignments.get(car2).add(k,id);

        distances.set(car1, distance(assignments.get(car1)));
        distances.set(car2, distance(assignments.get(car2)));
    }


    //necesitamos operador para cambiar conductores
    //TODO: que la llista sigui de tamany 2
     public void deleteCar(int car){
        assignments.remove(car);
     }


    //UTILS
    
    public int distance(ArrayList<Integer> car){
        //TODO
        return 20;
    }


    //INITIAL SOLUTIONS

    public void generateInitSol1(Usuarios us){
        int last_car = 0;
        for(Usuario u : us){
            if(u.isConductor());

        }
    }

    public void generateInitSol2(){

    }
}

