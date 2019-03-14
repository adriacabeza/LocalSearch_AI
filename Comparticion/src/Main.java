import IA.Comparticion.Usuario;
import aima.search.framework.GraphSearch;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.AStarSearch;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import IA.Comparticion.Usuarios;

public class Main {

    public static void main(String[] args) throws Exception {
        Usuarios users = new Usuarios(90,30,1);
        ComparticionState estado = new ComparticionState(users);
        estado.generateInitSol1();
        ComparticionSuccesorFunction comparticion = new ComparticionSuccesorFunction();
        comparticion.getSuccessors(estado);
    }

}