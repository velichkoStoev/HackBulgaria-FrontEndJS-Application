public class Main {
	public static void main(String[] args) {
		String[] words = { "yesterday", "Dog", "food", "walk" };
		String text = "Yesterday, I took my dog for a walk.\n It was crazy! My dog wanted only food.";
		
		System.out.println(maskOutWords(words, text));
	}

	private static String maskOutWords(String[] words, String text) {
		for (String word : words) {
			text = text.replaceAll("(?i)\\b" + word + "\\b", getStarsString(word.length()));
		}
		return text;
	}

	private static String getStarsString(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append("*");
		}

		return sb.toString();
	}
}
