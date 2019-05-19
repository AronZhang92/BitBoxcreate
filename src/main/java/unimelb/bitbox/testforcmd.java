package unimelb.bitbox;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class testforcmd {
    public static void main(String[] args) {
       CmdLineArgs cmd = new CmdLineArgs();
       CmdLineParser parser = new CmdLineParser(cmd);
        try {
            parser.parseArgument(args);
            System.out.println(cmd.getStirng1());
            System.out.println(cmd.getStirng());
        } catch (CmdLineException e) {
            e.printStackTrace();
        }

    }

}
