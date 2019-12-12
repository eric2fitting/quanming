package com.jizhi.util;

/**
 * 图片的上传工具
 */
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.springframework.stereotype.Component;

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
        return Bytes2File(imageByte,type);
    }
    public static String Bytes2File(byte[] imageByte , String type)
    {
        String path = null;
       try {
           int length = imageByte.length;
           //追加文件夹
           File file = new File("E:/Ericcccccccccccccccccccccc/pics");
           if(!file.exists()){
               file.mkdirs();
           }
           path = "E:/Ericcccccccccccccccccccccc/pics/"+UUID.randomUUID()+ "." + type;
           FileOutputStream fos = new FileOutputStream(path);//isAppend如果为true，为追加写入，否则为覆盖写入
           fos.write(imageByte,0,length);
           fos.close();
           path = path.replaceAll("E:/Ericcccccccccccccccccccccc/pics/","/pics/");
       }catch (Exception e){
           e.printStackTrace();
       }
       return path;
    }
	
}
