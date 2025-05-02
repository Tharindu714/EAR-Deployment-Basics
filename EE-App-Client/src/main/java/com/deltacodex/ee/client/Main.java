package com.deltacodex.ee.client;

//import com.deltacodex.ee.webapp.remote.UserDetails;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * <h2>Important Notes</h2>
 * <h5>How to find Default Port - 3700</h5>
 * "<a href="http://localhost:4848/common/index.jsf">Admin Console</a>" -> Server-config -> ORB -> IIOP Listener -> orb-listener-1
 * <br><br>
 * <h5>Initial Context Binding</h5>
 * <span><b>* We previously Use this method to get lookup from remote application </b></span><br>
 *UserDetails userDetails = (UserDetails) ctx.lookup("com.deltacodex.ee.webapp.remote.UserDetails"); <br>
 *System.out.println(userDetails.getUsername()); <br>
 *<br>
 * <span><b>* Instead of above method,you can use this method to bind initial context</b></span><br>
 * ctx.rebind("AppName", "Delta Codex Client Application");
 */

public class Main {
    public static void main(String[] args) {
        try {
            Properties env = new Properties();
            env.put("org.omg.CORBA.ORBInitialHost", "localhost"); // or 127.0.0.1
            env.put("org.omg.CORBA.ORBInitialPort", "3700");

            InitialContext ctx = new InitialContext(env);
            ctx.rebind("AppName", "Delta Codex Client Application");

        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
