package icesi.vip.alien.service.branchAndBound;

import java.util.ArrayList;

import icesi.vip.alien.service.branchAndBound.BranchAndBound.ModelNode;


public class Node {

	public ArrayList<Node> children;
	public NodeText text;

	public ArrayList<Node> getChildren() {
		return children;
	}

	public NodeText getText() {
		return text;
	}

	public Node(ModelNode copy) {
		try {

			if (copy != null) {

				children = new ArrayList<Node>();
				if (copy.solution != null) {
					text = new NodeText("Solution at Level " + copy.level, 
							copy.model.toString(),
							copy.solution.toString());
				} else {
					text = new NodeText("Solution at Level " + copy.level,
							copy.model.toString(),
							"UNFEASIBLE BRANCH");
				}

				if (copy.left != null) {
					children.add(new Node(copy.left));

				}

				if (copy.right != null) {
					children.add(new Node(copy.right));
				}
			} else {
				children = new ArrayList<Node>();

				text = new NodeText("UNFEASIBLE BRANCH", "", "");

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
