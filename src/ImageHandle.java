/**
 * @author 墨箫君
 * @version 1.0
 */
import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageHandle {
    public static boolean mergeImage(File[] files, int type, String targetFile) {
        int len = files.length;
        if (len < 1) {
            throw new RuntimeException("图片数量为0，不可以执行拼接");
        }
        // File[] src = new File[len];
        BufferedImage[] images = new BufferedImage[len];
        int[][] ImageArrays = new int[len][];

        for (int i = 0; i < len; i++) {
            try {
                // src[i] = new File(files[i]);
                images[i] = ImageIO.read(files[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            int width = images[i].getWidth();
            int height = images[i].getHeight();
            ImageArrays[i] = new int[width * height];
            ImageArrays[i] = images[i].getRGB(0, 0, width, height, ImageArrays[i], 0, width);
        }

        int newHeight = 0;
        int newWidth = 0;
        for (int i = 0; i < images.length; i++) {
            // 横向
            if (type == 1) {
                newHeight = newHeight > images[i].getHeight() ? newWidth : images[i].getHeight();
                // 错误代码，原先没有添加+ 号
                newWidth += images[i].getWidth();
            } else if (type == 2) {
                // 纵向
                newWidth = newHeight > images[i].getWidth() ? newWidth : images[i].getWidth();
                newHeight += images[i].getHeight();
            }
        }

        if (type == 1 && newWidth < 1) {
            return false;
        }

        if (type == 2 && newHeight < 1) {
            return false;
        }


        try {
            BufferedImage ImageNew = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            int height_i = 0;
            int width_i = 0;
            for (int i = 0; i < images.length; i++) {
                if (type == 1) {
                    ImageNew.setRGB(width_i, 0, images[i].getWidth(), newHeight, ImageArrays[i], 0, images[i].getWidth());
                    width_i += images[i].getWidth();
                } else if (type == 2) {
                    ImageNew.setRGB(0, height_i, newWidth, images[i].getHeight(), ImageArrays[i], 0, newWidth);
                    height_i += images[i].getHeight();
                }
            }
            // 输出想要的图片
            ImageIO.write(ImageNew, targetFile.split("\\.")[1], new File(targetFile));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {

        File[] files = new File("C:\\Users\\Flute\\Desktop\\健康码").listFiles();
        //健康码行程码放的文件夹的路径，并且里面不能有其他文件

        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().replaceAll("(.jpg|.png|.bmp|.gif)+", "").length() != files[i].getName().length()) {
                System.out.println(files[i].getName());
            }
        }
        mergeImage(files, 2, "F:\\软件201某某20101234.jpg");//拼接好的文件放在F盘

        List<Map<String, String>> lists = new ArrayList<Map<String, String>>();

        Map<String, String> map1 = new HashMap<>();
        map1.put("id", "1");
        map1.put("cpid", "0");
        map1.put("text", "One Level");

        Map<String, String> map2 = new HashMap<>();
        map2.put("id", "2");
        map2.put("cpid", "1");
        map2.put("text", "Two Level");


        Map<String, String> map3 = new HashMap<>();
        map3.put("id", "3");
        map3.put("cpid", "2");
        map3.put("text", "Three Level");

        lists.add(map1);
        lists.add(map2);
        lists.add(map3);
        List<Map<String, String>> listUp = new ArrayList<Map<String, String>>();
        List<Map<String, String>> listTemp = new ArrayList<Map<String, String>>();
        for (int i = 0; i < lists.size(); i++) {
            Map<String, String> map = lists.get(i);
            if (map.get("cpid") == "0") {
                listUp.add(map);
                lists.remove(i);
                System.out.println(map.get("text"));
            }
        }

        ItentorMap(listUp, lists, listTemp);

        Font font = new Font("微软雅黑", Font.BOLD, 70);                     //水印字体
        String srcImgPath = "F:\\软件201某某20101234.jpg"; //拼接好的图片位置
        String tarImgPath = "C:\\Users\\Flute\\Desktop\\F:\\软件201某某20101234.jpg"; //待存储的地址
        String waterMarkContent = "软件201某某20101234";  //水印内容
        Color color = new Color(0, 0, 0, 255);                               //水印图片色彩以及透明度
        new WaterMarkUtils().addWaterMark(srcImgPath, tarImgPath, waterMarkContent, color, font);
    }

    private static void ItentorMap(List<Map<String, String>> listUp, List<Map<String, String>> listDown, List<Map<String, String>> listTemp) {
        System.out.println("listUp:" + System.identityHashCode(listUp));
        System.out.println("listTemp:" + System.identityHashCode(listTemp));
        listTemp.clear();
        for (int i = 0; i < listUp.size(); i++) {
            Map<String, String> map = listUp.get(i);
            for (int j = 0; j < listDown.size(); j++) {
                Map<String, String> mapj = listDown.get(j);
                if (map.get("id") == mapj.get("cpid")) {
                    listTemp.add(mapj);
                    listDown.remove(j);
                    System.out.println(mapj.get("text"));
                }
            }

        }
        listUp.clear();
        // 必须这样写，如果写成listUp=listTemp，会导致2个list集合的内存地址一样。
        for (int i = 0; i < listTemp.size(); i++) {
            listUp.add(listTemp.get(i));
        }
        System.out.println("listUp:" + System.identityHashCode(listUp));
        System.out.println("listTemp:" + System.identityHashCode(listTemp));

        if (listDown.size() > 0) {
            ItentorMap(listUp, listDown, listTemp);
        }
    }

}