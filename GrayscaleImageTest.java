package assign01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;

/**
 * File containing tests to expose bugs in Assignment 01
 * 
 * @author Khang Hoang Nguyen
 *
 */
class GrayscaleImageTest {

	private GrayscaleImage smallSquare;
	private GrayscaleImage smallWide;

	@BeforeEach
	void setUp() {
		smallSquare = new GrayscaleImage(new double[][] { { 1, 2 }, { 3, 4 } });
		smallWide = new GrayscaleImage(new double[][] { { 1, 2, 3 }, { 4, 5, 6 } });
	}

	@Test
	void normalizedWorkOnNonSquaredImg() {
		double[][] imageData = { { 100, 150, 200, 250, 300 }, { 50, 75, 100, 125, 150 }, { 25, 38, 50, 63, 75 },
				{ 10, 15, 20, 25, 30 } }; 
		GrayscaleImage image = new GrayscaleImage(imageData);

		GrayscaleImage normalizedImage = image.normalized();

		
		double actualAverageBrightness = normalizedImage.averageBrightness();
		assertEquals(127.0, actualAverageBrightness, 0.01,
				"Average brightness of the normalized image should be approximately 127");
	}

	@Test
	void testEqualsWorksForDifferentSizedImgs() {
		double[][] imageData1 = { { 100, 150, 200 }, { 50, 75, 100 } };
		double[][] imageData2 = { { 100, 150 }, { 50, 75 } };
		GrayscaleImage image1 = new GrayscaleImage(imageData1);
		GrayscaleImage image2 = new GrayscaleImage(imageData2);
		assertNotEquals(image1, image2, "Images of different sizes should not be equal");
	}

	@Test
	void testCropThrowsCorrectly() {
		double[][] imageData = { { 100, 150, 200 }, { 50, 75, 100 } };
		GrayscaleImage image = new GrayscaleImage(imageData);
		assertThrows(IllegalArgumentException.class, () -> {
			image.cropped(0, 0, 4, 4);
		}, "Cropping with dimensions exceeding image bounds should throw IllegalArgumentException");
	}

	@Test
	void testSquarifedLargeImages() {
		double[][] imageData = new double[100][100];

		GrayscaleImage image = new GrayscaleImage(imageData);
		GrayscaleImage squaredImage = image.squarified();

		double[][] expectedData = new double[100][100];
		GrayscaleImage expected = new GrayscaleImage(expectedData);

		assertEquals(expected, squaredImage, "Squared image should have dimensions of 100x100");

	}

	@Test
	void testCropCorrectForNonSquaredImgs() {
		double[][] imageData = { { 100, 150, 200, 250 }, { 50, 75, 100, 125 }, { 25, 38, 50, 63 }, { 10, 15, 20, 25 } };
		GrayscaleImage image = new GrayscaleImage(imageData);

		GrayscaleImage croppedImage = image.cropped(1, 1, 2, 2);

		double[][] expectedData = { { 75, 100 }, { 38, 50 } };
		GrayscaleImage expected = new GrayscaleImage(expectedData);

		assertEquals(expected, croppedImage, "Cropped image should contain the expected pixel values");

		GrayscaleImage cropped = smallSquare.cropped(0, 0, 2, 2);
		assertEquals(smallSquare, cropped);

		GrayscaleImage croppedSinglePixel = smallSquare.cropped(1, 1, 1, 1);
		assertEquals(new GrayscaleImage(new double[][] { { 4 } }), croppedSinglePixel);

		GrayscaleImage croppedRectangle = smallWide.cropped(0, 1, 2, 1);
		assertEquals(new GrayscaleImage(new double[][] { { 2, 3 } }), croppedRectangle);

		assertThrows(IllegalArgumentException.class, () -> smallSquare.cropped(0, 0, 3, 2));
	}

	@Test
	void testGetPixelThrowsCorrectly() {
		double[][] imageData = { { 100, 150, 200 }, { 50, 75, 100 } };
		GrayscaleImage image = new GrayscaleImage(imageData);

		assertThrows(IllegalArgumentException.class, () -> {
			image.getPixel(-1, 0);
		}, "getPixel() should throw IllegalArgumentException for negative x-coordinate");

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			image.getPixel(3, 0);
		}, "getPixel() should throw IllegalArgumentException for x-coordinate exceeding width");

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			image.getPixel(0, -1);
		}, "getPixel() should throw IllegalArgumentException for negative y-coordinate");

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			image.getPixel(0, 2);
		}, "getPixel() should throw IllegalArgumentException for y-coordinate exceeding height");

	}

	@Test
	void testSquarifedRoundingCorrectlyForTallImgs() {

		double[][] squareData = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
		GrayscaleImage squareImage = new GrayscaleImage(squareData);
		GrayscaleImage squarifiedImage = squareImage.squarified();
		assertTrue(squarifiedImage.equals(squareImage));

		double[][] imageData = { { 100, 150 }, { 50, 75 }, { 25, 38 }, { 10, 15 }, { 5, 7 } };
		GrayscaleImage image = new GrayscaleImage(imageData);

		GrayscaleImage squaredImage = image.squarified();

		double[][] expectedData = { { 50, 75 }, { 25, 38 } };

		GrayscaleImage expected = new GrayscaleImage(expectedData);
		assertEquals(expected, squaredImage,
				"Squarified image should match the expected square portion of the original image");

	}

	@Test
	void testCroppedSmallWide() {
		var cropped = smallWide.cropped(0, 1, 2, 2);
		assertEquals(cropped, new GrayscaleImage(new double[][] { { 2, 3 }, { 5, 6 } }));
	}

}