public class TestGameLogic {

	static {
		System.loadLibrary("hello");
	}
	
	public native String hello(int winTotal, int collectedGold);
	
	public static void main(String[] args) {
		new TestGameLogic().hello(2,2);
	}
}
