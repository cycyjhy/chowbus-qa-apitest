package com.chowbus.qa.factory;

import com.chowbus.qa.step.BaseStep;
import com.chowbus.qa.step.HttpStep;

import static java.lang.String.valueOf;

public enum EnumStepFactory {
        HTTP("http") {
                @Override
                public BaseStep create() {
                        return new HttpStep();
                }
        },
        ;




private final String caseType;


        EnumStepFactory(String caseType) {
        this.caseType = caseType;
        }

public static EnumStepFactory createFactory(String caseType) {
        return valueOf(caseType.toUpperCase());
        }

public abstract BaseStep create();
}
