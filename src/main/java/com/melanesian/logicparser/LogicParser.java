package com.melanesian.logicparser;

import com.melanesian.reflection.MelanesianReflection;
import com.melanesian.reflection.Reflection;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class LogicParser {

    private ScriptEngine scriptEngine;
    private Reflection reflection;

    public LogicParser() {
        ScriptEngineManager seManager = new ScriptEngineManager();
        this.reflection = new MelanesianReflection();
        this.scriptEngine = seManager.getEngineByName("js");

    }

    public LogicParser(ScriptEngine scriptEngine) {
        this.scriptEngine = scriptEngine;
    }

    public LogicParser(Reflection reflection) {
        this.reflection = reflection;
    }

    /**
     *
     * @param bean
     * @param rule
     * @return
     * @throws ScriptException
     */
    public boolean parseBussinessRule(Object bean, String rule) throws ScriptException {
        String[] ruleTokens = rule.split(" ");

        for (int i=0; i<ruleTokens.length; i++) {
            if (ruleTokens[i].contains("<")){
                Object ruleToken = replacingBeanValues(bean, ruleTokens[i]);
                ruleTokens[i] = ruleToken != null ? ruleToken.toString() : "false";
            }
        }
        return (Boolean) scriptEngine.eval(String.join(" ", ruleTokens));
    }

    /**
     *
     * @param bean
     * @param ruleToken
     * @return
     */
    private Object replacingBeanValues (Object bean, String ruleToken) {
        String takeOutParenthess = ruleToken;
        takeOutParenthess = takeOutParenthess.substring(takeOutParenthess.indexOf("<") + 1);
        takeOutParenthess = takeOutParenthess.substring(0, takeOutParenthess.indexOf(">"));
        Object tokenValue = reflection.getObject(bean, takeOutParenthess);
        if (tokenValue != null && (!tokenValue.toString().equalsIgnoreCase("true")
                || !tokenValue.toString().equalsIgnoreCase("false")))
            tokenValue = "'".concat(tokenValue.toString()).concat("'");

        return tokenValue;
    }
}