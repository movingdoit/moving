package org.base.component.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.base.component.init.LogConfig;
import org.springframework.stereotype.Component;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Coordinate;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;

@Component
public class ImageUtil {

	/** 文件大小 */
	public static final String FILE_SIZE = "FILE_SIZE";
	/** 返回成功的消息 */
	public static final String SUCCESS_MSG = "OK";
	/** 保存返回的消息 */
	public static final String RESULT_MSG = "RESULT_MSG";
	/** 保存后的dfs id */
	public static final String ORI_IMG_DFS_ID = "ORI_IMG_DFS_ID";
	/** 保存后的水印图 */
	public static final String WATERMARKED_IMG= "WATERMARKED_IMG";
	
	/** 商品缩略图 */
	public static final int COMMODITY_THUMB_TYPE = 0;
	/** 活动缩略图 */
	public static final int ACTIVETY_THUMB_TYPE = 1;
	/** 门店广告图片 */
	public static final int ADVERT_THUMB_TYPE = 2;
	/** 商家信息 */
	public static final int ORGAN_THUMB_TYPE = 3;

	/** 上传的图片大小限制 */
	public static final String UPLOAD_IMAGE_SIZE_LIMIT = "2*1024*1024";// 字节

	/** 上传的图片类型限制 */
	public static final String UPLOAD_IMAGE_TYPE_LIMIT = "jpg,jpeg,png,bmp,gif";



	/** 源图压缩比,0到1之间的小数 */
	public static final String IMAGE_COMPRESS_QUALITY_RATE = "0.9";

	/** 图片压缩的规格（宽 和 高）单位像素，用半角逗号“,”隔开。如：宽x高,宽x高 */
	public static final String COMMODITT_THUMBING_IMG_WIDTH_HEIGHT = "350x350,54x54,750x600,120x120,100x100,170x170,50x50";

	/** 活动图片压缩规格 */
	public static final String ACTIVITY_THUMBING_IMG_WIDTH_HEIGHT = "64x48,750x359,120*73,239x145,350*180,1230x400";

	private static final Logger log = LogConfig.getEcosLog();

	private String picType = UPLOAD_IMAGE_TYPE_LIMIT;// 图片类型

	private String compressQulitiy = IMAGE_COMPRESS_QUALITY_RATE;// 源图压缩率
//	private int thumbType = 0;// 默认为商品图片
	private String commodityThumbsWidthHeight = COMMODITT_THUMBING_IMG_WIDTH_HEIGHT;// 商品图片缩略规格
	private String activityThumbsWidthHeight = ACTIVITY_THUMBING_IMG_WIDTH_HEIGHT;// 活动图片缩略规格

	// 各级别缩略图分辨率
	private int[][] thumbsWidthHeight = new int[][] { new int[] { 350, 350 }, new int[] { 54, 54 }, new int[] { 750, 600 }, new int[] { 120, 120 }, new int[] { 100, 100 }, new int[] { 160, 143 },
			new int[] { 170, 170 }, new int[] { 50, 50 } };

	// 各级别缩略图分辨率
	private int[][] lisenceImageWidthHeight = new int[][] { new int[] { 350, 350 }, new int[] { 750, 600 }};
	
	// 门店图片上传规格
	// private int[][] panWidthHeight = new int[][] { new int[] { 100, 100 },
	// new int[] { 50, 50 } };
	private synchronized void setThumbWidthAndHeight(String wh) {
		try {
			if (null == wh || 0 == wh.trim().length()) {
				return;
			}

			List<int[]> whList = new ArrayList<int[]>();
			String split[] = wh.split(",");
			if (null != split && split.length > 0) {
				for (int i = 0; i < split.length; i++) {
					String sp[] = split[i].split("x");
					if (null != sp && sp.length == 2) {
						whList.add(new int[] { Integer.valueOf(sp[0]), Integer.valueOf(sp[1]) });
					}
				}
			}

			this.thumbsWidthHeight = new int[whList.size()][];
			for (int i = 0; i < whList.size(); i++) {
				this.thumbsWidthHeight[i] = whList.get(i);
			}
		} catch (Exception e) {
			log.error("缩略图的宽高格式不正确");
		}
	}

	private void setThumbType(int thumbType) {
		if (thumbType == COMMODITY_THUMB_TYPE) {
			this.setThumbWidthAndHeight(this.commodityThumbsWidthHeight);// 商品图片
		} else if (thumbType == ACTIVETY_THUMB_TYPE) {
			this.setThumbWidthAndHeight(this.activityThumbsWidthHeight);// 活动货品
		} else {
			this.setThumbWidthAndHeight(this.commodityThumbsWidthHeight);// 商品图片
		}
	}


	public boolean isPic(String picPath) {
		if (LogicUtil.isNullOrEmpty(picPath)) {
			return false;
		}

		String endName = null;// 扩展名
		int i = picPath.lastIndexOf(".");
		if (i > 0) {
			endName = picPath.substring(i + 1);
		}

		if (LogicUtil.isNotNull(endName) && LogicUtil.isNotNull(this.picType)) {
			return this.picType.contains(endName.toLowerCase());
		}

		return false;
	}


//	public void setThumbType(int thumbType) {
//		this.thumbType = thumbType;
//	}


//
//
//
//	public String createCommoDescriptionPicPath(String thirdCatNo, String brandNo, String commoNo) {
//		String partPath = "commodity/";// 存放商品的统一文件夹
//		String descreption = "descreption/";
//		if (LogicUtil.isNotNullAndEmpty(thirdCatNo) && LogicUtil.isNotNullAndEmpty(brandNo) && LogicUtil.isNotNullAndEmpty(commoNo)) {
//			return this.formatDir(this.baseTargetDir) + partPath + thirdCatNo + "/" + brandNo + "/" + commoNo + "/" + descreption;
//		}
//		return null;
//	}
//
//
//	public String createAdvertPicPath(String pageName) {
//		if (LogicUtil.isNotNullAndEmpty(pageName)) {
//			String partPath = "advert/";// 存放广告的统一文件夹
//			// 生成YYYY-MM-DD格式的日期
//			String ymd = DateUtils.formatDate(new Date());
//			// 生成文件路径
//			return this.formatDir(this.baseTargetDir) + partPath + pageName + "/" + ymd.replaceAll("-", "/") + "/";
//		}
//		return null;
//	}
//
//	/**
//	 * 创建 广告图片存放地址
//	 * 
//	 * @param orginId
//	 *            商家Id
//	 * @return
//	 */
//
//	public String createAdvertPicPathNew(String orginIdAndOutletId) {
//		if (LogicUtil.isNotNullAndEmpty(orginIdAndOutletId)) {
//			String partPath = orginIdAndOutletId + "/" + "advert";
//			// 生成YYYY-MM-DD格式的日期
//			String ymd = DateUtils.formatDate(new Date());
//			// 生成文件路径
//			return this.formatDir(this.baseTargetDir) + partPath + "/" + ymd.replaceAll("-", "/") + "/";
//		}
//		return null;
//	}
//
//	/**
//	 * 创建 门店基本信息图片存放地址
//	 * 
//	 * @param orginId
//	 *            商家Id
//	 * @return
//	 */
//
//	public String createPanoInfoPicPath(String orginIdAndOutletId) {
//		if (LogicUtil.isNotNullAndEmpty(orginIdAndOutletId)) {
//			String partPath = orginIdAndOutletId + "/" + "panoInfo";
//			// 生成文件路径
//			return this.formatDir(this.baseTargetDir) + partPath + "/";
//		}
//		return null;
//	}
//
//	/**
//	 * 创建 商家信息图片存放地址(logo,营业执照等)
//	 * 
//	 * @param organId
//	 *            商家Id
//	 * @return
//	 */
//
//	public String createOrganPicPath(String organId) {
//		if (LogicUtil.isNotNullAndEmpty(organId)) {
//			String partPath = organId + "/" + "base";
//			// 生成文件路径
//			return this.formatDir(this.baseTargetDir) + partPath + "/";
//		}
//		return null;
//	}
//
//
//	public String createBrandPicPath(String brandNo) {
//		if (LogicUtil.isNotNullAndEmpty(brandNo) && brandNo.length() >= 4) {
//			String partPath = "brand/";// 存放品牌的统一文件夹
//			// 取品牌编号后2位作为图片文件夹名
//			String partNo1 = brandNo.substring(brandNo.length() - 4, brandNo.length() - 2);
//			String partNo2 = brandNo.substring(brandNo.length() - 2, brandNo.length());
//			return this.formatDir(this.baseTargetDir) + partPath + partNo1 + "/" + partNo2 + "/";
//		}
//		return null;
//	}
//
//
//	public String createSpecPropValOptionPicPath(String specPropNo) {
//		String partPath = "specprop/";// 存放规格属性值选项的指示图片
//		if (LogicUtil.isNotNullAndEmpty(specPropNo)) {
//			return this.formatDir(this.baseTargetDir) + partPath + specPropNo + "/";
//		}
//		return null;
//	}
//
//
//	public String createActivityPicPath(String activityType) {
//		String partPath = "activity/";// 存放规格属性值选项的指示图片
//		if (LogicUtil.isNotNullAndEmpty(activityType)) {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			return this.formatDir(this.baseTargetDir) + partPath + activityType + "/" + sdf.format(new Date()) + "/";
//		}
//		return null;
//	}


	public String saveImgFile(String imgSavingPath, byte[] imgData) {
		String targetDir = this.getFileDir(imgSavingPath);
		String newImgName = this.getNewFileName(imgSavingPath, "", true);

		// 保存文件
		FileOutputStream fos = null;
		try {
			File f = new File(targetDir);
			if (!f.exists()) {
				f.mkdirs();
			}

			fos = new FileOutputStream(targetDir + newImgName);
			fos.write(imgData); // 写文件到本地

		} catch (IOException e) {
			log.error("save file error,saving path is" + imgSavingPath, e);
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				log.error("save file error,saving path is" + imgSavingPath, e);
			}
		}

		return targetDir + newImgName;
	}

	public String saveImgFileCoupon(String imgSavingPath, byte[] imgData) {
		String targetDir = this.getFileDir(imgSavingPath);
		String newImgName = this.getNewFileName(imgSavingPath, "", true);

		// 保存文件
		FileOutputStream fos = null;
		try {
			File f = new File(targetDir);
			if (!f.exists()) {
				f.mkdirs();
			}

			fos = new FileOutputStream(targetDir + newImgName);
			fos.write(imgData); // 写文件到本地

		} catch (IOException e) {
			log.error("save file error,saving path is" + imgSavingPath, e);
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				log.error("save file error,saving path is" + imgSavingPath, e);
			}
		}

		return newImgName;
	}

	public Map<String, String> thumbingAndSavingImgFiles(String originalImgPath, byte[] originalImgData,int thumbType) {
		this.setThumbType(thumbType);// 设置压缩商品图片或活动图片，或者别的图片。
		Map<String, String> result = new HashMap<String, String>();
		try {
			String targetDir = this.getFileDir(originalImgPath);

			for (int i = 0; i < this.thumbsWidthHeight.length; i++) {
				int[] wh = this.thumbsWidthHeight[i];

				// 图片名称
				String targetImgPath = targetDir + this.getNewFileName(originalImgPath, wh[0] + "x" + wh[1], false);

				InputStream is = new ByteArrayInputStream(originalImgData);
				Thumbnails.of(is).size(wh[0], wh[1]).outputQuality(Double.valueOf(this.compressQulitiy)).keepAspectRatio(true).toFile(targetImgPath);
			}
			result.put(ORI_IMG_DFS_ID, originalImgPath);

		} catch (Exception e) {
			result.put(RESULT_MSG, "Image thumbing failed.");
			log.error("Image thumbing failed.", e);
		}

		result.put(FILE_SIZE, originalImgData.length + "");
		result.put(RESULT_MSG, SUCCESS_MSG);
		return result;
	}


	public Map<String, String> thumbingAndSavingAllImgFiles(String originalImgSavingPath, byte[] originalImgData, boolean addWhite,int thumbType) {
		this.setThumbType(thumbType);// 设置压缩商品图片或活动图片，或者别的图片。
		Map<String, String> result = new HashMap<String, String>();
		try {
			String targetDir = this.getFileDir(originalImgSavingPath);
			String targetPath = this.saveImgFile(originalImgSavingPath, originalImgData);// 保存源图
			log.info("============== thumbingAndSavingAllImgFiles targetPath:  =============" +targetPath);
			for (int i = 0; i < this.thumbsWidthHeight.length; i++) {
				int[] wh = this.thumbsWidthHeight[i];
				InputStream is = new ByteArrayInputStream(originalImgData);
				String targetImgPath = targetDir + this.getNewFileName(targetPath, wh[0] + "x" + wh[1], false);
				Thumbnails.of(is).size(wh[0], wh[1]).outputQuality(Double.valueOf(this.compressQulitiy)).keepAspectRatio(true).toFile(targetImgPath);
				if (addWhite) {
					File f = new File(targetImgPath);
					BufferedImage bi = ImageIO.read(f);
					BufferedImage image = new BufferedImage(wh[0], wh[1], BufferedImage.TYPE_INT_RGB);
					Graphics2D g = image.createGraphics();
					g.setColor(Color.white);
					g.fillRect(0, 0, wh[0], wh[1]);
					if (wh[0] == bi.getWidth(null))
						g.drawImage(bi, 0, (wh[1] - bi.getHeight(null)) / 2, bi.getWidth(null), bi.getHeight(null), Color.white, null);
					else
						g.drawImage(bi, (wh[0] - bi.getWidth(null)) / 2, 0, bi.getWidth(null), bi.getHeight(null), Color.white, null);
					g.dispose();
					ImageIO.write(image, this.getExtName(this.getFileName(targetImgPath)), f);
				}

			}
			result.put(ORI_IMG_DFS_ID, targetPath);

		} catch (Exception e) {
			result.put(RESULT_MSG, "Image thumbing failed.");
			log.error("Image thumbing failed.", e);
		}
		result.put(FILE_SIZE, originalImgData.length + "");
		result.put(RESULT_MSG, SUCCESS_MSG);
		return result;
	}

	/**
	 * 保存图片路径by xinggang
	 * @param originalImgSavingPath
	 * @param originalImgData
	 * @param addWhite
	 * @param thumbType
	 * @return
	 */
	public Map<String, String> SavingFiles(String originalImgSavingPath, byte[] originalImgData, boolean addWhite,int thumbType) {
		this.setThumbType(thumbType);// 设置压缩商品图片或活动图片，或者别的图片。
		Map<String, String> result = new HashMap<String, String>();
		try {
			String targetDir = this.getFileDir(originalImgSavingPath);
			String targetPath = this.saveImgFile(originalImgSavingPath, originalImgData);// 保存源图


			System.out.println("----------路径4-------------"+targetPath);
			result.put(ORI_IMG_DFS_ID, targetPath);

		} catch (Exception e) {
			result.put(RESULT_MSG, "Image thumbing failed.");
			log.error("Image thumbing failed.", e);
		}
		result.put(FILE_SIZE, originalImgData.length + "");
		result.put(RESULT_MSG, SUCCESS_MSG);
		return result;
	}

	/**
	 * @author luohong
	 * @param originalImgSavingPath 原始图片的保存路径
	 * @param originalImgData 原始图片文件的字节流
	 * @param addWhite 处理后的图片是否补白
	 * @param waterPrintImagePath 要使用的水印图
	 * @param targetWaterImgPath　要保存的水印后的图片路径
	 * */
	public Map<String, String> waterMarkAndSavingAllImgFiles(String originalImgSavingPath, byte[] originalImgData, boolean addWhite,int thumbType,String waterPrintImagePath) {
		this.setThumbType(thumbType);// 设置压缩营业执照图片，或者别的图片。
		Map<String, String> result = new HashMap<String, String>();
		try {
			String targetDir = this.getFileDir(originalImgSavingPath);
			String targetPath = this.saveImgFile(originalImgSavingPath, originalImgData);// 保存源图
			String targetWaterImgPath="";
			for (int i = 0; i < this.lisenceImageWidthHeight.length; i++) {
				int[] wh = this.lisenceImageWidthHeight[i];
				InputStream is = new ByteArrayInputStream(originalImgData);
				String targetImgPath = targetDir + this.getNewFileName(targetPath, wh[0] + "x" + wh[1], false);
				targetWaterImgPath = targetDir  + this.getNewFileName(targetPath, wh[0] + "x" + wh[1]+WATERMARKED_IMG, false);
				Thumbnails.of(is).size(wh[0], wh[1]).outputQuality(Double.valueOf(this.compressQulitiy)).keepAspectRatio(true).toFile(targetImgPath);
				if (addWhite) {
					File f = new File(targetImgPath);
					BufferedImage bi = ImageIO.read(f);
					BufferedImage image = new BufferedImage(wh[0], wh[1], BufferedImage.TYPE_INT_RGB);
					Graphics2D g = image.createGraphics();
					g.setColor(Color.white);
					g.fillRect(0, 0, wh[0], wh[1]);
					if (wh[0] == bi.getWidth(null))
						g.drawImage(bi, 0, (wh[1] - bi.getHeight(null)) / 2, bi.getWidth(null), bi.getHeight(null), Color.white, null);
					else
						g.drawImage(bi, (wh[0] - bi.getWidth(null)) / 2, 0, bi.getWidth(null), bi.getHeight(null), Color.white, null);
					g.dispose();
					ImageIO.write(image, this.getExtName(this.getFileName(targetImgPath)), f);
				}
				//加上水印
				watermarkAllOverImage(targetImgPath, targetWaterImgPath, waterPrintImagePath, 0.5f,10);
			}
			result.put(ORI_IMG_DFS_ID, targetPath);
			result.put(WATERMARKED_IMG, targetWaterImgPath);

		} catch (Exception e) {
			result.put(RESULT_MSG, "Image thumbing failed.");
			log.error("Image thumbing failed.", e);
		}

		result.put(FILE_SIZE, originalImgData.length + "");
		result.put(RESULT_MSG, SUCCESS_MSG);
		return result;
	}
	
	/**
	 * 图片裁剪
	 * 
	 * @param sourceImgPath
	 *            源图片路径
	 * @param targetImgPath
	 *            新图片路径
	 * @param position
	 *            位置 0正中间，1中间左边，2中间右边，3底部中间，4底部左边，5底部右边，6顶部中间，7顶部左边，8顶部右边，
	 *            其他为默认正中间
	 * @param width
	 *            裁剪宽度
	 * @param height
	 *            裁剪高度
	 * @throws Exception
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>
	 * @date: 2013-8-29下午07:04:38
	 */

	public boolean crop(String sourceImgPath, String targetImgPath, int position, int width, int height) {
		try {
			Thumbnails.of(sourceImgPath).sourceRegion(this.getPositions(position), width, height).size(width, height).outputQuality(Double.valueOf(this.compressQulitiy)).toFile(targetImgPath);
			return true;
		} catch (Exception e) {
			log.error("crop image failed.", e);
		}

		return false;
	}

	/**
	 * 给图片添加水印
	 * 
	 * @param sourceImgPath
	 *            将被添加水印图片 路径
	 * @param targetImgpath
	 *            含有水印的新图片路径
	 * @param watermarkImg
	 *            水印图片
	 * @param position
	 *            位置 0正中间，1中间左边，2中间右边，3底部中间，4底部左边，5底部右边，6顶部中间，7顶部左边，8顶部右边，
	 *            其他为默认正中间
	 * @param opacity
	 *            不透明度,0完全透明，1完全不透明
	 * @throws Exception
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>
	 * @date: 2013-8-29下午07:00:00
	 */

	public boolean watermark(String sourceImgPath, String targetImgpath, String watermarkImg, int position, float opacity) {
		try {
			Thumbnails.of(sourceImgPath).watermark(this.getPositions(position), ImageIO.read(new File(watermarkImg)), opacity).scale(1.0).outputQuality(Double.valueOf(this.compressQulitiy))
					.toFile(targetImgpath);
			return true;
		} catch (Exception e) {
			log.error("water mark image failed.", e);
		}
		return false;
	}

	/**
	 * 给图片添加水印
	 * 只好自己画出铺满的平铺水印图片啦
	 * */
	public boolean watermarkAllOverImage(String sourceImgPath, String targetImgpath, String watermarkImg,float opacity, int interval) {
		try {
			// 铺满水印图片
			BufferedImage image = Thumbnails.of(ImageIO.read(new File(watermarkImg))).scale(1.0).outputQuality(Double.valueOf(this.compressQulitiy)).rotate(30).asBufferedImage();
			BufferedImage fileBufferedImage = Thumbnails.of(sourceImgPath).scale(1.0).outputQuality(Double.valueOf(this.compressQulitiy)).asBufferedImage();
			Builder<BufferedImage> bfferedImageBuilder = Thumbnails.of(fileBufferedImage).scale(1.0).outputQuality(Double.valueOf(this.compressQulitiy));
			
			int imageHeight = fileBufferedImage.getHeight();
			int imageWidth = fileBufferedImage.getWidth();
			int watermarkimageHeight = image.getHeight();
			int watermarkimageWidth = image.getWidth();
			// 铺满水印图片
			//int height = interval+ watermarkimageHeight
			//int weight = interval + watermarkimageWidth
			for (int height = interval; height < imageHeight; height = height + interval+ watermarkimageHeight) {
				for (int weight = interval; weight < imageWidth; weight = weight + interval + watermarkimageWidth) {

					Position ps = new Coordinate(weight,height);	
					BufferedImage tempImage = bfferedImageBuilder.watermark(ps,image, opacity).asBufferedImage();
					bfferedImageBuilder = Thumbnails.of(tempImage).scale(1.0).outputQuality(Double.valueOf(this.compressQulitiy));
				}
			}
			bfferedImageBuilder.toFile(targetImgpath);
//			Thumbnails.of(sourceImgPath).watermark(this.getPositions(position), ImageIO.read(new File(watermarkImg)), opacity).scale(1.0).outputQuality(Double.valueOf(this.compressQulitiy))
//					.toFile(targetImgpath);
			return true;
		} catch (Throwable e) {
			log.error("water mark image failed.", e);
		}
		return false;
	}

	
	private Positions getPositions(int position) {
		Positions p = Positions.CENTER;
		switch (position) {
		case 0: {
			p = Positions.CENTER;
			break;
		}
		case 1: {
			p = Positions.CENTER_LEFT;
			break;
		}
		case 2: {
			p = Positions.CENTER_RIGHT;
			break;
		}
		case 3: {
			p = Positions.BOTTOM_CENTER;
			break;
		}
		case 4: {
			p = Positions.BOTTOM_LEFT;
			break;
		}
		case 5: {
			p = Positions.BOTTOM_RIGHT;
			break;
		}
		case 6: {
			p = Positions.TOP_CENTER;
			break;
		}
		case 7: {
			p = Positions.TOP_LEFT;
			break;
		}
		case 8: {
			p = Positions.TOP_RIGHT;
			break;
		}
		default: {
			p = Positions.CENTER;
			break;
		}
		}
		return p;
	}

	private String formatDir(String dir) {
		if (null != dir) {
			return dir.trim().replace("\\", "/").replaceAll("/$", "") + "/";
		}
		return "";
	}

	private String formatPath(String filePath) {
		if (null != filePath) {
			return filePath.replace("\\", "/");
		}
		return "";
	}

	public String getFileDir(String filePath) {
		if (null != filePath) {
			filePath = this.formatPath(filePath);
			return filePath.lastIndexOf("/") > -1 ? filePath.substring(0, filePath.lastIndexOf("/") + 1) : "";
		}
		return "";
	}

	private String getFileName(String filePath) {
		if (null != filePath) {
			filePath = this.formatPath(filePath);
			return filePath.lastIndexOf("/") > -1 ? filePath.substring(filePath.lastIndexOf("/") + 1) : filePath;
		}
		return "";
	}

	private String getPreName(String fileName) {
		if (null != fileName && fileName.trim().length() > 0) {
			return fileName.lastIndexOf(".") > 0 ? fileName.substring(0, fileName.lastIndexOf(".")) : "";
		}
		return "";
	}

	private String getExtName(String fileName) {
		if (null != fileName && fileName.trim().length() > 0) {
			return fileName.lastIndexOf(".") > 0 ? fileName.substring(fileName.lastIndexOf(".") + 1) : "";
		}
		return "";
	}

	private String getNewFileName(String filePath, String suffix, boolean isRename) {
		// 获取文件的前名称、后缀名
		String preName = "";
		String extName = this.getExtName(this.getFileName(filePath));

		// 是否重新命名
		if (!isRename) {
			preName = this.getPreName(this.getFileName(filePath));
		} else {
			// 对原文件名作处理，生成新的唯一的文件名
			preName = Md5Utils.hash(filePath + System.currentTimeMillis() + new Random().nextInt());
		}

		// 添加下划线
		if (null != suffix && !suffix.trim().equals("")) {
			suffix = "_" + suffix + "." + extName;
		}

		// 生成新文件名
		return preName + "." + extName + suffix;
	}


	public boolean deleteFile(String filePath) {
		File f = new File(filePath);
		if (f.exists()) {
			return f.delete();
		}
		return false;
	}

//
//	public byte[] getFile(String filePath) {
//		try {
//			new File(filePath);
//			FileInputStream fis = new FileInputStream(new File("E:/桌面存放/pic/4.jpg"));
//			byte[] in = new byte[fis.available()];
//			fis.read(in);
//			return in;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return new byte[0];
//	}

//	public void testDeleteFile() {
//		boolean flag = this.deleteFile("d:/1/11.jpg");
//		if (flag) {
//			System.out.println("删除成功");
//		} else {
//			System.out.println("删除失败");
//		}
//	}
//
//	public void testGetFile() {
//		byte[] data = this.getFile("d:/1/1.jpg");
//		System.out.println("数据字节长度：" + data.length);
//	}
//
//	public void testSaveImage() {
//		try {
//			FileInputStream fis = new FileInputStream(new File("d:/1/1.jpg"));
//			byte[] in = new byte[fis.available()];
//			fis.read(in);
//			String dfsid = this.saveImgFile("d:/1/2.jpg", in);
//			System.out.println(dfsid);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	public void thumbingAndSavingImgFiles() {
//		try {
//			FileInputStream fis = new FileInputStream(new File("d:/1/1.jpg"));
//			byte[] in = new byte[fis.available()];
//			fis.read(in);
//
//			Map<String, String> infoMap = this.thumbingAndSavingImgFiles("d:/1/1.jpg", in);
//			System.out.println(infoMap.get(ORI_IMG_DFS_ID));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void thumbingAndSavingAllImgFiles() {
//		try {
//			FileInputStream fis = new FileInputStream(new File("d:\\21.jpg"));
//			byte[] in = new byte[fis.available()];
//			fis.read(in);
//
//			Map<String, String> infoMap = this.thumbingAndSavingAllImgFiles("D://0dda74ffd7cf7823.jpg", in, true);
//			System.out.println(infoMap.get(ORI_IMG_DFS_ID));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}


	public Map<String, String> thumbingAndSavingImgFile(String imgSavingPath, byte[] originalImgData, int width, int height,int thumbType) {
		this.setThumbType(thumbType);// 设置压缩商品图片或活动图片，或者别的图片。
		Map<String, String> result = new HashMap<String, String>();
		try {

			InputStream is = new ByteArrayInputStream(originalImgData);
			Thumbnails.of(is).size(width, height).outputQuality(Double.valueOf(this.compressQulitiy)).keepAspectRatio(true).toFile(imgSavingPath);
		} catch (Exception e) {
			result.put(RESULT_MSG, "Image thumbing failed.");
			log.error("Image thumbing failed.", e);
		}

		result.put(FILE_SIZE, originalImgData.length + "");
		result.put(RESULT_MSG, SUCCESS_MSG);
		return result;
	}


//	public String createCommoPicPath(String thirdCatNo, String brandNo, String commoNo) {
//		String partPath = "commodity/";// 存放商品的统一文件夹
//		if (LogicUtil.isNotNullAndEmpty(thirdCatNo) && LogicUtil.isNotNullAndEmpty(brandNo) && LogicUtil.isNotNullAndEmpty(commoNo)) {
//			return this.formatDir(this.baseTargetDir) + partPath + thirdCatNo + "/" + brandNo + "/" + commoNo + "/";
//		}
//		return null;
//	}
//
//
//	public String createSpecPropPicPath(String thirdCatNo, String brandNo, String commoNo, String propNo, String valOptionNo) {
//		String partPath = "commodity/";// 存放商品中规格属性图片的统一文件夹
//		if (LogicUtil.isNotNullAndEmpty(thirdCatNo) && LogicUtil.isNotNullAndEmpty(brandNo) && LogicUtil.isNotNullAndEmpty(commoNo) && LogicUtil.isNotNullAndEmpty(propNo)
//				&& LogicUtil.isNotNullAndEmpty(valOptionNo)) {
//
//			return this.formatDir(this.baseTargetDir) + partPath + thirdCatNo + "/" + brandNo + "/" + commoNo + "/" + propNo + "_" + valOptionNo + "/";
//		}
//		return null;
//	}

//	// public int[][] getPanWidthHeight() {
//	// return panWidthHeight;
//	// }
//
	public static void main(String[] args) throws Exception {

		ImageUtil util = new ImageUtil();
		util.watermarkAllOverImage("/home/phenix/图片/1.jpg","/home/phenix/图片/result.jpg" , "/home/phenix/图片/1.png", 0.5f, 10);
	}
}
