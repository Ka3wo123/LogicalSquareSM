package pl.logicalsquare.IOproject.domain;

import lombok.Getter;

@Getter
public enum AirplaneTrafficState {
    INIT("0", 0),
    TAXIING("A", 1), IMMOBILISING("E", 1), NOT_IMMOBILISING("I", 1), STOP("O", 1),
    ENGINE_ON("A", 2), OPEN("E", 2), STEPS("I", 2), ENGINE_OFF("O", 2),
    UNLOADING("A", 3), LOCKED("E", 3), CLEANED("I", 3), EMPTY("O", 3);

    private String sentence;
    private int level;
    AirplaneTrafficState(String relation, int level) {
        this.sentence = relation;
        this.level = level;
    }

    public static AirplaneTrafficState getState(int level, String sentence) {
        for (AirplaneTrafficState state : values()) {
            if(state.level == level && state.sentence.equals(sentence)) {
                return state;
            }
        }
        throw new IllegalStateException("No state with level " + level + " and sentence " + sentence);
    }


}
