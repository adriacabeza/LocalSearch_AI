import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;



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
                --j;
            }
        }

        assignments.get(car2).add(i,id);
        assignments.get(car2).add(k,id);

        if(assignments.get(car1).size() != 0) distances.set(car1, distance(assignments.get(car1)));
        distances.set(car2, distance(assignments.get(car2)));
    }

    public void deleteCar(int car, int car2,int i, int j){
        swapOutside2(car,car2,assignments.get(car).get(0), i,j);
        assignments.remove(car);
        distances.remove(car);
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
    public int distance(List<Integer> car){
        HashSet aux = new HashSet<Integer>();
        int previousx = users.get(car.get(0)).getCoordOrigenX();
        int previousy = users.get(car.get(0)).getCoordOrigenY();
        int target_x, target_y;
        int distance = 0;
        aux.add(car.get(0));
        for(int i = 1; i < car.size(); ++i){
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
        return distance;
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
        distances.clear();
        for(ArrayList<Integer> car : cars){
            distances.add(distance(car));
        }
        this.assignments = cars;
    }


    public String toString(){
        StringBuffer sortida = new StringBuffer();
        for(int i = 0; i<assignments.size(); ++i){
            sortida.append("Cotxe "+ i + ":"+"\tdist: "+(distances.get(i)/10f)+ " km");
            for(int j : assignments.get(i)){
                sortida.append(" "+j);
            }
            sortida.append("\n");

        }
        return sortida.toString();
    }

    //Maxim nombre de persones en un cotxe
    public int getMaxSimultaneous(ArrayList<Integer> car){
        HashSet aux = new HashSet<Integer>();
        int max = 0;
        for(int i = 1; i < car.size()-1;++i){
            int id = car.get(i);
            if (aux.contains(id)){
                aux.remove(id);
            }
            else{
                aux.add(id);
                if (aux.size() > max){
                    max = aux.size();
                }
            }
        }
        return max;
    }


    //ens falta mirar si es una solució correcta
    public void generateInitSol2(){

        ArrayList<ArrayList<Integer>> cars = new ArrayList<>(users.size());
        //INSERT DRIVER
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
                int j = count%cars.size();
                cars.get(j).add(1,i);
                cars.get(j).add(cars.get(j).size()-1,i);
                ++count;
            }

        }
        distances.clear();
        for(ArrayList<Integer> car : cars){
            distances.add(distance(car));
        }
        this.assignments = cars;


    }

    public void generateInitSol4(){

        ArrayList<ArrayList<Integer>> cars = new ArrayList<>(users.size());
        //INSERT DRIVER
        for(int i = 0; i< users.size(); ++i){
            ArrayList<Integer> paxs = new ArrayList<>();
            Usuario u = users.get(i);
            if(u.isConductor()){
                paxs.add(i);
                paxs.add(i);
                cars.add(paxs);
            }
        }
        for(int i = 0; i<users.size(); ++i){
            Usuario u = users.get(i);
            if(!u.isConductor()){
                cars.get(0).add(1,i);
                cars.get(0).add(cars.get(0).size()-1,i);
            }

        }
        distances.clear();
        for(ArrayList<Integer> car : cars){
            distances.add(distance(car));
        }
        this.assignments = cars;
    }

    public void generateInitSol3(){

        ArrayList<ArrayList<Integer>> cars = new ArrayList<>(users.size());
        //INSERT DRIVER
        for(int i = 0; i< users.size(); ++i){
            ArrayList<Integer> paxs = new ArrayList<>();
            Usuario u = users.get(i);
            if(u.isConductor()){
                paxs.add(i);
                paxs.add(i);
                cars.add(paxs);
            }

        }
        for(int i = 0; i<users.size(); ++i){

            Usuario u = users.get(i);
            if(!u.isConductor()){
                cars.get(0).add(1,i);
                cars.get(0).add(1,i);
            }

        }
        distances.clear();
        for(ArrayList<Integer> car : cars){
            distances.add(distance(car));
        }
        this.assignments = cars;
    }
}

