package co.lotc.core.util;

import co.lotc.core.Tythan;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A data filtering class used for ensuring that any data you're pulling from a map is the correct
 * class, while also not limiting the different types of data. Using this one can map any sorts of
 * data while being certain it's providing the appropriate object when retrieved.
 */
public class DataMapFilter {

	// STATIC //
	private static final Map<String, String> DATA_MAP = new HashMap<>(); // input string to output string
	private static final Map<String, Class> CLASS_MAP = new HashMap<>(); // output string to class type

	/**
	 * Adds a new filter to the DataMapFilter
	 * @param aliases An array of strings that act as aliases for the key of this class.
	 * @param key The actual key to use when calling back to this data entry.
	 * @param clazz The class type that this data entry represents.
	 */
	public static void addFilter(String[] aliases, String key, Class clazz) {
		for (String str : aliases) {
			addFilter(str, key, clazz);
		}
	}

	/**
	 * Adds a new filter to the DataMapFilter
	 * @param key The key to use when calling back to this data entry.
	 * @param clazz The class type that this data entry represents.
	 */
	public static void addFilter(String key, Class clazz) {
		addFilter(key, key, clazz);
	}

	private static void addFilter(String alias, String key, Class clazz) {
		if (DATA_MAP.containsKey(alias.toLowerCase())) {
			if (Tythan.get().isDebugging()) {
				Tythan.get().getLogger().info("Duplicate mapping for key '" + alias.toLowerCase() + "'.");
			}
		} else {
			DATA_MAP.put(alias.toLowerCase(), key);
			CLASS_MAP.put(key, clazz);
		}
	}

	// INSTANCE //
	private final Map<String, Object> data = new HashMap<>();

	/**
	 * Merges all data from the provided DataMapFilter into this one.
	 * @param data The DataMapFilter to absorb.
	 * @return The DataMapFilter that was acted upon to allow chain statements.
	 */
	public DataMapFilter putAllData(DataMapFilter data) {
		for (Entry<String, Object> entry : data.data.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
		return this;
	}

	/**
	 * Merges all data from the proivided Object to Object map, presuming that the
	 * first Object is a String and throwing a Debugging error in the event that the
	 * first Object is not a String.
	 * @param data The Object to Object map to translate.
	 * @return The DataMapFilter that is being acted upon.
	 */
	public DataMapFilter putAllObject(Map<Object, Object> data) {
		for (Entry<Object, Object> entry : data.entrySet()) {
			if (entry.getKey() instanceof String) {
				put((String) entry.getKey(), entry.getValue());
			} else if (Tythan.get().isDebugging()) {
				Tythan.get().getLogger().warning("Failed to translate " + entry.getKey().toString() + " to a string. Had value " + entry.getValue().toString());
			}
		}
		return this;
	}

	/**
	 * Merges all data from the proivided String to Object map.
	 * @param data The String to Object map to translate.
	 * @return The DataMapFilter that is being acted upon.
	 */
	public DataMapFilter putAll(Map<String, Object> data) {
		for (Entry<String, Object> entry : data.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
		return this;
	}

	/**
	 * Attempt to add data to this DataMapFilter with the given key and object.
	 * @param key The key string to appropriately map this data
	 * @param value The object to map to this data. If it's not the appropriate class it will be rejected.
	 * @return The DataMapFilter being acted upon.
	 */
	public DataMapFilter put(String key, Object value) {
		String output = DATA_MAP.get(key);
		Class clazz = CLASS_MAP.get(output);
		try {
			if (value == null || (clazz != null && clazz.isInstance(value))) {
				data.put(output, value);
			} else {
				Tythan.get().getLogger().warning("Wrong data type submitted for '" + output + "'. Expected instance of " + clazz.toString() + ", received " + value.getClass().toString());
			}
		} catch (Exception e) {
			if (Tythan.get().isDebugging()) {
				String clazzString = null;
				String valueString = null;

				if (clazz != null) {
					clazzString = clazz.toString();
				}

				if (value != null) {
					valueString = value.getClass().toString();
				}

				Tythan.get().getLogger().warning("Wrong data type submitted for '" + output + "'. Expected instance of " + clazzString + ", received " + valueString);
				e.printStackTrace();
			}
		}

		return this;
	}

	/**
	 * @return Whether there is any data stored in this DataMapFilter
	 */
	public boolean isEmpty() {
		return data.isEmpty();
	}

	/**
	 * @param key The key to search for.
	 * @return The object which is mapped to the given key, if it exists.
	 */
	public Object get(String key) {
		return data.get(key);
	}

	/**
	 * @param key The key to search for.
	 * @return Whether or not there is any data for the given key.
	 */
	public boolean containsKey(String key) {
		return data.containsKey(key);
	}

	/**
	 * @return A set of keys found in this DataMapFilter.
	 */
	public Set<String> keySet() {
		return data.keySet();
	}

	/**
	 * @return A collection of values found in this DataMapFilter.
	 */
	public Collection<Object> values() {
		return data.values();
	}

}
