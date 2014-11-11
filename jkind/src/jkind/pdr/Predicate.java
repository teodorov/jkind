package jkind.pdr;

import java.util.List;

import de.uni_freiburg.informatik.ultimate.logic.Script;
import de.uni_freiburg.informatik.ultimate.logic.Term;

public class Predicate {
	private final AbstractedTerm abs;

	public Predicate(Term body, List<Term> variables) {
		this.abs = AbstractedTerm.make(body, variables);
	}

	public Term apply(Script script, List<Term> arguments) {
		return abs.apply(script, arguments);
	}
	
	@Override
	public String toString() {
		return abs.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abs == null) ? 0 : abs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Predicate)) {
			return false;
		}
		Predicate other = (Predicate) obj;
		if (abs == null) {
			if (other.abs != null) {
				return false;
			}
		} else if (!abs.equals(other.abs)) {
			return false;
		}
		return true;
	}
}
