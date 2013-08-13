package jkind;

import java.io.IOException;
import java.util.Arrays;

import jkind.lustre.Program;
import jkind.lustre.parsing.LustreLexer;
import jkind.lustre.parsing.LustreParser;
import jkind.lustre.parsing.LustreParser.ProgramContext;
import jkind.lustre.parsing.LustreToAstVisitor;
import jkind.lustre.parsing.StdoutErrorListener;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class Main {
	final public static String VERSION = "1.4";

	public static void main(String[] args) {
		String availableEntryPoints = "Available entry points: -jkind, -jlustre2kind, -jlustre2excel, -benchmark";
		if (args.length == 0) {
			System.out.println("JKind Suite " + VERSION);
			System.out.println(availableEntryPoints);
			System.exit(0);
		}

		String entryPoint = args[0];
		String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

		switch (entryPoint) {
		case "-jkind":
			JKind.main(subArgs);
			break;

		case "-jlustre2kind":
			JLustre2Kind.main(subArgs);
			break;

		case "-jlustre2excel":
			JLustre2Excel.main(subArgs);
			break;
			
		case "-benchmark":
			Benchmark.main(subArgs);
			break;

		default:
			System.out.println("Unknown entry point: " + entryPoint);
			System.out.println(availableEntryPoints);
			System.exit(-1);
		}
	}

	public static Program parseLustre(String filename) throws IOException, RecognitionException {
		CharStream stream = new ANTLRFileStream(filename);
		LustreLexer lexer = new LustreLexer(stream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LustreParser parser = new LustreParser(tokens);

		ProgramContext program;

		// Due to performance issues in the Antlr 4.0 release, we use a 2-stage
		// parsing approach. https://github.com/antlr/antlr4/issues/192
		parser.removeErrorListeners();
		parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
		parser.setErrorHandler(new BailErrorStrategy());
		try {
			program = parser.program();
		} catch (ParseCancellationException e) {
			tokens.reset();
			parser.addErrorListener(new StdoutErrorListener());
			parser.setErrorHandler(new DefaultErrorStrategy());
			parser.getInterpreter().setPredictionMode(PredictionMode.LL);
			program = parser.program();
		}

		if (parser.getNumberOfSyntaxErrors() > 0) {
			System.exit(-1);
		}

		return new LustreToAstVisitor().program(program);
	}
}
