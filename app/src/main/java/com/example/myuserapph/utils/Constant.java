package com.example.myuserapph.utils;

public class Constant {
    public static final String LOW = "Low";
    public static final String NORMAL = "Normal";
    public static final String HIGH = "High";
    public static final String REQUEST_KEY = "date_time_key";
    public static final String DATE_KEY = "date";
    public static final String TIME_KEY = "time";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";
    public static final String HOUR = "hour";
    public static final String MINUTE = "minute";

    public static class item{
        public final static String ADD_PRODUCTS="AddProducts";
        public final static String VIEWPRODUCT="ViewProduct";
        public final static String ADD_CATAGORIS="Add Catagoris";
        public final static String VIEW_ORDERS="View Orders";
        public final static String VIEW_USERS="View Users";

    }

    public static class DbCollection{
        public static final String COLLECTION_PRODUCT = "Products";
        public static final String COLLECTION_PURCHASE = "Purchases";
        public static final String COLLECTION_CATEGORY = "Categories";
        public static final String COLLECTION_ORDERS = "Orders";
        public static final String COLLECTION_USERS = "Users";
        public static final String COLLECTION_CART = "Cart";
    }
    public static class PaymentMehod{
        public static final String COD="Cash On Delivery";
        public static final String ONLINE="ONLENE";
    }
}
