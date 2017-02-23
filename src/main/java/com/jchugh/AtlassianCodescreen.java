package com.jchugh;

import com.jchugh.service.ChatMessageParser;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import static org.kohsuke.args4j.OptionHandlerFilter.ALL;
import static spark.Spark.port;

/**
 * @author  jchugh
 * The entry point for the app
 */
public class AtlassianCodescreen {

    @Option(name="-p",usage="properties file path",metaVar="/config/codescreen.properties", aliases = "--properties")
    private String propertiesFilePath = this.getClass().getClassLoader().getResource("codescreen.properties").getFile();
    private static  Logger logger = LoggerFactory.getLogger("AtlassianCodescreen");
    private static final String PORT_PROPERTY = "message.parser.port";

    public static void main(String[] args) throws Exception{
        AtlassianCodescreen atlassianCodescreen = new AtlassianCodescreen();
        atlassianCodescreen.parseArgs(args);
        Properties properties = atlassianCodescreen.loadProperties();
        int port = Integer.valueOf(properties.getProperty(PORT_PROPERTY, "4567"));
        port(port);
        ChatMessageParser parser = new ChatMessageParser(properties);
        System.out.println("App Started. Use Verb POST {\"message\" : \"sample @mention\"} @ localhost:" + port + "/api/v1/parse");
        parser.init();
    }

    private Properties loadProperties() throws Exception{
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(this.propertiesFilePath));
        } catch (FileNotFoundException e) {
            System.err.println("Properties file not found, please check path. Current path supplied: " + this.propertiesFilePath);
        }
        return properties;
    }

    private void parseArgs(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch( CmdLineException e ) {
            System.err.println(e.getMessage());
            System.err.println("java -jar AtlassianCodescreen [options...] arguments...");
            parser.printUsage(System.err);
            System.err.println();
            System.err.println("  Example: java -jar AtlassianCodescreen.jar " + parser.printExample(ALL));
        }
    }
}
