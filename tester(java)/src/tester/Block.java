package tester;

public class Block {

    String question;

    String answer1;
    String answer2;
    String answer3;

    Block(String question, String answer1, String answer2, String answer3){
        this.question=question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
    }

    int true_answer;
    int selected_answer = 0;

    public int getTrue_answer() {
        return true_answer;
    }

    public void setTrue_answer(int true_answer) {
        this.true_answer = true_answer;
    }

    public int getSelected_answer() {
        return selected_answer;
    }

    public void setSelected_answer(int selected_answer) {
        this.selected_answer = selected_answer;
    }


}
