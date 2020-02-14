package tester;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuestionsReader {
    private String[] lines;
    private int[] answers;

    private int questionSize;

    public QuestionsReader(String path, int variantsCount) throws IOException {
        System.out.println("test");

        List<String> list = new ArrayList<>(32);
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            if (line.trim().length() > 0) {
                list.add(line);
            }
        }

        br.close();
        fr.close();

        questionSize = 1 + variantsCount;

        if (list.size() % questionSize != 0) {
            throw new IOException("Wrong questions count");
        }

        // TODO check if several variants are equal

        lines = list.toArray(new String[0]);
        answers = new int[getQuestionsCount()];

        for (int i = 0; i < getQuestionsCount(); ++i) { // TODO optimize
            String[] variants = new String[questionSize - 1];

            for (int j = 0; j < questionSize - 1; ++j) {
                variants[j] = lines[i * questionSize + j + 1];
            }

            String answer = variants[0];

            List<String> shuffled = Arrays.asList(variants);
            Collections.shuffle(shuffled);

            answers[i] = shuffled.indexOf(answer);

            for (int j = 0; j < shuffled.size(); ++j) {
                lines[i * questionSize + j + 1] = shuffled.get(j);
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

    @Override
    public String toString() {
        return String.join("\n", lines);
    }
}
