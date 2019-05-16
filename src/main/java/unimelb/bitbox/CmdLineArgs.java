package unimelb.bitbox;

import org.kohsuke.args4j.Option;

public class CmdLineArgs {
    @Option(required = true,name = "-h",aliases = " host")
    private String host;
}
