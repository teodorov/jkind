package jkind.solvers;

import java.io.PrintWriter;
import java.util.List;

import jkind.sexp.Sexp;

public abstract class Solver {
	public abstract void initialize();
	public abstract void stop();
	
	public abstract void send(Sexp sexp);
	public abstract void send(List<Sexp> sexps);
	public abstract SolverResult query(Sexp sexp);
	
	public abstract void push();
	public abstract void pop();
	
	
	/** Debugging */
	
	protected PrintWriter debug;
	
	public void setDebug(PrintWriter debug) {
		this.debug = debug;
	}

	public void debug(String str) {
		if (debug != null) {
			debug.println(str);
		}
	}
}