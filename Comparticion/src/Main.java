import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import IA.Comparticion.Usuarios;
import org.apache.commons.cli.*;

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
            Search search = new SimulatedAnnealingSearch(steps, stiter, k, lamb);
            SearchAgent agent = new SearchAgent(problem, search);

            ComparticionGoalTest test = new ComparticionGoalTest();

            System.out.println(test.isGoalState(search.getGoalState()));

            return (ComparticionState) search.getGoalState();
          } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        Options options = new Options();

        Option opt_s = Option.builder("s")
            .longOpt("seed")
            .desc("seed to use for random")
            .type(Number.class)
            .required(false)
            .hasArg()
            .build();
        options.addOption(opt_s);

        Option opt_n = Option.builder("n")
            .longOpt("n")
            .desc("number of people")
            .type(Number.class)
            .required(false)
            .hasArg()
            .build();
        options.addOption(opt_n);

        Option opt_m = Option.builder("m")
            .longOpt("m")
            .desc("number of drivers")
            .type(Number.class)
            .required(false)
            .hasArg()
            .build();
        options.addOption(opt_m);

        Option opt_a = Option.builder("a")
            .longOpt("algorithm")
            .desc("algorithm to use. Valid options: 'HC' for Hill Climbing. 'SA' for Simulated Annealing")
            .required(false)
            .hasArg()
            .build();
        options.addOption(opt_a);

        Option opt_i = Option.builder("i")
            .longOpt("initial-solution")
            .desc("initial solution to use. valid options: 1, 2, 3 or 4.")
            .type(Number.class)
            .required(false)
            .hasArg()
            .build();
        options.addOption(opt_i);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        double timeS, timeD;
        int seed = 1234;
        int n = 20;
        int m = 10;
        String a = "HC";
        int i = 3;

        try {
            cmd = parser.parse(options, args);
            if(cmd.hasOption("s"))
                seed = ((Number) cmd.getParsedOptionValue("s")).intValue();
            if(cmd.hasOption("n"))
                n = ((Number) cmd.getParsedOptionValue("n")).intValue();
            if(cmd.hasOption("m"))
                m = ((Number) cmd.getParsedOptionValue("m")).intValue();
            else
                m = n/2;
            if(cmd.hasOption("a"))
                a = cmd.getOptionValue("a");
            if(cmd.hasOption("i"))
                i = ((Number) cmd.getParsedOptionValue("i")).intValue();

            timeS = System.currentTimeMillis();
            users = new Usuarios(n, m, seed);
            ComparticionState state = new ComparticionState(users);

            switch(i){
                case 1:
                    state.generateInitSol1();
                    break;
                case 2:
                    state.generateInitSol2();
                    break;
                case 3:
                    state.generateInitSol3();
                    break;
                case 4:
                    state.generateInitSol4();
                    break;
            }

            switch(a){
                case "HC":
                    finalstate = ComparticionHillClimbingSearch(state);
                    break;
                case "SA":
                    finalstate = ComparticionSimulatedAnnealingSearch(state, 250000, 10, 5, 0.5);
                    break;
            }

            timeD = (System.currentTimeMillis() - timeS);
            System.out.println(" people "+ n + " drivers:"+ m + " time:" + timeD+ " ms\n\n");

            if(finalstate != null) displayState(users, finalstate);

            //System.exit(0);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
    }

    private static void displayState(Usuarios usuarios, ComparticionState state) {
    	SwingUtilities.invokeLater(new Runnable() {
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
                    frame.setLocation(0, 0);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }

}
