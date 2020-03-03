package tester;

import java.util.Random;

/**
 *  Tester
 *
 *  Author DMotsniy & Mirniy18
 */

public class VariantsShuffler {
    private int[] variants;

    public VariantsShuffler(int questionsCount, int variantsCount) {
        variants = new int[questionsCount];

        Random rand = new Random();

        for (int i = 0; i < questionsCount; ++i) {
            variants[i] = rand.nextInt(variantsCount);
        }
    }

    public int getVariant(int index) {
        return variants[index];
    }
}
