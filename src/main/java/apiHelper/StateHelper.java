package apiHelper;

import java.util.concurrent.ConcurrentHashMap;



public class StateHelper {

	private static volatile ConcurrentHashMap<Long, ConcurrentHashMap<String, Object>> CrossStoryState = new ConcurrentHashMap<Long, ConcurrentHashMap<String, Object>>();
	private static volatile ConcurrentHashMap<Long, ConcurrentHashMap<String, Object>> CrossScenarioState = new ConcurrentHashMap<Long, ConcurrentHashMap<String, Object>>();
	private static volatile ConcurrentHashMap<Long, ConcurrentHashMap<String, Object>> CrossApplicationState = new ConcurrentHashMap<Long, ConcurrentHashMap<String, Object>>();

	/**
	 * 
	 * 
	 * Volatile : http://tutorials.jenkov.com/java-concurrency/volatile.html
	 * 
	 * Synchronized Block :
	 * http://tutorials.jenkov.com/java-concurrency/synchronized.html
	 * 
	 * ConcurrentHashMap :
	 * https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ConcurrentHashMap.html
	 * 
	 * this method used to store values during the story inside
	 * ConcurrentHashMap, each one of ConcurrentHashMap of stored inside another
	 * one as a value and its key represented by current thread id.
	 * 
	 * @param key
	 *            : represent the key to store it inside ConcurrentHashMap to
	 *            retrieve the value with it.
	 * @param value
	 *            to store it inside ConcurrentHashMap during the story.
	 * 
	 * 
	 * 
	 */
	public static void setStoryState(String key, Object value) {
		if (key != null && value != null) {
			synchronized (CrossStoryState) {
				ConcurrentHashMap<String, Object> chm = CrossStoryState.get(Thread.currentThread().getId());

				if (chm == null)

					chm = new ConcurrentHashMap<String, Object>();

				chm.put(key, value);
				CrossStoryState.put(Thread.currentThread().getId(), chm);

			}
		} else {


			System.out.print("Null Value not allowed");

		}
	}

	/**
	 * this method used to get saved data during the story,
	 * 
	 * @param key
	 *            the key that related to specific value to retrieve it using
	 *            this key.
	 * @return Object the value that stored during the story, null if there is
	 *         no value found for this thread and this key.
	 */
	public static Object getStoryState(String key) {

		if (key != null && checkStoryStateContainsKey(key)) {
			synchronized (CrossStoryState) {
				return CrossStoryState.get(Thread.currentThread().getId()).get(key);

			}
		} else {
			return null;
		}
	}

	/**
	 * this method used to clear all data that saved in story.
	 * 
	 */
	public static void clearStoryState() {
		synchronized (CrossStoryState) {
			if (CrossStoryState==null)
			CrossStoryState.clear();

		}
	}
	
	public static void clearStoryStateForCurrentThread() {
		synchronized (CrossStoryState) {
			ConcurrentHashMap<String, Object> chm = CrossStoryState.get(Thread.currentThread().getId());
			chm = new ConcurrentHashMap<String, Object>();
			CrossStoryState.put(Thread.currentThread().getId(), chm);
		}
	}

	/**
	 * this method used to check if the key stored or exist inside the
	 * ConcurrentHashMap for current thread during the story.
	 * 
	 * @param keyState
	 *            to check if it exist.
	 * 
	 * @return Boolean true if the keyState found , otherwise return false.
	 */
	public static boolean checkStoryStateContainsKey(String keyState) {
		synchronized (CrossStoryState) {

			return CrossStoryState.get(Thread.currentThread().getId()).containsKey(keyState);

		}
	}

	/**
	 * this method used to store values during the Scenario inside
	 * ConcurrentHashMap, each one of ConcurrentHashMap of stored inside another
	 * one as a value and its key represented by current thread id.
	 * 
	 * @param key
	 *            represent the key to store it inside ConcurrentHashMap to
	 *            retrieve the value with it.
	 * @param value
	 *            to store it inside ConcurrentHashMap during the Scenario.
	 */

	// Save Scenario key with Object Value

	public static void setScenarioState(String key, Object value) {
		if (key != null && value != null) {

			synchronized (CrossScenarioState) {

				ConcurrentHashMap<String, Object> chm = CrossScenarioState.get(Thread.currentThread().getId());

				if (chm == null)

					chm = new ConcurrentHashMap<String, Object>();

				chm.put(key, value);
				CrossScenarioState.put(Thread.currentThread().getId(), chm);

			}
		} else {

			System.out.print("Null Value not Allowed");
		}
	}

	/**
	 * this method used to get saved data during the Scenario.
	 * 
	 * @param key
	 *            the key that related to specific value to retrieve it using
	 *            this key.
	 * @return Object the value that stored during the Scenario, null if there
	 *         is no value found for this thread and this key.
	 */

	public static Object getScenarioState(String key) {

		if (key != null && checkScenarioStateContainsKey(key)) {
			synchronized (CrossScenarioState) {

				return CrossScenarioState.get(Thread.currentThread().getId()).get(key);

			}
		} else {
			return null;
		}
	}

	/**
	 * 
	 * this method used to clear all data that saved in Scenario.
	 * 
	 */

	public static void clearScenarioState() {
		synchronized (CrossScenarioState) {
			CrossScenarioState.clear();
		}
	}

	public static void clearScenarioStateForCurrentThread() {
		synchronized (CrossScenarioState) {
			ConcurrentHashMap<String, Object> chm = CrossScenarioState.get(Thread.currentThread().getId());
			chm = new ConcurrentHashMap<String, Object>();
			CrossScenarioState.put(Thread.currentThread().getId(), chm);
		}
	}
	
	/**
	 * this method used to check if the key stored or exist inside the
	 * ConcurrentHashMap for current thread during the Scenario.
	 * 
	 * @param keyState
	 *            to check if it exist.
	 * 
	 * @return Boolean true if the keyState found , otherwise return false.
	 */

	public static boolean checkScenarioStateContainsKey(String keyState) {

		synchronized (CrossScenarioState) {

			return CrossScenarioState.get(Thread.currentThread().getId()).containsKey(keyState);

		}
	}

	// Save Key - Value Cross Application //

	/**
	 * this method used to store values during the full run inside
	 * ConcurrentHashMap, each one of ConcurrentHashMap of stored inside another
	 * one as a value and its key represented by current thread id.
	 * 
	 * @param key
	 *            represent the key to store it inside ConcurrentHashMap to
	 *            retrieve the value with it.
	 * @param value
	 *            to store it inside ConcurrentHashMap during the story.
	 */
	public static void setApplicationState(String key, Object value) {

		if (key != null && value != null) {

			synchronized (CrossApplicationState) {

				ConcurrentHashMap<String, Object> chm = CrossApplicationState.get(Thread.currentThread().getId());

				if (chm == null)

					chm = new ConcurrentHashMap<String, Object>();

				chm.put(key, value);
				CrossApplicationState.put(Thread.currentThread().getId(), chm);

			}
		} else {


System.out.print("Null Value Not Allowed");
		}
	}

	/**
	 * this method used to get saved data during the full run.
	 * 
	 * @param key
	 *            the key that related to specific value to retrieve it using
	 *            this key.
	 * @return Object the value that stored during the Scenario, null if there
	 *         is no value found for this thread and this key.
	 */

	public static Object getApplicationState(String key) {

		if (key != null && checkApplicationStateContainsKey(key)) {
			synchronized (CrossApplicationState) {
				return CrossApplicationState.get(Thread.currentThread().getId()).get(key);

			}
		} else {
			return null;
		}
	}

	/**
	 * this method used to check if the key stored or exist inside the
	 * ConcurrentHashMap for current thread during the full run.
	 * 
	 * @param keyState
	 *            to check if it exist.
	 * 
	 * @return Boolean true if the keyState found , otherwise return false.
	 */

	public static boolean checkApplicationStateContainsKey(String keyState) {
		synchronized (CrossApplicationState) {

			return CrossApplicationState.get(Thread.currentThread().getId()).containsKey(keyState);

		}
	}

	public static void test() {
		StateHelper.setStoryState("key1", "test1");
		
		StateHelper.setScenarioState("Tareq", "Tareq1990");

	}

	public static void main(String args[]) {

		StateHelper.test();

		// StateHelper.setStoryState("key1", "test1");
		StateHelper.setStoryState("key2", "test2");
		StateHelper.setApplicationState("key3", "key3");
		StateHelper.setApplicationState("key4", "key4");
		StateHelper.setScenarioState("key5", "key5");
		StateHelper.setScenarioState("key6", "key6");
		
		System.out.println(StateHelper.getScenarioState("Tareq"));
		System.out.println(StateHelper.getStoryState(("key1")));
		System.out.println(CrossScenarioState.toString());
		System.out.println(CrossApplicationState.toString());
		System.out.println(CrossStoryState.toString());
		System.out.println(CrossScenarioState.toString());
		System.out.println(CrossApplicationState.toString());

		StateHelper.clearStoryState();
		StateHelper.clearScenarioState();

		System.out.println(CrossApplicationState.toString());
		System.out.println(CrossStoryState.toString());
		System.out.println(CrossScenarioState.toString());
		System.out.println(CrossApplicationState.toString());

	}

}