package tester;

import javafx.scene.image.Image;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FileHandler {
	private static final byte SALT = -42;

	// tokens must be lower case in code, but they can be any in files
	private static final String TOKEN_QUESTION = "q";
	private static final String TOKEN_CHOICE = "a";
	private static final String TOKEN_PICTURE = "p";

	private static final List<String> TOKENS = Arrays.asList(TOKEN_QUESTION, TOKEN_CHOICE, TOKEN_PICTURE);
	private static final List<String> TOKENS_END = Arrays.asList("end" + TOKEN_QUESTION, "end" + TOKEN_CHOICE, "end" + TOKEN_PICTURE);

	private static final String SETTING_KEY = "key";

	private String[] lines;
	private byte[] answers;

	private int questionSize;

	private List<File> pictures;

	private Map<String, String> settings;

	public FileHandler(String path, int choicesCount, boolean encryptFile) throws IOException {
		List<String> list = readFile(path, choicesCount);

		questionSize = 1 + choicesCount;

		lines = list.toArray(new String[0]);

		if (answers == null) {
			answers = new byte[getQuestionsCount()];

			shuffleAnswers(choicesCount);

			if (encryptFile) {
				this.encryptFile(path);
			}
		}
	}

	public int getQuestionsCount() {
		return lines.length / questionSize;
	}

	public String[] getQuestions() {
		String[] result = new String[getQuestionsCount()];
		for (int i = 0; i < getQuestionsCount(); ++i) {
			result[i] = lines[i * questionSize];
		}
		return result;
	}

	public String[] getChoices(int questionIndex) {
		String[] variants = new String[questionSize - 1];
		for (int i = 0; i < questionSize - 1; ++i) {
			variants[i] = lines[questionIndex * questionSize + i + 1];
		}

		return variants;
	}

	public int getAnswerIndex(int questionIndex) {
		return answers[questionIndex];
	}

	public Image getPicture(int questionIndex) throws FileNotFoundException { // TODO maybe read picture once
		File file = pictures == null ? null : (pictures.size() > questionIndex ? pictures.get(questionIndex) : null);

		if (file == null)
			return null;

		if (!file.exists())
			throw new FileNotFoundException(String.format("Image %s not found", file.getPath()));

		return new Image(file.toURI().toString());
	}

	public String getSetting(String name) {
		return settings.get(name);
	}
	public String getSetting(String name, String defaultValue) {
		return settings.getOrDefault(name, defaultValue);
	}

	private List<String> readFile(String path, int choicesCount) throws IOException {
		List<String> result;
		settings = new HashMap<>(0);

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String token;
			AtomicInteger lineIndex = new AtomicInteger(1);

			for (; (token = br.readLine()) != null; lineIndex.getAndIncrement()) {
				token = token.trim();

				if (token.length() == 0)
					continue;

				if (token.matches("^[a-zA-Z0-9]+:[ \t]+[a-zA-Z0-9]+$")) {
					settings.put(token.substring(0, token.indexOf(":")).trim(), token.substring(token.indexOf(":") + 1).trim());

					continue;
				}

				if (token.equalsIgnoreCase(TOKEN_QUESTION)) {
					token = token.toLowerCase();

					break;
				}

				if (!token.startsWith("//"))
					throw new IOException(String.format("Unexpected line at %s: %s", lineIndex.toString(), token));
			}

			if (token == null) {
				throw new IOException(String.format("Expected %s, line: %s", TOKEN_QUESTION, lineIndex.toString()));
			}

			result = new ArrayList<>(32);
			int choices = -1;
			int questionIndex = -1;

			while (true) {
				String lines = readToken(br, getEndToken(token), lineIndex);

				switch (token) {
					case TOKEN_QUESTION:
						++questionIndex;

						if (choices != -1 && choices != choicesCount)
							throw new IOException(String.format("Wrong choices count, line: %s", lineIndex.toString()));

						result.add(lines);
						choices = 0;
						break;
					case TOKEN_CHOICE:
						result.add(lines);
						++choices;
						break;
					case TOKEN_PICTURE:
						setPicture(lines, questionIndex, lineIndex.get() - 1);
						break;
				}

				token = getNextToken(br, lineIndex);

				if (token == null)
					break;
			}

			if (choices != choicesCount)
				throw new IOException(String.format("Wrong choices count, line: %s", lineIndex.toString()));
		}

		if (settings.containsKey(SETTING_KEY)) {
			answers = Encryption.toDecryptedArray(settings.get(SETTING_KEY), result.size() / (1 + choicesCount), SALT);
		}

		return result;
	}

	private void setPicture(String path, int questionIndex, int lineIndex) throws IOException {
		if (pictures == null) {
			pictures = new ArrayList<>(Math.min(4, questionIndex + 1));

			for (int i = 0; i < questionIndex; ++i) {
				pictures.add(null);
			}
		}

		if (pictures.size() < questionIndex) {
			for (int i = pictures.size(); i < questionIndex; ++i) {
				pictures.add(null);
			}
		}

		if (pictures.size() == questionIndex) {
			File file = new File(path);

			if (!file.exists()) {
				throw new FileNotFoundException(String.format("Image %s not found, line: %d", path, lineIndex));
			}

			pictures.add(new File(path));
		} else {
			throw new IOException(String.format("Question cannot have multiple pictures, line: %d", lineIndex));
		}
	}

	private static String readToken(BufferedReader br, String endToken, AtomicInteger lineIndex) throws IOException {
		StringBuilder result = new StringBuilder();
		String line;

		while ((line = br.readLine()) != null) {
			lineIndex.getAndIncrement();

			String trimmed = line.trim();

			if (trimmed.toLowerCase().equalsIgnoreCase(endToken)) {
				int length = result.length() - 1;

				while (length >= 0 && result.charAt(length) <= ' ')
					--length;

				if (result.length() == 0)
					throw new IOException(String.format("Empty token, line: %s", lineIndex.toString()));

				return result.substring(0, length + 1);
			}

			if (result.length() > 0 || trimmed.length() > 0) {
				result.append(line).append('\n');
			}
		}

		throw new IOException(String.format("Expected %s, line: %s", endToken, lineIndex.toString()));
	}

	private static String getNextToken(BufferedReader br, AtomicInteger lineIndex) throws IOException {
		String line;

		while ((line = br.readLine()) != null) {
			lineIndex.getAndIncrement();
			String token = line.trim().toLowerCase();

			if (token.length() == 0)
				continue;

			if (TOKENS.contains(token))
				return token;

			if (!token.startsWith("//"))
				throw new IOException(String.format("Unexpected line at %s: %s", lineIndex.toString(), line));
		}

		return null;
	}

	private void shuffleAnswers(int choicesCount) throws IOException {
		Random rnd = new Random();

		for (int i = 0; i < getQuestionsCount(); ++i) {
			Set<String> set = new HashSet<>();

			for (int j = 0; j < choicesCount; ++j) {
				if (!set.add(lines[i * questionSize + j + 1])) {
					throw new IOException("Duplicated choices");
				}
			}

			int answer = answers[i];

			for (int j = choicesCount - 1; j > 0; --j) {
				int r = rnd.nextInt(j + 1);
				String tmp = lines[i * questionSize + r + 1];
				lines[i * questionSize + r + 1] = lines[i * questionSize + j + 1];
				lines[i * questionSize + j + 1] = tmp;
				if (r == answer)
					answer = j;
				else if (j == answer)
					answer = r;
			}

			answers[i] = (byte) answer;

			for (int j = 0; j < choicesCount; ++j) {
				lines[i * questionSize + j + 1] = lines[i * questionSize + j + 1];
			}
		}
	}

	public void encryptFile(String path) throws IOException {
		String key = Encryption.toEncryptedString(answers, SALT);

		BufferedWriter bw = new BufferedWriter(new FileWriter(path));

		bw.append(SETTING_KEY).append(": ").append(key).write('\n');

		for (int i = 0; i < lines.length; ++i) {
			String token = (i & 3) == 0 ? TOKEN_QUESTION : TOKEN_CHOICE;

			bw.append(token).append('\n').append(lines[i]).append('\n').append(getEndToken(token)).write('\n');
			if ((i & 3) == 3 && pictures != null && pictures.size() > i / questionSize) {
				bw.append(TOKEN_PICTURE).append('\n').append(pictures.get(i / questionSize).getPath()).append('\n').append(getEndToken(TOKEN_PICTURE)).write('\n');
			}
		}

		bw.flush();
		bw.close();
	}

	public void decryptFile(String path, boolean shuffleBack) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(path));

		for (int i = 0; i < getQuestionsCount(); ++i) {
			int j = i * questionSize;
			if (answers[i] != 0) {
				String t = lines[j + answers[i] + 1];
				lines[j + answers[i] + 1] = lines[j + 1];
				lines[j + 1] = t;
			}

			bw.append(TOKEN_QUESTION).append('\n').append(lines[j]).append('\n').append(getEndToken(TOKEN_QUESTION)).write('\n');

			for (int k = 1; k < questionSize; ++k) {
				bw.append(TOKEN_CHOICE).append('\n').append(lines[j + k]).append('\n').append(getEndToken(TOKEN_CHOICE)).write('\n');
			}

			if (pictures != null && pictures.size() > i) {
				bw.append(TOKEN_PICTURE).append('\n').append(pictures.get(i).getPath()).append('\n').append(getEndToken(TOKEN_PICTURE)).write('\n');
			}
		}

		bw.flush();
		bw.close();

		if (shuffleBack) { // FIXME
			shuffleAnswers(questionSize - 1);
		}
	}

	private static String getEndToken(String token) {
		return TOKENS_END.get(TOKENS.indexOf(token));
	}

	@Override
	public String toString() {
		return String.join("\n", lines) + "\n\n" + Arrays.toString(answers);
	}
}
