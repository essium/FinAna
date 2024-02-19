package org.dm.fin.ana.app;

import org.dm.fin.ana.ana.Analysis;
import org.dm.fin.ana.data.CsiRepo;
import org.dm.fin.ana.model.CsiInfo;
import org.dm.fin.ana.utils.DateUtil;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.util.Arrays;

import static org.dm.fin.ana.utils.DateUtil.FMT_STANDARD;

public class Csi {
    @Option(name = "-s", aliases = "--start", usage = "start date")
    private String start;
    @Option(name = "-i", aliases = "--input", usage = "input csi index")
    private String input;
    @Option(name = "-o", aliases = "--output", usage = "output prefix")
    private String output;
    @Option(name = "-l", aliases = "--list", usage = "industry list")
    private String list;
    @Option(name = "-d", aliases = "--dir", usage = "input directory")
    private String dir;

    public static void main(String[] args) {
        new Csi().execute(args);
    }

    private void execute(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(Arrays.copyOfRange(args, 1, args.length));
        } catch (CmdLineException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(-1);
        }
        switch (args[0]) {
            case "all":
                all();
                break;
            case "spec":
                spec();
                break;
            case "data":
                data();
                break;
            default:
                System.err.println("all supported command: all");
                break;
        }
    }

    private void all() {
        Long startTime = DateUtil.date2Long(start, FMT_STANDARD);
        CsiRepo repo = CsiRepo.load();
        CsiInfo latest = CsiRepo.readXls(new File(input), input.substring(input.length() - 12, input.length() - 4));
        Analysis.Tie4Industry(repo, latest, startTime, output);
    }

    private void spec() {

    }

    private void data() {
        CsiRepo repo = CsiRepo.fromDir(dir);
        CsiRepo.save(repo);
    }
}
