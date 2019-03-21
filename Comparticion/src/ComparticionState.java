import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;



public class ComparticionState {

    //attributes
    private ArrayList<ArrayList<Integer>> assignments;
    private ArrayList<Integer> distances;
    private Usuarios users;


    // CONSTRUCTORS

    public ComparticionState(Usuarios users){
        this.assignments = new ArrayList<>();
        this.distances= new ArrayList<>();
        this.users = users;
    }

    public ComparticionState(ArrayList<ArrayList<Integer>> assignments, ArrayList<Integer> distances, Usuarios users){
        this.assignments = assignments;
        this.distances = distances;
        this.users = users;
    }

    public ComparticionState(ComparticionState n){
        this.distances = new ArrayList<>(n.distances);
        this.users = n.users;
        this.assignments = new ArrayList(n.assignments.size());
        for (int i = 0; i < n.assignments.size(); ++i){
            this.assignments.add(new ArrayList<Integer>(n.getassignments().get(i)));

        }
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

    public int calc_distance(int x, int y, int x2, int y2){
        return Math.abs(x2-x)+Math.abs(y2-y);
    }

//CONFIEM
    public int distance(ArrayList<Integer> car){
        HashSet aux = new HashSet<Integer>();
        int previousx = users.get(car.get(0)).getCoordOrigenX();
        int previousy = users.get(car.get(0)).getCoordOrigenY();
        int target_x, target_y;
        int distance = 0;
        for(int i = 0; i < car.size(); ++i){

            if(aux.contains(car.get(i))){
                target_x = users.get(car.get(i)).getCoordDestinoX();
                target_y =  users.get(car.get(i)).getCoordDestinoY();
            } else{
                target_x = users.get(car.get(i)).getCoordOrigenX();
                target_y=  users.get(car.get(i)).getCoordOrigenY();
                aux.add(car.get(i));
            }
            distance += calc_distance(previousx,previousy, target_x, target_y);
            previousx = target_x;
            previousy = target_y;
        }
        return distance/10;
    }


    //INITIAL SOLUTIONS

    //DEIXAR UNA PERSONA EN UN COTXE
    public void generateInitSol1(){
        ArrayList<ArrayList<Integer>> cars = new ArrayList<>(users.size());

        for(int i = 0; i< users.size(); ++i){
            ArrayList<Integer> paxs = new ArrayList<>();
            Usuario u = users.get(i);
            if(u.isConductor()){
                paxs.add(i);
                paxs.add(i);
                cars.add(paxs);
            }

        }
        int count = 0;
        for(int i = 0; i<users.size(); ++i){

            Usuario u = users.get(i);
            if(!u.isConductor()){
                cars.get(count%cars.size()).add(1,i);
                cars.get(count%cars.size()).add(1,i);
                ++count;
            }

        }
        for(ArrayList<Integer> car : cars){
            distances.add(distance(car));
        }
        this.assignments = cars;
    }


    public String toString(){
        StringBuffer sortida = new StringBuffer();
        for(int i = 0; i<assignments.size(); ++i){
            sortida.append("Cotxe "+ i + ":"+"\tdist: "+distances.get(i)+ " km");
            for(int j : assignments.get(i)){
                sortida.append(" "+j);
            }
            sortida.append("\n");

        }
        return sortida.toString();
    }

    //ens falta mirar si es una solució correcta
//    public void generateInitSol2(){
//
//    }
}

