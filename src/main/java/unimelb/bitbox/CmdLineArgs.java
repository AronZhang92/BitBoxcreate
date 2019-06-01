package unimelb.bitbox;

import org.kohsuke.args4j.Option;

public class CmdLineArgs {
	@Option(required = true,name = "-c",aliases = "command")
    private static String command;
	
    @Option(required = true,name = "-s",aliases = "server")
    private static String server;

    @Option(required = false,name = "-i",aliases = "identify")
    private static String identify;

    @Option(required = false,name = "-p",aliases = "peer")
    private static String peer;

    public  String getserver() {
	    return server;
    }
    public  String getcommand() {
    	return command;
    }
    public  String getidentify() {
        return identify;
    }
    public  String getpeer() {
        return peer;
    }
}
