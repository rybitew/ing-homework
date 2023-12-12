package pl.ing.homework.grading.model;

public enum AgeGroup {
    AGE_GROUP_1(0, 19),
    AGE_GROUP_2(20,26),
    AGE_GROUP_3(27,35),
    AGE_GROUP_4(36,45),
    AGE_GROUP_5(46,55),
    AGE_GROUP_6(56,65),
    AGE_GROUP_7(65);

    AgeGroup(int ceiling) {
        this.floor = null;
        this.ceiling = ceiling;
    }
    AgeGroup(int floor, int ceiling) {
        this.floor = floor;
        this.ceiling = ceiling;
    }

    private final Integer floor;
    private final Integer ceiling;
    public Integer floor() {
        return floor;
    }
    public Integer ceiling() {
        return ceiling;
    }
}
