package tester;

import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class QuestionsReader {
    private String[] lines;
    private int[] answers;

    private int questionSize;

    private HashMap<Integer, File> pictures;

    public QuestionsReader(String path, int variantsCount) throws IOException {
        List<String> list = new ArrayList<>(32);
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int lineIndex = 1;

        for (; (line = br.readLine()) != null; ++lineIndex) {
            line = line.trim();

            if (line.length() == 0) {
                continue;
            }

            if (line.equalsIgnoreCase("q")) {
                break;
            }

            br.close();
            fr.close();
            throw new IOException(String.format("Unexpected line at %d: %s", lineIndex, line));
        }

        Character previousToken = 'q', lastToken = null;
        int sameTokensInARow = 1;
        int questionIndex = 0;

        do {
            for (int j = 0; j < 1 + variantsCount; ++j) {
                Pair<Integer, Pair<Character, String>> pair = readLinesUntilNextBreakToken(br, lineIndex);
                lineIndex = pair.getKey();
                lastToken = pair.getValue().getKey();
                line = pair.getValue().getValue();

                if (previousToken == 'p') {
                    if (pictures == null) {
                        pictures = new HashMap<>(4);
                    }

                    File file = new File(line);

                    if (!file.exists()) {
                        br.close();
                        fr.close();
                        throw new FileNotFoundException(String.format("Image %s not found", line));
                    }

                    pictures.put(questionIndex, file);
                } else {
                    list.add(line);
                }

                if (lastToken != null) {
                    if (lastToken == 'p') {
                        --j;
                    }

                    if (lastToken == previousToken) {
                        ++sameTokensInARow;

                        if (lastToken == 'a' && sameTokensInARow > variantsCount
                                || (lastToken == 'q' || lastToken == 'p') && sameTokensInARow > 1) {
                            br.close();
                            fr.close();
                            throw new IOException(String.format("Unexpected token at %d: %c", lineIndex, lastToken));
                        }
                    } else {
                        sameTokensInARow = 1;
                        previousToken = lastToken;
                    }
                }
            }
            ++questionIndex;
        } while (lastToken != null);

        br.close();
        fr.close();

        if (!Objects.equals(previousToken, 'a') || sameTokensInARow != variantsCount) {
            throw new IOException("Wrong questions count");
        }

        questionSize = 1 + variantsCount;

        lines = list.toArray(new String[0]);
        answers = new int[getQuestionsCount()];

        Random rnd = new Random();

        for (int i = 0; i < getQuestionsCount(); ++i) {
            Set<String> set = new HashSet<>();

            for (int j = 0; j < variantsCount; ++j) {
                if (!set.add(lines[i * questionSize + j + 1])) {
                    throw new IOException("Duplicated variants");
                }
            }

            int answer = 0;

            for (int j = variantsCount - 1; j > 0; --j) {
                int r = rnd.nextInt(j + 1);
                String tmp = lines[i * questionSize + r + 1];
                lines[i * questionSize + r + 1] = lines[i * questionSize + j + 1];
                lines[i * questionSize + j + 1] = tmp;
                if (r == answer)
                    answer = j;
                else if (j == answer)
                    answer = r;
            }

            answers[i] = answer;

            for (int j = 0; j < variantsCount; ++j) {
                lines[i * questionSize + j + 1] = lines[i * questionSize + j + 1];
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

    public String[] getVariants(int questionIndex) {
        String[] variants = new String[questionSize - 1];
        for (int i = 0; i < questionSize - 1; ++i) {
            variants[i] = lines[questionIndex * questionSize + i + 1];
        }

        return variants;
    }

    public int getAnswerIndex(int questionIndex) {
        return answers[questionIndex];
    }

    public BufferedImage getPicture(int questionIndex) throws IOException {
        if (pictures == null) {
            return null;
        }

        File file = pictures.getOrDefault(questionIndex, null);

        return file == null ? null : ImageIO.read(file);
    }

    private Pair<Integer, Pair<Character, String>> readLinesUntilNextBreakToken(BufferedReader br, int lineIndex) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;

        List<Character> tokens = Arrays.asList('a', 'q', 'p');

        while ((line = br.readLine()) != null) {
            if (line.length() == 1 && tokens.contains(Character.toLowerCase(line.charAt(0)))) {
                break;
            }
            builder.append(line).append('\n');
            ++lineIndex;
        }

        return new Pair<>(lineIndex, new Pair<>(line == null ? null : Character.toLowerCase(line.charAt(0)), builder.toString().trim()));
    }

    @Override
    public String toString() {
        return String.join("\n", lines);
    }
}
