package com.melanesian.reflection;

import com.melanesian.logicparser.tokenizer.Tokenizer;
import com.melanesian.param.Address;
import com.melanesian.param.City;
import com.melanesian.param.ClientAddress;
import com.melanesian.param.Province;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class NestedReflectionTest extends TestCase {

    private NestedReflection nestedReflection;

    @Before
    public void initialize() {
        nestedReflection = new NestedReflection();
    }

    @Test
    public void guns() {
        Tokenizer tokenizer = new Tokenizer("addressName == 'something' && cityName == 'city of duran'");
        tokenizer.process();
        System.out.println(tokenizer);
    }

    @Test
    public void testInvokingMethod() {
        NumberParam numberParam = new NumberParam();

        Assert.assertNull(nestedReflection.getObject(numberParam, "stringCallback(1, 'terlalu lama sendiri')"));

        Assert.assertEquals("My name is Melanesian, i'am 23 years old, my sallary is around 2000000.0 Rupiah and i already have corona",
                nestedReflection.getObject(numberParam, "aStatement(23, 2000000D, true)"));

        Assert.assertEquals("My name is Melanesian, i'am 17 years old, my sallary is around 2000000.0 Rupiah and i dont have corona",
                nestedReflection.getObject(numberParam, "aStatement(17, 2000000D, false)"));
    }

    @Test
    public void testGet() {
        ClientAddress clientAddress = createClientAdress();
        Assert.assertEquals( "province number 2", nestedReflection.getObject(clientAddress, "addresses.2.province.provinceName"));
        Assert.assertEquals( "city number 1", nestedReflection.getObject(clientAddress, "addresses.1.city.cityName"));
        Assert.assertEquals( "testing melanesian reflection", nestedReflection.getObject(clientAddress, "detailAdress"));
    }

    @Test
    public void testGetField() throws IllegalAccessException{
        Province province = createProvince();
        Field field = nestedReflection.getField(province.getClass(), "provinceName");
        field.setAccessible(true);
        Assert.assertEquals("SOME PROVINCE NAME", field.get(province).toString());

    }

    @Test
    public void testGetWithSingleParamArrayFiltering() {
        ClientAddress clientAddress = createClientAdress();
        Assert.assertEquals("address name number 0",
                nestedReflection.getObject(clientAddress, "addresses.SPArrayFilter{addressName.equals('address name number 0')}.addressName"));
        Assert.assertEquals("address name number 1",
                nestedReflection.getObject(clientAddress, "addresses.SPArrayFilter{addressName == 'address name number 1'}.addressName"));
        Assert.assertEquals("address name number 2",
                nestedReflection.getObject(clientAddress, "addresses.SPArrayFilter{addressName == 'address name number 2'}.addressName"));
        Assert.assertEquals("address name number 3",
                nestedReflection.getObject(clientAddress, "addresses.SPArrayFilter{addressName == 'address name number 3'}.addressName"));
        Assert.assertEquals("address name number 4",
                nestedReflection.getObject(clientAddress, "addresses.SPArrayFilter{addressName == 'address name number 4'}.addressName"));
    }

    @Test
    public void testGetWithSingleParamArrayFiltering_CannotFind() {
        ClientAddress clientAddress = createClientAdress();
        Assert.assertNull(nestedReflection.getObject(clientAddress, "addresses.SPArrayFilter{addressName == 'address namumber 0'}"));
    }


    public class NumberParam {
        @SuppressWarnings("unused method. This method called by reflection")
        public void stringCallback(int value, String statement) {
            System.out.println(statement+" "+value);
        }

        @SuppressWarnings("unused method. This method called by reflection")
        public String aStatement(int value, Double doubleValue, boolean isHaveCorona) {
            return "My name is Melanesian, i'am "+value+
                    " years old, my sallary is around "+doubleValue+" Rupiah "
                    +"and i "+(isHaveCorona?"already have":"dont have")+" corona";
        }
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