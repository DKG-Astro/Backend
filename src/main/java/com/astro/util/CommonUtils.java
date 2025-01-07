package com.astro.util;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CommonUtils {

    public static Map<String, String> seperateNames(String fullName) {
        Map<String, String> map = new HashMap();
        if (fullName == null) {
            map.put("fname", "");
            map.put("lname", "");
            return map;
        }
        String[] arr = fullName.split(" ");
        if (arr.length == 1) {
            map.put("fname", arr[0]);
            map.put("lname", "");
        } else if (arr.length > 1) {
            map.put("fname", arr[0]);
            map.put("lname", arr[arr.length - 1]);
        }
        return map;
    }

    public static String returnOfNotNull(String value, String oldValue) {
        if (value != null && !value.isEmpty()) {
            return value;
        }
        return oldValue;
    }

    public static MultipartFile convertBase64ToMultipartFile(String base64String, String fileName) throws Exception {
        byte[] data = Base64.getDecoder().decode(base64String);
        String path = fileName + ".png";
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        outputStream.write(data);
        FileItem fileItem = new DiskFileItem("file", "text/plain", false, file.getName(), (int) file.length(), file.getParentFile());
        fileItem.getOutputStream();
        if (file.exists()) {
            System.out.println("File Exist => " + file.getName() + " :: " + file.getAbsolutePath());
        }
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/image", input.readAllBytes());
        return multipartFile;
    }

    public static Date getDate(String value, String format) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Integer getYearsByDate(String date) {
        Date parse = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            parse = sdf.parse(date);
        } catch (ParseException e) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                parse = sdf.parse(date);
            } catch (Exception ex) {

            }
        }
        if (parse == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(parse.getTime());
        Calendar crnt = Calendar.getInstance();
        int years = crnt.get(Calendar.YEAR) - c.get(Calendar.YEAR);
        return years;
    }

    public static BigDecimal getBigDecimal(String income) {
        BigDecimal incm = null;
        try {
            incm = new BigDecimal(income);
        } catch (Exception e) {

        }
        return incm;
    }

    public static String getDate(Date value, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(value);
    }

    public static String makeNullToBlank(String val) {
        if(val == null){
            val = "";
        }
        return val;
    }

    public static Long getLong(String val) {
        Long incm = null;
        try {
            incm = Long.parseLong(val);
        } catch (Exception e) {

        }
        return incm;
    }

    public static LocalDate convertStringToDateObject(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateString, formatter);
    }
}
