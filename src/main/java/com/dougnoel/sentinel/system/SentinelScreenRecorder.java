package com.dougnoel.sentinel.system;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import org.monte.media.Format;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class SentinelScreenRecorder {
	private static ScreenRecorder screenRecorder;

	private SentinelScreenRecorder(){
		// This constructor acts only to defeat instantiation
	}

	/**
	 * Starts a recording of the main screen of the computer. Will capture the entire desktop, not just the application.
	 * For Windows machines, the recording will be at &lt; C:/users/(youruser)/Videos/ &gt;.
	 * For Mac machines, the recording will be at the Movies folder in the user directory.
	 * @throws IOException if the recording cannot be written
	 * @throws AWTException if AWT does not have access to record
	 */
	public static void startRecording() throws IOException, AWTException {

		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		screenRecorder = new ScreenRecorder(gc, gc.getBounds(), 
				new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
				new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
						CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
						Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
				new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
				null,
				new File("./reports/"));
		screenRecorder.start();
	}

	/**
	 * Stops the recording of the main screen of the computer.
	 * @throws IOException if the recording cannot be written
	 */
	public static void stopRecording() throws IOException {
		screenRecorder.stop();
	}
}