package com.example.editory

import android.content.res.AssetManager
import android.os.Environment
import android.util.Log
import java.io.*

object FfmpegCommandGenerator {
    fun getAudioMergeCommand(videoPath: String, audioPath: String, dest: File):Array<String>{
        return arrayOf(
            "-i",
            videoPath,
            "-i",
            audioPath,
            "-preset",
            "ultrafast",
            "-c:v",
            "copy",
            "-c:a",
            "aac",
            "-map",
            "0:v:0",
            "-map",
            "1:a:0",
            "-shortest",
            dest.absolutePath
        )
    }

    fun getAddFontCommand(text:String ,assetManager: AssetManager, videoPath: String, dest: File):Array<String>{
        val font = Environment.getDataDirectory().absolutePath + "/font.ttf"
        if (!File(font).exists()) {
            copyFonts(assetManager)
        }
        return arrayOf(
            "-i",
            videoPath,
            "-preset",
            "ultrafast",
            "-vf",
            "drawtext=fontfile=" + font + ":" +
                    "text='" + text + "': fontcolor=" + "#ffaa88@1" /*textDialogBinding.color.getSelectedItem()*/ + ": fontsize=100: x=(w-text_w)/2: y=(h-text_h)/2",
            "-codec:a",
            "copy",
            dest.absolutePath
        )
    }
//    fun getCommands(effect: Int, videoPath: String,dest: File, video: VideoView): Array<String>? {
//        var command: Array<String>? = null
//        when (effect) {
//            Constants.ADD_AUDIO -> command = arrayOf(
//                "-i",
//                videoPath,
//                "-i",
//                audioPath,
//                "-preset",
//                "ultrafast",
//                "-c:v",
//                "copy",
//                "-c:a",
//                "aac",
//                "-map",
//                "0:v:0",
//                "-map",
//                "1:a:0",
//                "-shortest",
//                dest.absolutePath
//            )
//            Constants.FADE_IN -> command = arrayOf(
//                "-y",
//                "-i",
//                videoPath,
//                "-preset",
//                "ultrafast",
//                "-acodec",
//                "copy",
//                "-vf",
//                "fade=t=in:st=0:d=5,fade=t=out:st=" + video.duration.toString() + ":d=0",
//                dest.absolutePath
//            )
//            Constants.FADE_OUT -> command = arrayOf(
//                "-y",
//                "-i",
//                videoPath,
//                "-preset",
//                "ultrafast",
//                "-acodec",
//                "copy",
//                "-vf",
//                "fade=t=in:st=0:d=0,fade=t=out:st=" + (video.duration - 5).toString() + ":d=5",
//                dest.absolutePath
//            )
//            Constants.ADD_FONT -> {
//                val text: String = textDialogBinding.text.getText().toString()
//                val font = Environment.getExternalStorageDirectory().absolutePath + "/font.ttf"
//                if (!File(font).exists()) {
//                    copyFonts()
//                }
//                command = arrayOf(
//                    "-i",
//                    videoPath,
//                    "-preset",
//                    "ultrafast",
//                    "-vf",
//                    "drawtext=fontfile=" + font + ":" +
//                            "text='" + text + "': fontcolor=" + "#ffaa88@1" /*textDialogBinding.color.getSelectedItem()*/ + ": fontsize=100: x=(w-text_w)/2: y=(h-text_h)/2",
//                    "-codec:a",
//                    "copy",
//                    dest.absolutePath
//                )
//            }
//            Constants.SPEED_0_5X -> command = arrayOf(
//                "-y",
//                "-i",
//                videoPath,
//                "-preset",
//                "ultrafast",
//                "-filter_complex",
//                "[0:v]setpts=2.0*PTS[v];[0:a]atempo=0.5[a]",
//                "-map",
//                "[v]",
//                "-map",
//                "[a]",
//                "-b:v",
//                "2097k",
//                "-r",
//                "60",
//                "-vcodec",
//                "mpeg4",
//                dest.absolutePath
//            )
//            Constants.SPEED_2X -> command = arrayOf(
//                "-y",
//                "-i",
//                videoPath,
//                "-preset",
//                "ultrafast",
//                "-filter_complex",
//                "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]",
//                "-map",
//                "[v]",
//                "-map",
//                "[a]",
//                "-b:v",
//                "2097k",
//                "-r",
//                "60",
//                "-vcodec",
//                "mpeg4",
//                dest.absolutePath
//            )
//            Constants.REVERSE -> command = arrayOf(
//                "-i",
//                videoPath,
//                "-preset",
//                "ultrafast",
//                "-vf",
//                "reverse",
//                "-af",
//                "areverse",
//                dest.absolutePath
//            )
//        }
//        return command
//    }

    private fun copyFonts(assetManager: AssetManager) {
        var fontFiles: Array<String>? = null
        try {
            fontFiles = assetManager.list("")
        } catch (e: IOException) {
            Log.e("tag", "Failed to get asset file list.", e)
        }
        if (fontFiles != null) {
            for (filename in fontFiles) {
                var inputStream: InputStream
                var out: OutputStream
                try {
                    inputStream = assetManager.open(filename)
                    val outDir = Environment.getDataDirectory().absolutePath
                    val outFile = File(outDir, filename)
                    out = FileOutputStream(outFile)
                    copyFile(inputStream, out)
                    inputStream.close()
                    out.flush()
                    out.close()
                } catch (e: IOException) {
                    Log.e("tag", "Failed to copy asset file: $filename", e)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun copyFile(inputStream: InputStream, out: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        while (inputStream.read(buffer).also { read = it } != -1) {
            out.write(buffer, 0, read)
        }
    }
}