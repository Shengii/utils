@Test
    public void test() {
        List<List<String>> phoneList = new ArrayList<>();
        File file = new File("C:\\Users\\172609\\Desktop\\新闻封面.log");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                List<String> collect = Arrays.stream(tempString.split("\\+")).collect(Collectors.toList());
                phoneList.add(collect);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        downloadPicture(phoneList);
    }

    public void downloadPicture(List<List<String>> phoneList){
        for (List<String> strings : phoneList) {
            DownloadUtils.downloadPicture(strings.get(0),strings.get(1));
        }
    }
	
	
	package com.kyexpress.erp.kuasheng.provider.util;

import com.google.common.collect.Lists;
import com.kyexpress.framework.utils.StringUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chenruisheng
 * @version 1.0
 * @description
 * @date 2021/9/27 15:24
 */
public class DownloadUtils {
    public static void downloadPicture(String name, String path) {
        URL url = null;
        int imageNumber = 0;

        try {
            url = new URL(path);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            String imageName = "C:\\Users\\172609\\Desktop" + File.separator + name + ".png";

            FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context = output.toByteArray();
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重命名文件名
     * @param oldFilePath 旧文件名称
     * @param newFilePath 新文件名称
     */
    public static void renameFile(String oldFilePath, String newFilePath){
        System.out.println("》》》oldFilePath="+oldFilePath);
        System.out.println("》》》newFilePath="+newFilePath);

        File oldFile = new File(oldFilePath);
        boolean b = oldFile.renameTo(new File(newFilePath));
        System.out.println("b="+b);
    }


    public static Map<String,String> getNewsId(){
        Map<String,String> nameIdMap = new HashMap<>();

        File file = new File("C:\\Users\\172609\\Desktop\\新闻封面.log");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                List<String> collect = Arrays.stream(tempString.split("\\+")).collect(Collectors.toList());
                nameIdMap.put(collect.get(0),collect.get(1));
            }
            reader.close();
            return nameIdMap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return nameIdMap;
    }


    public static void writeFile(List<String> phoneList) throws IOException {
        File f = new File("C:\\Users\\172609\\Desktop\\新闻修改封面SQL.sql");
        if (f.exists()) {
            System.out.print("文件存在");
        } else {
            System.out.print("文件不存在");
            try {
                f.createNewFile();// 不存在则创建  
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedWriter output = new BufferedWriter(new FileWriter(f,true));//true,则追加写入text文本
        try {
            phoneList.forEach(phone->{
                try {
                    output.write(phone);
                    output.write("\r\n");//换行
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e1) {
                }
            }
        }
    }


    public static void testRenameFile(){
        Map<String, String> newsId = getNewsId();
        String path = "D:\\kuasheng\\2022-05";
        File file = new File(path);
        if (!file.isDirectory()){
            return;
        }
        for (String name : file.list()) {
            String id = newsId.get(name);
            if (StringUtils.isNotRealEmpty(id)){
                String oldFilePath = path + File.separator + name;
                String newFilePath = path + File.separator + id;
                renameFile(oldFilePath,newFilePath);
            }
        }
    }

    public static void replementSql(){
        String path = "D:\\kuasheng\\2022-05\\新闻通知封面待替换";
        File file = new File(path);
        if (!file.isDirectory()){
            return;
        }
        List<String> sqlList = Lists.newArrayList();
        for (String name : file.list()) {
            String sql = "update news_notification set cover_img = 'https://ky-im.kye-erp.com/app/news/cover/80/%s' where id = '%s' ;";
            sql = String.format(sql,name,name);
            sqlList.add(sql);
        }
        try {
            writeFile(sqlList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        replementSql();
    }
}
