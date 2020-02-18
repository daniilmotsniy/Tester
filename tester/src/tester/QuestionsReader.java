package tester;

import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class QuestionsReader {
    private static final byte SALT = -42;

    private String[] lines;
    private byte[] answers;

    private int questionSize;

    private HashMap<Integer, File> pictures;

    public QuestionsReader(String path, int variantsCount) throws IOException {
        List<String> list = readFile(path, variantsCount);

        questionSize = 1 + variantsCount;

        lines = list.toArray(new String[0]);

        if (answers == null) {
            answers = new byte[getQuestionsCount()];

            shuffleAnswers(variantsCount);

            String key = toEncryptedString(answers);

            FileWriter fw = new FileWriter(path + "_");
            BufferedWriter bw = new BufferedWriter(fw);

            bw.append(key).write("\n");

            for (int i = 0; i < lines.length; ++i) {
                bw.append((i & 3) == 0 ? "Q" : "A").append("\n").append(lines[i]).write("\n");
                File picture = pictures == null ? null : pictures.getOrDefault(i / questionSize, null);
                if ((i & 3) == 3 && picture != null) {
                    bw.append("P").append("\n").append(picture.getPath()).write("\n");
                }
            }

            bw.flush();

            bw.close();
            fw.close();
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
        File file = pictures == null ? null : pictures.getOrDefault(questionIndex, null);

        return file == null ? null : ImageIO.read(file);
    }

    private List<String> readFile(String path, int variantsCount) throws IOException {
        List<String> result = new ArrayList<>(32);

        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int lineIndex = 1;

        String key = null;

        for (; (line = br.readLine()) != null; ++lineIndex) {
            line = line.trim();

            if (line.length() == 0) {
                continue;
            }

            if (line.matches("^[0-9]+$")) {
                key = line;

                line = br.readLine();
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
                    result.add(line);
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

//        if (!Objects.equals(previousToken, 'a') || sameTokensInARow != variantsCount) { TODO check
//            throw new IOException("Wrong questions count");
//        }

        if (key != null) {
            answers = toDecryptedArray(key, result.size() / (1 + variantsCount));
        }

        return result;
    }

    private void shuffleAnswers(int variantsCount) throws IOException {
        Random rnd = new Random();

        for (int i = 0; i < getQuestionsCount(); ++i) {
            Set<String> set = new HashSet<>();

            for (int j = 0; j < variantsCount; ++j) {
                if (!set.add(lines[i * questionSize + j + 1])) {
                    throw new IOException("Duplicated variants");
                }
            }

            int answer = answers[i];

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

            answers[i] = (byte) answer;

            for (int j = 0; j < variantsCount; ++j) {
                lines[i * questionSize + j + 1] = lines[i * questionSize + j + 1];
            }
        }
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

    private static String toEncryptedString(byte[] data) {
        byte[] encrypted = encrypt(data);

        StringBuilder builder = new StringBuilder();

        for (byte b : encrypted) {
            builder.append(b);
        }

        return builder.toString();
    }

    private static byte[] toDecryptedArray(String str, int count) {
        byte[] result = new byte[count];

        for (int i = 0; i < count; ++i) {
            result[i] = (byte) (str.charAt(i) - '0');
        }

        return decrypt(result, count);
    }

    private static byte[] encrypt(byte[] in) {
        byte[] en1 = encode(in);

        xorAll(en1);

        return decode(en1, in.length);
    }

    private static byte[] decrypt(byte[] data, int count) {
        return decode(xorAll(encode(data)), count);
    }

    private static byte[] encode(byte[] data) {
        byte[] result = new byte[(data.length - 1) / 4 + 1];

        for (int i = 0; i < data.length; ++i) {
            result[i / 4] |= (byte) (data[i] << ((3 - (i & 0b11)) * 2));
        }

        return result;
    }

    private static byte[] decode(byte[] bytes, int count) {
        byte[] result = new byte[count];

        for (int i = 0; i < count; ++i) {
            result[i] = (byte) ((bytes[i / 4] >>> ((3 - (i & 0b11)) * 2)) & 0b11);
        }

        return result;
    }

    private static byte[] xorAll(byte[] data) {
        for (int i = 0; i < data.length; ++i) {
            data[i] ^= SALT;
        }

        return data;
    }

    @Override
    public String toString() {
        return String.join("\n", lines) + "\n\n" + Arrays.toString(answers);
    }
}
