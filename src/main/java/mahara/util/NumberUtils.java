package mahara.util;

//import org.apache.commons.lang3.StringUtils;

public class NumberUtils {

	public static boolean equals(final Integer intVal1, final Integer intVal2) {
		if (intVal1 == null || intVal2 == null) {
			return false;
		}
		return intVal1.equals(intVal2);
	}

	public static int compares(final Integer intVal1, final Integer intVal2) {
		if (intVal1 == null || intVal2 == null) {
			return -2;
		}
		return intVal1.compareTo(intVal2);
	}

	public static boolean isEmpty(final Integer intVal) {
//		String strVal = integerToString(intVal);
//		return StringUtils.isEmpty(strVal);
		return intVal == null;
	}

//	public static String integerToString(final Integer intVal) {
//		if (intVal == null) {
//			return null;
//		}
//		return String.valueOf(intVal);
//	}

	public static boolean equals(final Long longVal1, final Long longVal2) {
		if (longVal1 == null || longVal2 == null) {
			return false;
		}
		return longVal1.equals(longVal2);
	}

	public static long compares(final Long longVal1, final Long longVal2) {
		if (longVal1 == null || longVal2 == null) {
			return -2;
		}
		return longVal1.compareTo(longVal2);
	}

	public static boolean isNull(final Long longVal) {
//		String strVal = NumberUtils.longToString(longVal);
//		return StringUtils.isEmpty(strVal);
		return longVal == null;
	}

//	public static String longToString(final Long longVal) {
//		if (longVal == null) {
//			return null;
//		}
//		return String.valueOf(longVal);
//	}
}
