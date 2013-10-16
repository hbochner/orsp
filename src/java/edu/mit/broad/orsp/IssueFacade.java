package edu.mit.broad.orsp;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Created with IntelliJ IDEA.
 * User: hbochner
 * Date: 10/3/13
 * Time: 11:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class IssueFacade
{
    JiraProxy proxy = null;
    String    key   = null;

//    String summary, idr;
//    List<String> fundingSource;
//    Map<String, String> PI, PM;

    public IssueFacade(String key) {
        this.key = key;
        proxy = new JiraProxy(key);
    }

    // for creating new items
    public IssueFacade() {
        proxy = new JiraProxy();
    }

    public String getSummary()
            throws IOException
    {
        return proxy.getString("Summary");
    }

    public void setSummary(String value)
            throws IOException
    {
        proxy.setString("Summary", value);
    }

    public String[] getFundingSource()
            throws IOException
    {
        return proxy.getMultiValueString("funding source");
    }

    public List<String> getFundingAsList()
            throws IOException
    {
        List<String> result = new ArrayList<>();
        result.add("-1");
        String[] strs = getFundingSource();
        if (strs == null) {
            return result;
        }

        for (String s : strs) {
            result.add(s);
        }

        return result;
    }

    public void setFundingSource(String[] values)
            throws IOException
    {
        proxy.setMultiValueString("funding source", values);
    }

    public void setFundingSource(String value)
            throws IOException
    {
        String[] values = {value};
        proxy.setMultiValueString("funding source", values);
    }

    public Map<String, String> getPi()
            throws IOException
    {
        return proxy.getMap("Broad PI");
    }

    public void setPiName(String value)
            throws IOException
    {
        proxy.setNameString("Broad PI", value);
    }

    public Map<String, String> getPm()
            throws IOException
    {
        return proxy.getMap("Broad PM");
    }


    public void setPmName(String value)
            throws IOException
    {
        proxy.setNameString("Broad PM", value);
    }

    public String getIdr()
            throws IOException
    {
        return proxy.getValueString("Initial Determination Request?");
    }

    public void setIdr(String value)
            throws IOException
    {
        proxy.setValueString("Initial Determination Request?", value);
    }

    public void copyParams(Map<String, Object> params)
            throws InvocationTargetException, IllegalAccessException
    {
        BeanUtils.copyProperties(this, params);
    }

    public void update()
            throws IOException
    {
        proxy.update();
    }

    public String add() throws IOException {
        key = proxy.addIssue();
        return key;
    }
}
