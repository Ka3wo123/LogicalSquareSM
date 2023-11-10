package pl.logicalsquare.IOproject.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum AirplaneTrafficState {
    INIT("0", 0, true),
    TAXIING("A", 1, true), IMMOBILISING("E", 1, true), NOT_IMMOBILISING("I", 1, true), STOP("O", 1, true),
    ENGINE_ON("A", 2, true), OPEN("E", 2, true), STEPS("I", 2, true), ENGINE_OFF("O", 2, true),
    UNLOADING("A", 3, true), LOCKED("E", 3, true), CLEANED("I", 3, true), EMPTY("O", 3, true);

    private String sentence;
    private int level;
    private boolean isTrue;
//    AirplaneTrafficState(String relation, int level) {
//        this.sentence = relation;
//        this.level = level;
//    }

    AirplaneTrafficState(String sentence, int level, boolean isTrue) {
        this.sentence = sentence;
        this.level = level;
        this.isTrue = isTrue;
    }

    public void setStatus(boolean newStatus) {
        this.isTrue = newStatus;
    }

    public boolean getIsTrue() {
        return isTrue;
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
