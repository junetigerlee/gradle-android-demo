
package cn.com.incito.driver.models;

import java.io.Serializable;

/**
 * ModelSelectedCity
 * 
 * @description ModelSelectedCity
 * @author qiujiaheng
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class ModelSelectedCity implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 8565402845533693604L;

    public String province;// 省

    public int provinceResId;// /县

    public String city;// 市

    public String country;// /县

    @Override
    public boolean equals(Object o) {
        ModelSelectedCity another = (ModelSelectedCity) o;
        if (province.equals(another.province) && city.endsWith(another.city) && country.equals(another.country)) {
            return true;
        }
        return false;
    }

}
