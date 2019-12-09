import java.io.PrintStream;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class HuffmanTree2 {
	// New branch's ASCII value. 
	private static final int BRANCH_ASCII = 96;

	// The root of the tree.
	private HuffmanNode root;

	/**
	 * Creates a Huffman Node and builds up a tree
	 */
	public class HuffmanNode implements Comparable<HuffmanNode> {
		// The left child of this tree node
		private HuffmanNode leftChild;
		// The right child of this tree node
		private HuffmanNode rightChild;
		// The frequency of node
		private int nodeFrequency;
		// The ASCII value of character
		private int character;

		/**
		 * Creates a leaf node for each character/frequency.
		 * 
		 * @param character
		 *            the ASCII value of character
		 * @param nodeFrequency
		 *            the frequency of each character in the file
		 */
		public HuffmanNode(int character, int nodeFrequency) {
			this(character, nodeFrequency, null, null);
		}

		/**
		 * Combines two nodes of the smallest frequencies to make a new branch
		 * node then puts back into the queue
		 * 
		 * @param character
		 *            the ASCII value of character
		 * @param nodeFrequency
		 *            the frequency of each character
		 * @param leftChild
		 *            the left hand side node of this
		 * @param rightChild
		 *            the right hand side node of this
		 */
		public HuffmanNode(int character, int nodeFrequency, HuffmanNode leftChild, HuffmanNode rightChild) {
			// Builds a left leaf and a right leaf
			this.leftChild = leftChild;
			this.rightChild = rightChild;

			// Builds a new branch
			this.nodeFrequency = nodeFrequency;
			this.character = character;
		}

		/**
		 * Compares the frequency of the two nodes. 
		 * 
		 * @param node
		 *            The Huffman node
		 * 
		 * @return Returns 0 if two frequencies are equal. Returns -1 if this -
		 *         node < 0. Returns 1 if this - node > 0.
		 */
		@Override
		public int compareTo(HuffmanNode node) {
			return (int) Math.signum(this.nodeFrequency - node.nodeFrequency);
		}
		
		/** 
		 * Checks if node is a leaf. 
		 * 
		 * @return Returns true if leaf. 
		 */
		public boolean isLeaf() {
			return (this.leftChild == null && this.rightChild == null);
		}
	}

	/**
	 * Constructs a Huffman tree using the given array of frequencies where
	 * count[i] is the number of occurrences of the character with ASCII value
	 * i.
	 * 
	 * @param count
	 *            array of frequencies where count[i] is the number of
	 *            occurrences of the character with ASCII value
	 */
	public HuffmanTree2(int[] count) {

		// A priority queue to store leaf nodes;
		Queue<HuffmanNode> priorityQ = new PriorityQueue<HuffmanNode>();

		// The pseudo-eof character (pseudo-eof's ASCII value is 256 ) to
		// signify the end of file
		HuffmanNode pseudoEoF = new HuffmanNode(count.length, 1);

		// Goes through the count list and creates
		// a leaf node for each character/frequency pair.
		// Puts them into a priority queue.
		for (int i = 0; i < count.length; i++) {

			// If the frequency of each character is greater than 0, then puts
			// it
			// into priority queue. Otherwise ignore it.
			if (count[i] > 0) {
				priorityQ.offer(new HuffmanNode(i, count[i]));
			}
		}
		// Adds into priority queue
		priorityQ.offer(pseudoEoF);

		/*
		 * Picks the two nodes with the smallest frequencies from the priority
		 * queue, and creates a new branch node a frequency that is the sum of
		 * the frequencies of the two children. This new node is then put back
		 * into the priority queue.
		 */
		while (priorityQ.size() >= 2) {

			// Gets the first value from the queue and sets as the left child
			HuffmanNode leftChild = priorityQ.remove();

			// Get the second value form the queue and sets as the right child
			HuffmanNode rightChild = priorityQ.remove();

			// Sets the frequency as the sum of left and right child to build a
			// branch
			int sumFrequency = leftChild.nodeFrequency + rightChild.nodeFrequency;

			// Creates a new branch node
			HuffmanNode branchNode = new HuffmanNode(BRANCH_ASCII, sumFrequency, leftChild, rightChild);

			// Adds a new branch node into priorityQ
			priorityQ.offer(branchNode);
		}
		// Gets the root node
		root = priorityQ.remove();
	}

	/**
	 * Constructs a Huffman tree from the Scanner. Assumes the Scanner contains
	 * a tree description in standard format.
	 * 
	 * @param codeInput
	 * 				Scanner object input code. 
	 */
	public HuffmanTree2(Scanner codeInput) {
		// Creates a root;
		root = new HuffmanNode(BRANCH_ASCII, 0);

		while (codeInput.hasNext()) {

			int character = Integer.parseInt(codeInput.nextLine());
			String code = codeInput.nextLine();

			// The parent node
			HuffmanNode parent = root;

			int emptyCharacter = 1;
			int length = code.length();

			// Finds the Node position of the tree.
			// If we find it, place it. If not, keep going.
			for (int i = 0; i < length; i++) {
				// If the length of code is 1, then gives the character
				if (code.length() == 1) {
					emptyCharacter = character;
				}

				if (code.charAt(0) == '0') {
					// Creates leftChild with emptyCharacter if leftChild is
					// null
					if (parent.leftChild == null) {
						parent.leftChild = new HuffmanNode(emptyCharacter, 0);
					}
					// Moves to next node
					parent = parent.leftChild;
					// Removes the first digit of code
					code = code.substring(1);

				} else {
					// Creates rightChild with emptyCharacter if rightChild is
					// null
					if (parent.rightChild == null) {
						parent.rightChild = new HuffmanNode(emptyCharacter, 0);
					}
					// Moves to next one
					parent = parent.rightChild;
					// Removes the first digit of code
					code = code.substring(1);
				}
			}
		}
	}

	/**
	 * Writes the current tree to the given output stream in standard format.
	 * 
	 * @param output
	 * 				PrintStream object to write. 
	 */
	public void write(PrintStream output) {
		this.printSubtreePreOrder(output, this.root, "");

	}

	/**
	 * Prints the leaf nodes. The first line prints the ASCII value of the
	 * character. The second line prints the code (0's and 1's) for the
	 * character with this ASCII value: 0 for each left branch and 1 for each
	 * right branch.
	 * 
	 * @param out
	 *            the PrintStream to print to
	 * @param root
	 *            the root of the subtree
	 * @param code
	 *            the code (0's and 1's) for the character with this ASCII value
	 */
	private void printSubtreePreOrder(PrintStream out, HuffmanNode root, String code) {
		if (root != null) {
			// Prints out node if it is a leaf.
			if (root.isLeaf()) {
				out.println(root.character);
				out.println(code);

				// Adds 0 to each left branch and 1 to each right branch
			} else {
				printSubtreePreOrder(out, root.leftChild, code + "0");
				printSubtreePreOrder(out, root.rightChild, code + "1");
			}
		}
	}

	/**
	 * Reads bits from the given input stream and writes the corresponding
	 * characters to the output. Stops reading when it encounters a character
	 * with value equal to eof.
	 * 
	 * @param input
	 *            input stream to read
	 * @param output
	 *            the PrintStream to print to
	 * @param eof
	 *            The ASCII value is 256
	 */
	public void decode(BitInputStream input, PrintStream output, int eof) {
		HuffmanNode parent = root;

		// Stops at eof character.
		while (parent.character != eof) {
			// Checks if the node is a leaf.
			if (parent.isLeaf()) {
				// Writes the integer code for that character to the output
				// file.
				output.write(parent.character);
				// Goes back to the top of the tree.
				parent = root;

				// Reads code from input.
				// If 0 goes to the left, if 1 goes to the right.
			} else if (input.readBit() == 0) {
				// Sets the left child as the parent.
				parent = parent.leftChild;
			} else {
				// Sets the right child as the parent.
				parent = parent.rightChild;
			}
		}
	}
	/**
	 * Assigns codes for each character of the tree.  
	 * Assumes the array has null values before the method is called.  
	 * Fills in a String for each character in the tree indicating its code.
	 * @param codes
	 */
	public void assign(String[] codes) {
		assign(this.root, codes, "");
		
	}
	
	private void assign(HuffmanNode root, String[] codeArray, String code) {
		if (root != null) {
			// Prints out node if it is a leaf.
			if (root.isLeaf()) {
				codeArray[root.character] = code;

				// Adds 0 to each left branch and 1 to each right branch
			} else {
				assign(root.leftChild, codeArray, code + "0");
				assign(root.rightChild, codeArray, code + "1");
			}
		}
	}
	
	//Writes the current tree to the output stream using the standard bit representation.
	public void writeHeader(BitOutputStream output) {
		writeHeader(output, this.root);
		//write a end of header character = 257
		output.writeBit(257);
	}
	/**
	 * using a preorder traversal.  
	 * For a branch node, we write a 0 indicating that it is a branch. 
	 *  We don’t need to write anything more, because the branch nodes contain no data. 
	 *   For a leaf node, we will write a 1.  
	 *   Then we need to write the ASCII value of the character stored at this leaf.  
	 * @param output
	 * @param root
	 */
	private void writeHeader(BitOutputStream output, HuffmanNode root) {
		if (root != null) {
			// Prints out node if it is a leaf.
			if (root.isLeaf()) {
				output.writeBit(1);
				write9(output, root.character);
				// Adds 0 to each left branch and 1 to each right branch
			} else {
				output.writeBit(0);
			}
			writeHeader(output, root.leftChild);
			writeHeader(output, root.rightChild);
		}
	}
	
	// pre : 0 <= n < 512
	// post: writes a 9-bit representation of n to the given output stream
	private void write9(BitOutputStream output, int n) {
	    for (int i = 0; i < 9; i++) {
	        output.writeBit(n % 2);
	        n /= 2;
	    }
	}
		
}