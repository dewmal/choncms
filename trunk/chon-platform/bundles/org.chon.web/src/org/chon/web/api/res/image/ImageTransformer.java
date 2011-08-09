package org.chon.web.api.res.image;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageTransformer {
	public static final int TYPE_STRECH = 1;
	public static final int TYPE_STRECH_CUT = 2;
	public static final int TYPE_STRECH_NO_CUT = 3;
	static class Size {
		public Size(int width, int height) {
			this.width = width;
			this.height = height;
		}
		int width;
		int height;
	}
	
	static interface GetScaledImgeDim {
		Size calc(int w0, int h0, int w1, int h1);
	}
	
	public static void scaleImage(File image, int width, int height, int type,
			String imageType, OutputStream out) {
		try {
			scaleImage(new FileInputStream(image), width, height, type, imageType, out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//TODO: make clear image scale service
	public static void scaleImage(InputStream image, int width, int height, int type,
			String imageType, OutputStream out) {
		
		try {

			BufferedImage orig = ImageIO.read(image);
			
			BufferedImage outImg = null;
			
			switch (type) {
			case TYPE_STRECH:
				outImg = scaleImage(width, height, orig, new GetScaledImgeDim() {
					public Size calc(int w0, int h0, int w1, int h1) {
						return new Size(w1, h1);
					}});
				break;
			case TYPE_STRECH_CUT:
				orig = scaleImage(width, height, orig, new GetScaledImgeDim() {

					public Size calc(int w0, int h0, int w1, int h1) {
						float x;
						if((float)w0/(float)w1 < (float)h0/(float)h1) {
							x = (float)w1/(float)w0;
						} else {
							x = (float)h1/(float)h0;
						}
						return new Size((int) (x*w0), (int) (x*h0));
					}});
				outImg = cutImage(width, height, orig);
				break;
			case TYPE_STRECH_NO_CUT:
				outImg = scaleImage(width, height, orig, new GetScaledImgeDim() {

					public Size calc(int w0, int h0, int w1, int h1) {
						float x;
						if((float)w0/(float)w1 > (float)h0/(float)h1) {
							x = (float)w1/(float)w0;
						} else {
							x = (float)h1/(float)h0;
						}
						return new Size((int) (x*w0), (int) (x*h0));
					}});
				break;
			case 6:
				outImg = scaleImage(width, height, orig, new GetScaledImgeDim() {

					public Size calc(int w0, int h0, int w1, int h1) {
						float x;
						if((float)w0/(float)w1 > (float)h0/(float)h1) {
							x = (float)w1/(float)w0;
						} else {
							x = (float)h1/(float)h0;
						}
						return new Size((int) (x*w0), (int) (x*h0));
					}});
				break;	
			default:
				throw new RuntimeException("Unimplemented type " + type + "... Can be  " +
						"TYPE_STRECH = 1; " +
						"TYPE_STRECH_CUT = 2;" + 
						"TYPE_STRECH_NO_CUT = 3;");
			}
			
			
			ImageIO.write(outImg, imageType, out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static BufferedImage cutImage(int width, int height,
			BufferedImage orig) {
		int w0=orig.getWidth();
		int h0=orig.getHeight();
		
		int w1=width;
		int h1=height;
		
		BufferedImage outImg = new BufferedImage(w1, h1, orig.getType()!=0 ? orig.getType() : BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = outImg.createGraphics();
		g.drawImage(orig, (-w0+w1)/2, (-h0+h1)/2, w0, h0, null);
		g.dispose();
		
		return outImg;
	}
	private static BufferedImage scaleImage(int width, int height,
			BufferedImage orig, GetScaledImgeDim dim) {
		int w0 = orig.getWidth();
		int h0 = orig.getHeight();
		int w1 = width;
		int h1 = height;
		
		Size size = dim.calc(w0, h0, w1, h1);
		
		
		BufferedImage outImg = new BufferedImage(size.width, size.height, orig.getType()!=0 ? orig.getType() : BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = outImg.createGraphics();
		g.setComposite(AlphaComposite.Src);

		//g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		//g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		

		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		  
		g.drawImage(orig, 0, 0, size.width, size.height, null);
		g.dispose();
		//return blurImage(outImg);
		return outImg;
	}
	
	@SuppressWarnings("unchecked")
	public static BufferedImage blurImage(BufferedImage image) {
		float ninth = 1.0f / 9.0f;
		float[] blurKernel = { ninth, ninth, ninth, ninth, ninth, ninth, ninth,
				ninth, ninth };

		Map map = new HashMap();

		map.put(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		map.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		map.put(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		RenderingHints hints = new RenderingHints(map);
		BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, blurKernel),
				ConvolveOp.EDGE_NO_OP, hints);
		return op.filter(image, null);
	} 
}