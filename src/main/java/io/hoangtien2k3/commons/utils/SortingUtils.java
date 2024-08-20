/*
 * Copyright 2024 author - Hoàng Anh Tiến
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 */
package io.hoangtien2k3.commons.utils;

import io.hoangtien2k3.commons.constants.Constants;
import io.hoangtien2k3.commons.constants.Regex;
import io.hoangtien2k3.commons.model.TokenUser;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SortingUtils {
    // for example
    public static void main(String[] args) {
        String sort = "-username,+id,++object";
        System.out.println(parseSorting(sort, TokenUser.class));
    }

    /**
     * Parses the sorting configuration string and converts it to a database query
     * string.
     *
     * @param sortConfig
     *            the sorting configuration string
     * @param objectClass
     *            the class of the object to be sorted
     * @return the database query string for sorting
     */
    public static String parseSorting(String sortConfig, Class objectClass) {
        List<String> convertSorting =
                convertSorting(sortConfig.replaceAll(Regex.CAMELCASE, Constants.Sorting.FILED_DISPLAY), objectClass);
        if (convertSorting == null || convertSorting.isEmpty()) {
            return null;
        }
        return String.join(
                Constants.Sorting.SPLIT_OPERATOR,
                convertSorting.toString().toLowerCase().replace("[", "").replace("]", ""));
    }

    /**
     * Converts the sorting configuration string to a list of sorting fields.
     *
     * @param sortConfig
     *            the sorting configuration string
     * @param objectClass
     *            the class of the object to be sorted
     * @return the list of sorting fields
     */
    public static List<String> convertSorting(String sortConfig, Class objectClass) {
        if (DataUtil.isNullOrEmpty(sortConfig)) {
            return null;
        }
        List<String> filedNames = getFieldsOfClass(objectClass);
        String[] sortElements = sortConfig.split(Constants.Sorting.SPLIT_OPERATOR);
        List<String> orderList = new LinkedList<>();
        for (String element : sortElements) {
            if (element == null) {
                continue;
            }
            if (element.startsWith(Constants.Sorting.MINUS_OPERATOR)) {
                handleElement(element, Constants.Sorting.MINUS_OPERATOR, filedNames, orderList);
            } else if (element.startsWith(Constants.Sorting.PLUS_OPERATOR)) {
                handleElement(element, Constants.Sorting.PLUS_OPERATOR, filedNames, orderList);
            } else {
                log.error("Filed invalid ", element);
            }
        }
        return orderList;
    }

    /**
     * Handles individual sorting elements and adds them to the order list.
     *
     * @param element
     *            the sorting element
     * @param operator
     *            the sorting operator
     * @param fields
     *            the list of valid fields
     * @param orderList
     *            the list to add the sorting element to
     */
    private static void handleElement(String element, String operator, List<String> fields, List<String> orderList) {
        if (element.length() <= operator.length()) {
            return;
        }
        String field = element.substring(element.indexOf(operator) + operator.length());
        if (fields.contains(field)) {
            orderList.add(field + " " + convertOperator(operator));
        }
    }

    /**
     * Retrieves all field names of the given class.
     *
     * @param object
     *            the class of the object
     * @return the list of field names
     */
    private static List<String> getFieldsOfClass(Class object) {
        List<String> filedNames = new ArrayList<>();
        for (Field field : getAllFields(object)) {
            filedNames.add(field.getName().replaceAll(Regex.CAMELCASE, Constants.Sorting.FILED_DISPLAY));
        }
        return filedNames;
    }

    /**
     * Converts the sorting operator to the corresponding SQL keyword.
     *
     * @param operator
     *            the sorting operator
     * @return the SQL keyword for the sorting operator
     */
    private static String convertOperator(String operator) {
        if (Constants.Sorting.MINUS_OPERATOR.equals(operator)) {
            return Constants.Sorting.DESC;
        } else {
            return Constants.Sorting.ASC;
        }
    }

    /**
     * Retrieves all fields of the given class, including inherited fields.
     *
     * @param clazz
     *            the class of the object
     * @return the array of fields
     */
    private static Field[] getAllFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            Field[] superFields = getAllFields(superClass);
            Field[] allFields = new Field[fields.length + superFields.length];
            System.arraycopy(fields, 0, allFields, 0, fields.length);
            System.arraycopy(superFields, 0, allFields, fields.length, superFields.length);
            fields = allFields;
        }
        return fields;
    }
}
