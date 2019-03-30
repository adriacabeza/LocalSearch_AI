import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.*;
import javax.swing.JFrame;
import IA.Comparticion.Usuarios;

public class Main {

    static ComparticionState finalstate;
    static Usuarios users;

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


    private static ComparticionState ComparticionHillClimbingSearch(ComparticionState state) {
        try {
            Problem problem;
            problem = new Problem(state, new ComparticionSuccesorFunction(), new ComparticionGoalTest(), new ComparticionHeuristicFunction2());
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem, search);

            //printActions(agent.getActions());
           // printInstrumentation(agent.getInstrumentation());
            ComparticionGoalTest test = new ComparticionGoalTest();
           System.out.print(((ComparticionState) search.getGoalState()).toString());
            System.out.println(test.isGoalState(search.getGoalState()));

            return (ComparticionState) search.getGoalState();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    private static ComparticionState ComparticionSimulatedAnnealingSearch(ComparticionState estat, int steps, int stiter, int k, double lamb) {
        try {
            Problem problem;
            problem = new Problem(estat, new ComparticionSuccesorFunction2(), new ComparticionGoalTest(), new ComparticionHeuristicFunction2());
            Search search = new SimulatedAnnealingSearch(steps, stiter,k,lamb);
            SearchAgent agent = new SearchAgent(problem, search);

            ComparticionGoalTest test = new ComparticionGoalTest();
            //System.out.print(((ComparticionState) search.getGoalState()).toString());
            System.out.println(test.isGoalState(search.getGoalState()));

            return (ComparticionState) search.getGoalState();
          } catch (Exception e) {
            e.printStackTrace();
            return null;
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


            int i = 100;
            double time4 = System.currentTimeMillis();
            users = new Usuarios(i, i/2, 1234);
            ComparticionState state = new ComparticionState(users);
            state.generateInitSol3();
            finalstate = ComparticionHillClimbingSearch(state);
            time4 = (System.currentTimeMillis() - time4);
            System.out.println(" people "+ i + " drivers:"+ i/2+ " time:" + time4+ " ms\n\n");
             displayState(users, finalstate);
            System.exit(0);
        /*
        }

        for(int i = 0; i < 5; ++i) {
            switch (i) {
               case 0:
                    double time1 = System.currentTimeMillis();
                    System.out.println("Using first generate intial solution");
                    state.generateInitSol1();
                    System.out.println("\nHill Climbing\n");
                    //ComparticionHillClimbingSearch(state);
                    time1 = (System.currentTimeMillis() - time1)/1000;
                    //double time11 = System.currentTimeMillis();
                    // System.out.println("\nSimulated Annealing Search\n");
                    ComparticionSimulatedAnnealingSearch(state,225000, 10, 5, 0.401);
                    //time11 = (System.currentTimeMillis() - time11)/1000;
                    //System.out.println("Time Simulated Annealing: "+ time11);
                    System.out.println("Time Hill Climbing: "+ time1);
                    break;
                case 1:
                    double time2 = System.currentTimeMillis();
                    System.out.println("Using second generate intial solution");
                    state.generateInitSol2();
                    System.out.println("\nHill Climbing\n");
                    ComparticionHillClimbingSearch(state);
                   time2 = (System.currentTimeMillis() - time2)/1000;
                    //double time21 = System.currentTimeMillis();
                    //  System.out.println("\nSimulated Annealing Search\n");
                   // ComparticionSimulatedAnnealingSearch(state, 100000, 10, 5, 0.01);
                   // time21 = (System.currentTimeMillis() - time21)/1000;
                    System.out.println("Time Hill Climbing: "+ time2);
                    //System.out.println("Time Simulated Annealing: "+ time21);

                    break;
               case 2:
                    double time3 = System.currentTimeMillis();
                    System.out.println("Using third generate intial solution");
                    state.generateInitSol3();
                    System.out.println("\nHill Climbing\n");
                    ComparticionHillClimbingSearch(state);
                    time3 = (System.currentTimeMillis() - time3)/1000;
                    //double time31 =  System.currentTimeMillis();
                    //System.out.println("\nSimulated Annealing Search\n");
                    //ComparticionSimulatedAnnealingSearch(state);
                   // time31 = (System.currentTimeMillis() - time31)/1000;
                    System.out.println("Time Hill Climbing: "+ time3);
                    //System.out.println("Time Simulated Annealing: "+ time31);
                    break;
               case 3:
                    double time4= System.currentTimeMillis();
                    System.out.println("Using forth generate intial solution");
                    state.generateInitSol4();
                    System.out.println("\nHill Climbing\n");
                    ComparticionHillClimbingSearch(state);
                    time4 = (System.currentTimeMillis() - time4)/1000;
                    //double time41= System.currentTimeMillis();
                    //System.out.println("\nSimulated Annealing Search\n");
                    //ComparticionSimulatedAnnealingSearch(state, 100000, 10, 5, 0.01);
                    //time41 = (System.currentTimeMillis() - time41)/1000;
                    System.out.println("Time Hill Climbing: "+ time4);
                    //System.out.println("Time Simulated Annealing: "+ time41);


            }
            System.out.println("\n\n");
            System.out.println("-----------------------------------------------------------------");

        }



        state.generateInitSol1();
        for(int i = 50000; i < 250000; i+= 25000){
            for(double l = 0.0001; l < 0.05; l += 0.0025){
                        double timeSIMULATED= System.currentTimeMillis();
                        System.out.println("\n Steps "+ i+ " Siter "+ 10+ " k "+ 5 + " Lambda "+String.format("%.4f",l));
                        ComparticionSimulatedAnnealingSearch(state,i,10,5,l);
                        timeSIMULATED = (System.currentTimeMillis() - timeSIMULATED);
                        System.out.println("Time Simulated Annealing: "+ timeSIMULATED);
                    }
                }
            }




    */

    }

    private static void displayState(Usuarios usuarios, ComparticionState state) {
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new JFrame();
					frame.setVisible(true);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					int w = 800;
					int h = 800;
					frame.getContentPane().setPreferredSize(new Dimension(w, h));
					frame.pack();
					CityStatePanel city = new CityStatePanel(usuarios, state, w, h);
					frame.add(city);
					frame.pack();
					frame.setLocationRelativeTo(null);
					frame.setTitle("State Display");
					frame.setResizable(false);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }

}
