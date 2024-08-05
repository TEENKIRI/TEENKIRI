package com.beyond.teenkiri.subject.domain;

// 숫자만 넣는게 불가!
public enum Grade {
    GRADE_1, GRADE_2, GRAGE_3, GRADE_4, GRADE_5, GRADE_6;

//    GRADE_1(1),
//    GRADE_2(2),
//    GRADE_3(3),
//    GRADE_4(4),
//    GRADE_5(5),
//    GRADE_6(6);
//
//    private final int value;
//
//    Grade(int value) {
//        this.value = value;
//    }
//
//    public int getValue() {
//        return value;
//    }
//
//    public static Grade fromValue(int value) {
//        for (Grade grade : Grade.values()) {
//            if (grade.getValue() == value) {
//                return grade;
//            }
//        }
//        throw new IllegalArgumentException("Unexpected value: " + value);
//    }
}
