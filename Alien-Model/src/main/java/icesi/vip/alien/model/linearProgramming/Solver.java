package icesi.vip.alien.model.linearProgramming;

public interface Solver {

	/**
	 * This method represents the solve of the solution depends of a model
	 * 
	 * @param model model to do the solve
	 * @return Solution
	 */
	public Solution solve(Model model);
}

