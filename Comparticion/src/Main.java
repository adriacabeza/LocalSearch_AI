import IA.Comparticion.Usuario;
import aima.search.framework.GraphSearch;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.AStarSearch;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;

import java.util.*;

import IA.Comparticion.Usuarios;

public class Main {


    public static void opcions(int op) {
        if (op == 0) {
            System.out.println("Hill Climbing: -1");
            System.out.println("Simulated Annealing: -2");
            System.out.println("Exit: -3");
        }

    }

    /////////AIXO HO HE COPIAT MAYBE ENS ÉS UTIL
    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }


    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }
    ///////////


    private static void ComparticionHillClimbingSearch(ComparticionState state) {
        try {
            Problem problem;
            problem = new Problem(state, new ComparticionSuccesorFunction(), new ComparticionGoalTest(), new ComparticionHeuristicFunction2());
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem, search);

            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
            ComparticionGoalTest test = new ComparticionGoalTest();
            System.out.print(((ComparticionState) search.getGoalState()).toString());
            System.out.println(test.isGoalState(search.getGoalState()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static void ComparticionSimulatedAnnealingSearch(ComparticionState estat) {
        try {
            Problem problem;
            problem = new Problem(estat, new ComparticionSuccesorFunction2(), new ComparticionGoalTest(), new ComparticionHeuristicFunction2());
            Search search = new SimulatedAnnealingSearch(100000, 10, 5, 0.01);
            SearchAgent agent = new SearchAgent(problem, search);

            ComparticionGoalTest test = new ComparticionGoalTest();
            System.out.print(((ComparticionState) search.getGoalState()).toString());
            System.out.println(test.isGoalState(search.getGoalState()));
          } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        /*
        Scanner in = new Scanner(System.in);
        Random random = new Random();
        int ops = 0;
        while (ops != -3) {
            opcions(0);
            ops = in.nextInt();
            if (ops == -1) {
                System.out.println("Introduce a seed number or '-1'  if you want to use a random seed");
                Integer seed = in.nextInt();
                if (seed < 0) seed = random.nextInt();
                else seed = random.nextInt(10000);
                Usuarios users = new Usuarios(90, 30, 1234);
                ComparticionState state = new ComparticionState(users);
                state.generateInitSol1();
                ComparticionHillClimbingSearch(state);

            }

        }*/
        //12 7 1234

        Usuarios users = new Usuarios(200, 100, 1234);
        ComparticionState state = new ComparticionState(users);
        int option = 0;

        switch (option){
            case 0:
                System.out.println("Using first generate intial solution");
                state.generateInitSol1();
                System.out.println("\nHill Climbing\n");
                ComparticionHillClimbingSearch(state);
                System.out.println("\nSimulated Annealing Search\n");
                ComparticionSimulatedAnnealingSearch(state);
                break;
            case 1:
                System.out.println("Using second generate intial solution");
                state.generateInitSol2();
                System.out.println("\nHill Climbing\n");
                ComparticionHillClimbingSearch(state);
                System.out.println("\nSimulated Annealing Search\n");
                ComparticionSimulatedAnnealingSearch(state);

                break;
            case 2:
                System.out.println("Using third generate intial solution");
                state.generateInitSol3();
                System.out.println("\nHill Climbing\n");
                ComparticionHillClimbingSearch(state);
                System.out.println("\nSimulated Annealing Search\n");
                ComparticionSimulatedAnnealingSearch(state);
            case 3:
                System.out.println("Using forth generate intial solution");
                state.generateInitSol4();
                System.out.println("\nHill Climbing\n");
                ComparticionHillClimbingSearch(state);
                System.out.println("\nSimulated Annealing Search\n");
                ComparticionSimulatedAnnealingSearch(state);
        }
    }

}