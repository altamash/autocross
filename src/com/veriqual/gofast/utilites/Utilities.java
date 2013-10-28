package com.veriqual.gofast.utilites;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

import com.veriqual.gofast.model.Tagging;
import com.veriqual.gofast.model.Video;

public class Utilities {
//	mFirstVideo.setOnPreparedListener(new OnPreparedListener() {
//    @Override
//    public void onPrepared(MediaPlayer mp) {
//
//        mp.setOnSeekCompleteListener(new OnSeekCompleteListener() {
//            @Override
//            public void onSeekComplete(MediaPlayer mp) {
//            	mFirstVideo.start();
//            }
//        });
//
//    }
//});
	
//	mFirstVideo.seekTo(0);

//	String PATH =Environment.getExternalStorageDirectory().toString()+"/load";
//	Log.v("LOG_TAG", "PATH: " + PATH);
//
//	File file = new File(PATH);
//	file.mkdirs();
//	File outputFile = new File(file,"file1");
//	downloadFile("http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8", outputFile);
	
//	DownloadFromUrl("http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8", "file1");
	
	private static void downloadFile(String url, File outputFile) {
		try {
			URL u = new URL(url);
			URLConnection conn = u.openConnection();
			int contentLength = conn.getContentLength();

			DataInputStream stream = new DataInputStream(u.openStream());

			byte[] buffer = new byte[contentLength];
			stream.readFully(buffer);
			stream.close();

			DataOutputStream fos = new DataOutputStream(new FileOutputStream(
					outputFile));
			fos.write(buffer);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			return; // swallow a 404
		} catch (IOException e) {
			return; // swallow a 404
		}
	}
	
	public void DownloadFromUrl(String DownloadUrl, String fileName) {

		try {
			File root = android.os.Environment.getExternalStorageDirectory();

			File dir = new File(root.getAbsolutePath() + "/xmls");
			if (dir.exists() == false) {
				dir.mkdirs();
			}

			URL url = new URL(DownloadUrl); // you can write here any link
			File file = new File(dir, fileName);

			long startTime = System.currentTimeMillis();
			Log.d("DownloadManager", "download begining");
			Log.d("DownloadManager", "download url:" + url);
			Log.d("DownloadManager", "downloaded file name:" + fileName);

			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(5000);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.flush();
			fos.close();
			Log.d("DownloadManager",
					"download ready in"
							+ ((System.currentTimeMillis() - startTime) / 1000)
							+ " sec");

		} catch (IOException e) {
			Log.d("DownloadManager", "Error: " + e);
		}

	}
	
	public static String getLastTag(Tagging tagging) {
		Map<String, Integer> tags = tagging.getTags();
		int size = tags.size();
		if(size != 0) {
			Entry<String, Integer>[] entries = new Entry[size];
			return (entries[size - 1]).getKey();
		}
		return null;
	}
	
	public String generateTagMsg(String currentVideo, Video first, Video second) {
		String msg = null;
		String firstVideoLastTag = getLastTag(first.getTagging());
		String secondVideoLastTag = getLastTag(second.getTagging());
		if (currentVideo.equals(Video.FIRSTVIDEO)) {
			if (firstVideoLastTag == null) {
				msg = "Set start tag for first video";
			} else {
				if (secondVideoLastTag == null) {
					msg = "Set start tag for second video, first";
				} else if (!firstVideoLastTag.equals(secondVideoLastTag)) {
					msg = "Set " + firstVideoLastTag + " tag for second video";
				} else {
					msg = "Set tag/finish tag for first video";
				}
			}
		} else { 
			if (firstVideoLastTag == null) {
				msg = "Set start tag for first video";
			} else {
				if (secondVideoLastTag == null) {
					msg = "Set start tag for second video";
				} else {
					if (firstVideoLastTag.equals(secondVideoLastTag)) {
						msg = "Set tag for first video, first";
					} else if (firstVideoLastTag.equals(Tagging.FINISHTAG)) {
						msg = "Set finish tag for second video";
					} else {
						msg = "Set " + firstVideoLastTag + "tag for second video";
					}
				}
			}
		}
		return msg;
	}
}
