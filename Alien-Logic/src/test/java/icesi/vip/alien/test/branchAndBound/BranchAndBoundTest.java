package icesi.vip.alien.test.branchAndBound;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import icesi.vip.alien.service.branchAndBound.BranchAndBoundContainer;
import icesi.vip.alien.model.linearProgramming.Model;

@RunWith(SpringRunner.class)
@SpringBootApplication
class BranchAndBoundTest {
	
private Model model;
	
	public final static double SIGMA_VALUE= 0.01;
	
	public void stageOne() {
		String type="MINIMIZE";
		String vars="X1:I,X2:I,X3:I,X4:C,X5:C";
		String objectiveFunction="5,1,1,2,3";
		String constraints="0,1,-5,4,2,>=,-2;5,-1,0,0,1,>=,7;1,1,6,1,0,>=,4";
		try {
			Model m = new Model(type);
			String[] varsS = vars.split(",");
			String[] coef = objectiveFunction.split(",");
			String[] cons = constraints.split(";");
			for (int i = 0; i < varsS.length; i++) {
				m.addVariable(varsS[i].split(":")[0], varsS[i].split(":")[1], Double.parseDouble(coef[i]));
			}
			int varcount = varsS.length;
			for (int i = 0; i < cons.length; i++) {
				double[] c = new double[varcount];
				String[] cons2 = cons[i].split(",");
				for (int j = 0; j < c.length; j++) {
					c[j] = Double.parseDouble(cons2[j]);
				}
				m.addConstraint(c, cons2[cons2.length - 2], Double.parseDouble(cons2[cons2.length - 1]), "C" + i);
			}
			model= m;
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void branchAndBoundTesting() {
		stageOne();
		try {
			BranchAndBoundContainer jar = new BranchAndBoundContainer(model);
			assertTrue(jar.getOptimalSolution().feasible);
			
			System.out.println("NOTHING");
			System.out.println(jar.getOptimalSolution().z);
			System.out.println(jar.getOptimalSolution().variables.get("X1"));
			System.out.println(jar.getOptimalSolution().variables.get("X2"));
			System.out.println(jar.getOptimalSolution().variables.get("X3"));
			System.out.println(jar.getOptimalSolution().variables.get("X4"));
			System.out.println(jar.getOptimalSolution().variables.get("X5"));
			
			double x1= jar.getOptimalSolution().variables.get("X1");
			double result= Math.abs(1.0- x1)/ 1.0;
			assertTrue(result< SIGMA_VALUE);
			double x2= jar.getOptimalSolution().variables.get("X2");
			result= Math.abs(0- x2);
			assertTrue(result< SIGMA_VALUE);
			double x3= jar.getOptimalSolution().variables.get("X3");
			result= Math.abs(1.0- x3)/ 1.0;
			assertTrue(result< SIGMA_VALUE);
			double x4= jar.getOptimalSolution().variables.get("X4");
			result= Math.abs(0- x4);
			assertTrue(result< SIGMA_VALUE);
			double x5= jar.getOptimalSolution().variables.get("X5");
			result= Math.abs(2.0- x5)/ 2.0;
			assertTrue(result< SIGMA_VALUE);
			
			double z= jar.getOptimalSolution().z;
			result= Math.abs(12.0- z)/ 12.0;
			assertTrue(result< SIGMA_VALUE);
			
		} catch (Exception e) {
			fail();
		}
	}
	
}
