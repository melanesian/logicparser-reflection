package com.melanesian.logicparser;

import com.melanesian.param.Address;
import com.melanesian.param.City;
import com.melanesian.param.ClientAddress;
import com.melanesian.param.Province;
import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LogicParserTest {

    @Test
    public void parseBussinessRule() throws ScriptException {
        LogicParser logicParser = new LogicParser();
        String rules = "<addresses.0.province.provinceName> == 'province number 0'";
        Assert.assertTrue(logicParser.parseBussinessRule(createClientAdress(), rules));
    }

    @Test
    public void parseBussinessRule_InvokingMethod() throws ScriptException {
        LogicParser logicParser = new LogicParser();
        String rules = "<addresses.0.province.getProvinceName()> == 'province number 0'";
        Assert.assertTrue(logicParser.parseBussinessRule(createClientAdress(), rules));
    }

    @Test
    public void containsObjectTest() throws ScriptException{
        ClientAddress clientAddress = createClientAdress();
        LogicParser logicParser = new LogicParser();
        Assert.assertTrue(logicParser.parseBussinessRule(clientAddress, "<containAddressName(address name number 1)> && <addresses.0.province.getProvinceName()> == 'province number 0'"));
    }

    @Test
    public void testing_Invoke_Single_Boolean() throws ScriptException {
        LogicParser logicParser = new LogicParser();
        boolean logicTest = logicParser.parseBussinessRule(createClientAdress(), "<containAddressName(address name number 1)>");
        Assert.assertTrue(logicTest);
    }

    @Test
    public void testing_Invoke_Single_Boolean_With_2_param() throws ScriptException {
        LogicParser logicParser = new LogicParser();
        boolean logicTest = logicParser.parseBussinessRule(createClientAdress(), "<containAddressNameAndCityName(address name number 1,city number 1)>");
        Assert.assertTrue(logicTest);
    }

    @Test
    public void testing_Invoke_Single_Boolean_With_2_param_Test_False() throws ScriptException {
        LogicParser logicParser = new LogicParser();
        boolean logicTest = logicParser.parseBussinessRule(createClientAdress(), "<containAddressNameAndCityName(address name number 1,city number 2)>");
        Assert.assertFalse(logicTest);
    }

    private ClientAddress createClientAdress() {
        ClientAddress clientAddress = new ClientAddress();
        List<Address> addresses = new ArrayList<Address>();
        for (int i=0; i<5; i++) {
            City city = new City();
            Province province = new Province();
            Address address = new Address();

            city.setCityName("city number "+i);
            city.setCityCode(""+1);

            province.setProvinceName("province number "+i);
            province.setProvinceCode(""+i);
            address.setCity(city);
            address.setProvince(province);
            address.setAddressName("address name number "+i);
            addresses.add(address);
        }
        clientAddress.setAddresses(addresses);
        clientAddress.setDetailAdress("testing melanesian reflection");
        return clientAddress;
    }

    private Province createProvince() {
        Province province = new Province();
        province.setProvinceName("SOME PROVINCE NAME");
        province.setProvinceCode("SOME PROVINCE CODE");
        return province;
    }

}