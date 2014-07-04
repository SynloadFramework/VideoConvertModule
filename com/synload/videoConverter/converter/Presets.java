package com.synload.videoConverter.converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import com.synload.videoConverter.VideoConvertModule;
import com.synload.videoConverter.converter.models.Video;

public class Presets{
	public static String vp84k(Video video, String extra){
		
		return VideoConvertModule.prop.getProperty("ffmpeg")+
		" -i "+
		VideoConvertModule.prop.getProperty("uploadPath")+video.getTemp()+
		extra +
		" -threads 8 -r "+
		video.getFPS()+
		" -vcodec vp8 -acodec libvorbis -ac 2 -maxrate 10000k -b 30000k -ab 196k -ar 44100 -s 3840x2160 "+
		VideoConvertModule.prop.getProperty("videoPath")+video.getVideo()+
		".webm";
	}
	public static String custom(Video video,String customName, String extra) throws IOException{
		boolean foundMatch = customName.matches("(?si)([0-9a-zA-Z.]+)");
		if(!foundMatch){
			return vp8480(video, extra);
		}
		InputStream ips = new FileInputStream(new File(VideoConvertModule.prop.getProperty("customPath")+customName));
        InputStreamReader isr = new InputStreamReader(ips);
        BufferedReader br = new BufferedReader(isr);
		String line,out = "";
		while ((line = br.readLine()) != null) {
			out+=line;
		}
		br.close();
		isr.close();
		return out.replace(
			"%FFMPEG%", 
			VideoConvertModule.prop.getProperty("ffmpeg")
		).replace(
			"%FPS%", 
			video.getFPS()
		).replace(
			"%EXTRA%", 
			extra
		).replace(
			"%TEMP%", 
			VideoConvertModule.prop.getProperty("uploadPath")+video.getTemp()
		).replace(
			"%FINAL%", 
			VideoConvertModule.prop.getProperty("videoPath")+video.getVideo()
		);
	}
	public static String vp8480(Video video, String extra){
		return VideoConvertModule.prop.getProperty("ffmpeg")+
		" -i "+
		VideoConvertModule.prop.getProperty("uploadPath")+video.getTemp()+
		extra +
		" -threads 8 -r "+
		video.getFPS()+
		" -vcodec vp8 -acodec libvorbis -ac 2 -maxrate 1500k -b 2500k -ab 196k -ar 44100 -s 852x480 "+
		VideoConvertModule.prop.getProperty("videoPath")+video.getVideo()+
		".webm";
	}
	public static String vp8(Video video, String extra){
		return VideoConvertModule.prop.getProperty("ffmpeg")+
		" -i "+
		VideoConvertModule.prop.getProperty("uploadPath")+video.getTemp()+
		extra +
		" -threads 8 -r "+
		video.getFPS()+
		" -vcodec vp8 -acodec libvorbis -ac 2 -maxrate 3000k -b 5000k -ab 196k -sn -ar 44100 -s 1600x900 "+
		VideoConvertModule.prop.getProperty("videoPath")+video.getVideo()+
		".webm";
	}
	public static Hashtable<String,String> requestBuild(Video video){
		Hashtable<String,String> gen = new Hashtable<String,String>();
		gen.put("vid", video.getParams().get("vid"));
		gen.put("fmt", video.getFormat());
		if(video.getParams().containsKey("subtitleReturn") && video.getSubtitles().size()>0){
			if(video.getParams().get("subtitleReturn").equalsIgnoreCase("vtt")){
				gen.put("vtt","@"+video.getSubtitles().get(0)+".vtt");
			}else if(video.getParams().get("subtitleReturn").equalsIgnoreCase("srt")){
				gen.put("srt","@"+video.getSubtitles().get(0)+".srt");
			}else if(video.getParams().get("subtitleReturn").equalsIgnoreCase("ass")){
				gen.put("ass","@"+video.getSubtitles().get(0)+".ass");
			}else{
				gen.put("srt","@"+video.getSubtitles().get(0)+".srt");
				gen.put("ass","@"+video.getSubtitles().get(0)+".ass");
				gen.put("vtt","@"+video.getSubtitles().get(0)+".vtt");
			}
		}
		return gen;
	}
}