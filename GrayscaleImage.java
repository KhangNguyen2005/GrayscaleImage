package assign01;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * 
 * @author Khang Hoang Nguyen
 *
 */
public class GrayscaleImage {
	private double[][] imageData;

	/**
	 * Initialize an image from a 2D array of doubles This constructor creates a
	 * copy of the input array
	 * 
	 * @param data initial pixel values
	 * @throws IllegalArgumentException if the input array is empty or "jagged"
	 *                                  meaning not all rows are the same length
	 */
	public GrayscaleImage(double[][] data) {
		if (data.length == 0 || data[0].length == 0) {
			throw new IllegalArgumentException("Image is empty");
		}

		imageData = new double[data.length][data[0].length];
		for (var row = 0; row < imageData.length; row++) {
			if (data[row].length != imageData[row].length) {
				throw new IllegalArgumentException("All rows must have the same length");
			}
			for (var col = 0; col < imageData[row].length; col++) {
				imageData[row][col] = data[row][col];
			}
		}
	}

	/**
	 * Fetches an image from the specified URL and converts it to grayscale Uses the
	 * AWT Graphics2D class to do the conversion, so it may add an item to your
	 * dock/menu bar as if you're loading a GUI program
	 * 
	 * @param url where to download the image
	 * @throws IOException if the image can't be downloaded for some reason
	 */
	public GrayscaleImage(URL url) throws IOException {
		var inputImage = ImageIO.read(url);

		var grayImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g2d = grayImage.createGraphics();
		g2d.drawImage(inputImage, 0, 0, null);
		g2d.dispose();
		imageData = new double[grayImage.getHeight()][grayImage.getWidth()];

		var grayRaster = grayImage.getRaster();
		for (var row = 0; row < imageData.length; row++) {
			for (var col = 0; col < imageData[0].length; col++) {

				imageData[row][col] = grayRaster.getSampleDouble(col, row, 0);
			}
		}
	}

	/**
	 * Saves the image as a PNG file.
	 * 
	 * @param filename
	 * @throws IOException if the file can't be written
	 */
	public void savePNG(File filename) throws IOException {
		var outputImage = new BufferedImage(imageData[0].length, imageData.length, BufferedImage.TYPE_BYTE_GRAY);
		var raster = outputImage.getRaster();
		for (var row = 0; row < imageData.length; row++) {
			for (var col = 0; col < imageData[0].length; col++) {
				raster.setSample(col, row, 0, imageData[row][col]);
			}
		}
		ImageIO.write(outputImage, "png", filename);
	}

	/**
	 * Get the pixel brightness value at the specified coordinates (0,0) is the top
	 * left corner of the image, (width -1, height -1) is the bottom right corner
	 * 
	 * @param x horizontal position, increases left to right
	 * @param y vertical position, **increases top to bottom**
	 * @return the brightness value at the specified coordinates
	 * @throws IllegalArgumentException if x, y are not within the image
	 *                                  width/height
	 */
	public double getPixel(int x, int y) {
		if (x < 0 || y < 0 || x >= imageData[0].length || y >= imageData.length) {
			throw new IllegalArgumentException("Coordinates (" + x + ", " + y + ") are out of bounds");
		}
		return imageData[y][x];
	}

	/**
	 * Two images are equal if they have the same size and each corresponding pixel
	 * in the two images is exactly equal
	 * 
	 * @param other - the object to compare this image with
	 * @return - true if the images are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof GrayscaleImage)) {
			return false;
		}

		GrayscaleImage otherImage = (GrayscaleImage) other;

		if (imageData[0].length != otherImage.imageData[0].length || imageData.length != otherImage.imageData.length)
			return false;

		for (int row = 0; row < imageData.length; row++) {
			for (int col = 0; col < imageData[row].length; col++) {
				if (imageData[row][col] != otherImage.imageData[row][col])
					return false;
			}
		}
		return true;
	}

	/**
	 * Computes the average of all values in image data
	 * 
	 * @return the average of the imageData array
	 */
	public double averageBrightness() {
		double sumOfPixelValues = 0;

		if (imageData.length == 0 || imageData[0].length == 0) {
			return 0;
		}

		int totalPixels = imageData.length * imageData[0].length;

		for (int row = 0; row < imageData.length; row++) {
			for (int col = 0; col < imageData[0].length; col++) {
				sumOfPixelValues += imageData[row][col];
			}

		}

		return sumOfPixelValues / totalPixels;
	}

	/**
	 * Return a new GrayScale image where the average new average brightness is 127
	 * To do this, uniformly scale each pixel (ie, multiply each imageData entry by
	 * the same value) Due to rounding, the new average brightness will not be 127
	 * exactly, but should be very close The original image should not be modified
	 * 
	 * @return a GrayScale image with pixel data uniformly rescaled so that its
	 *         averageBrightness() is 127
	 */
	public GrayscaleImage normalized() {

		double averageBrightness = this.averageBrightness();

		if (averageBrightness == 0) {
			return new GrayscaleImage(imageData);
		}

		double targetAverageBrightness = 127.0;

		double adjustedScalingFactor = targetAverageBrightness / averageBrightness;

		double[][] normalizedImageData = new double[imageData.length][imageData[0].length];
		for (int row = 0; row < imageData.length; row++) {
			for (int col = 0; col < imageData[row].length; col++) {
				normalizedImageData[row][col] = imageData[row][col] * adjustedScalingFactor;
			}
		}

		return new GrayscaleImage(normalizedImageData);
	}

	/**
	 * Returns a new grayscale image that has been "mirrored" across the y-axis In
	 * other words, each row of the image should be reversed The original image
	 * should be unchanged
	 * 
	 * @return a new GrayscaleImage that is a mirrored version of the this
	 */
	public GrayscaleImage mirrored() {
		int numRows = imageData.length;
		int numCols = imageData[0].length;
		double[][] mirroredImage = new double[numRows][numCols];

		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				mirroredImage[row][col] = imageData[row][numCols - 1 - col];
			}
		}
		GrayscaleImage mirrored = new GrayscaleImage(mirroredImage);

		return mirrored;
	}

	/**
	 * Returns a new GrayscaleImage of size width x height, containing the part of
	 * `this` from startRow -> startRow + height - 1, startCol -> startCol + width -
	 * 1 The original image should be unmodified
	 * 
	 * @param startRow
	 * @param startCol
	 * @param width
	 * @param height
	 * @return A new GrayscaleImage containing the sub-image in the specified
	 *         rectangle
	 * @throws IllegalArgumentException if the specified rectangle goes outside the
	 *                                  bounds of the original image
	 */
	public GrayscaleImage cropped(int startRow, int startCol, int width, int height) {
		if (startRow < 0 || startCol < 0 || width <= 0 || height <= 0 || startCol + width > imageData[0].length
				|| startRow + height > imageData.length) {
			throw new IllegalArgumentException("Invalid cropping dimensions");
		}

		double[][] croppedImage = new double[height][width];

		for (int row = startRow; row < startRow + height; row++) {
			for (int col = startCol; col < startCol + width; col++) {
				croppedImage[row - startRow][col - startCol] = imageData[row][col];
			}
		}

		GrayscaleImage cropped = new GrayscaleImage(croppedImage);
		return cropped;

	}

	/**
	 * Returns a new "centered" square image (new width == new height) For example,
	 * if the width is 20 pixels greater than the height, this should return a
	 * height x height image, with 10 pixels removed from the left and right edges
	 * of the image If the number of pixels to be removed is odd, remove 1 fewer
	 * pixel from the left or top part (note this convention should be
	 * SIMPLER/EASIER to implement than the alternative) The original image should
	 * not be changed
	 * 
	 * @return a new, square, GrayscaleImage
	 */

	public GrayscaleImage squarified() {
		int numRows = imageData.length;
		int numCols = imageData[0].length;

		int squaredSize = Math.min(numRows, numCols);

		int removedRows = numRows - squaredSize;
		int removedCols = numCols - squaredSize;

		int topRowsToRemove = removedRows / 2;
		int leftColsToRemove = removedCols / 2;

		double[][] squaredImage = new double[squaredSize][squaredSize];

		for (int row = 0; row < squaredSize; row++) {
			for (int col = 0; col < squaredSize; col++) {
				squaredImage[row][col] = imageData[row + topRowsToRemove][col + leftColsToRemove];
			}
		}

		return new GrayscaleImage(squaredImage);
	}

}
