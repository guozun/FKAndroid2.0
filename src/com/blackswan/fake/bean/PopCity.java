package com.blackswan.fake.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PopCity {
	private String regionId;
	private String provinceCode;
	private String popCityCode;
	private String districtCode;
	private String province;
	private String popCity;
	private String district;
	
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getPopCity() {
		return popCity;
	}
	public void setPopCity(String popCity) {
		this.popCity = popCity;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}

	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getPopCityCode() {
		return popCityCode;
	}
	public void setPopCityCode(String popCityCode) {
		this.popCityCode = popCityCode;
	}
	public String getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
	public static final Parcelable.Creator<PopCity> CREATOR = new Creator<PopCity>() {  
        public PopCity createFromParcel(Parcel source) {  
        	PopCity mPopCity = new PopCity();  
        	mPopCity.regionId = source.readString();  
        	mPopCity.province = source.readString();  
        	mPopCity.popCity = source.readString();  
        	mPopCity.district = source.readString();  
        	mPopCity.provinceCode = source.readString();  
        	mPopCity.popCityCode = source.readString();  
        	mPopCity.districtCode = source.readString();  
        	
            return mPopCity;  
        }  
        public PopCity[] newArray(int size) {  
            return new PopCity[size];  
        }  
    };  
      
    public int describeContents() {  
        return 0;  
    }  
    public void writeToParcel(Parcel parcel, int flags) {  
        parcel.writeString(regionId);  
        parcel.writeString(province);  
        parcel.writeString(popCity);  
        parcel.writeString(district);  
        parcel.writeString(provinceCode);  
        parcel.writeString(popCityCode);  
        parcel.writeString(districtCode);  
    }  
}
