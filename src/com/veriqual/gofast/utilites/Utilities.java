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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.util.ByteArrayBuffer;

import android.R.anim;
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
		Entry<String, Integer> entry = null;
		for(Entry<String, Integer> tag : tags.entrySet()) {
			entry = tag;
		}
		if(size != 0) {
			return entry.getKey();
		}
		return null;
	}
	
	public static String generateTagMsg(String currentVideo, Video first, Video second) {
		String msg = null;
		String firstVideoLastTag = getLastTag(first.getTagging());
		String secondVideoLastTag = getLastTag(second.getTagging());
		if (currentVideo.equals(Video.FIRSTVIDEO)) {
			if (firstVideoLastTag == null) {
				msg = "Set [Start Tag] for first video at this location?";
			} else {
				if (secondVideoLastTag == null) {
					msg = "Set [Start Tag] for second video, first!";
				} else if (!firstVideoLastTag.equals(secondVideoLastTag)) { //
					msg = "Set [" + firstVideoLastTag + "] tag for second video, first!";
				} else {
					msg = "Set [Tag/Finish Tag] for first video";
				}
			}
		} else { 
			if (firstVideoLastTag == null) {
				msg = "Set [Start Tag] for first video, first!";
			} else {
				if (secondVideoLastTag == null) {
					msg = "Set [Start Tag] for second video at this location?";
				} else {
					if (firstVideoLastTag.equals(secondVideoLastTag)) {
						msg = "Set tag for first video, first!";
					} else if (firstVideoLastTag.equals(Tagging.FINISHTAG)) {
						msg = "Set [Finish Tag] for second video at this location?";
					} else {
						msg = "Set " + firstVideoLastTag + " tag for second video at this location?";
					}
				}
			}
		}
		return msg;
	}
	
	public static boolean isHideInput(String currentVideo, Video first, Video second) {
		boolean hideInput;
		String firstVideoLastTag = getLastTag(first.getTagging());
		String secondVideoLastTag = getLastTag(second.getTagging());
		if (currentVideo.equals(Video.FIRSTVIDEO)) {
			if (firstVideoLastTag == null) {
				hideInput = true;
			} else {
				if (secondVideoLastTag == null) {
					hideInput = true;
				} else if (!firstVideoLastTag.equals(secondVideoLastTag)) {
					hideInput = true;
				} else {
					hideInput = false;
				}
			}
		} else { 
//			if (firstVideoLastTag == null) {
				hideInput = true;
//			} else {
//				if (secondVideoLastTag == null) {
//					hideInput = true;
//				} else {
//					if (firstVideoLastTag.equals(secondVideoLastTag)) {
//						hideInput = true;
//					} else if (firstVideoLastTag.equals(Tagging.FINISHTAG)) {
//						hideInput = true;
//					} else {
//						hideInput = true;
//					}
//				}
//			}
		}
		return hideInput;
	}
	
	public static String getTag(String currentVideo, Video first, Video second) {
		String msg = null;
		String tag = null;
		String firstVideoLastTag = getLastTag(first.getTagging());
		String secondVideoLastTag = getLastTag(second.getTagging());
		if (currentVideo.equals(Video.FIRSTVIDEO)) {
			if (firstVideoLastTag == null) {
				msg = "Set start tag for first video";
				tag = Tagging.STARTTAG;
			} else {
				if (secondVideoLastTag == null) {
					msg = "Set start tag for second video, first";
				} else if (!firstVideoLastTag.equals(secondVideoLastTag)) {
					msg = "Set [" + firstVideoLastTag + "] tag for second video";
				} else {
					msg = "Set tag/finish tag for first video";
				}
			}
		} else { 
			if (firstVideoLastTag == null) {
				msg = "Set start tag for first video, first";
			} else {
				if (secondVideoLastTag == null) {
					msg = "Set start tag for second video";
					tag = Tagging.STARTTAG;
				} else {
					if (firstVideoLastTag.equals(secondVideoLastTag)) {
						msg = "Set tag for first video, first";
					} else if (firstVideoLastTag.equals(Tagging.FINISHTAG)) {
						msg = "Set finish tag for second video";
						tag = Tagging.FINISHTAG;
					} else {
						msg = "Set [" + firstVideoLastTag + "] tag for second video";
						tag = firstVideoLastTag;
					}
				}
			}
		}
		return tag;
	}
	
	public static int getTagDlgIcon(String currentVideo, Video first, Video second) {
		int icon;
		String firstVideoLastTag = getLastTag(first.getTagging());
		String secondVideoLastTag = getLastTag(second.getTagging());
		if (currentVideo.equals(Video.FIRSTVIDEO)) {
			if (firstVideoLastTag == null) {
				icon = com.veriqual.gofast.R.drawable.tag;
			} else {
				if (secondVideoLastTag == null) {
					icon = com.veriqual.gofast.R.drawable.info;
				} else if (!firstVideoLastTag.equals(secondVideoLastTag)) {
					icon = com.veriqual.gofast.R.drawable.info;
				} else {
					icon = com.veriqual.gofast.R.drawable.tag;
				}
			}
		} else { 
			if (firstVideoLastTag == null) {
				icon = com.veriqual.gofast.R.drawable.info;
			} else {
				if (secondVideoLastTag == null) {
					icon = com.veriqual.gofast.R.drawable.tag;
				} else {
					if (firstVideoLastTag.equals(secondVideoLastTag)) {
						icon = com.veriqual.gofast.R.drawable.info;
					} else if (firstVideoLastTag.equals(Tagging.FINISHTAG)) {
						icon = com.veriqual.gofast.R.drawable.tag;
					} else {
						icon = com.veriqual.gofast.R.drawable.tag;
					}
				}
			}
		}
		return icon;
	}
	
	public static boolean isTagDlgOneButton(String currentVideo, Video first, Video second) {
		String msg = null;
		boolean oneButton;
		String firstVideoLastTag = getLastTag(first.getTagging());
		String secondVideoLastTag = getLastTag(second.getTagging());
		if (currentVideo.equals(Video.FIRSTVIDEO)) {
			if (firstVideoLastTag == null) {
				msg = "Set [Start Tag] for first video at this location?";
				oneButton = false;
			} else {
				if (secondVideoLastTag == null) {
					msg = "Set [Start Tag] for second video, first!";
					oneButton = true;
				} else if (!firstVideoLastTag.equals(secondVideoLastTag)) {
					msg = "Set [" + firstVideoLastTag + "] tag for second video at this location?";
					oneButton = true;
				} else {
					msg = "Set [Tag/Finish Tag] for first video";
					oneButton = false;
				}
			}
		} else { 
			if (firstVideoLastTag == null) {
				msg = "Set [Start Tag] for first video, first!";
				oneButton = true;
			} else {
				if (secondVideoLastTag == null) {
					msg = "Set [Start Tag] for second video at this location?";
					oneButton = false;
				} else {
					if (firstVideoLastTag.equals(secondVideoLastTag)) {
						msg = "Set tag for first video, first!";
						oneButton = true;
					} else if (firstVideoLastTag.equals(Tagging.FINISHTAG)) {
						msg = "Set [Finish Tag] for second video at this location?";
						oneButton = false;
					} else {
						msg = "Set " + firstVideoLastTag + " tag for second video at this location?";
						oneButton = false;
					}
				}
			}
		}
		return oneButton;
	}
	
	public static ArrayList<HashMap> populateList(Video first, Video second) {
		ArrayList<HashMap> list = new ArrayList<HashMap>();
		for (Entry<String, Integer> entry : first.getTagging().getTags().entrySet()) {
			HashMap temp = new HashMap();
			temp.put(Constant.FIRST_COLUMN,entry.getKey());
            temp.put(Constant.SECOND_COLUMN, String.valueOf(entry.getValue()));
            if(second.getTagging().getTags().get(entry.getKey()) != null) {
            	temp.put(Constant.THIRD_COLUMN, String.valueOf(second.getTagging().getTags().get(entry.getKey())));
            	temp.put(Constant.FOURTH_COLUMN, String.valueOf(entry.getValue() - second.getTagging().getTags().get(entry.getKey())));
            }
            list.add(temp);
		}
		return list;
		
	}
	
	public static ArrayList<HashMap> populateList() {
		 
		ArrayList<HashMap> list = new ArrayList<HashMap>();
 
        HashMap temp = new HashMap();
            temp.put(Constant.FIRST_COLUMN,"Colored Notebooks");
            temp.put(Constant.SECOND_COLUMN, "By NavNeet");
            temp.put(Constant.THIRD_COLUMN, "Rs. 200");
            temp.put(Constant.FOURTH_COLUMN, "Per Unit");
        list.add(temp);
 
        HashMap temp1 = new HashMap();
            temp1.put(Constant.FIRST_COLUMN,"Diaries");
            temp1.put(Constant.SECOND_COLUMN, "By Amee Products");
            temp1.put(Constant.THIRD_COLUMN, "Rs. 400");
            temp1.put(Constant.FOURTH_COLUMN, "Per Unit");
        list.add(temp1);
 
        HashMap temp2 = new HashMap();
            temp2.put(Constant.FIRST_COLUMN,"Note Books and Stationery");
            temp2.put(Constant.SECOND_COLUMN, "By National Products");
            temp2.put(Constant.THIRD_COLUMN, "Rs. 600");
            temp2.put(Constant.FOURTH_COLUMN, "Per Unit");
        list.add(temp2);
 
        HashMap temp3 = new HashMap();
            temp3.put(Constant.FIRST_COLUMN,"Corporate Diaries");
            temp3.put(Constant.SECOND_COLUMN, "By Devarsh Prakashan");
            temp3.put(Constant.THIRD_COLUMN, "Rs. 800");
            temp3.put(Constant.FOURTH_COLUMN, "Per Unit");
        list.add(temp3);
 
        HashMap temp4 = new HashMap();
            temp4.put(Constant.FIRST_COLUMN,"Writing Pad");
            temp4.put(Constant.SECOND_COLUMN, "By TechnoTalaktive Pvt. Ltd.");
            temp4.put(Constant.THIRD_COLUMN, "Rs. 100");
            temp4.put(Constant.FOURTH_COLUMN, "Per Unit");
        list.add(temp4);
		return list;
    }
}
