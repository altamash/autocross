package com.veriqual.gofast.utilites;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.veriqual.gofast.model.ComparisonsList;
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
		Map<String, Long> tags = tagging.getTags();
		int size = tags.size();
		Entry<String, Long> entry = null;
		for(Entry<String, Long> tag : tags.entrySet()) {
			entry = tag;
		}
		if(size != 0) {
			return entry.getKey();
		}
		return null;
	}
	
	public static Long getLastTagValue(Tagging tagging) {
		Map<String, Long> tags = tagging.getTags();
		int size = tags.size();
		Entry<String, Long> entry = null;
		for(Entry<String, Long> tag : tags.entrySet()) {
			entry = tag;
		}
		if(size != 0) {
			return entry.getValue();
		}
		return 0l;
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
		for (Entry<String, Long> entry : first.getTagging().getTags().entrySet()) {
//			if(entry.getKey().equals(Tagging.STARTTAG)) continue;
			HashMap temp = new HashMap();
			temp.put(Constant.FIRST_COLUMN,entry.getKey());
            temp.put(Constant.SECOND_COLUMN, getFormattedTime(entry.getValue()));
            if(second.getTagging().getTags().get(entry.getKey()) != null) {
            	temp.put(Constant.THIRD_COLUMN, getFormattedTime(second.getTagging().getTags().get(entry.getKey())));
            	temp.put(Constant.FOURTH_COLUMN, getFormattedTime(entry.getValue() - second.getTagging().getTags().get(entry.getKey())));
            }
            list.add(temp);
		}
		return list;
		
	}
	
	public static String getFormattedTime(long time) {
		String str = "";
		if (time < 0) {
			time = -time;
			str = "-";
		}
		long minutes = time / 60000;
		long seconds = (time % 60000) / 1000;
		long hndrds = ((time % 60000) % 1000) / 100;
		
		String m = String.valueOf(minutes);
		String s = String.valueOf(seconds);
		String h = String.valueOf(hndrds);
		
		m = ("00" + m).substring(m.length());
		s = ("00" + s).substring(s.length());
		h = ("00" + h).substring(h.length());
		return str + m + ":" + s + ":" + h;
	}
	
	public static void saveComparison(Context context, ComparisonsList comparisonsList) throws IOException, ClassNotFoundException {
		String FILENAME = "comparisons";
		FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		oos.writeObject(comparisonsList);

		oos.close();
		fos.close();
	}
	
	public static ComparisonsList getComparisonsList(Context context) {
		ComparisonsList list = ComparisonsList.getInstance();
		String FILENAME = "comparisons";
		FileInputStream fis;
		ObjectInputStream ois;
		try {
			fis = context.openFileInput(FILENAME);
			ois = new ObjectInputStream(fis);
			list = (ComparisonsList) ois.readObject();
			ois.close();
			fis.close();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static void saveAssetToSD(Context context, String name) {
//		InputStream stream = null;
//		int i = 0;
//		try {
//			stream = context.getAssets().open(name);
//			i = stream.read();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		InputStream is;
		 try
		 {
		     is = context.getAssets().open(name);
		     int siz = is.available();
		     byte[] buffer = new byte[siz];
		     is.read(buffer);
		         //This text contains the content of the file..
//		     String text = new String(buffer);
//		         is.close();
		         saveFileToCard(buffer, name);
		         is.close();
		 }
		 catch (Exception e)
		 {
//		      Toast.makeText(CheatSheet.this,"File Not Found Error.Please ensure that file is not deleted.", Toast.LENGTH_SHORT).show();
		 }
	}
	
	private static String getRealPathFromURI(Context context, Uri contentURI) {
	    Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
	    if (cursor == null) { // Source is Dropbox or other similar local file path
	        return contentURI.getPath();
	    } else { 
	        cursor.moveToFirst(); 
	        int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA); 
	        return cursor.getString(idx); 
	    }
	}
	
	private static void saveFileToCard(byte[] data, String filename) {
		File file = new File(Environment.getExternalStorageDirectory(),
				filename);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// handle exception
		} catch (IOException e) {
			// handle exception
		}
	}
}
