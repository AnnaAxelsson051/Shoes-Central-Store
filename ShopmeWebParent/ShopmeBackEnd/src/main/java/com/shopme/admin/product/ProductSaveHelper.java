package com.shopme.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;

public class ProductSaveHelper {
	//private static final logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);
	
	//Deleting extra images also in db if object contains such a file
		static void deleteExtraImageWhenRemovedFromForm(Product product) {
		     String extraImageDir = "../product-images/" + product.getId() + "/extras";
			Path dirPath = Paths.get(extraImageDir);
			
			try {
				Files.list(dirPath).forEach(file -> {
					String filename = file.toFile().getName();
					try {
						Files.delete(file);
						System.out.println("Deleted extra image " + filename);
					} catch (IOException e) {
						//LOGGER.error("Could not delete extra image " + filename);
				System.out.println("Could not delete extra image " + filename);
					}
				});
			} catch (IOException ex) {
				System.out.println("Could not list directory " + dirPath);
			}
		}

		
		static void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, 
				Product product) {
			if (imageIDs == null || imageIDs.length == 0) return;
			
			Set<ProductImage> images = new HashSet<>();
			
			for (int count = 0; count < imageIDs.length; count++) {
				Integer id = Integer.parseInt(imageIDs[count]);
				String name = imageNames[count];
				
				images.add(new ProductImage(id, name, product));
			}
			
			product.setImages(images);
			
		}

		//Adds details to product before saving, (if details are specified by user)
		static void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues, Product product) {
			if (detailNames == null || detailNames.length == 0) 
				return;
			
			for(int count = 0; count < detailNames.length; count++) {
				String name = detailNames[count];
				String value = detailValues[count];
				Integer id = Integer.parseInt(detailIDs[count]);
				
				if (id != 0) {
					product.addDetail(id,  name, value);
				} else if (!name.isEmpty() && !value.isEmpty())
					product.addDetail(name,  value);
			}
			
		}

		static void saveUploadedImages(MultipartFile mainImageMultipart, 
				MultipartFile[] extraImageMultiparts, Product savedProduct) throws IOException{
			if(!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			String uploadDir = "../product-images/" + savedProduct.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
			}
			if (extraImageMultiparts.length > 0) {
				String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";
				for (MultipartFile multipartFile : extraImageMultiparts) {
					if (!multipartFile.isEmpty()) 
						continue;
					
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);	
				}
		}
		}

		
		static void setNewExtraImageNames(MultipartFile [] extraImageMultiparts, Product product) {
			if (extraImageMultiparts.length > 0) {
				for (MultipartFile multipartFile : extraImageMultiparts) {
					if (!multipartFile.isEmpty()) {
						String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					if (!product.containsImageName(fileName)) {
						product.addExtraImage(fileName);
					}
					}
				}
			}
		}
		
		static void setMainImageName(MultipartFile mainImageMultipart, Product product) {
			if(!mainImageMultipart.isEmpty()) {
				String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
				product.setMainImage(fileName);
		}
		}

}
