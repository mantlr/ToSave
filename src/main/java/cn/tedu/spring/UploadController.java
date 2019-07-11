package cn.tedu.spring;



import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Controller
public class UploadController {

	CommonsMultipartResolver c;

	@RequestMapping("upload.do")
	@ResponseBody
	public String upload(
			@RequestParam("file") MultipartFile file,
			HttpServletRequest request
			) throws Exception {
		System.out.println("UploadController.upload()");

		//判断文件是否为空
		boolean isEmpty=file.isEmpty();
		System.out.println("\tisEmpty=" + isEmpty);
		if(isEmpty) {
			throw new RuntimeException("上传失败！上传的文件为空！");
		}

		//检查文件大小
		long filesize=file.getSize();
		System.out.println("filesize:"+filesize);
		if(filesize>1*1024*1024) {
			throw new RuntimeException("上传失败！上传的文件大小超出了限制！");
		}

		//检查文件MIME类型
		String contentType=file.getContentType();
		System.out.println("contentType:"+contentType);
		List<String> types=new ArrayList<String>();
		types.add("image/jpeg");
		types.add("image/png");
		types.add("image/gif");
		
		if(!types.contains(contentType)) {
			throw new RuntimeException("上传失败！不支持上传的文件！");
		}

		//准备文件夹
		String parent=request.getServletContext().getRealPath("upload");
		System.out.println("\tpath=" + parent);

		//获取原始文件名
		String originalFilename=file.getOriginalFilename();

		//确定最终保存使用的文件
		String filename=UUID.randomUUID().toString();
		String suffix="";
		int beginIndex=originalFilename.lastIndexOf(".");
		if(beginIndex!=-1) {
			suffix=originalFilename.substring(beginIndex).toString();
		}

		//执行保存文件
		File dest=new File(parent,filename+suffix);
		file.transferTo(dest);
		return "OK";
	}

}
