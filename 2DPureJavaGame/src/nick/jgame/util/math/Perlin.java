package nick.jgame.util.math;

import java.util.Random;

public final class Perlin {

	private static float[ ][ ] genPerlinNoise(final float[ ][ ] baseNoise, final int octaveCount)
	{

		// An array of 2D arrays containing the smooth noise.

		final float[ ][ ][ ] smoothNoise = new float[octaveCount][ ][ ];

		/* Generate smooth noise */
		for (int index = 0; index < octaveCount; index++)
		{
			smoothNoise[index] = genSmoothNoise(baseNoise, index);
		}
		// Constants
		final int width = baseNoise.length;
		final int height = baseNoise[0].length;
		final float persistance = 0.5f;
		final float[ ][ ] perlinNoise = new float[width][height];
		// Variables
		float amplitude = 1;
		float totalAmplitude = 0;

		// Blend noise together
		for (int octave = octaveCount - 1; octave >= 0; octave--)
		{
			amplitude *= persistance;
			totalAmplitude += amplitude;

			for (int x = 0; x < width; x++)
			{
				for (int y = 0; y < height; y++)
				{
					perlinNoise[x][y] += smoothNoise[octave][x][y] * amplitude;
				}
			}
		}

		// Normalization
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				perlinNoise[x][y] /= totalAmplitude;
			}
		}

		return perlinNoise;
	}

	public static float[ ][ ] genSmoothNoise(final float[ ][ ] baseNoise, final int octave)
	{

		final int width = baseNoise.length;
		final int height = baseNoise[0].length;

		final float[ ][ ] smoothNoise = new float[width][height];

		final int samplePeriod = (int) Math.pow(2, octave);
		final float sampleFrequency = 1.0f / samplePeriod;

		for (int x = 0; x < width; x++)
		{
			// Calculate the horizontal sampling indices.
			final int sample_x0 = (x / samplePeriod) * samplePeriod;
			final int sample_x1 = (sample_x0 + samplePeriod) % width; // wrap around
			final float horizontal_blend = (x - sample_x0) * sampleFrequency;

			for (int y = 0; y < height; y++)
			{
				// Calculate the vertical sampling indices
				final int sample_y0 = (y / samplePeriod) * samplePeriod;
				final int sample_y1 = (sample_y0 + samplePeriod) % height; // wrap around
				final float vertical_blend = (y - sample_y0) * sampleFrequency;

				// Blend the top two corners
				final float top = interpolate(baseNoise[sample_x0][sample_y0],
						baseNoise[sample_x1][sample_y0], horizontal_blend);

				// Blend the bottom two corners
				final float bottom = interpolate(baseNoise[sample_x0][sample_y1],
						baseNoise[sample_x1][sample_y1], horizontal_blend);

				// Final blend
				smoothNoise[x][y] = interpolate(top, bottom, vertical_blend);
			}
		}

		return smoothNoise;
	}

	public static float[ ][ ] getNoise(final Random rand, final int width, final int height, final byte count) {

		final float[ ][ ] base = getWhiteNoise(rand, width, height);
		return genPerlinNoise(base, count);
	}

	public static float[ ][ ] getWhiteNoise(final Random rand, final int width, final int height) {

		final float[ ][ ] noise = new float[width][height];

		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				noise[x][y] = rand.nextFloat( );
			}
		}

		return noise;
	}

	private static float interpolate(final float x0, final float x1, final float alpha) {

		return (x0 * (1 - alpha)) + (alpha * x1);
	}

	public static float[ ][ ] roundNoise(final float[ ][ ] toRound, final byte decimalPlaces) {

		if (decimalPlaces >= 10) { return toRound; }
		final float[ ][ ] toRet = new float[toRound.length][toRound[0].length];
		final float factor = (float) Math.pow(10, decimalPlaces);
		for (int x = 0; x < toRet.length; x++) {
			for (int y = 0; y < toRet[0].length; y++) {
				toRet[x][y] = Math.round(toRound[x][y] * factor) / factor;
			}
		}
		return toRet;
	}
}
