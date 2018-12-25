import java.lang.reflect.Array;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A unit test class for lists that implement IndexedUnsortedList. 
 * This is a set of black box tests that should work for any implementation
 * of this interface.
 * 
 * NOTE: One example test is given for each interface method using a new list to
 * get you started.
 * 
 * @author mvail, mhthomas, amussell (inspiration for using lambdas)
 */
public class ListTester {
	//possible lists that could be tested
	private static enum ListToUse {
		goodList, badList, arrayList, singleLinkedList, doubleLinkedList
	};
	// TODO: THIS IS WHERE YOU CHOOSE WHICH LIST TO TEST
	private final static ListToUse LIST_TO_USE = ListToUse.doubleLinkedList;

	// possible results expected in tests
	private enum Result {
		IndexOutOfBounds, IllegalState, NoSuchElement, 
		ConcurrentModification, UnsupportedOperation, 
		NoException, UnexpectedException,
		True, False, Pass, Fail, 
		MatchingValue,
		ValidString, IndexOutOfBoundsException
	};

	// named elements for use in tests
	private static final Integer ELEMENT_A = new Integer(1);
	private static final Integer ELEMENT_B = new Integer(2);
	private static final Integer ELEMENT_C = new Integer(3);
	private static final Integer ELEMENT_D = new Integer(4);
	private static final Integer ELEMENT_X = new Integer(-1);//element that should appear in no lists
	private static final Integer ELEMENT_Z = new Integer(-2);//element that should appear in no lists

	// determine whether to include ListIterator functionality tests
	private final boolean SUPPORTS_LIST_ITERATOR; //initialized in constructor
	
	//tracking number of tests and test results
	private int passes = 0;
	private int failures = 0;
	private int totalRun = 0;

	private int secTotal = 0;
	private int secPasses = 0;
	private int secFails = 0;

	//control output - modified by command-line args
	private boolean printFailuresOnly = true;
	private boolean showToString = true;
	private boolean printSectionSummaries = true;

	/**
	 * Valid command line args include:
	 *  -a : print results from all tests (default is to print failed tests, only)
	 *  -s : hide Strings from toString() tests
	 *  -m : hide section summaries in output
	 * @param args not used
	 */
	

	public static void main(String[] args) {
		// to avoid every method being static
		ListTester tester = new ListTester(args);
		tester.runTests();
	}

	/** tester constructor
	 * @param args command line args
	 */
	public ListTester(String[] args) {
		for (String arg : args) {
			if (arg.equalsIgnoreCase("-a"))
				printFailuresOnly = false;
			if (arg.equalsIgnoreCase("-s"))
				showToString = false;
			if (arg.equalsIgnoreCase("-m"))
				printSectionSummaries = false;
		}
		switch (LIST_TO_USE) {
		case doubleLinkedList:
			SUPPORTS_LIST_ITERATOR = true;
			break;
		default:
			SUPPORTS_LIST_ITERATOR = false;
			break;
		}
	}
	
	
	/** Print test results in a consistent format
	 * @param testDesc description of the test
	 * @param result indicates if the test passed or failed
	 */
	private void printTest(String testDesc, boolean result) {
		totalRun++;
		if (result) { passes++; }
		else { failures++; }
		if (!result || !printFailuresOnly) {
			System.out.printf("%-46s\t%s\n", testDesc, (result ? "   PASS" : "***FAIL***"));
		}
	}

	/** Print a final summary */
	private void printFinalSummary() {
		String verdict = String.format("\nTotal Tests Run: %d,  Passed: %d (%.1f%%),  Failed: %d\n",
				totalRun, passes, passes*100.0/totalRun, failures);
		String line = "";
		for (int i = 0; i < verdict.length(); i++) {
			line += "-";
		}
		System.out.println(line);
		System.out.println(verdict);
	}

	/** Print a section summary */
	private void printSectionSummary() {
		secTotal = totalRun - secTotal;
		secPasses = passes - secPasses;
		secFails = failures - secFails;
		System.out.printf("\nSection Tests: %d,  Passed: %d,  Failed: %d\n", secTotal, secPasses, secFails);
		secTotal = totalRun; //reset for next section
		secPasses = passes;
		secFails = failures;		
		System.out.printf("Tests Run So Far: %d,  Passed: %d (%.1f%%),  Failed: %d\n",
				totalRun, passes, passes*100.0/totalRun, failures);
	}
	
	/////////////////////
	// XXX runTests()
	/////////////////////

	/** Run tests to confirm required functionality from list constructors and methods */
	private void runTests() {
		//Possible list contents after a scenario has been set up
		Integer[] LIST_A = {ELEMENT_A};
		String STRING_A = "A";
		Integer[] LIST_BA = {ELEMENT_B, ELEMENT_A};
		String STRING_BA = "BA";
		Integer[] LIST_AB = {ELEMENT_A, ELEMENT_B};
		String STRING_AB = "AB";
		Integer[] LIST_B = {ELEMENT_B};
		String STRING_B = "B";
		Integer[] LIST_BC = {ELEMENT_B, ELEMENT_C};
		String STRING_BC = "BC";
		Integer[] LIST_ABC = {ELEMENT_A, ELEMENT_B, ELEMENT_C};
		String STRING_ABC = "ABC";
		Integer[] LIST_AC = {ELEMENT_A, ELEMENT_C};
		String STRING_AC = "AC";
		Integer[] LIST_CA = {ELEMENT_C, ELEMENT_A};
		String STRING_CA = "CA";
		Integer[] LIST_BAC = {ELEMENT_B, ELEMENT_A, ELEMENT_C};
		String STRING_BAC = "BAC";
		Integer[] LIST_BBC = {ELEMENT_B, ELEMENT_B, ELEMENT_C};
		String STRING_BBC = "BBC";
		Integer[] LIST_CBC = {ELEMENT_C, ELEMENT_B, ELEMENT_C};
		String STRING_CBC = "CBC";
		Integer[] LIST_CAB = {ELEMENT_C, ELEMENT_A, ELEMENT_B};
		String STRING_CAB = "CAB";
		Integer[] LIST_DBC = {ELEMENT_D, ELEMENT_B, ELEMENT_C};
		String STRING_DBC = "DBC";
		Integer[] LIST_DC = {ELEMENT_D, ELEMENT_C};
		String STRING_DC = "DC";
		Integer[] LIST_C = {ELEMENT_C};
		String STRING_C = "C";
		Integer[] LIST_ACB = {ELEMENT_A, ELEMENT_C, ELEMENT_B};
		String STRING_ACB = "ACB";
		Integer[] LIST_CB = {ELEMENT_C, ELEMENT_B};
		String STRING_CB = "CB";
		Integer[] LIST_ABD = {ELEMENT_A, ELEMENT_B, ELEMENT_D};
		String STRING_ABD = "ABD";
		Integer[] LIST_ADC = {ELEMENT_A, ELEMENT_D, ELEMENT_C};
		String STRING_ADC = "ADC";
 		
		//newly constructed empty list
		testEmptyList(newList, "newList"); //1

		//1-element to empty list
		testEmptyList(A_removeFirst_emptyList, "A_removeFirst_emptyList"); //2
		
		//empty to 1-element list
		testSingleElementList(emptyList_addToFrontA_A, "emptyList_addToFrontA_A", LIST_A, STRING_A); //3
		
		//1-element to 2-element
		testTwoElementList(A_addToFrontB_BA, "A_addToFrontB_BA", LIST_BA, STRING_BA); //4
		testTwoElementList(A_addToRearB_AB, "A_addToRearB_AB", LIST_AB, STRING_AB); //5
		testTwoElementList(B_addToFrontA_AB, "B_addToFrontA_AB", LIST_AB, STRING_AB); //6
		testTwoElementList(A_addToRearC_AC, "A_addToRearC_AC", LIST_AC, STRING_AC); //7
		
		//1-element to changed 1-element via set()
		testSingleElementList(A_set0B_B, "A_set0B_B", LIST_B, STRING_B); //8
		
		//2-element to 1-element
		testSingleElementList(AB_removeFirst_B, "AB_removeFirst_B", LIST_B, STRING_B); //9
		testSingleElementList(AB_removeLast_A, "AB_removeLast_A", LIST_A, STRING_A); //10
		testSingleElementList(BA_removeLast_B, "BA_removeLast_B", LIST_B, STRING_B); //11
		testSingleElementList(AB_removeLast_A, "AB_removeLast_A", LIST_A, STRING_A); //12
		
		//2-element to 3-element
		testThreeElementList(AB_addToRearC_ABC, "AB_addToRearC_ABC", LIST_ABC, STRING_ABC); //13
		testThreeElementList(AC_addToFrontB_BAC, "AC_addToFrontB_BAC", LIST_BAC, STRING_BAC); //14
		testThreeElementList(BC_addToFrontA_ABC, "BC_addToFrontA_ABC", LIST_ABC, STRING_ABC); //15
		testThreeElementList(AB_addToFrontC_CAB, "AB_addToFrontC_CAB", LIST_CAB, STRING_CAB); //16
		testThreeElementList(AB_addAfterCA_ACB, "AB_addAfterCA_ACB", LIST_ACB, STRING_ACB); //17
		testThreeElementList(AB_addAfterCB_ABC, "AB_addAfterCB_ABC", LIST_ABC, STRING_ABC); //18
		//testThreeElementList(AC_addToFrontB_BAC(), "AC_addToFrontB_BAC()", LIST_BAC, STRING_BAC);
		testThreeElementList(AC_addToRearB_ACB, "AC_addToRearB_ACB", LIST_ACB, STRING_ACB);
		
		//2-element to changed 2-element via set()
		testTwoElementList(AB_set1C_AC, "AB_set1C_AC", LIST_AC, STRING_AC); //18
		
		//3-element to 2-element
		testTwoElementList(ABC_removeFirst_BC, "ABC_removeFirst_BC", LIST_BC, STRING_BC); //19
		testTwoElementList(DBC_removeFirst_BC, "DBC_removeFirst_BC", LIST_BC, STRING_BC); //20
		
		//3-element to changed 3-element via set()
		
		//testTwoElementList(BAC_removeLast_BA, "BAC_removeLast_BA", LIST_BA, STRING_BA); //21
		testTwoElementList(ABC_removeLast_AB, "ABC_removeLast_AB", LIST_AB, STRING_AB);
		testThreeElementList(ABC_set0D_DBC, "ABC_set0D_DBC", LIST_DBC, STRING_DBC); //22
		
		
		
		//Iterator Scenarios
		testEmptyList(A_iterRemoveAfterNextA_emptyList, "A_iterRemoveAfterNextA_emptyList"); //1
		testSingleElementList(AB_iterRemoveAfterNextA_B, "AB_iterRemoveAfterNextA_B", LIST_B, STRING_B); //2
		testSingleElementList(AB_iterRemoveAfterNextB_A, "AB_iterRemoveAfterNextB_A", LIST_A, STRING_A); //3
		testTwoElementList(ABC_iterRemoveAfterNextA_BC, "ABC_iterRemoveAfterNextA_BC", LIST_BC, STRING_BC); //4
		testTwoElementList(ABC_iterRemoveAfterNextB_AC, "ABC_iterRemoveAfterNextB_AC", LIST_AC, STRING_AC); //5
		testTwoElementList(ABC_iterRemoveAfterNextC_AB, "ABC_iterRemoveAfterNextC_AB", LIST_AB, STRING_AB); //6
		
		
		testEmptyList(A_iterRemoveAfterPreviousA_emptyList, "A_iterRemoveAfterPreviousA_emptyList");
		testSingleElementList(A_iterSetBAfterNextA_B, "A_iterSetBAfterNextA_B", LIST_B, STRING_B);
		
		testTwoElementList(AB_iterSetCAfterNextA_CB, "AB_iterSetCAfterNextA_CB", LIST_CB, STRING_CB);
		testThreeElementList(AB_iterAddCAfterNextA_ACB, "AB_iterAddCAfterNextA_ACB", LIST_ACB, STRING_ACB);
		testTwoElementList(A_iterAddBAfterNextA_AB, "A_iterAddBAfterNextA_AB", LIST_AB, STRING_AB);
		testSingleElementList(emptyList_iterAddA_A, "emptyList_iterAddA_A", LIST_A, STRING_A);
		testThreeElementList(ABC_iterSetDAfterNextB_ADC, "ABC_iterSetDAfterNextB_ADC", LIST_ADC, STRING_ADC);
		//testSingleElementList(A_iterSetBAfterNextA_B, "A_iterSetBAfterNextA_B", LIST_B, STRING_B);
		
		testEmptyList(B_iterRemoveAfterNextB_emptyList, "B_iterRemoveAfterNextB_emptyList");
		testSingleElementList(BC_iterRemoveAfterNextB_C, "BC_iterRemoveAfterNextB_C", LIST_C, STRING_C);
		testTwoElementList(A_iterAddBAfterPreviousA_BA, "A_iterAddBAfterPreviousA_BA", LIST_BA, STRING_BA);
		testTwoElementList(ABC_iterRemoveAfterPreviousA_BC, "ABC_iterRemoveAfterPreviousA_BC", LIST_BC, STRING_BC);
		//testSingleElementList(A_iterAddBAfterNextA_AB, "A_iterAddBAfterNextA_AB", LIST_AB, STRING_AB);
		
		
		//Iterator concurrency tests
		test_IterConcurrency();
		if (SUPPORTS_LIST_ITERATOR) {
			test_ListIterConcurrency();
		}

		// report final verdict
		printFinalSummary();
	}

	//////////////////////////////////////
	// XXX SCENARIO BUILDERS
	//////////////////////////////////////

	/**
	 * Returns a IndexedUnsortedList for the "new empty list" scenario.
	 * Scenario: no list -> constructor -> [ ]
	 * 
	 * NOTE: the return type is a basic IndexedUnsortedList reference, so each test method
	 * will need to cast the reference to the specific interface (Indexed or
	 * IndexedUnsortedList) containing the method being tested.
	 *
	 * @return a new, empty IndexedUnsortedList
	 */
	private IndexedUnsortedList<Integer> newList() {
		IndexedUnsortedList<Integer> listToUse;
		switch (LIST_TO_USE) {
		//case goodList:
			//listToUse = new GoodList<Integer>();
			//break;
		//case badList:
			//listToUse = new BadList<Integer>();
			//break;
		//case arrayList:
			//listToUse = new IUArrayList<Integer>();
			//break;
		//case singleLinkedList:
			//listToUse = new IUSingleLinkedList<Integer>();
			//break;
		case doubleLinkedList:
			listToUse = new IUDoubleLinkedList<Integer>();
			break;
		default:
			listToUse = null;
		}
		return listToUse;
	}
	// The following creates a "lambda" reference that allows us to pass a scenario
	//  builder method as an argument. You don't need to worry about how it works -
	//  just make sure each scenario building method has a corresponding Scenario 
	//  assignment statement as in these examples. 

	
	private Scenario<Integer> newList = () -> newList();

	/** Scenario: empty list -> addToFront(A) -> [A] 
	 * @return [A] after addToFront(A)
	 */
	private IndexedUnsortedList<Integer> emptyList_addToFrontA_A() {
		IndexedUnsortedList<Integer> list = newList(); 
		list.addToFront(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> emptyList_addToFrontA_A = () -> emptyList_addToFrontA_A(); //lambda

	/** Scenario: [A] -> addToFront(B) -> [B,A] 
	 * @return [B,A] after addToFront(B)
	 */
	private IndexedUnsortedList<Integer> A_addToFrontB_BA() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A(); 
		list.addToFront(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_addToFrontB_BA = () -> A_addToFrontB_BA();
	
	/** Scenario: [BA] -> removeLast -> [B] 
	 * @return [B] after removeLast
	 */
	private IndexedUnsortedList<Integer> BA_removeLast_B() {
		IndexedUnsortedList<Integer> list = A_addToFrontB_BA(); 
		list.removeLast();
		return list;
	}
	private Scenario<Integer> BA_removeLast_B = () -> BA_removeLast_B();
	
	/** Scenario: [A] -> removeFirst -> [] 
	 * @return [] after removeFirst
	 */
	private IndexedUnsortedList<Integer> A_removeFirst_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.removeFirst();
		return list;
	}
	private Scenario<Integer> A_removeFirst_emptyList = () -> A_removeFirst_emptyList();
	
	/** Scenario: [B] -> addToFront(A) -> [A, B] 
	 * @return [A, B] after addToFront(A)
	 */
	private IndexedUnsortedList<Integer> B_addToFrontA_AB() {
		IndexedUnsortedList<Integer> list = BA_removeLast_B();
		list.addToFront(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> B_addToFrontA_AB = () -> B_addToFrontA_AB();
	
	
	/** Scenario: [A] -> addToRear(B) -> [A,B] 
	 * @return [A,B] after addToRear(B)
	 */
	private IndexedUnsortedList<Integer> A_addToRearB_AB() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.addToRear(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_addToRearB_AB = () -> A_addToRearB_AB();
	
	/** Scenario: [A, B] -> removeFirst -> [B] 
	 * @return [B] after removeFirst
	 */
	private IndexedUnsortedList<Integer> AB_removeFirst_B() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.removeFirst();
		return list;
	}
	private Scenario<Integer> AB_removeFirst_B = () -> AB_removeFirst_B();
	
	/** Scenario: [A, B] -> removeLast -> [A] 
	 * @return [A] after removeLast
	 */
	private IndexedUnsortedList<Integer> AB_removeLast_A() { 
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.removeLast();
		return list;
	}
	private Scenario<Integer> AB_removeLast_A = () -> AB_removeLast_A();
	
	/** Scenario: [A] -> addToRear(C) -> [A, C] 
	 * @return [A, C] after addToRear(C)
	 */
	private IndexedUnsortedList<Integer> A_addToRearC_AC() {
		IndexedUnsortedList<Integer> list = AB_removeLast_A();
		list.addToRear(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> A_addToRearC_AC = () -> A_addToRearC_AC();
	
	/** Scenario: [A] -> set[0](B) -> [B] 
	 * @return [B] after set[0](B)
	 */
	private IndexedUnsortedList<Integer> A_set0B_B() {
		IndexedUnsortedList<Integer> list = AB_removeLast_A();
		list.set(0, ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_set0B_B = () -> A_set0B_B();
	
	/** Scenario: [A, B] -> set[1](c) -> [C,B] 
	 * @return [C,B] after set[1](c)
	 */
	private IndexedUnsortedList<Integer> AB_set1C_AC() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.set(1, ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_set1C_AC = () -> AB_set1C_AC();
	
	/** Scenario: [A, B] -> addToRear(C) -> [A, B, C] 
	 * @return [A, B, C] after addToRear(C)
	 */
	private IndexedUnsortedList<Integer> AB_addToRearC_ABC() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.addToRear(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_addToRearC_ABC = () -> AB_addToRearC_ABC();
	
	
	
	/** Scenario: [A, C] -> addToFront(B) -> [B, A, C] 
	 * @return [B, A, C] after addToFront(B)
	*/
	private IndexedUnsortedList<Integer> AC_addToFrontB_BAC(){
		IndexedUnsortedList<Integer> list = AB_set1C_AC();
		list.addToFront(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> AC_addToFrontB_BAC = () -> AC_addToFrontB_BAC();
	
	
	
	/** Scenario: [B, A, C] -> set[0](B) -> [B, A, C] 
	 * @return [B, A, C] after set[0](B)
	 */
	private IndexedUnsortedList<Integer> ABC_removeLast_AB() {
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		list.removeLast();
		return list;
	}
	private Scenario<Integer> ABC_removeLast_AB = () -> ABC_removeLast_AB();
	
	/** Scenario: [B, B, C] -> removeFirst -> [B, C] 
	 * @return [B, B, C] after removeFirst
	 */
	private IndexedUnsortedList<Integer> ABC_removeFirst_BC() {
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		list.removeFirst();
		return list;
	}
	private Scenario<Integer> ABC_removeFirst_BC = () -> ABC_removeFirst_BC();

	/** Scenario: [B, C] -> addToFrontA -> [A, B, C]
	 * @return [A, B, C] after addToFrontA
	 */
	private IndexedUnsortedList<Integer> BC_addToFrontA_ABC() {
		IndexedUnsortedList<Integer> list = ABC_removeFirst_BC();
		list.addToFront(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> BC_addToFrontA_ABC = () -> BC_addToFrontA_ABC();
	
	/** Scenario: [A, B, C] -> set[0](D) -> [D, B, C]
	 * @return [D, B, C] after set[0](D)
	 */
	private IndexedUnsortedList<Integer> ABC_set0D_DBC() {
		IndexedUnsortedList<Integer> list = BC_addToFrontA_ABC();
		list.set(0, ELEMENT_D);
		return list;
	}
	private Scenario<Integer> ABC_set0D_DBC = () -> ABC_set0D_DBC();
	
	/** Scenario: [D, B, C] -> removeFirst -> [B, C]
	 * @return [B, C] after removeFirst
	 */
	private IndexedUnsortedList<Integer> DBC_removeFirst_BC() {
		IndexedUnsortedList<Integer> list = ABC_set0D_DBC();
		list.removeFirst();
		return list;
	}
	private Scenario<Integer> DBC_removeFirst_BC = () -> DBC_removeFirst_BC();	
	
	/** Scenario: [A, B] -> addToFrontC -> [C, A, B]
	 * @return [C, A, B] after addToFrontC
	 */
	private IndexedUnsortedList<Integer> AB_addToFrontC_CAB() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.addToFront(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_addToFrontC_CAB = () -> AB_addToFrontC_CAB();
	
	/** Scenario: [A, B] -> addAfterCA -> [A, C, B]
	 * @return [A, C, B] after addAfterCA
	 */
	private IndexedUnsortedList<Integer> AB_addAfterCA_ACB() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.addAfter(ELEMENT_C, ELEMENT_A);
		return list;
	}
	private Scenario<Integer> AB_addAfterCA_ACB = () -> AB_addAfterCA_ACB();
	
	/** Scenario: [A, B] -> addAfterCB -> [A, B, C]
	 * @return [A, B, C] after addAfterCB
	 */
	private IndexedUnsortedList<Integer> AB_addAfterCB_ABC() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.addAfter(ELEMENT_C, ELEMENT_B);
		return list;
	}
	private Scenario<Integer> AB_addAfterCB_ABC = () -> AB_addAfterCB_ABC();
	
	/** Scenario: [A, C] -> addToRearB -> [A, C, B]
	 * @return [A, C, B] after addToRearB
	 */
	private IndexedUnsortedList<Integer> AC_addToRearB_ACB() {
		IndexedUnsortedList<Integer> list = A_addToRearC_AC();
		list.addToRear(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> AC_addToRearB_ACB = () -> AC_addToRearB_ACB();
	
	
	//Iterator Tests
	
	/** Scenario: [A] -> iteratorRemoveAfterNextA -> [ ] 
	 * @return [ ] after iteratorRemoveAfterNextA
	 */
	private IndexedUnsortedList<Integer> B_iterRemoveAfterNextB_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A(); 
		Iterator<Integer> it = list.iterator();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> B_iterRemoveAfterNextB_emptyList = () -> B_iterRemoveAfterNextB_emptyList();
	
	/** Scenario: [A, B] -> iterSetCAfterNextA -> [C, B] 
	 * @return [C, B] after iterSetCAfterNextA
	 */	
	private IndexedUnsortedList<Integer> AB_iterSetCAfterNextA_CB() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		ListIterator<Integer> lit = list.listIterator();
		lit.next();
		lit.set(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_iterSetCAfterNextA_CB = () -> AB_iterSetCAfterNextA_CB();
	
	/** Scenario: [A, B, C] -> iterSetDAfterNextB -> [A, D, C] 
	 * @return [A, D, C] after iterSetDAfterNextB
	 */	
	private IndexedUnsortedList<Integer> ABC_iterSetDAfterNextB_ADC(){
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		ListIterator<Integer> lit = list.listIterator(1);
		lit.next();
		lit.set(ELEMENT_D);
		return list;
	}
	private Scenario<Integer> ABC_iterSetDAfterNextB_ADC = () -> ABC_iterSetDAfterNextB_ADC();
	
	/** Scenario: [A] -> iterAddBAfterNextA -> [A, B] 
	 * @return [A, B] after iterAddBAfterNextA
	 */	
	private IndexedUnsortedList<Integer> A_iterAddBAfterNextA_AB() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		ListIterator<Integer> lit = list.listIterator();
		lit.next();
		lit.add(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_iterAddBAfterNextA_AB = () -> A_iterAddBAfterNextA_AB();
	
	/** Scenario: [A] -> iterAddBAfterPreviousA -> [B, A] 
	 * @return [B, A] after iterAddBAfterPreviousA
	 */
	private IndexedUnsortedList<Integer> A_iterAddBAfterPreviousA_BA() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		ListIterator<Integer> lit = list.listIterator(1);
		lit.previous();
		lit.add(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_iterAddBAfterPreviousA_BA = () -> A_iterAddBAfterPreviousA_BA();
	
	/** Scenario: [A] -> iterSetBAfterNextA -> [B] 
	 * @return [B] after iterSetBAfterNextA
	 */
	private IndexedUnsortedList<Integer> A_iterSetBAfterNextA_B(){
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		ListIterator<Integer> lit = list.listIterator();
		lit.next();
		lit.set(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_iterSetBAfterNextA_B = () -> A_iterSetBAfterNextA_B();
	

	/** Scenario: [A] -> iterRemoveAfterNextA -> [] 
	 * @return [] after iterRemoveAfterNextA
	 */
	private IndexedUnsortedList<Integer> A_iterRemoveAfterNextA_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		ListIterator<Integer> lit = list.listIterator();
		lit.next();
		lit.remove();
		return list;
	}
	private Scenario<Integer> A_iterRemoveAfterNextA_emptyList = () -> A_iterRemoveAfterNextA_emptyList();
	
	/** Scenario: [B, C] -> iterRemoveAfterNextB -> [C] 
	 * @return [C] after iterRemoveAfterNextB
	 */
	private IndexedUnsortedList<Integer> BC_iterRemoveAfterNextB_C() {
		IndexedUnsortedList<Integer> list = DBC_removeFirst_BC();
		ListIterator<Integer> lit = list.listIterator();
		lit.next();
		lit.remove();
		return list;
	}
	private Scenario<Integer> BC_iterRemoveAfterNextB_C = () -> BC_iterRemoveAfterNextB_C();
	
	/** Scenario: [A, B, C] -> iterRemoveAfterPreviousA -> [B, C] 
	 * @return [B, C] after iterRemoveAfterPreviousA
	 */
	private IndexedUnsortedList<Integer> ABC_iterRemoveAfterPreviousA_BC() {
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		ListIterator<Integer> lit = list.listIterator(1);
		lit.previous();
		lit.remove();
		return list;
	}
	private Scenario<Integer> ABC_iterRemoveAfterPreviousA_BC = () -> ABC_iterRemoveAfterPreviousA_BC();
	
	/** Scenario: [B, C] -> iterRemoveAfterNextB -> [C] 
	 * @return [C] after iterRemoveAfterNextB
	 */
	private IndexedUnsortedList<Integer> A_iterRemoveAfterPreviousA_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		ListIterator<Integer> lit = list.listIterator();
		lit.next();
		lit.remove();
		return list;
	}
	private Scenario<Integer> A_iterRemoveAfterPreviousA_emptyList = () -> A_iterRemoveAfterPreviousA_emptyList();
	
	/** Scenario: [ ] -> iterAddA -> [A] 
	 * @return [A] after iterAddA
	 */	private IndexedUnsortedList<Integer> emptyList_iterAddA_A() {
		IndexedUnsortedList<Integer> list = A_removeFirst_emptyList();
		ListIterator<Integer> lit = list.listIterator();
		lit.add(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> emptyList_iterAddA_A = () -> emptyList_iterAddA_A();
	
	/** Scenario: [A, B] -> iterAddCAfterNextA -> [A, C, B] 
	 * @return [A, C, B] after iterAddCAfterNextA
	 */
	private IndexedUnsortedList<Integer> AB_iterAddCAfterNextA_ACB() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		ListIterator<Integer> lit = list.listIterator();
		lit.next();
		lit.add(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_iterAddCAfterNextA_ACB = () -> AB_iterAddCAfterNextA_ACB();
	

	/** Scenario: [A, B] -> iteratorRemoveAfterNextA -> [B] 
	 * @return [B] after iteratorRemoveAfterNextA
	 */
	private IndexedUnsortedList<Integer> AB_iterRemoveAfterNextA_B() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		Iterator<Integer> it = list.iterator();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> AB_iterRemoveAfterNextA_B = () -> AB_iterRemoveAfterNextA_B();

	/** Scenario: [A, B] -> iteratorRemoveAfterNextAfterNextB -> [A] 
	 * @return [A] after iteratorRemoveAfterNextAfterNextB
	 */
	private IndexedUnsortedList<Integer> AB_iterRemoveAfterNextB_A() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		Iterator<Integer> it = list.iterator();
		it.next();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> AB_iterRemoveAfterNextB_A = () -> AB_iterRemoveAfterNextB_A();
	
	/** Scenario: [A, B, C] -> iteratorRemoveAfterNextA -> [B, C] 
	 * @return [B, C] after iteratorRemoveAfterNextA
	 */
	private IndexedUnsortedList<Integer> ABC_iterRemoveAfterNextA_BC() {
		IndexedUnsortedList<Integer> list = BC_addToFrontA_ABC(); 
		Iterator<Integer> it = list.iterator();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> ABC_iterRemoveAfterNextA_BC = () -> ABC_iterRemoveAfterNextA_BC();
	
	/** Scenario: [A, B, C] -> iteratorRemoveAfterNextB -> [A, C] 
	 * @return [A, C] after iteratorRemoveAfterNextB
	 */
	private IndexedUnsortedList<Integer> ABC_iterRemoveAfterNextB_AC() {
		IndexedUnsortedList<Integer> list = BC_addToFrontA_ABC(); 
		Iterator<Integer> it = list.iterator();
		it.next();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> ABC_iterRemoveAfterNextB_AC = () -> ABC_iterRemoveAfterNextB_AC();
	
	/** Scenario: [A, B, C] -> ABC_iterRemoveAfterNextC_AB -> [A, B] 
	 * @return [A, B] after ABC_iterRemoveAfterNextC_AB
	 */
	private IndexedUnsortedList<Integer> ABC_iterRemoveAfterNextC_AB() {
		IndexedUnsortedList<Integer> list = BC_addToFrontA_ABC(); 
		Iterator<Integer> it = list.iterator();
		it.next();
		it.next();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> ABC_iterRemoveAfterNextC_AB = () -> ABC_iterRemoveAfterNextC_AB();
	
	
	/////////////////////////////////
	//XXX Tests for 0-element list
	/////////////////////////////////
	
	/** Run all tests on scenarios resulting in an empty list
	 * @param scenario lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 */
	private void testEmptyList(Scenario<Integer> scenario, String scenarioName) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			// IndexedUnsortedList
			printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.True));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 0));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront", testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX", testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1", testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0", testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1", testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddX", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1", testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0", testRemoveIndex(scenario.build(), 0, null, Result.IndexOutOfBounds));
			// Iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.False));
			printTest(scenarioName + "_testIterNext", testIterNext(scenario.build().iterator(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testIterRemove", testIterRemove(scenario.build().iterator(), Result.IllegalState));
			// ListIterator
			if (SUPPORTS_LIST_ITERATOR) {
				 printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
			      printTest(scenarioName + "_testListIterNeg1", testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
			      printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
			      printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.IndexOutOfBounds));
			      printTest(scenarioName + "_testListIterHasNext", testIterHasNext(scenario.build().listIterator(), Result.False));
			      printTest(scenarioName + "_testListIterNext", testIterNext(scenario.build().listIterator(), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIterRemove", testIterRemove(scenario.build().listIterator(), Result.IllegalState));
			      printTest(scenarioName + "_testListIterHasPrevious", testListIterHasPrevious(scenario.build().listIterator(), Result.False));
			      printTest(scenarioName + "_testListIterPrevious", testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIterAdd", testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIterSet", testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
			      printTest(scenarioName + "_testListIterNextIndex", testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0NextIndex", testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIterPreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}
	
	//////////////////////////////////
	//XXX Tests for 1-element list
	//////////////////////////////////
	
	/** Run all tests on scenarios resulting in a single element list
	 * @param scenario lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 * @param contents elements expected in the list after scenario has been set up
	 * @param contentsString contains character labels corresponding to values in contents
	 */
	private void testSingleElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents, String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			// IndexedUnsortedList
			printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(0), testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testContains" + contentsString.charAt(0), testContains(scenario.build(), contents[0], Result.True));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 1));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront", testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(0), testAddAfter(scenario.build(), contents[0], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX", testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1", testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0", testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1", testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2", testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(0), testIndexOf(scenario.build(), contents[0], 0));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1", testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0", testRemoveIndex(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove1", testRemoveIndex(scenario.build(), 1, null, Result.IndexOutOfBounds));
			// Iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));			
			printTest(scenarioName + "_testIterNext", testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testIterRemove", testIterRemove(scenario.build().iterator(), Result.IllegalState));
			
			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 1), Result.False));
			printTest(scenarioName + "_iterNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 1), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
			
			printTest(scenarioName + "_iterNextRemove_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.False));
			printTest(scenarioName + "_iterNextRemove_testIterNext", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextRemove_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
			// ListIterator
			if (SUPPORTS_LIST_ITERATOR) {
				  //Double Linked Testers
				  printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
			      printTest(scenarioName + "_testListIterNeg1", testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
			      printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
			      printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
			      printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.IndexOutOfBounds));
			      
			      printTest(scenarioName + "_testListIterHasNext", testIterHasNext(scenario.build().listIterator(), Result.True));
			      printTest(scenarioName + "_testListIterNext", testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIterNextIndex", testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
			      
			      printTest(scenarioName + "_testListIterHasPrevious", testListIterHasPrevious(scenario.build().listIterator(), Result.False));
			      printTest(scenarioName + "_testListIterPrevious", testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIterPreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
			      
			      printTest(scenarioName + "_testListIterRemove", testIterRemove(scenario.build().listIterator(), Result.IllegalState));
			      printTest(scenarioName + "_testListIterAdd", testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIterSet", testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
			      
			      printTest(scenarioName + "_testListIterNextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
			      printTest(scenarioName + "_testListIterNextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIterNextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIterNextRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 1)), Result.IllegalState));
			      
			      printTest(scenarioName + "_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1)), Result.IllegalState));
			      printTest(scenarioName + "_testListIterNextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIterNextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));
			      
			      printTest(scenarioName + "_testListIter0HasNext", testIterHasNext(scenario.build().listIterator(0), Result.True));
			      printTest(scenarioName + "_testListIter0Next", testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0NextIndex", testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0HasPrevious", testListIterHasPrevious(scenario.build().listIterator(0), Result.False));
			      printTest(scenarioName + "_testListIter0Previous", testListIterPrevious(scenario.build().listIterator(0), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIter0PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0Remove", testIterRemove(scenario.build().listIterator(0), Result.IllegalState));
			      printTest(scenarioName + "_testListIter0Add", testListIterAdd(scenario.build().listIterator(0), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter0Set", testListIterSet(scenario.build().listIterator(0), ELEMENT_X, Result.IllegalState));
			      
			      printTest(scenarioName + "_testListIter0NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter0NextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter0NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter0NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter0NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));
			      
			      printTest(scenarioName + "_testListIter1HasNext", testIterHasNext(scenario.build().listIterator(1), Result.False));
			      printTest(scenarioName + "_testListIter1Next", testIterNext(scenario.build().listIterator(1), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIter1NextIndex", testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1HasPrevious", testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
			      printTest(scenarioName + "_testListIter1Previous", testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1Remove", testIterRemove(scenario.build().listIterator(1), Result.IllegalState));
			      printTest(scenarioName + "_testListIter1Add", testListIterAdd(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter1Set", testListIterSet(scenario.build().listIterator(1), ELEMENT_X, Result.IllegalState));
			      
			      printTest(scenarioName + "_testListIter1PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter1PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter1PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter1PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter1PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter1PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	/////////////////////////////////
	//XXX Tests for 2-element list
	/////////////////////////////////
	
	/** Run all tests on scenarios resulting in a two-element list
	 * @param scenario lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 * @param contents elements expected in the list after scenario has been set up
	 * @param contentsString contains character labels corresponding to values in contents 
	 */
	private void testTwoElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents, String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			//TODO: tests for scenarios ending in a 2-element list
			printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(0), testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testContains" + contentsString.charAt(0), testContains(scenario.build(), contents[0], Result.True));
			printTest(scenarioName + "_testContains" + contentsString.charAt(1), testContains(scenario.build(), contents[1], Result.True));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 2));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront", testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(0), testAddAfter(scenario.build(), contents[0], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(1), testAddAfter(scenario.build(), contents[1], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX", testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1", testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0", testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1", testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2", testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex3", testAddAtIndex(scenario.build(), 3, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 2, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testGet2", testGet(scenario.build(), 2, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(0), testIndexOf(scenario.build(), contents[0], 0));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(1), testIndexOf(scenario.build(), contents[1], 1));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			
			printTest(scenarioName + "_2_testRemoveNeg1", testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_2_testRemove0", testRemoveIndex(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_2_testRemove1", testRemoveIndex(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_2_testRemove2", testRemoveIndex(scenario.build(), 2, null, Result.IndexOutOfBounds));
			// Iterator
			
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));
			printTest(scenarioName + "_testIterNext", testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testIterRemove", testIterRemove(scenario.build().iterator(), Result.IllegalState));
			
			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 1), Result.True));
			printTest(scenarioName + "_iterNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 1), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
			
			printTest(scenarioName + "_iterNextRemove_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.True));
			printTest(scenarioName + "_iterNextRemove_testIterNext", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNextRemove_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
			
			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 2), Result.False));
			printTest(scenarioName + "_iterNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 2), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 2), Result.NoException));
			
			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.False));
			printTest(scenarioName + "_iterNext_testIterNext",  testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), contents[1], Result.NoSuchElement));
			printTest(scenarioName + "_iterNext_testIterRemove",testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.IllegalState));
			
			Iterator<Integer> it = iterAfterRemove(iterAfterNext(scenario.build(), 1));
			it.next();
			printTest(scenarioName + "_iterNextRemoveNext_testIterHasNext", testIterHasNext(it, Result.False));
			printTest(scenarioName + "_iterNextRemoveNext_testIterNext", testIterNext(it, contents[1], Result.NoSuchElement));
			printTest(scenarioName + "_iterNextRemoveNext_testIterNext", testIterRemove(it, Result.NoException));
			
			if (SUPPORTS_LIST_ITERATOR) {
				  //Double Linked List Tests
				  printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
			      printTest(scenarioName + "_testListIterNeg1", testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
			      printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
			      printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
			      printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.NoException));
			      printTest(scenarioName + "_testListIter3", testListIter(scenario.build(), 3, Result.IndexOutOfBounds));
			      
			      printTest(scenarioName + "_testListIterHasNext", testIterHasNext(scenario.build().listIterator(), Result.True));
			      printTest(scenarioName + "_testListIterNext", testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIterNextIndex", testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
			      
			      printTest(scenarioName + "_testListIterHasPrevious", testListIterHasPrevious(scenario.build().listIterator(), Result.False));
			      printTest(scenarioName + "_testListIterPrevious", testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIterPreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
			      
			      printTest(scenarioName + "_testListIterRemove", testIterRemove(scenario.build().listIterator(), Result.IllegalState));
			      printTest(scenarioName + "_testListIterAdd", testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIterSet", testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));

			      printTest(scenarioName + "_testListIterNextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
			      printTest(scenarioName + "_2_testListIterNextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(), 2), Result.NoException)); 
			      printTest(scenarioName + "_testListIterNextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_2_testListIterNextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(), 2), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIterNextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_testListIterNextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(), 2), ELEMENT_X, Result.NoException));
			      
			      printTest(scenarioName + "_testListIterNextRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 1)), Result.IllegalState));
			      printTest(scenarioName + "_2_testListIterNextRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 2)), Result.IllegalState));

			      printTest(scenarioName + "_1_1_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), Result.NoException));
			      printTest(scenarioName + "_2_1_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 1), Result.NoException));
			      printTest(scenarioName + "_2_2_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 2), Result.NoException));
			      
			      printTest(scenarioName + "_1_1_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1)), Result.IllegalState));
			      printTest(scenarioName + "_2_1_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 1)), Result.IllegalState));
			      printTest(scenarioName + "_2_2_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 2)), Result.IllegalState));

			    
			      printTest(scenarioName + "_1_1_testListIterNextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_2_1_testListIterNextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_2_testListIterNextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 2), ELEMENT_X, Result.NoException));			
			    
			      printTest(scenarioName + "_1_1_testListIterNextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_1_testListIterNextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 1), ELEMENT_X, Result.NoException));  
			      printTest(scenarioName + "_2_2_testListIterNextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 2), ELEMENT_X, Result.NoException));
			      
			      printTest(scenarioName + "_testListIter0HasNext", testIterHasNext(scenario.build().listIterator(0), Result.True));
			      printTest(scenarioName + "_testListIter0Next", testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0NextIndex", testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
			      
			      printTest(scenarioName + "_testListIter0HasPrevious", testListIterHasPrevious(scenario.build().listIterator(0), Result.False));
			      printTest(scenarioName + "_testListIter0Previous", testListIterPrevious(scenario.build().listIterator(0), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIter0PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
			      
			      printTest(scenarioName + "_testListIter0Remove", testIterRemove(scenario.build().listIterator(0), Result.IllegalState));
			      printTest(scenarioName + "_testListIter0Add", testListIterAdd(scenario.build().listIterator(0), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter0Set", testListIterSet(scenario.build().listIterator(0), ELEMENT_X, Result.IllegalState));
			      
			      printTest(scenarioName + "_1_testListIter0NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 1), Result.NoException));
			      printTest(scenarioName + "_2_testListIter0NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 2), Result.NoException));
			      printTest(scenarioName + "_1_testListIter0NextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_testListIter0NextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(0), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter0NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_testListIter0NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(0), 2), ELEMENT_X, Result.NoException));
			      
			      printTest(scenarioName + "_1_1_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), Result.NoException));
			      printTest(scenarioName + "_2_1_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 2), 1), Result.NoException));
			      printTest(scenarioName + "_2_2_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 2), 2), Result.NoException));

			      printTest(scenarioName + "_1_1_testListIter0NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_2_1_testListIter0NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 2), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_2_2_testListIter0NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 2), 2), ELEMENT_X, Result.NoException));			

			      printTest(scenarioName + "_1_1_testListIter0NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_1testListIter0NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 2), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_2testListIter0NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 2), 2), ELEMENT_X, Result.NoException));

			      //~ NEW
			      printTest(scenarioName + "_testListIter1HasNext", testIterHasNext(scenario.build().listIterator(1), Result.True));
			      printTest(scenarioName + "_testListIter1Next", testIterNext(scenario.build().listIterator(1), contents[1], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1NextIndex", testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1HasPrevious", testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
			      printTest(scenarioName + "_testListIter1Previous", testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1Remove", testIterRemove(scenario.build().listIterator(1), Result.IllegalState));
			      printTest(scenarioName + "_testListIter1Add", testListIterAdd(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter1Set", testListIterSet(scenario.build().listIterator(1), ELEMENT_X, Result.IllegalState));
			      
			      printTest(scenarioName + "_1_1_testListIter1NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(1), 1), Result.NoException));
			      printTest(scenarioName + "_1_1_testListIter1NextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_1_1_testListIter1NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));

			      printTest(scenarioName + "_1_1_testListIter1NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 1), 2), Result.NoException));			      
			      printTest(scenarioName + "_1_2_testListIter1NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 1), 2), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_1_2_testListIter1NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 1), 2), ELEMENT_X, Result.NoException));
			      
			      //2
			      printTest(scenarioName + "_testListIter2HasNext", testIterHasNext(scenario.build().listIterator(2), Result.False));
			      printTest(scenarioName + "_testListIter2Next", testIterNext(scenario.build().listIterator(2), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIter2NextIndex", testListIterNextIndex(scenario.build().listIterator(2), 2, Result.MatchingValue)); //no??
			      
			      printTest(scenarioName + "_testListIter2HasPrevious", testListIterHasPrevious(scenario.build().listIterator(2), Result.True));
			      printTest(scenarioName + "_testListIter2Previous", testListIterPrevious(scenario.build().listIterator(2), contents[1], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter2PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(2), 1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter2Remove", testIterRemove(scenario.build().listIterator(2), Result.IllegalState));
			      printTest(scenarioName + "_testListIter2Add", testListIterAdd(scenario.build().listIterator(2), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter2Set", testListIterSet(scenario.build().listIterator(2), ELEMENT_X, Result.IllegalState));
			      
			      printTest(scenarioName + "_testListIter2PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(2), 1), Result.NoException));
			      printTest(scenarioName + "_2_testListIter2PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(2), 2), Result.NoException));
			      printTest(scenarioName + "_testListIter2PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_2_testListIter2PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(2), 2), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter2PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_testListIter2PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(2), 2), ELEMENT_X, Result.NoException));

			      printTest(scenarioName + "_2_1_1_testListIter2PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), Result.NoException));
			      printTest(scenarioName + "_2_2_1_testListIter2PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 2), 1), Result.NoException));
			      printTest(scenarioName + "_2_2_2_testListIter2PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 2), 2), Result.NoException));
			      
			      printTest(scenarioName + "_2_1_1_testListIter2PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_2_1_testListIter2PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 2), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_2_2_testListIter2PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 2), 2), ELEMENT_X, Result.NoException));

			      printTest(scenarioName + "_2_1_1_testListIter2PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_2_1_testListIter2PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 2), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_2_2testListIter2PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 2), 2), ELEMENT_X, Result.NoException));
			}
		
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	//////////////////////////////////
	//XXX Tests for 3-element list
	//////////////////////////////////
	
	/** Run all tests on scenarios resulting in a three-element list
	 * @param scenario lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 * @param contents elements expected in the list after scenario has been set up
	 * @param contentsString contains character labels corresponding to values in contents 
	 */
	private void testThreeElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents, String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(0), testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(1), testRemoveElement(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(2), testRemoveElement(scenario.build(), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testContains" + contentsString.charAt(0), testContains(scenario.build(), contents[0], Result.True));
			printTest(scenarioName + "_testContains" + contentsString.charAt(1), testContains(scenario.build(), contents[1], Result.True));
			printTest(scenarioName + "_testContains" + contentsString.charAt(2), testContains(scenario.build(), contents[2], Result.True));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 3));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront", testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(0), testAddAfter(scenario.build(), contents[0], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(1), testAddAfter(scenario.build(), contents[1], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(2), testAddAfter(scenario.build(), contents[2], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX", testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1", testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0", testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1", testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2", testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2", testAddAtIndex(scenario.build(), 3, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex3", testAddAtIndex(scenario.build(), 4, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet2", testSet(scenario.build(), 2, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet3", testSet(scenario.build(), 3, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testGet2", testGet(scenario.build(), 2, contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testGet3", testGet(scenario.build(), 3, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(0), testIndexOf(scenario.build(), contents[0], 0));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(1), testIndexOf(scenario.build(), contents[1], 1));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(2), testIndexOf(scenario.build(), contents[2], 2));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1", testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0", testRemoveIndex(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove1", testRemoveIndex(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemove2", testRemoveIndex(scenario.build(), 2, contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testRemove3", testRemoveIndex(scenario.build(), 3, null, Result.IndexOutOfBounds));
			// Iterator
			
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));
			printTest(scenarioName + "_testIterNext", testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testIterRemove", testIterRemove(scenario.build().iterator(), Result.IllegalState));
			
			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 1), Result.True));
			printTest(scenarioName + "_iterNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 1), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
			
			printTest(scenarioName + "1_iterNextRemove_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.True));
			printTest(scenarioName + "1_iterNextRemove_testIterNext", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), contents[1], Result.MatchingValue));
			printTest(scenarioName + "1_iterNextRemove_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
			
			Iterator<Integer> it = iterAfterRemove(iterAfterNext(scenario.build(), 1));
			it.next();
			printTest(scenarioName + "_iterNextRemoveNext_testIterHasNext", testIterHasNext(it, Result.True));
			printTest(scenarioName + "_iterNextRemoveNext_testIterNext", testIterNext(it, contents[2], Result.MatchingValue));
			printTest(scenarioName + "_iterNextRemoveNext_testIterNext", testIterRemove(it, Result.NoException));

			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 2), Result.True));
			printTest(scenarioName + "_iterNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 2), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_iterNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 2), Result.NoException));
						
			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.True));
			printTest(scenarioName + "_iterNext_testIterNext",  testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_iterNext_testIterRemove",testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.IllegalState));
			
			Iterator<Integer> it1 = iterAfterRemove(iterAfterNext(scenario.build(), 2));
			it1.next();
			printTest(scenarioName + "_iterNextRemoveNext_testIterHasNext", testIterHasNext(it1, Result.False));
			printTest(scenarioName + "_iterNextRemoveNext_testIterNext", testIterNext(it1, contents[2], Result.NoSuchElement));
			printTest(scenarioName + "_iterNextRemoveNext_testIterNext", testIterRemove(it1, Result.NoException));
 
			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 3), Result.False));
			printTest(scenarioName + "_iterNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 3), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 2), Result.NoException));
			
			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 3)), Result.False));
			printTest(scenarioName + "_iterNext_testIterNext",  testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 3)), contents[2], Result.NoSuchElement));
			printTest(scenarioName + "_iterNext_testIterRemove",testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 3)), Result.IllegalState));
			
			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 3)), Result.False));
			printTest(scenarioName + "_iterNext_testIterNext",  testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 3)), contents[2], Result.NoSuchElement));
			printTest(scenarioName + "_iterNext_testIterRemove",testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.IllegalState));

			if (SUPPORTS_LIST_ITERATOR) {
				  //Double Linked List Tests
				  printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
			      printTest(scenarioName + "_testListIterNeg1", testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
			      printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
			      printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
			      printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.NoException));
			      printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 3, Result.NoException));
			      printTest(scenarioName + "_testListIter3", testListIter(scenario.build(), 4, Result.IndexOutOfBounds));
			      
			      printTest(scenarioName + "_testListIterHasNext", testIterHasNext(scenario.build().listIterator(), Result.True));
			      printTest(scenarioName + "_testListIterNext", testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIterNextIndex", testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
			      
			      printTest(scenarioName + "_testListIterHasPrevious", testListIterHasPrevious(scenario.build().listIterator(), Result.False));
			      printTest(scenarioName + "_testListIterPrevious", testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIterPreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
			      
			      printTest(scenarioName + "_testListIterRemove", testIterRemove(scenario.build().listIterator(), Result.IllegalState));
			      printTest(scenarioName + "_testListIterAdd", testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIterSet", testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));

			      printTest(scenarioName + "_testListIterNextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
			      printTest(scenarioName + "_2_testListIterNextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(), 2), Result.NoException));
			      printTest(scenarioName + "_3_testListIterNextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(), 3), Result.NoException));
			      
			      printTest(scenarioName + "_testListIterNextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_2_testListIterNextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(), 2), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_2_testListIterNextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(), 3), ELEMENT_X, Result.NoException));			

			      printTest(scenarioName + "_testListIterNextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_testListIterNextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_testListIterNextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(), 3), ELEMENT_X, Result.NoException));
			      
			      printTest(scenarioName + "_testListIterNextRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 1)), Result.IllegalState));
			      printTest(scenarioName + "_2_testListIterNextRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 2)), Result.IllegalState));
			      printTest(scenarioName + "_3_testListIterNextRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 3)), Result.IllegalState));

			      printTest(scenarioName + "_1_1_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), Result.NoException));
			      printTest(scenarioName + "_2_1_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 1), Result.NoException));
			      printTest(scenarioName + "_2_2_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 2), Result.NoException));
			      printTest(scenarioName + "_3_1_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 3), 1), Result.NoException));
			      printTest(scenarioName + "_3_2_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 3), 2), Result.NoException));
			      printTest(scenarioName + "_3_3_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 3), 3), Result.NoException));
			      
			      printTest(scenarioName + "_1_1_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1)), Result.IllegalState));
			      printTest(scenarioName + "_2_1_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 1)), Result.IllegalState));
			      printTest(scenarioName + "_2_2_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 2)), Result.IllegalState));
			      printTest(scenarioName + "_3_1_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 3), 1)), Result.IllegalState));
			      printTest(scenarioName + "_3_2_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 3), 2)), Result.IllegalState));
			      printTest(scenarioName + "_3_3_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 3), 3)), Result.IllegalState));

			      printTest(scenarioName + "_1_1_testListIterNextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_2_1_testListIterNextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_2_testListIterNextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_1_testListIterNextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 3), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_2_testListIterNextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 3), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_3_testListIterNextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 3), 3), ELEMENT_X, Result.NoException));
			    
			      printTest(scenarioName + "_1_1_testListIterNextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_1_testListIterNextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 1), ELEMENT_X, Result.NoException));  
			      printTest(scenarioName + "_2_2_testListIterNextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_1_testListIterNextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 3), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_2_testListIterNextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 3), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_3_testListIterNextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 3), 3), ELEMENT_X, Result.NoException));
			      
			      printTest(scenarioName + "_testListIter0HasNext", testIterHasNext(scenario.build().listIterator(0), Result.True));
			      printTest(scenarioName + "_testListIter0Next", testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0NextIndex", testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
			      
			      printTest(scenarioName + "_testListIter0HasPrevious", testListIterHasPrevious(scenario.build().listIterator(0), Result.False));
			      printTest(scenarioName + "_testListIter0Previous", testListIterPrevious(scenario.build().listIterator(0), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIter0PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
			      
			      printTest(scenarioName + "_testListIter0Remove", testIterRemove(scenario.build().listIterator(0), Result.IllegalState));
			      printTest(scenarioName + "_testListIter0Add", testListIterAdd(scenario.build().listIterator(0), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter0Set", testListIterSet(scenario.build().listIterator(0), ELEMENT_X, Result.IllegalState));
			      
			      printTest(scenarioName + "_1_testListIter0NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 1), Result.NoException));
			      printTest(scenarioName + "_2_testListIter0NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 2), Result.NoException));
			      printTest(scenarioName + "_3_testListIter0NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 3), Result.NoException));
			      
			      printTest(scenarioName + "_1_testListIter0NextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_testListIter0NextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(0), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_testListIter0NextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(0), 3), ELEMENT_X, Result.NoException));

			      printTest(scenarioName + "_testListIter0NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_testListIter0NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(0), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_testListIter0NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(0), 3), ELEMENT_X, Result.NoException));

			      printTest(scenarioName + "_1_1_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), Result.NoException));
			      printTest(scenarioName + "_2_1_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 2), 1), Result.NoException));
			      printTest(scenarioName + "_2_2_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 2), 2), Result.NoException));
			      printTest(scenarioName + "_3_1_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 3), 1), Result.NoException));
			      printTest(scenarioName + "_3_2_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 3), 2), Result.NoException));
			      printTest(scenarioName + "_3_3_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 3), 3), Result.NoException));

			      printTest(scenarioName + "_1_1_testListIter0NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_2_1_testListIter0NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 2), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_2_2_testListIter0NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 2), 2), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_3_1_testListIter0NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 3), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_3_2_testListIter0NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 3), 2), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_3_3_testListIter0NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 3), 3), ELEMENT_X, Result.NoException));			

			      printTest(scenarioName + "_1_1_testListIter0NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_1_testListIter0NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 2), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_2_testListIter0NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 2), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_1_testListIter0NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 3), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_2_testListIter0NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 3), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_3_testListIter0NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 3), 3), ELEMENT_X, Result.NoException));
	
			      //~ NEW
			      printTest(scenarioName + "_testListIter1HasNext", testIterHasNext(scenario.build().listIterator(1), Result.True));
			      printTest(scenarioName + "_testListIter1Next", testIterNext(scenario.build().listIterator(1), contents[1], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1NextIndex", testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1HasPrevious", testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
			      printTest(scenarioName + "_testListIter1Previous", testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1Remove", testIterRemove(scenario.build().listIterator(1), Result.IllegalState));
			      printTest(scenarioName + "_testListIter1Add", testListIterAdd(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter1Set", testListIterSet(scenario.build().listIterator(1), ELEMENT_X, Result.IllegalState));
			      
			      printTest(scenarioName + "_1_1_testListIter1NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(1), 1), Result.NoException));
			      printTest(scenarioName + "_1_2_testListIter1NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(1), 2), Result.NoException));

			      printTest(scenarioName + "_1_1_testListIter1NextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_1_2_testListIter1NextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(1), 2), ELEMENT_X, Result.NoException));			

			      printTest(scenarioName + "_1_1_testListIter1NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_1_2_testListIter1NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(1), 2), ELEMENT_X, Result.NoException));

			      printTest(scenarioName + "_1_1_1_testListIter1NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 1), 1), Result.NoException));
			      printTest(scenarioName + "_1_1_2_testListIter1NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 1), 2), Result.NoException));
			      printTest(scenarioName + "_1_2_1_testListIter1NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 2), 1), Result.NoException));
			      printTest(scenarioName + "_1_2_2_testListIter1NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 2), 2), Result.NoException));
			      printTest(scenarioName + "_1_2_3_testListIter1NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 2), 3), Result.NoException));

			      printTest(scenarioName + "_1_1_1_testListIter1NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_1_1_2_testListIter1NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 1), 2), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_1_2_1_testListIter1NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 2), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_1_2_2_testListIter1NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 2), 2), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_1_2_3_testListIter1NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 2), 3), ELEMENT_X, Result.NoException));			

			      printTest(scenarioName + "_1_1_1_testListIter1NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_1_1_2_testListIter1NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 1), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_1_2_1_testListIter1NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 2), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_1_2_2_testListIter1NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 2), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_1_2_3_testListIter1NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(1), 2), 3), ELEMENT_X, Result.NoException));
			      
			      //Iterator Position: 2
			      printTest(scenarioName + "_testListIter2HasNext", testIterHasNext(scenario.build().listIterator(2), Result.True));
			      printTest(scenarioName + "_testListIter2Next", testIterNext(scenario.build().listIterator(2), contents[2], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter2NextIndex", testListIterNextIndex(scenario.build().listIterator(2), 2, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter2HasPrevious", testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
			      printTest(scenarioName + "_testListIter2Previous", testListIterPrevious(scenario.build().listIterator(2), contents[1], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter2PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(2), 1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter2Remove", testIterRemove(scenario.build().listIterator(2), Result.IllegalState));
			      printTest(scenarioName + "_testListIter2Add", testListIterAdd(scenario.build().listIterator(2), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter2Set", testListIterSet(scenario.build().listIterator(2), ELEMENT_X, Result.IllegalState));
			      
			      printTest(scenarioName + "_2_1_testListIter1NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(2), 1), Result.NoException));
			      printTest(scenarioName + "_2_1_testListIter1NextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_1_testListIter1NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));

			      printTest(scenarioName + "_2_1_1_testListIter1NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(2), 1), 1), Result.NoException));
			      printTest(scenarioName + "_2_1_2_testListIter1NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(2), 1), 2), Result.NoException));
			      printTest(scenarioName + "_2_1_3_testListIter1NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(2), 1), 3), Result.NoException));

			      printTest(scenarioName + "_2_1_1_testListIter1NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(2), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_2_1_2_testListIter1NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(2), 1), 2), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_2_1_3_testListIter1NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(2), 1), 3), ELEMENT_X, Result.NoException));			
			      
			      printTest(scenarioName + "_2_1_1_testListIter1NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(2), 1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_1_2_testListIter1NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(2), 1), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_1_3_testListIter1NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(2), 1), 3), ELEMENT_X, Result.NoException));
			      
			      //Iterator position: 3
			      printTest(scenarioName + "_testListIter3HasNext", testIterHasNext(scenario.build().listIterator(3), Result.False));
			      printTest(scenarioName + "_testListIter3Next", testIterNext(scenario.build().listIterator(3), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIter3NextIndex", testListIterNextIndex(scenario.build().listIterator(3), 3, Result.MatchingValue));
			      
			      printTest(scenarioName + "_testListIter3HasPrevious", testListIterHasPrevious(scenario.build().listIterator(3), Result.True));
			      printTest(scenarioName + "_testListIter3Previous", testListIterPrevious(scenario.build().listIterator(3), contents[2], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter3PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(3), 2, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter3Remove", testIterRemove(scenario.build().listIterator(3), Result.IllegalState));
			      printTest(scenarioName + "_testListIter3Add", testListIterAdd(scenario.build().listIterator(3), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter3Set", testListIterSet(scenario.build().listIterator(3), ELEMENT_X, Result.IllegalState));
			      
			      printTest(scenarioName + "_1_testListIter3PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(3), 1), Result.NoException));
			      printTest(scenarioName + "_2_testListIter3PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(3), 2), Result.NoException));
			      printTest(scenarioName + "_3_testListIter3PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(3), 3), Result.NoException));

			      printTest(scenarioName + "_1_testListIter2PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(3), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_2_testListIter2PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(3), 2), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_3_testListIter2PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(3), 3), ELEMENT_X, Result.NoException));			

			      printTest(scenarioName + "_1_testListIter2PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(3), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_2_testListIter2PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(3), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_testListIter2PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(3), 3), ELEMENT_X, Result.NoException));

			      printTest(scenarioName + "_3_1_1_testListIter2PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 1), 1), Result.NoException));
			      printTest(scenarioName + "_3_2_1_testListIter2PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 2), 1), Result.NoException));
			      printTest(scenarioName + "_3_2_2_testListIter2PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 2), 2), Result.NoException));
			      printTest(scenarioName + "_3_3_1_testListIter2PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 3), 1), Result.NoException));
			      printTest(scenarioName + "_3_3_2_testListIter2PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 3), 2), Result.NoException));
			      printTest(scenarioName + "_3_3_3_testListIter2PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 3), 3), Result.NoException));
			      
			      printTest(scenarioName + "_3_1_1_testListIter2PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_2_1_testListIter2PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 2), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_2_2_testListIter2PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 2), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_3_1_testListIter2PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 3), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_3_2_testListIter2PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 3), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_3_3_testListIter2PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 3), 3), ELEMENT_X, Result.NoException));

			      printTest(scenarioName + "_3_1_1_testListIter2PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_2_1_testListIter2PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 2), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_2_2_testListIter2PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 2), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_3_1_testListIter2PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 3), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_3_2_testListIter2PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 3), 2), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_3_3_3_testListIter2PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 3), 3), ELEMENT_X, Result.NoException));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	////////////////////////////
	// XXX LIST TEST METHODS
	////////////////////////////

	/** Runs removeFirst() method on given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveFirst(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.removeFirst();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveFirst", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs removeLast() method on given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveLast(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.removeLast();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveLast", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs removeLast() method on given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element element to remove
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveElement(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.remove(element);
			if (retVal.equals(element)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveElement", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs first() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testFirst(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.first();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testFirst", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs last() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testLast(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.last();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testLast", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs contains() method on a given list and element and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testContains(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			if (list.contains(element)) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testContains", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs isEmpty() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIsEmpty(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			if (list.isEmpty()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIsEmpty", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs size() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedSize
	 * @return test success
	 */
	private boolean testSize(IndexedUnsortedList<Integer> list, int expectedSize) {
		try {
			return (list.size() == expectedSize);
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testSize", e.toString());
			e.printStackTrace();
			return false;
		}
	}

	/** Runs toString() method on given list and attempts to confirm non-default or empty String
	 * difficult to test - just confirm that default address output has been overridden
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testToString(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			String str = list.toString().trim();
			if (showToString) {
				System.out.println("toString() output: " + str);
			}
			if (str.length() < (list.size() + list.size()/2 + 2)) { //elements + commas + '[' + ']'
				result = Result.Fail;
			} else {
				char lastChar = str.charAt(str.length() - 1);
				char firstChar = str.charAt(0);
				if (firstChar != '[' || lastChar != ']') {
					result = Result.Fail;
				} else if (str.contains("@")
						&& !str.contains(" ")
						&& Character.isLetter(str.charAt(0))
						&& (Character.isDigit(lastChar) || (lastChar >= 'a' && lastChar <= 'f'))) {
					result = Result.Fail; // looks like default toString()
				} else {
					result = Result.ValidString;
				}
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testToString", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs addToFront() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddToFront(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.addToFront(element);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddToFront",  e.toString());
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs addToRear() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddToRear(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.addToRear(element);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddToRear", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs addAfter() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param target
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddAfter(IndexedUnsortedList<Integer> list, Integer target, Integer element, Result expectedResult) {
		Result result;
		try {
			list.addAfter(element, target);
			result = Result.NoException;
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAfter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs add(int, T) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddAtIndex(IndexedUnsortedList<Integer> list, int index, Integer element, Result expectedResult) {
		Result result;
		try {
			list.add(index, element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAtIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs add(T) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAdd(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.add(element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAtIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs set(int, T) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testSet(IndexedUnsortedList<Integer> list, int index, Integer element, Result expectedResult) {
		Result result;
		try {
			list.set(index, element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testSet", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs get() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index
	 * @param expectedElement
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testGet(IndexedUnsortedList<Integer> list, int index, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.get(index);
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testGet", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs remove(index) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index
	 * @param expectedElement
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveIndex(IndexedUnsortedList<Integer> list, int index, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.remove(index);
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs indexOf() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedIndex
	 * @return test success
	 */
	private boolean testIndexOf(IndexedUnsortedList<Integer> list, Integer element, int expectedIndex) {
		try {
			return list.indexOf(element) == expectedIndex;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIndexOf", e.toString());
			e.printStackTrace();
			return false;
		}
	}

	////////////////////////////
	// XXX ITERATOR TESTS
	////////////////////////////

	/** Runs iterator() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIter(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.iterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs list's iterator hasNext() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to hasNext()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterHasNext(Iterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			if (iterator.hasNext()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterHasNext", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs list's iterator next() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to hasNext()
	 * @param expectedValue the Integer expected from next() or null if an exception is expected
	 * @param expectedResult MatchingValue or expected exception
	 * @return test success
	 */
	private boolean testIterNext(Iterator<Integer> iterator, Integer expectedValue, Result expectedResult) {
		Result result;
		try {
			Integer retVal = iterator.next();
			if (retVal.equals(expectedValue)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterNext", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs list's iterator remove() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to remove()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterRemove(Iterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			iterator.remove();
			result = Result.NoException;
		} catch (IllegalStateException e) {
			result = Result.IllegalState;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterRemove", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs iterator() method twice on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterConcurrent(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it1 = list.iterator();
			@SuppressWarnings("unused")
			Iterator<Integer> it2 = list.iterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	//////////////////////////////////////////////////////////
	//XXX HELPER METHODS FOR TESTING ITERATORS
	//////////////////////////////////////////////////////////
	
	/** Helper for testing iterators. Return an Iterator that has been advanced numCallsToNext times.
	 * @param list
	 * @param numCallsToNext
	 * @return Iterator for given list, after numCallsToNext
	 */
	private Iterator<Integer> iterAfterNext(IndexedUnsortedList<Integer> list, int numCallsToNext) {
		Iterator<Integer> it = list.iterator();
		for (int i = 0; i < numCallsToNext; i++) {
			it.next();
		}
		return it;
	}

	/** Helper for testing iterators. Return an Iterator that has had remove() called once.
	 * @param iterator
	 * @return same Iterator following a call to remove()
	 */
	private Iterator<Integer> iterAfterRemove(Iterator<Integer> iterator) {
		iterator.remove();
		return iterator;
	}

	////////////////////////////////////////////////////////////////////////
	// XXX LISTITERATOR TESTS
	// Note: can use Iterator tests for hasNext(), next(), and remove()
	////////////////////////////////////////////////////////////////////////

	/** Runs listIterator() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIter(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.listIterator();
			result = Result.NoException;
		} catch (UnsupportedOperationException e) {
			result = Result.UnsupportedOperation;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs listIterator(index) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @param startingIndex
	 * @return test success
	 */
	private boolean testListIter(IndexedUnsortedList<Integer> list, int startingIndex, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.listIterator(startingIndex);
			result = Result.NoException;
		} catch (UnsupportedOperationException e) {
			result = Result.UnsupportedOperation;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator's hasPrevious() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to hasPrevious()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterHasPrevious(ListIterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			if (iterator.hasPrevious()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterHasPrevious", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator previous() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to hasPrevious()
	 * @param expectedValue the Integer expected from next() or null if an exception is expected
	 * @param expectedResult MatchingValue or expected exception
	 * @return test success
	 */
	private boolean testListIterPrevious(ListIterator<Integer> iterator, Integer expectedValue, Result expectedResult) {
		Result result;
		try {
			Integer retVal = iterator.previous();
			if (retVal.equals(expectedValue)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterPrevious", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator add() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to add()
	 * @param element new Integer for insertion
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterAdd(ListIterator<Integer> iterator, Integer element, Result expectedResult) {
		Result result;
		try {
			iterator.add(element);
			result = Result.NoException;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterAdd", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator set() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to set()
	 * @param element replacement Integer for last returned element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterSet(ListIterator<Integer> iterator, Integer element, Result expectedResult) {
		Result result;
		try {
			iterator.set(element);
			result = Result.NoException;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (IllegalStateException e) {
			result = Result.IllegalState;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterSet", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator nextIndex() and checks result against expected Result
	 * @param iterator already positioned for the call to nextIndex()
	 * @param expectedIndex
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterNextIndex(ListIterator<Integer> iterator, int expectedIndex, Result expectedResult) {
		Result result;
		try {
			int idx = iterator.nextIndex();
			if (idx == expectedIndex) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterNextIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator previousIndex() and checks result against expected Result
	 * @param iterator already positioned for the call to previousIndex()
	 * @param expectedIndex
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterPreviousIndex(ListIterator<Integer> iterator, int expectedIndex, Result expectedResult) {
		Result result;
		try {
			int idx = iterator.previousIndex();
			if (idx == expectedIndex) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterPreviousIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs listIterator() method twice on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterConcurrent(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			ListIterator<Integer> it1 = list.listIterator();
			@SuppressWarnings("unused")
			ListIterator<Integer> it2 = list.listIterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs listIterator(index) method twice on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index1
	 * @param index2
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterConcurrent(IndexedUnsortedList<Integer> list, int index1, int index2, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			ListIterator<Integer> it1 = list.listIterator(index1);
			@SuppressWarnings("unused")
			ListIterator<Integer> it2 = list.listIterator(index2);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	//////////////////////////////////////////////////////////
	//XXX HELPER METHODS FOR TESTING LISTITERATORS
	//////////////////////////////////////////////////////////
	
	/** Helper for testing ListIterators. Return a ListIterator that has been advanced numCallsToNext times.
	 * @param iterator
	 * @param numCallsToNext
	 * @return same iterator after numCallsToNext
	 */
	private ListIterator<Integer> listIterAfterNext(ListIterator<Integer> iterator, int numCallsToNext) {
		for (int i = 0; i < numCallsToNext; i++) {
			iterator.next();
		}
		return iterator;
	}

	/** Helper for testing ListIterators. Return a ListIterator that has been backed up numCallsToPrevious times.
	 * @param iterator
	 * @param numCallsToPrevious
	 * @return same iterator after numCallsToPrevious
	 */
	private ListIterator<Integer> listIterAfterPrevious(ListIterator<Integer> iterator, int numCallsToPrevious) {
		for (int i = 0; i < numCallsToPrevious; i++) {
			iterator.previous();
		}
		return iterator;
	}

	/** Helper for testing ListIterators. Return a ListIterator that has had remove() called once.
	 * @param iterator
	 * @return same Iterator following a call to remove()
	 */
	private ListIterator<Integer> listIterAfterRemove(ListIterator<Integer> iterator) {
		iterator.remove();
		return iterator;
	}

	////////////////////////////////////////////////////////
	// XXX Iterator Concurrency Tests
	// Can simply use as given. Don't need to add more.
	////////////////////////////////////////////////////////

	/** run Iterator concurrency tests */
	private void test_IterConcurrency() {
		System.out.println("\nIterator Concurrency Tests\n");		
		try {
			printTest("emptyList_testConcurrentIter", testIterConcurrent(newList(), Result.NoException));
			IndexedUnsortedList<Integer> list = newList();
			Iterator<Integer> it1 = list.iterator();
			Iterator<Integer> it2 = list.iterator();
			it1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2HasNext", testIterHasNext(it2, Result.False));
			list = newList();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2Next", testIterNext(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2Remove", testIterRemove(it2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("A_iter1HasNext_testIter2HasNext", testIterHasNext(it2, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("A_iter1HasNext_testIter2Next", testIterNext(it2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("A_iter1HasNext_testIter2Remove", testIterRemove(it2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			printTest("A_iter1Next_testIter2HasNext", testIterHasNext(it2, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			printTest("A_iter1Next_testIter2Next", testIterNext(it2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			printTest("A_iter1Next_testIter2Remove", testIterRemove(it2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			it1.remove();
			printTest("A_iter1NextRemove_testIter2HasNext", testIterHasNext(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			it1.remove();
			printTest("A_iter1NextRemove_testIter2Next", testIterNext(it2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			it1.remove();
			printTest("A_iter1NextRemove_testIter2Remove", testIterRemove(it2, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeFirst();
			printTest("A_removeFirst_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeFirst();
			printTest("A_removeFirst_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeFirst();
			printTest("A_removeLast_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));			

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_remove_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_remove_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.first();
			printTest("A_first_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.first();
			printTest("A_first_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.first();
			printTest("A_first_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.last();
			printTest("A_last_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.last();
			printTest("A_last_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.last();
			printTest("A_last_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.size();
			printTest("A_size_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.size();
			printTest("A_size_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.size();
			printTest("A_size_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterNextConcurrent", testIterNext(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.get(0);
			printTest("A_get0_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.get(0);
			printTest("A_get0_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.get(0);
			printTest("A_get_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", "test_IteratorConcurrency");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	////////////////////////////////////////////////////////
	// XXX ListIterator Concurrency Tests
	// Will add tests for double-linked list
	////////////////////////////////////////////////////////

	/** run ListIterator concurrency tests */
	private void test_ListIterConcurrency() {
		System.out.println("\nListIterator Concurrency Tests\n");
		try {
			//TODO: will add for double-linked list
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", "test_ListIterConcurrency");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}
}// end class IndexedUnsortedListTester

/** Interface for builder method Lambda references used above */
interface Scenario<T> {
	IndexedUnsortedList<T> build();
}