package com.melanesian.reflection;

import com.melanesian.reflection.param.Address;
import com.melanesian.reflection.param.City;
import com.melanesian.reflection.param.ClientAddress;
import com.melanesian.reflection.param.Province;
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
public class MelanesianReflectionTest extends TestCase {

    MelanesianReflection melanesianReflection;

    @Before
    public void initialize() {
        melanesianReflection = new MelanesianReflection();
    }

    @Test
    public void testGet() {
        ClientAddress clientAddress = createClientAdress();
        Assert.assertEquals( "province number 2",melanesianReflection.get(clientAddress, "addresses.2.province.provinceName"));
        Assert.assertEquals( "city number 1",melanesianReflection.get(clientAddress, "addresses.1.city.cityName"));
        Assert.assertEquals( "testing melanesian reflection",melanesianReflection.get(clientAddress, "detailAdress"));
    }

    @Test
    public void testGetField() throws IllegalAccessException{
        Province province = createProvince();
        Field field = melanesianReflection.getField(province.getClass(), "provinceName");
        field.setAccessible(true);
        Assert.assertEquals("SOME PROVINCE NAME", field.get(province).toString());

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