package org.dm.fin.ana.app;

import org.dm.fin.ana.ana.Analysis;
import org.dm.fin.ana.data.StatRepo;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.Arrays;
import java.util.List;

public class Stat {
    @Option(name = "-s", aliases = "--start", usage = "start year")
    private Integer start;
    @Option(name = "-l", aliases = "--list", usage = "stock list")
    private String list;
    @Option(name = "-i", aliases = "--input", usage = "input")
    private String input;
    @Option(name = "-o", aliases = "--output", usage = "output")
    private String output;

    public static void main(String[] args) {
        new Stat().execute(args);
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
            case "data" :
                data();
                break;
            case "show":
                show();
                break;
        }
    }

    private void data() {
        StatRepo repo = StatRepo.fromFile(input);
        StatRepo.save(repo);
    }

    private void show() {
        StatRepo repo = StatRepo.load();
        List<String> stocks = List.of(list.split(","));
        Analysis.show(repo, stocks, start, output);
    }
}
