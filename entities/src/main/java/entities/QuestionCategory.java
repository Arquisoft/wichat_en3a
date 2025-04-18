package entities;

public enum QuestionCategory {
    SPORT("Sport"),
    GEOGRAPHY("Geography"),
    POP_CULTURE("Pop Culture"),
    BIOLOGY("Biology");

    private final String displayName;

    QuestionCategory(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
