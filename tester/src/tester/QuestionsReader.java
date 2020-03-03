package tester;

import javafx.scene.image.Image;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  Tester
 *
 *  Author DMotsniy & Mirniy18
 */

public class QuestionsReader {
    private static final byte SALT = -66;

    // tokens must be lower case in the code, but they can be any in files
    private static final String TOKEN_QUESTION = "q";
    private static final String TOKEN_CHOICE = "a";
    private static final String TOKEN_PICTURE = "p";

    private static final List<String> TOKENS = Arrays.asList(TOKEN_QUESTION, TOKEN_CHOICE, TOKEN_PICTURE);

    private String[] lines;
    private byte[] answers;

    private int questionSize;

    private List<File> pictures;

    public QuestionsReader(String path, int choicesCount) throws IOException {
        this(path, choicesCount, true);
    }

    public QuestionsReader(String path, int choicesCount, boolean encryptFile) throws IOException {
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

    public String[] getVariants(int questionIndex) { // TODO rename
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

    public void encryptFile(String path) throws IOException {
        String key = Encryption.toEncryptedString(answers, SALT);

        BufferedWriter bw = new BufferedWriter(new FileWriter(path));

        bw.append(key).write('\n');

        for (int i = 0; i < lines.length; ++i) {
            bw.append((i & 3) == 0 ? TOKEN_QUESTION : TOKEN_CHOICE).append('\n').append(lines[i]).write('\n');
            if ((i & 3) == 3 && pictures != null && pictures.size() > i / questionSize) {
                bw.append(TOKEN_PICTURE).append('\n').append(pictures.get(i / questionSize).getPath()).write('\n');
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

            bw.append(TOKEN_QUESTION).append('\n').append(lines[j]).write('\n');

            for (int k = 1; k < questionSize; ++k) {
                bw.append(TOKEN_CHOICE).append('\n').append(lines[j + k]).write('\n');
            }

            if (pictures != null && pictures.size() > i) {
                bw.append(TOKEN_PICTURE).append('\n').append(pictures.get(i).getPath()).write('\n');
            }
        }

        bw.flush();
        bw.close();

        if (shuffleBack) {
            shuffleAnswers(questionSize - 1);
        }
    }

    private List<String> readFile(String path, int choicesCount) throws IOException {
        List<String> result;
        String key = null;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String token;
            AtomicInteger lineIndex = new AtomicInteger(1);

            for (; (token = br.readLine()) != null; lineIndex.getAndIncrement()) {
                token = token.trim();

                if (token.length() == 0) // skip blanks
                    continue;

                if (token.matches("^[0-9]+$")) { // find key
                    key = token;

                    lineIndex.getAndIncrement();
                    token = br.readLine();
                }

                token = token.toLowerCase();

                if (token.equalsIgnoreCase(TOKEN_QUESTION)) // find first token
                    break;

                br.close();
                throw new IOException(String.format("Unexpected line at %s: %s", lineIndex.toString(), token));
            }

            result = new ArrayList<>(32);
            String previousToken = null;
            int sameTokensInARow = 0, previousTokensInARow = -1, questionIndex = -1;

            do {
                if (token.equals(previousToken)) {
                    ++sameTokensInARow;
                } else {
                    previousTokensInARow = sameTokensInARow;
                    sameTokensInARow = 1;
                }

                String[] linesAndToken = readToken(br, lineIndex);

                switch (token) {
                    case TOKEN_QUESTION:
                        if (previousToken != null) {
                            switch (previousToken) {
                                case TOKEN_QUESTION:
                                    throw new IOException(String.format("Question must have choices, line: %s", lineIndex.toString()));
                                case TOKEN_CHOICE:
                                    if (previousTokensInARow != choicesCount) {
                                        throw new IOException(String.format("Wrong choices count, line: %s", lineIndex.toString()));
                                    }
                                    break;
                            }
                        }
                        ++questionIndex;
                    case TOKEN_CHOICE:
                        result.add(linesAndToken[0]);
                        break;
                    case TOKEN_PICTURE:
                        if (previousToken.equals(TOKEN_CHOICE) && previousTokensInARow != choicesCount) {
                            throw new IOException(String.format("Wrong choices count, line: %s", lineIndex.toString()));
                        }
                        setPicture(linesAndToken[0], questionIndex, lineIndex.get() - 1);
                        break;
                }

                previousToken = token;
                token = linesAndToken[1];
            } while (token != null);
        }

        if (key != null) {
            answers = Encryption.toDecryptedArray(key, result.size() / (1 + choicesCount), SALT);
        }
        return result;
    }

    private void setPicture(String path, int questionIndex, int lineIndex) throws IOException {
        if (pictures == null) {
            pictures = new ArrayList<>(Math.min(4, questionIndex + 1)); // TODO maybe do not reserve capacity for unread questions

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

    private static String[] readToken(BufferedReader br, AtomicInteger lineIndex) throws IOException {
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            lineIndex.getAndIncrement();
            if (TOKENS.contains(line.toLowerCase())) // TODO add "line.length() == N" if all tokens have the same size
                break;
            result.append(line).append('\n');
        }

        return new String[]{result.toString().trim(), line == null ? null : line.toLowerCase()};
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



    @Override
    public String toString() {
        return String.join("\n", lines) + "\n\n" + Arrays.toString(answers);
    }
}
