package com.melanesian.reflection.interpreter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SPArrayFilterTest {

    @Test
    public void invokeExpression() throws IllegalAccessException{
        List<TestingClassNumberOne> arrayClassNumberOne = new ArrayList<>();
        TestingClassNumberOne numberOne = new TestingClassNumberOne();
        TestingClassNumberOne numberOne1 = new TestingClassNumberOne();
        numberOne.setFieldNumberOne("code_number_one");
        numberOne.setFieldNumberTwo("field_two_at_class_number_one");
        numberOne.setFieldNumberThree("field_three_at_class_number_one");

        numberOne1.setFieldNumberOne("code_number_two");
        numberOne1.setFieldNumberTwo("field_two_at_class_number_two");
        numberOne1.setFieldNumberThree("field_three_at_class_number_two");
        arrayClassNumberOne.add(numberOne);
        arrayClassNumberOne.add(numberOne1);
        Expression expression = new SPArrayFilter(arrayClassNumberOne, "arr{fieldNumberOne = code_number_two}");

        TestingClassNumberOne filteredObject = (TestingClassNumberOne) expression.invokeExpression();
        System.out.println(filteredObject.getFieldNumberOne().concat(", ")
                .concat(filteredObject.getFieldNumberTwo())
                .concat(", ").concat(filteredObject.getFieldNumberThree()));


    }

    private class TestingClassNumberOne {

        private String fieldNumberOne;
        private String fieldNumberTwo;
        private String fieldNumberThree;

        private String getFieldNumberOne() {
            return fieldNumberOne;
        }

        private void setFieldNumberOne(String fieldNumberOne) {
            this.fieldNumberOne = fieldNumberOne;
        }

        private String getFieldNumberTwo() {
            return fieldNumberTwo;
        }

        private void setFieldNumberTwo(String fieldNumberTwo) {
            this.fieldNumberTwo = fieldNumberTwo;
        }

        private String getFieldNumberThree() {
            return fieldNumberThree;
        }

        private void setFieldNumberThree(String fieldNumberThree) {
            this.fieldNumberThree = fieldNumberThree;
        }
    }
}