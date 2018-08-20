package com.emapgo.android.demo.module;

import java.util.List;

/**
 * Created by ben on 2018/7/13.
 */

public class TestModel {


    /**
     * City : 110000
     * Version : 18Q1
     * RestrictedArea : [{"GeoID":"1010000001","Brief":"五环路以内（不含）本地机动车工作日尾号限行","Shape":"Polygon","Points":[[[[116.300375,39.780063],[116.299317,39.779461],[116.299253,39.779422]]]],"Links":[101841889,101841981,101842100,101842161]}]
     */

    private String City;
    private String Version;
    private List<RestrictedAreaBean> RestrictedArea;

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public List<RestrictedAreaBean> getRestrictedArea() {
        return RestrictedArea;
    }

    public void setRestrictedArea(List<RestrictedAreaBean> RestrictedArea) {
        this.RestrictedArea = RestrictedArea;
    }

    public static class RestrictedAreaBean {
        /**
         * GeoID : 1010000001
         * Brief : 五环路以内（不含）本地机动车工作日尾号限行
         * Shape : Polygon
         * Points : [[[[116.300375,39.780063],[116.299317,39.779461],[116.299253,39.779422]]]]
         * Links : [101841889,101841981,101842100,101842161]
         */

        private String GeoID;
        private String Brief;
        private String Shape;
        private List<List<List<List<Double>>>> Points;
        private List<Integer> Links;

        public String getGeoID() {
            return GeoID;
        }

        public void setGeoID(String GeoID) {
            this.GeoID = GeoID;
        }

        public String getBrief() {
            return Brief;
        }

        public void setBrief(String Brief) {
            this.Brief = Brief;
        }

        public String getShape() {
            return Shape;
        }

        public void setShape(String Shape) {
            this.Shape = Shape;
        }

        public List<List<List<List<Double>>>> getPoints() {
            return Points;
        }

        public void setPoints(List<List<List<List<Double>>>> Points) {
            this.Points = Points;
        }

        public List<Integer> getLinks() {
            return Links;
        }

        public void setLinks(List<Integer> Links) {
            this.Links = Links;
        }
    }
}
