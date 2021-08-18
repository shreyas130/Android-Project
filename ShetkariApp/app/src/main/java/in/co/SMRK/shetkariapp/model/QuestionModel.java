package in.co.SMRK.shetkariapp.model;

/**
 * Created by Jack on 8/28/2017.
 */

public class QuestionModel  {

    private int QuestionId;
    private String QuestionTitle;
    private String QuestionDateTime;




    private String question;
    private String questionDate;
    private String answerTitle;
    private String answerDate;

    public QuestionModel(int questionId, String questionTitle, String questionDateTime) {
        QuestionId = questionId;
        QuestionTitle = questionTitle;
        QuestionDateTime = questionDateTime;
    }

    public QuestionModel(String question, String questionDate, String answerTitle, String answerDate) {
        this.question = question;
        this.questionDate = questionDate;
        this.answerTitle = answerTitle;
        this.answerDate = answerDate;
    }

    public QuestionModel() {
    }

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int questionId) {
        QuestionId = questionId;
    }

    public String getQuestionTitle() {
        return QuestionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        QuestionTitle = questionTitle;
    }

    public String getQuestionDateTime() {
        return QuestionDateTime;
    }

    public void setQuestionDateTime(String questionDateTime) {
        QuestionDateTime = questionDateTime;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionDate() {
        return questionDate;
    }

    public void setQuestionDate(String questionDate) {
        this.questionDate = questionDate;
    }

    public String getAnswerTitle() {
        return answerTitle;
    }

    public void setAnswerTitle(String answerTitle) {
        this.answerTitle = answerTitle;
    }

    public String getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(String answerDate) {
        this.answerDate = answerDate;
    }
}
