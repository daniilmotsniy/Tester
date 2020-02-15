package tester;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class QuestionsReader {
    private String[] lines;
    private int[] answers;

    private int questionSize;

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

            throw new IOException(String.format("Unexpected line at %d: %s", lineIndex, line));
        }

        out:
        while (true) {
            for (int j = 0; j < variantsCount; ++j) {
                lineIndex = readLinesUntil(br, list, lineIndex, true);
                if (lineIndex == -1) {
                    break out;
                }
            }
            lineIndex = readLinesUntil(br, list, lineIndex, false);

            if (lineIndex == -1) {
                break;
            }
        }

        br.close();
        fr.close();

        questionSize = 1 + variantsCount;

        if (list.size() % questionSize != 0) { // TODO maybe cannot execute
            throw new IOException("Wrong questions count");
        }

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

    private int readLinesUntil(BufferedReader br, List<String> to, int lineIndex, boolean untilAnswer) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;
        boolean endOfFile = true;

        while ((line = br.readLine()) != null) {
            if (line.equalsIgnoreCase(untilAnswer ? "a" : "q")) {
                endOfFile = false;
                break;
            }
            if (line.equalsIgnoreCase(untilAnswer ? "q" : "a")) {
                throw new IOException(String.format("Unexpected line at %d: %s", lineIndex, line));
            }
            builder.append(line).append('\n');
            ++lineIndex;
        }

        to.add(builder.toString().trim());
        return endOfFile ? -1 : (lineIndex + 1);
    }

    @Override
    public String toString() {
        return String.join("\n", lines);
    }
}
