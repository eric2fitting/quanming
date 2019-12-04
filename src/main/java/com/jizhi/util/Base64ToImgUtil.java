package com.jizhi.util;

/**
 * 图片的上传工具
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.StringUtils;

import sun.misc.BASE64Decoder;

@Component
public class Base64ToImgUtil {
	public String  Base64ToImg(String base64,HttpServletRequest request) {
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] byteImg;
		try {
			//若前端传来的图片是空
			if(StringUtils.isEmptyOrWhitespaceOnly(base64)) {
				return null;
			}else {
				//把前端传来的base64转成字节码
				byteImg = decoder.decodeBuffer(base64);
				//生成文件名
				UUID uuid=UUID.randomUUID();
				String files = uuid+ ".jpg";
				//获取工程内部的文件夹路径（需要在你的项目中的webapp下面创建文件夹pics）
				String parentPath = request.getSession().getServletContext().getRealPath("/pics");
				//文件上传后的网络位置（保存到数据库，便于日后根据路径查看）
				String imgLocation = parentPath+"\\"+files;
				// 生成图片
	            File imageFile = new File(parentPath,files);
	            if(!imageFile.getParentFile().exists()){
	                imageFile.getParentFile().mkdir();
	            }
	            OutputStream op = new FileOutputStream(imageFile);
	            op.write(byteImg);
	            op.flush();
	            op.close();
	            return imgLocation;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		

	}
	
	
}
