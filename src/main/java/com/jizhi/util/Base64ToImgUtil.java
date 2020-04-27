package com.jizhi.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图片的上传工具
 */

import org.springframework.stereotype.Component;

import net.coobird.thumbnailator.Thumbnails;
import sun.misc.BASE64Decoder;


@Component
public class Base64ToImgUtil {
	/*
	 * public String Base64ToImg(String base64,HttpServletRequest request) {
	 * BASE64Decoder decoder = new BASE64Decoder(); byte[] byteImg; try {
	 * //若前端传来的图片是空 if(StringUtils.isEmpty(base64)) { return null; }else {
	 * //把前端传来的base64转成字节码 byteImg = decoder.decodeBuffer(base64); //生成文件名 UUID
	 * uuid=UUID.randomUUID(); String files = uuid.toString().replaceAll("-", "")+
	 * ".jpg"; //获取工程内部的文件夹路径（需要在你的项目中的webapp下面创建文件夹pics） String parentPath =
	 * "E:\\Ericcccccccccccccccccccccc\\pics"; //文件上传后的网络位置（保存到数据库，便于日后根据路径查看）
	 * String imgLocation = parentPath+"\\"+files; // 生成图片 File imageFile = new
	 * File(parentPath,files); if(!imageFile.getParentFile().exists()){
	 * imageFile.getParentFile().mkdir(); } OutputStream op = new
	 * FileOutputStream(imageFile); op.write(byteImg); op.flush(); op.close();
	 * return files; } } catch (IOException e) { e.printStackTrace(); } return null;
	 * 
	 * 
	 * }
	 */
	private static final Logger log = LoggerFactory.getLogger(Base64ToImgUtil.class);
	public String base64(String imageFile) {
        String type = imageFile.split(",")[0].split("/")[1].split(";")[0];
        imageFile = imageFile.split(",")[1];
        BASE64Decoder decoder = new BASE64Decoder();
        // Base64解码
        byte[] imageByte = null;
        try {
            imageByte = decoder.decodeBuffer(imageFile);
            for (int i = 0; i < imageByte.length; ++i){
                if (imageByte[i] < 0) {// 调整异常数据
                    imageByte[i] += 256;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //把图片压缩至500k
        imageByte=compressPicForScale(imageByte,500);
        return Bytes2File(imageByte,type);
    }
	
    public static byte[] compressPicForScale(byte[] imageBytes, long desFileSize) {
        if (imageBytes.length < desFileSize * 1024) {
            return imageBytes;
        }
        long srcSize = imageBytes.length;
        double accuracy = getAccuracy(srcSize / 1024);
        try {
            while (imageBytes.length > desFileSize * 1024) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
                Thumbnails.of(inputStream)
                        .scale(accuracy)
                        .outputQuality(accuracy)
                        .toOutputStream(outputStream);
                imageBytes = outputStream.toByteArray();
            }
        } catch (Exception e) {
        	log.info("图片压缩失败");
           System.out.println("【图片压缩】msg=图片压缩失败!");
        }
        return imageBytes;
    }
	private static double getAccuracy(long size) {
        double accuracy;
        if (size < 900) {
            accuracy = 0.85;
        } else if (size < 2047) {
            accuracy = 0.6;
        } else if (size < 3275) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }

    
    
    public static String Bytes2File(byte[] imageByte , String type)
    {
        String path = null;
       try {
           int length = imageByte.length;
           //追加文件夹
           //File file = new File("E:/Ericcccccccccccccccccccccc/pics/");
           File file = new File("D:/pics/");
           if(!file.exists()){
               file.mkdirs();
           }
           //path = "E:/Ericcccccccccccccccccccccc/pics/"+UUID.randomUUID()+ "." + type;
           path = "D:/pics/"+UUID.randomUUID()+ "." + type;
           FileOutputStream fos = new FileOutputStream(path);//isAppend如果为true，为追加写入，否则为覆盖写入
           fos.write(imageByte,0,length);
           fos.close();
           //path = path.replaceAll("E:/Ericcccccccccccccccccccccc/pics/","/pics/");
           path = path.replaceAll("D:/pics/","/pics/");
       }catch (Exception e){
           e.printStackTrace();
       }
       return path;
    }
	
}
